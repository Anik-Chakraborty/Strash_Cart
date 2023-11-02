package com.example.strash_cart;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;

public class ViewPagerAdapter extends PagerAdapter {
    private Context context;
    ArrayList<Uri> ImageUrls;
    LayoutInflater layoutInflater;

    public ViewPagerAdapter(){}

    public ViewPagerAdapter(Context context, ArrayList<Uri> imageUrls) {
        try{
            this.context = context;
            ImageUrls = imageUrls;
            layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        catch (Exception e){
            Log.i("Problem", e.getMessage());
        }
    }

    @Override
    public int getCount() {
        return ImageUrls.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view= layoutInflater.inflate(R.layout.show_imgaes_layout,container,false);
        ImageView imageView=view.findViewById(R.id.UploadImage);
        imageView.setImageURI(ImageUrls.get(position));
        try{

            container.addView(view);

        }catch (Exception e){
            Log.i("Problem", e.getMessage());
        }
        return view ;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull View container, int position, @NonNull Object object) {
        ((RelativeLayout)object).removeView(container);
    }
}
