package com.example.strash_cart;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class eco_products_adapter extends RecyclerView.Adapter<eco_products_adapter.viewHolder>{

    ArrayList<ItemModel> dataList;
    Context context;

    public eco_products_adapter(ArrayList<ItemModel> dataList, Context context ) {
        this.dataList = dataList;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.eco_products_card,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        final int pos = position;
        holder.eco_product_name.setText(dataList.get(position).getItemName());
        holder.eco_product_price.setText(dataList.get(position).getItemPrice());
//        holder.eco_product_image.setImageURI(Uri.parse(dataList.get(position).getItemImageFirst()));
        Picasso.get().load(dataList.get(position).getItemImageFirst()).into(holder.eco_product_image);

        holder.eco_product_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Eco_Product_Display.class);
                //pass data
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("ProductName", dataList.get(pos).getItemName());
                intent.putExtra("ProductPrice", dataList.get(pos).getItemPrice());
                intent.putExtra("ProductDes", dataList.get(pos).getItemDesc());
                intent.putExtra("ProductImg", dataList.get(pos).getImageUrls());
                intent.putExtra("ProductId", dataList.get(pos).getItemId());


                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        TextView eco_product_name, eco_product_price;
        ImageView eco_product_image;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            eco_product_name = itemView.findViewById(R.id.eco_product_name);
            eco_product_price = itemView.findViewById(R.id.eco_product_price);
            eco_product_image = itemView.findViewById(R.id.eco_product_image);

        }
    }

}
