package com.example.strash_cart;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link businessHome#newInstance} factory method to
 * create an instance of this fragment.
 */
public class businessHome extends Fragment {
    FirebaseAuth auth;
    FirebaseUser firebaseUser;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public businessHome() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment businessHome.
     */
    // TODO: Rename and change types and number of parameters
    public static businessHome newInstance(String param1, String param2) {
        businessHome fragment = new businessHome();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        String userId = firebaseUser.getUid();
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_business_home, container, false);
        Button add_item_business= view.findViewById(R.id.add_item_business);
        Button btn_buy_metarial = view.findViewById(R.id.btn_buy_metarial);
        Button listed_item = view.findViewById(R.id.listed_item);
        TextView userName = view.findViewById(R.id.user_name_buss);
        Button btn_order = view.findViewById(R.id.btn_order);

        btn_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), order_display.class);
                startActivity(intent);
            }
        });


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Registered Users");
        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readUserDetails = snapshot.getValue(ReadWriteUserDetails.class);
                if(readUserDetails !=null){

                    userName.setText(readUserDetails.FullName);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        add_item_business.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), addItemBusiness.class);
                startActivity(intent);
            }
        });

        btn_buy_metarial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(getActivity(),buy_trash.class);
                startActivity(intent2);
            }
        });
        listed_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3= new Intent(getActivity(),listedItem.class);
                startActivity(intent3);
            }
        });

        return view;
    }
}