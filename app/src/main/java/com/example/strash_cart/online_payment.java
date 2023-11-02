package com.example.strash_cart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class online_payment extends AppCompatActivity {
    String buyerPhone, deliveryAddress, itemId, totalAmount;
    Bundle cartList;
    ArrayList<ItemModel> cartItemList;
    Button upi_send_btn;
    Set<String> cartItemSet;
    FirebaseFirestore firestore;
    TextView totalAmountTxt;
    final int UPI_PAYMENT = 0;
    String name, note, upiId;
    boolean flag=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_payment);

        upi_send_btn = findViewById(R.id.upi_send_btn);
        totalAmountTxt = findViewById(R.id.totalAmount);

        upiId= "devchat000@okhdfcbank";
        name ="StrashCart";
        note ="Have some money bro";

        firestore = FirebaseFirestore.getInstance();

        cartItemList = new ArrayList<>();
        SharedPreferences pref = getSharedPreferences("cartItemIdSharedPre", MODE_PRIVATE);
        cartItemSet = pref.getStringSet("cartItemIdStringSet", new HashSet<>());

        buyerPhone = getIntent().getStringExtra("buyerPhone");
        totalAmount = getIntent().getStringExtra("totalAmount");
        deliveryAddress = getIntent().getStringExtra("deliveryAddress");

        totalAmount= totalAmount+".00";
        totalAmountTxt.setText("Total Amount: ₹ "+" "+totalAmount);

        firestore.collection("Items")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                itemId =document.getId().toString();
                                Log.i("Doc Id",itemId);
                                if(cartItemSet.contains(itemId)){
                                    Toast.makeText(online_payment.this, itemId, Toast.LENGTH_SHORT).show();
                                    ItemModel obj = document.toObject(ItemModel.class);
                                    cartItemList.add(obj);
                                }
                            }
                        } else {
                            Toast.makeText(online_payment.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


        upi_send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                payUsingUpi(totalAmount, upiId, name, note);

                if(flag){
                    int cartItemListSize = cartItemList.size();

                    //add order in firestore

                    for(int i=0; i<cartItemListSize; i++){
                        Map<String, Object> orderDetail = new HashMap<>();
                        orderDetail.put("productId", cartItemList.get(i).getItemId().toString());
                        orderDetail.put("buyerPhone", buyerPhone);
                        orderDetail.put("sellerPhone", cartItemList.get(i).getUserPhone().toString());
                        orderDetail.put("productPrice", cartItemList.get(i).getItemPrice().toString());
                        orderDetail.put("payment", "UPI");
                        orderDetail.put("deliverAddress", deliveryAddress);

                        // Add a new document with a generated ID
                        firestore.collection("order")
                                .add(orderDetail)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Toast.makeText(online_payment.this, "order done", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(online_payment.this, "order failed", Toast.LENGTH_SHORT).show();
                                    }
                                });


                    }





                    SharedPreferences.Editor editor = pref.edit();
                    editor.clear();
                    Intent intent2 = new Intent(online_payment.this,cash_on_delivery.class);
                    startActivity(intent2);
                }

            }
        });



    }


    void payUsingUpi(String amount, String upiId, String name, String note) {

        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", upiId)
                .appendQueryParameter("pn", name)
                .appendQueryParameter("tn", note)
                .appendQueryParameter("am", amount)
                .appendQueryParameter("cu", "INR")
                .build();


        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        upiPayIntent.setData(uri);

        // will always show a dialog to user to choose an app
        Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");

        // check if intent resolves
        if(null != chooser.resolveActivity(getPackageManager())) {
            startActivityForResult(chooser, UPI_PAYMENT);
        } else {
            Toast.makeText(online_payment.this,"No UPI app found, please install one to continue",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case UPI_PAYMENT:
                if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                    if (data != null) {
                        String trxt = data.getStringExtra("response");
                        Log.d("UPI", "onActivityResult: " + trxt);
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add(trxt);

                        upiPaymentDataOperation(dataList);
                    } else {
                        Log.d("UPI", "onActivityResult: " + "Return data is null");
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("nothing");
                        upiPaymentDataOperation(dataList);
                    }
                } else {
                    Log.d("UPI", "onActivityResult: " + "Return data is null"); //when user simply back without payment
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
                break;
        }
    }

    private void upiPaymentDataOperation(ArrayList<String> data) {
        if (isConnectionAvailable(online_payment.this)) {
            String str = data.get(0);
            Log.d("UPIPAY", "upiPaymentDataOperation: "+str);
            String paymentCancel = "";
            if(str == null) str = "discard";
            String status = "";
            String approvalRefNo = "";
            String response[] = str.split("&");
            for (int i = 0; i < response.length; i++) {
                String equalStr[] = response[i].split("=");
                if(equalStr.length >= 2) {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                        status = equalStr[1].toLowerCase();
                    }
                    else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                        approvalRefNo = equalStr[1];
                    }
                }
                else {
                    paymentCancel = "Payment cancelled by user.";
                }
            }

            if (status.equals("success")) {
                //Code to handle successful transaction here.
                Toast.makeText(online_payment.this, "Transaction successful.", Toast.LENGTH_SHORT).show();
                flag = true;
                Log.d("UPI", "responseStr: "+approvalRefNo);
            }
            else if("Payment cancelled by user.".equals(paymentCancel)) {
                Toast.makeText(online_payment.this, "Payment cancelled by user.", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(online_payment.this, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(online_payment.this, "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }

}