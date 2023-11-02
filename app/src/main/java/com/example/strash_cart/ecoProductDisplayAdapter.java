package com.example.strash_cart;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ecoProductDisplayAdapter extends RecyclerView.Adapter<ecoProductDisplayAdapter.viewHolder> {
    ArrayList<String> imgUrl;

    public ecoProductDisplayAdapter(ArrayList<String> imgUrl) {
        this.imgUrl = imgUrl;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.eco_product_display_img,parent,false);
        return new ecoProductDisplayAdapter.viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
//        holder.img.setImageURI(Uri.parse(imgUrl.get(position)));
        Picasso.get().load(imgUrl.get(position)).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return imgUrl.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        ImageView img;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.eco_product_display_img);
        }
    }
}
