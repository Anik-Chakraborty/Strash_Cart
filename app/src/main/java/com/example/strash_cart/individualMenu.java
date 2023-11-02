package com.example.strash_cart;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashSet;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link individualMenu#newInstance} factory method to
 * create an instance of this fragment.
 */
public class individualMenu extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public individualMenu() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment individualMenu.
     */
    // TODO: Rename and change types and number of parameters
    public static individualMenu newInstance(String param1, String param2) {
        individualMenu fragment = new individualMenu();
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
        // Inflate the layout for this fragment
        View indiMenuFrag = inflater.inflate(R.layout.fragment_individual_menu, container, false);

        Button move_profile = indiMenuFrag.findViewById(R.id.btn_profile);
        Button menu_logout = indiMenuFrag.findViewById(R.id.menu_logout);
        ImageView btn_back_menu = indiMenuFrag.findViewById(R.id.btn_back_menu);
        Button btn_about = indiMenuFrag.findViewById(R.id.btn_about);
        Button btn_feedback = indiMenuFrag.findViewById(R.id.btn_feedback);

        btn_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),feedback.class);
                startActivity(intent );
            }
        });

        btn_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),About.class);
                startActivity(intent );
            }
        });

        btn_back_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpOnBackPressed();
            }
        });

        move_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), profile.class);
                startActivity(intent);
            }
        });

        menu_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                SharedPreferences pref = getContext().getSharedPreferences("cartItemIdSharedPre", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
//                    editor.remove("cartItemIdStringSet");
                editor.clear();
                editor.commit();

                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(),MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);


            }
        });


        return indiMenuFrag;
    }

    private void setUpOnBackPressed() {
        requireActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                setEnabled(false);
                requireActivity().onBackPressed();
            }
        });
    }
}