package com.example.strash_cart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthRegistrar;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    TextView move_register;
    TextInputEditText login_email, login_password;
    Button btn_login;
    FirebaseAuth authProfile;
    String login_email_txt, login_password_txt, fullName, emailAddress, phoneNumber, userMode;
    ProgressBar sign_in_progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       move_register = findViewById(R.id.move_register);
       login_email = findViewById(R.id.login_email);
       login_password = findViewById(R.id.login_password);
       btn_login = findViewById(R.id.btn_log_in);
       sign_in_progress = findViewById(R.id.sign_in_progress);

       authProfile = FirebaseAuth.getInstance();

        move_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent register_activity = new Intent(MainActivity.this, register_user.class);
                startActivity(register_activity);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sign_in_progress.setVisibility(View.VISIBLE);
                login_email_txt = login_email.getText().toString();
                login_password_txt = login_password.getText().toString();

                if(TextUtils.isEmpty(login_email_txt)){
                    Toast.makeText(MainActivity.this, "Please Enter Email", Toast.LENGTH_SHORT).show();
                    login_email.setError("Email is required");
                    login_email.requestFocus();
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(login_email_txt).matches()){
                    Toast.makeText(MainActivity.this, "Please Re-Enter Email", Toast.LENGTH_SHORT).show();
                    login_email.setError("Valid email is required");
                    login_email.requestFocus();
                }
                else if(TextUtils.isEmpty(login_password_txt)){
                    Toast.makeText(MainActivity.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                    login_password.setError("Password is required");
                    login_password.requestFocus();
                }
                else{
                    sign_in_progress.setVisibility(View.VISIBLE);
                    loginUser(login_email_txt,login_password_txt);
                }
            }
        });

    }

    private void loginUser(String login_email_txt, String login_password_txt) {
        authProfile.signInWithEmailAndPassword(login_email_txt,login_password_txt).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    Toast.makeText(MainActivity.this,"Login Successful",Toast.LENGTH_SHORT).show();
                    FirebaseUser firebaseUser = authProfile.getCurrentUser();

                    if(firebaseUser == null){
                        Toast.makeText(MainActivity.this,"Something went wrong",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        showUserProfile(firebaseUser);
                    }

                }
                else{
                    Toast.makeText(MainActivity.this, "Login Failed, Try Again", Toast.LENGTH_SHORT).show();
                }
                sign_in_progress.setVisibility(View.GONE);
            }
        });
    }

    private void showUserProfile(FirebaseUser firebaseUser) {
        String userID = firebaseUser.getUid();

        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");
        referenceProfile.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readUserDetails = snapshot.getValue(ReadWriteUserDetails.class);
                if(readUserDetails !=null){
//                    fullName = firebaseUser.getDisplayName();
//                    emailAddress = firebaseUser.getEmail();
//                    phoneNumber = readUserDetails.PhoneNumber;
                    userMode = readUserDetails.UserMode;

                    if(userMode.equals("Individual")){
                        startActivity(new Intent(MainActivity.this, individual_home.class));
                        finish();
                    }
                    else if(userMode.equals("Business")){
                        startActivity(new Intent(MainActivity.this,business_home.class));
                        finish();
                    }
                    else{
                        Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Something went wrong(Canceled)", Toast.LENGTH_SHORT).show();
            }
        });
    }


}