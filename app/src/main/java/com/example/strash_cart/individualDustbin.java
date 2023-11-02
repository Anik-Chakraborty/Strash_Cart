package com.example.strash_cart;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link individualDustbin#newInstance} factory method to
 * create an instance of this fragment.
 */
public class individualDustbin extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LatLng point1 = new LatLng(22.517795, 88.357548);
    private LatLng point2 = new LatLng(22.518647, 88.358331);
    private LatLng point3 = new LatLng(22.518132, 88.358685);
    private LatLng point4 = new LatLng(22.518806, 88.359404);

    private Marker mPoint1;
    private Marker mPoint2;
    private Marker mPoint3;
    private Marker mPoint4;

    List<Marker> markerList= new ArrayList<>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public individualDustbin() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment individualDustbin.
     */
    // TODO: Rename and change types and number of parameters
    public static individualDustbin newInstance(String param1, String param2) {
        individualDustbin fragment = new individualDustbin();
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
        View view = inflater.inflate(R.layout.fragment_individual_dustbin, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment)this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return view;
    }

    public void onMapReady(GoogleMap googleMap){
        mMap = googleMap;

        mPoint1 = mMap.addMarker(new MarkerOptions().position(point1).title("Dustbin 1"));
        mPoint1.setTag(0);
        markerList.add(mPoint1);

        mPoint2 = mMap.addMarker(new MarkerOptions().position(point2).title("Dustbin 2"));
        mPoint2.setTag(0);
        markerList.add(mPoint2);

        mPoint3 = mMap.addMarker(new MarkerOptions().position(point3).title("Dustbin 3"));
        mPoint3.setTag(0);
        markerList.add(mPoint3);

        mPoint4 = mMap.addMarker(new MarkerOptions().position(point4).title("Dustbin 4"));
        mPoint4.setTag(0);
        markerList.add(mPoint4);

        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        for(Marker m : markerList){
            LatLng latLng = new LatLng(m.getPosition().latitude,m.getPosition().longitude);
            mMap.addMarker(new MarkerOptions().position(latLng));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5));
        }

//        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//            @Override
//            public void onMapClick(@NonNull LatLng latLng) {
//                MarkerOptions markerOptions = new MarkerOptions();
//
//                markerOptions.position(latLng);
//                markerOptions.title(latLng.latitude+":"+latLng.latitude);
//                mMap.clear();
//                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
//                mMap.addMarker(markerOptions);
//            }
//        });
    }
}