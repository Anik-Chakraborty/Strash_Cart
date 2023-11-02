package com.example.strash_cart;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class myadapter extends RecyclerView.Adapter<myadapter.myviewholder>{

    ArrayList<model> datalist;

    public myadapter(ArrayList<model> datalist) {
        this.datalist = datalist;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_trash,parent,false);
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {
        holder.material_type.setText(datalist.get(position).getType());
        holder.material_des.setText(datalist.get(position).getDescription());
        holder.material_location.setText(datalist.get(position).getLocation());
    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    class myviewholder extends RecyclerView.ViewHolder
    {
        TextView material_type, material_des, material_location;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            material_type = itemView.findViewById(R.id.material_type);
            material_des = itemView.findViewById(R.id.material_des);
            material_location = itemView.findViewById(R.id.material_location);
        }
    }
}




