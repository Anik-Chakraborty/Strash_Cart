package com.example.strash_cart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class profile extends AppCompatActivity {
    Button btn_back;
    FirebaseAuth auth;
    TextView profile_full_name_1, profile_full_name_2, profile_email, profile_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        btn_back = findViewById(R.id.btn_back);
        profile_full_name_1 = findViewById(R.id.profile_full_name_1);
        profile_full_name_2 = findViewById(R.id.profile_full_name_2);
        profile_email = findViewById(R.id.profile_email);
        profile_phone = findViewById(R.id.profile_phone);

        try{
            String uid = auth.getInstance().getCurrentUser().getUid().toString();


            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Registered Users");
            databaseReference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ReadWriteUserDetails readUserDetails = snapshot.getValue(ReadWriteUserDetails.class);
                    if(readUserDetails !=null){

                         profile_full_name_1.setText(readUserDetails.FullName);
                         profile_full_name_2.setText(readUserDetails.FullName);
                         profile_email.setText(readUserDetails.EmailAddress);
                         profile_phone.setText(readUserDetails.PhoneNumber);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        catch (Exception e){
            Toast.makeText(profile.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });


    }

}