
package com.example.strash_cart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Splash_StrashCart extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_strash_cart);

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(firebaseUser == null){
                    Intent home_activity =new Intent(Splash_StrashCart.this, MainActivity.class);
                    startActivity(home_activity);
                    finish();
                }
                else{
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
                                String userMode = readUserDetails.UserMode;

                                if(userMode.equals("Individual")){
                                    startActivity(new Intent(Splash_StrashCart.this, individual_home.class));
                                    finish();
                                }
                                else if(userMode.equals("Business")){
                                    startActivity(new Intent(Splash_StrashCart.this,business_home.class));
                                    finish();
                                }
                                else{
                                    Toast.makeText(Splash_StrashCart.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(Splash_StrashCart.this, "Something went wrong(Canceled)", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        }, 2000);
    }
}