package com.example.strash_cart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;

public class manage_otp extends AppCompatActivity {
    TextInputEditText otp_editText;
    Button btn_verify;
    String phoneNumber, user_id, user_full_name, user_email, user_mode, otpId;
    FirebaseAuth mAuth;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_otp);

        phoneNumber=getIntent().getStringExtra("mobile").toString();
        user_id = getIntent().getStringExtra("user_id").toString();
        user_full_name= getIntent().getStringExtra("user_full_name").toString();
        user_email= getIntent().getStringExtra("user_email").toString();
        user_mode= getIntent().getStringExtra("user_mode").toString();

        otp_editText=findViewById(R.id.otp_editText);
        btn_verify=findViewById(R.id.btn_verify);
        mAuth=FirebaseAuth.getInstance();

        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(otp_editText.getText().toString().isEmpty())
                    Toast.makeText(getApplicationContext(),"Blank Field cannot be processed", Toast.LENGTH_LONG).show();
                else if (otp_editText.getText().toString().length()!=6)
                    Toast.makeText(getApplicationContext(),"Invalid OTP",Toast.LENGTH_LONG).show();
                else{
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(otpId,otp_editText.getText().toString());
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });
        initiateOtp();

    }

    private void initiateOtp() {



        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                otpId = s;
            }

            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_LONG).show();

            }

        };

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)       // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();

                            ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(user_full_name,user_email,phoneNumber,user_mode);
                            DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");
                            referenceProfile.child(user_id).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(manage_otp.this, "Data Stored Successfully",Toast.LENGTH_SHORT ).show();
                                    startActivity(new Intent(manage_otp.this,MainActivity.class));
                                    finish();

                                }
                            });


                        } else {
                            Toast.makeText(getApplicationContext(),"Sign in Code Error",Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }
}