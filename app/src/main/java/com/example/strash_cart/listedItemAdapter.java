package com.example.strash_cart;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class listedItemAdapter extends RecyclerView.Adapter<listedItemAdapter.viewHolder> {
    ArrayList<ItemModel> listedItem;
    public listedItemAdapter(ArrayList<ItemModel> listedItem) {
        this.listedItem = listedItem;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listed_item_single,parent,false);
        return new listedItemAdapter.viewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.listed_item_single_name.setText(listedItem.get(position).getItemName());
        holder.listed_item_single_price.setText("Price"+" "+"₹"+" "+listedItem.get(position).getItemPrice());
        Picasso.get().load(listedItem.get(position).getItemImageFirst()).into(holder.listed_item_single_img);

    }

    @Override
    public int getItemCount() {
        return listedItem.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        ImageView listed_item_single_img;
        TextView listed_item_single_name, listed_item_single_price;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            listed_item_single_img = itemView.findViewById(R.id.listed_item_single_img);
            listed_item_single_name = itemView.findViewById(R.id.listed_item_single_name);
            listed_item_single_price = itemView.findViewById(R.id.listed_item_single_price);
        }
    }

}
