package com.example.strash_cart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class register_user extends AppCompatActivity {

    Button btn_back,btn_register;
    TextInputEditText full_name,email,password,re_password;
    TextView txt_sign_in;
    ProgressBar sign_up_progress;
    RadioGroup user_mode_rg;
    RadioButton user_mode_rb;
    String user_full_name,user_email,user_password,user_re_password,user_mode, phone_number;

    private  static final String TAG ="register_user";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        //find the views
        btn_back= findViewById(R.id.btn_back);
        txt_sign_in= findViewById(R.id.txt_sign_in);
        full_name= findViewById(R.id.sign_up_full_name);
        email= findViewById(R.id.sign_up_email);
        password=findViewById(R.id.sign_up_password);
        re_password=findViewById(R.id.sign_up_re_password);
        btn_register= findViewById(R.id.register_btn);
        sign_up_progress=findViewById(R.id.sign_up_progress);
        user_mode_rg = findViewById(R.id.user_mode_rg);

        user_mode_rg.clearCheck();

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        txt_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent move_log_in_activity = new Intent(register_user.this, MainActivity.class);
                startActivity(move_log_in_activity);
                finish();
            }
        });


        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedGenderId = user_mode_rg.getCheckedRadioButtonId();
                user_mode_rb = findViewById(selectedGenderId);

                user_full_name = full_name.getText().toString();
                user_email= email.getText().toString();
                user_password= password.getText().toString();
                user_re_password= re_password.getText().toString();


                if (TextUtils. isEmpty(user_full_name)) {
                    Toast.makeText(register_user.this, "Please enter your full name", Toast.LENGTH_LONG).show();
                    full_name.setError("Full Name is required");
                    full_name.requestFocus();
                }
                else if (TextUtils. isEmpty(user_email)) {
                    Toast.makeText(register_user.this, "Please enter correct email", Toast.LENGTH_LONG).show();
                    email.setError("Email is required");
                    email.requestFocus();
                }

                else if (TextUtils. isEmpty(user_password)) {
                    Toast.makeText(register_user.this, "Please enter correct password", Toast.LENGTH_LONG).show();
                    password.setError("Password is required");
                    password.requestFocus();
                }
                else if (user_password. length()<6) {
                    Toast.makeText(register_user.this, "Password length should be greater than 6", Toast.LENGTH_LONG).show();
                    password.setError("Strong password is required");
                    password.requestFocus();
                }
                else if (TextUtils. isEmpty(user_re_password)) {
                    Toast.makeText(register_user.this, "Please re_enter password", Toast.LENGTH_LONG).show();
                    re_password.setError("Password is required");
                    re_password.requestFocus();
                }
                else if (!(user_re_password.equals(user_password))){
                    Toast.makeText(register_user.this, "Please check password", Toast.LENGTH_LONG).show();
                    re_password.setError("Password doesn't match");
                    re_password.requestFocus();
                }
                else{

                    try {
                        user_mode = user_mode_rb.getText().toString();
                        sign_up_progress.setVisibility(View.VISIBLE);
                        register_user_firebase(user_full_name,user_email,user_password,user_mode);
                    }catch (NullPointerException e){
                        Toast.makeText(register_user.this, "Select User Mode", Toast.LENGTH_SHORT).show();
                    }

                }

            }



        });

    }
    private void register_user_firebase(String user_full_name, String user_email, String user_password, String user_mode) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(user_email, user_password).addOnCompleteListener(register_user.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(register_user.this,"user register successful",Toast.LENGTH_SHORT).show();
                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    firebaseUser.sendEmailVerification();
                    phone_number="01230";
                    ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(user_full_name,user_email,phone_number,user_mode);
                    DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");
                    referenceProfile.child(firebaseUser.getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(register_user.this, "Data Stored Successfully",Toast.LENGTH_SHORT ).show();


                        }
                    });


                    Intent phone_verify = new Intent(register_user.this,verify_mobile_otp.class);
                    phone_verify.putExtra("user_id",firebaseUser.getUid());
                    phone_verify.putExtra("user_full_name", user_full_name);
                    phone_verify.putExtra("user_email", user_email);
                    phone_verify.putExtra("user_mode", user_mode);
                    startActivity(phone_verify);
                    finish();



                }
                else{
                    try{
                        throw  task.getException();
                    } catch (FirebaseAuthWeakPasswordException e){
                        password.setError("Your password is too weak. Kindly use a mix of alphabets, special character & number");
                        password.requestFocus();
                    } catch(FirebaseAuthInvalidCredentialsException e){
                        email.setError("Your email is invalid. Kindly re-enter your email");
                        email.requestFocus();
                    }
                    catch(FirebaseAuthUserCollisionException e){
                        email.setError("User is already registered");
                        email.requestFocus();
                    }
                    catch (Exception e){
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(register_user.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        sign_up_progress.setVisibility(View.GONE);
                    }
                }

            }
        });
    }
}