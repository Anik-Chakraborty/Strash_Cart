package com.example.strash_cart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

public class EcoProducts extends AppCompatActivity {
    RecyclerView eco_products_recycle;
    FirebaseFirestore db;
    ArrayList<ItemModel> eco_data;
    eco_products_adapter eco_adapter;
    ImageView btn_back_eco_prd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eco_products);

        eco_products_recycle = findViewById(R.id.eco_products_recycle);
        btn_back_eco_prd  = findViewById(R.id.btn_back_eco_prd);
        GridLayoutManager gridLayoutManager= new GridLayoutManager(this, 2);
        eco_products_recycle.setLayoutManager(gridLayoutManager);


        db = FirebaseFirestore.getInstance();
        eco_data = new ArrayList<>();
        eco_adapter = new eco_products_adapter(eco_data, getApplicationContext());

        eco_products_recycle.setAdapter(eco_adapter);

        btn_back_eco_prd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });


        try {
            db.collection("Items")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {

                                for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.i("Id", document.getId());
                                    ItemModel obj = document.toObject(ItemModel.class);
                                    eco_data.add(obj);

                                }
                                eco_adapter.notifyDataSetChanged();
                            } else {
                                Log.i("not success", "failed");
                            }
                        }
                    });
        }
        catch (Exception e){
            Log.i("Issue",e.getMessage());
        }


    }
}