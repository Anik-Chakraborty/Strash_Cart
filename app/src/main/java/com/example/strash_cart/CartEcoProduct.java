package com.example.strash_cart;

import static java.lang.Short.valueOf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class CartEcoProduct extends AppCompatActivity {
    int totalAmount;
    ImageView btn_back;
    RecyclerView cartItem;
    ArrayList<ItemModel> cartItemList;
    CartItemAdapter cartItemAdapter;
    Set<String> cartItemSet;
    FirebaseFirestore firebaseFirestore;
    String itemId;
    TextView totalAmountTxt;
    Button cart_cash_delivery, cart_online_payment;
    TextInputEditText cart_address;
    FirebaseAuth auth;
    String buyerPhone, buyerUserId ,deliveryAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_eco_product);

        Log.i("Start", "Start");

        btn_back = findViewById(R.id.btn_back);
        cartItem = findViewById(R.id.cartItem);
        cart_cash_delivery = findViewById(R.id.cart_cash_delivery);
        cart_online_payment = findViewById(R.id.cart_online_payment);
        totalAmountTxt = findViewById(R.id.totalAmount);
        cart_address = findViewById(R.id.cart_address);

        auth = FirebaseAuth.getInstance();
        buyerUserId = auth.getCurrentUser().getUid();

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });


        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");
        referenceProfile.child(buyerUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readUserDetails = snapshot.getValue(ReadWriteUserDetails.class);
                if(readUserDetails !=null){

                    buyerPhone = readUserDetails.PhoneNumber.toString();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CartEcoProduct.this, "Something went wrong(Data not fetched successfully)", Toast.LENGTH_SHORT).show();
            }
        });





        cartItemSet= new HashSet<>();

        SharedPreferences pref = getSharedPreferences("cartItemIdSharedPre", MODE_PRIVATE);
        cartItemSet = pref.getStringSet("cartItemIdStringSet", new HashSet<>());

        cartItemList = new ArrayList<>();
        cartItem.setLayoutManager(new LinearLayoutManager(this));
        cartItemAdapter = new CartItemAdapter(cartItemList,this);
        cartItem.setAdapter(cartItemAdapter);
        cartItemAdapter.setOnItemClickListener(new CartItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                cartItemSet.remove(cartItemList.get(position).getItemId());
                SharedPreferences.Editor editor = pref.edit();
//                editor.remove("cartItemIdStringSet");
                editor.clear();
                editor.putStringSet("cartItemIdStringSet", cartItemSet);
                editor.commit();

                totalAmount = totalAmount - Integer.parseInt(cartItemList.get(position).getItemPrice());
                totalAmountTxt.setText("Total Amount: ₹"+" "+totalAmount);

                cartItemList.remove(position);
                cartItemAdapter.notifyItemRemoved(position);

            }

        });

        firebaseFirestore = FirebaseFirestore.getInstance();


        Log.i("ID", cartItemSet.toString());

        firebaseFirestore.collection("Items")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                itemId =document.getId().toString();
                                Log.i("Doc Id",itemId);
                               if(cartItemSet.contains(itemId)){
                                   Toast.makeText(CartEcoProduct.this, itemId, Toast.LENGTH_SHORT).show();
                                   ItemModel obj = document.toObject(ItemModel.class);
                                   cartItemList.add(obj);
                               }
                               cartItemAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Toast.makeText(CartEcoProduct.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,new IntentFilter("TotalAmount"));

        cart_cash_delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deliveryAddress= cart_address.getText().toString();
                if(TextUtils.isEmpty(deliveryAddress)){
                    cart_address.setError("Address is required");
                    cart_address.requestFocus();
                }
                else{
                    int cartItemListSize = cartItemList.size();

                    //add order in firestore

                    for(int i=0; i<cartItemListSize; i++){
                        Map<String, Object> orderDetail = new HashMap<>();
                        orderDetail.put("productId", cartItemList.get(i).getItemId().toString());
                        orderDetail.put("buyerPhone", buyerPhone);
                        orderDetail.put("sellerPhone", cartItemList.get(i).getUserPhone().toString());
                        orderDetail.put("productPrice", cartItemList.get(i).getItemPrice().toString());
                        orderDetail.put("payment", "Cash On Delivery");
                        orderDetail.put("deliverAddress", deliveryAddress);

                        // Add a new document with a generated ID
                        firebaseFirestore.collection("order")
                                .add(orderDetail)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Toast.makeText(CartEcoProduct.this, "order done", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(CartEcoProduct.this, "order failed", Toast.LENGTH_SHORT).show();
                                    }
                                });


                    }

                    SharedPreferences.Editor editor = pref.edit();
                    editor.clear();
                    Intent intent = new Intent(CartEcoProduct.this, cash_on_delivery.class);
                    startActivity(intent);
                }

            }
        });


        cart_online_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deliveryAddress= cart_address.getText().toString();
                if(TextUtils.isEmpty(deliveryAddress)){
                    cart_address.setError("Address is required");
                    cart_address.requestFocus();
                }

                else{
                    Intent intent2 = new Intent(CartEcoProduct.this,online_payment.class);
                    intent2.putExtra("deliveryAddress", deliveryAddress);
                    intent2.putExtra("buyerPhone", buyerPhone);
                    intent2.putExtra("totalAmount", Integer.toString(totalAmount));
                    startActivity(intent2);
                }

            }
        });


    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            totalAmount = intent.getIntExtra("totalAmount",0);
            totalAmountTxt.setText("Total Amount: ₹"+" "+totalAmount);
        }
    };
}