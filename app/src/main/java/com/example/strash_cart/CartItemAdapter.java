package com.example.strash_cart;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.viewHolder>{
    ArrayList<ItemModel> cartData;
    Context context;
    ArrayAdapter<String> adapterItems;

    int totalAmount=0;

    private OnItemClickListener listener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener clickListener){
        listener = clickListener;
    }


    public CartItemAdapter(ArrayList<ItemModel> cartItemList, Context context) {
        cartData = cartItemList;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_single,parent,false);
        return new viewHolder(view , listener);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.cart_prd_name.setText(cartData.get(position).getItemName());
        holder.cart_prd_price.setText("Price"+" "+"₹"+" "+cartData.get(position).getItemPrice());
        Picasso.get().load(cartData.get(position).getItemImageFirst()).into(holder.cart_prd_img);


//            String[] quantity = {"1", "2", "3", "4", "Max 5"};
//            adapterItems = new ArrayAdapter<String>(context, R.layout.cart_single,quantity);
//            adapterItems.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            holder.autoCompleteTextView.setAdapter(adapterItems);

    //Total amount pass to cart activity
        totalAmount = totalAmount + Integer.parseInt(cartData.get(position).getItemPrice());
        Intent intent = new Intent("TotalAmount");
        intent.putExtra("totalAmount", totalAmount);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    @Override
    public int getItemCount() {
        return cartData.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        ImageView cart_prd_img, cart_delete;
        TextView cart_prd_name, cart_prd_price;
        AutoCompleteTextView autoCompleteTextView;


        public viewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            cart_delete = itemView.findViewById(R.id.cart_delete);
            cart_prd_img =itemView.findViewById(R.id.cart_prd_img);
            cart_prd_name =itemView.findViewById(R.id.cart_prd_name);
            cart_prd_price =itemView.findViewById(R.id.cart_prd_price);
            autoCompleteTextView = itemView.findViewById(R.id.auto_complete_txt);





            cart_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(getBindingAdapterPosition());
                }
            });



        }
    }
}
