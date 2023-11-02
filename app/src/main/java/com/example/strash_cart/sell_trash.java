package com.example.strash_cart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.HashMap;
import java.util.Map;

public class sell_trash extends AppCompatActivity {
    String[] items = {"Paper", "Wood", "Metal", "Plastic", "Non-Recyclable"};
    AutoCompleteTextView autoCompleteTxt;
    ArrayAdapter<String> adapterItems;
    Button post_trash, btn_back;
    TextInputEditText trash_location, trash_des;
    String trashDes, trashLocation, trashType, userEmail;
    FirebaseAuth auth;
    FirebaseFirestore firebaseFirestore;
    Tag TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_trash);

        post_trash = findViewById(R.id.post_trash);
        btn_back = findViewById(R.id.btn_back);
        autoCompleteTxt = findViewById(R.id.auto_complete_txt);
        trash_des = findViewById(R.id.trash_des);
        trash_location=findViewById(R.id.trash_location);
        adapterItems = new ArrayAdapter<String>(this, R.layout.list_item,items);
        autoCompleteTxt.setAdapter(adapterItems);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
//        String firebaseUserUid = firebaseUser.getUid();

        autoCompleteTxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
            }
        });

        post_trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trashDes = trash_des.getText().toString();
                trashLocation =trash_location.getText().toString();
                trashType = autoCompleteTxt.getText().toString();

                if(TextUtils.isEmpty(trashDes)){
                    trash_des.setError("Please describe your trash items");
                    trash_des.requestFocus();
                }
                else if(TextUtils.isEmpty(trashLocation)){
                    trash_location.setError("Enter your pickup location");
                    trash_location.requestFocus();
                }
                else if(TextUtils.isEmpty(trashType)){
                    autoCompleteTxt.setError("Select trash type");
                    autoCompleteTxt.requestFocus();
                }
                else{
                    //if every thing is ok then we will upload the details in fire store
                    userEmail = firebaseUser.getEmail();

                    if(TextUtils.isEmpty(userEmail)){
                        Toast.makeText(sell_trash.this, "Something went wrong please try again", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Map<String, Object> user_trash_db = new HashMap<>();
                        user_trash_db.put("Type", trashType);
                        user_trash_db.put("Description", trashDes);
                        user_trash_db.put("Location", trashLocation);
                        user_trash_db.put("email",userEmail );

                        firebaseFirestore.collection("user_trash")
                                .add(user_trash_db)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
//                                       Log.i(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                        Toast.makeText(sell_trash.this, "Data uploaded successfully", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
//                                        Toast.makeText(sell_trash.this, "Error when adding data", Toast.LENGTH_SHORT).show();
                                        Toast.makeText(sell_trash.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                    }

                }
            }
        });
    }
}