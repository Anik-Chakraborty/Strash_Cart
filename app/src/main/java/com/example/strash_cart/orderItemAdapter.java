package com.example.strash_cart;

import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class orderItemAdapter extends RecyclerView.Adapter<orderItemAdapter.viewHolder> {

    ArrayList<orderItem> orderItemArrayList;
    FirebaseFirestore firebaseFirestore= FirebaseFirestore.getInstance();



    public orderItemAdapter(ArrayList<orderItem> orderItemArrayList) {
        this.orderItemArrayList = orderItemArrayList;
    }


    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_single,parent,false);
        return new orderItemAdapter.viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        int pos = position;

        Log.i("id",orderItemArrayList.get(pos).getProductId());

        try{
            firebaseFirestore.collection("Items")
                    .whereEqualTo("itemId", orderItemArrayList.get(pos).getProductId())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {

                                        Log.i("Id", document.getId());
                                        ItemModel itemDes = document.toObject(ItemModel.class);

                                            Picasso.get().load(itemDes.getItemImageFirst()).into(holder.order_prd_img);
                                            holder.order_prd_name.setText(itemDes.getItemName());
                                            holder.order_prd_price.setText("₹ "+orderItemArrayList.get(pos).getProductPrice().toString());
                                            holder.order_prd_buyer_phn.setText("Buyer Phone Number: "+orderItemArrayList.get(pos).getBuyerPhone().toString());
                                            holder.order_prd_delivery_add.setText("Delivery Address: "+orderItemArrayList.get(pos).getDeliverAddress().toString());
                                            holder.order_prd_payment.setText("Payment Method: "+orderItemArrayList.get(pos).getPayment().toString());





                                }

                            } else {
                                Log.i("item des not found", "failed");
                            }
                        }
                    });
        }catch (Exception e){
            Log.i(" Error", e.getMessage());
        }

    }



    @Override
    public int getItemCount() {
        return orderItemArrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        TextView order_prd_name, order_prd_price, order_prd_buyer_phn, order_prd_delivery_add, order_prd_payment;
        ImageView order_prd_img;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            order_prd_name = itemView.findViewById(R.id.order_item_name);
            order_prd_price = itemView.findViewById(R.id.order_item_price);
            order_prd_delivery_add = itemView.findViewById(R.id.order_buyer_address);
            order_prd_payment = itemView.findViewById(R.id.payment_method);
            order_prd_buyer_phn = itemView.findViewById(R.id.order_buyer_phone);
            order_prd_img = itemView.findViewById(R.id.order_item_img);
        }
    }
}
