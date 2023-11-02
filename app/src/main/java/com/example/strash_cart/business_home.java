package com.example.strash_cart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class business_home extends AppCompatActivity {
    BottomNavigationView btm_nav_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_home);


        btm_nav_bar = findViewById(R.id.btm_navbar_business);

        btm_nav_bar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if(id == R.id.home){
                    load_frag(new businessHome(), false);
                }

                else if(id == R.id.menu){
                    load_frag(new individualMenu(), false);
                }
                else{
                    load_frag(new individualDustbin(), false);
                }
                return true;
            }
        });

        btm_nav_bar.setSelectedItemId(R.id.home);
    }

    public void load_frag(Fragment fragment, boolean flag){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if(flag){
            ft.add(R.id.business_frame_layout, fragment);
        }
        else{
            ft.replace(R.id.business_frame_layout, fragment);
        }

        ft.commit();
    }
}