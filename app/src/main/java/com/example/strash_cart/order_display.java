package com.example.strash_cart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class order_display extends AppCompatActivity {

    ImageView btn_back;
    RecyclerView order_recycle;
    FirebaseFirestore firebaseFirestore;
    ArrayList<orderItem> order_item_list;
    orderItemAdapter order_item_adapter;
    FirebaseAuth auth;
    String uid, phone_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_display);

        btn_back = findViewById(R.id.btn_back_order);
        order_recycle = findViewById(R.id.order_recycle);


        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        uid = auth.getCurrentUser().getUid();
        order_item_list = new ArrayList<>();

        order_recycle.setLayoutManager(new LinearLayoutManager(this));
        order_item_adapter = new orderItemAdapter(order_item_list);
        order_recycle.setAdapter(order_item_adapter);



        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");
        referenceProfile.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readUserDetails = snapshot.getValue(ReadWriteUserDetails.class);
                if(readUserDetails !=null){

                   phone_number = readUserDetails.PhoneNumber;
                    firebaseFirestore.collection("order")
                            .whereEqualTo("sellerPhone", phone_number)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            orderItem obj = document.toObject(orderItem.class);
                                            order_item_list.add(obj);
                                        }
                                        order_item_adapter.notifyDataSetChanged();
                                    } else {
                                        Toast.makeText(order_display.this, "Something went wrong(order not fetched)", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(order_display.this, "Something went wrong(data not fetched)", Toast.LENGTH_SHORT).show();
            }
        });


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
    }
}