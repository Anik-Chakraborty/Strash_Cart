package com.example.strash_cart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Eco_Product_Display extends AppCompatActivity {
    RecyclerView eco_product_display_recycle;
    ArrayList<String> imageUrls;
    String prdName, prdDes, prdPrice, prdId;
    TextView prdNameTxt , prdDesTxt, prdPriceTxt;
    Button btn_add_cart, btn_back;
    Set<String> cartItemSet;
    boolean checkPrdId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eco_product_display);

        prdNameTxt = findViewById(R.id.prdName);
        prdDesTxt = findViewById(R.id.prdDes);
        prdPriceTxt = findViewById(R.id.prdPrice);
        eco_product_display_recycle = findViewById(R.id.eco_product_display_recycle);
        btn_add_cart = findViewById(R.id.btn_add_cart);
        btn_back =findViewById(R.id.btn_back);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        cartItemSet= new HashSet<>();

        SharedPreferences pref = getSharedPreferences("cartItemIdSharedPre", MODE_PRIVATE);
        cartItemSet = pref.getStringSet("cartItemIdStringSet", new HashSet<>());

        checkPrdId = false;

        imageUrls = getIntent().getStringArrayListExtra("ProductImg");
        prdName = getIntent().getStringExtra("ProductName").toString();
        prdDes = getIntent().getStringExtra("ProductDes").toString();
        prdPrice = "Price"+" "+"₹"+" "+getIntent().getStringExtra("ProductPrice").toString();
        prdId = getIntent().getStringExtra("ProductId").toString();

        Log.i("prdId",prdId);

        if( cartItemSet.contains(prdId)){
            Toast.makeText(Eco_Product_Display.this, "In cart", Toast.LENGTH_SHORT).show();
            checkPrdId=true;
            btn_add_cart.setBackgroundColor(Color.rgb(207, 209, 209));
            btn_add_cart.setTextColor(Color.rgb(255, 255, 255));
        }

        prdNameTxt.setText(prdName);
        prdDesTxt.setText(prdDes);
        prdPriceTxt.setText(prdPrice);

        LinearLayoutManager linearLayout = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        eco_product_display_recycle.setLayoutManager(linearLayout);

        eco_product_display_recycle.setAdapter(new ecoProductDisplayAdapter(imageUrls));


        btn_add_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkPrdId){
                    Toast.makeText(Eco_Product_Display.this, "Product already present added to cart", Toast.LENGTH_SHORT).show();
                }
                else{
                    cartItemSet.add(prdId);
                    SharedPreferences.Editor editor = pref.edit();
//                    editor.remove("cartItemIdStringSet");
                    editor.clear();
                    editor.putStringSet("cartItemIdStringSet", cartItemSet);
                    editor.commit();
                    Toast.makeText(Eco_Product_Display.this, "Product is added to cart successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}