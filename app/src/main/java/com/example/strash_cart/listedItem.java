package com.example.strash_cart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;

public class listedItem extends AppCompatActivity {
    ImageView listed_item_back;
    RecyclerView listed_item_recycle;
    ArrayList<ItemModel> listedItem;
    listedItemAdapter listed_item_adapter;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
    String UserPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listed_item);

        listed_item_back = findViewById(R.id.listed_item_back);
        listed_item_recycle = findViewById(R.id.listed_item_recycle);
        listedItem = new ArrayList<>();

        listed_item_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        listed_item_recycle.setLayoutManager(new LinearLayoutManager(this));
        listed_item_adapter = new listedItemAdapter(listedItem);
        listed_item_recycle.setAdapter(listed_item_adapter);

        String userID = firebaseUser.getUid();

        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");
        referenceProfile.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readUserDetails = snapshot.getValue(ReadWriteUserDetails.class);
                if(readUserDetails !=null){
                    UserPhone = readUserDetails.PhoneNumber;
                    Toast.makeText(listedItem.this,UserPhone, Toast.LENGTH_SHORT).show();

                    try{
                        firebaseFirestore.collection("Items")
                                .whereEqualTo("userPhone", UserPhone)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {

                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                Log.i("Id", document.getId());
                                                ItemModel obj = document.toObject(ItemModel.class);
                                                listedItem.add(obj);

                                            }
                                            listed_item_adapter.notifyDataSetChanged();
                                        } else {
                                            Log.i("not success", "failed");
                                        }
                                    }
                                });
                    }catch (Exception e){
                        Toast.makeText(listedItem.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(listedItem.this, "User Phone number not fetched", Toast.LENGTH_SHORT).show();
            }
        });








    }
}