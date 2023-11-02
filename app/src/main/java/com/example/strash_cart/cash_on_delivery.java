package com.example.strash_cart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class cash_on_delivery extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_on_delivery);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent moveToHome =new Intent(cash_on_delivery.this, individual_home.class);
                startActivity(moveToHome);
                finish();
            }
        }, 2000);
    }

}