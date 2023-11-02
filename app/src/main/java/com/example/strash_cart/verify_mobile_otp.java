package com.example.strash_cart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseUser;
import com.hbb20.CountryCodePicker;

public class verify_mobile_otp extends AppCompatActivity {
    CountryCodePicker ccp;
    TextInputEditText mobile;
    Button btn_otp;
    String user_id, user_full_name, user_email, user_mode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_mobile_otp);

        btn_otp = findViewById(R.id.btn_otp);
        ccp = findViewById(R.id.ccp);
        mobile = findViewById(R.id.mobile);

        ccp.registerCarrierNumberEditText(mobile);

        user_id= getIntent().getStringExtra("user_id").toString();
        user_full_name= getIntent().getStringExtra("user_full_name").toString();
        user_email= getIntent().getStringExtra("user_email").toString();
        user_mode= getIntent().getStringExtra("user_mode").toString();



        btn_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phn_num = mobile.getText().toString();
                if(TextUtils.isEmpty(phn_num)){
                    mobile.setError("Valid Mobile Number Required");
                }
                else{
                    Intent intent=new Intent(verify_mobile_otp.this,manage_otp.class);
                    intent.putExtra("mobile",ccp.getFullNumberWithPlus().replace(" "," "));
                    intent.putExtra("user_id", user_id);
                    intent.putExtra("user_full_name", user_full_name);
                    intent.putExtra("user_email", user_email);
                    intent.putExtra("user_mode", user_mode);
                    startActivity(intent);
                }

        }});
    }
}