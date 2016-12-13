package com.project.food.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.project.food.R;
import com.project.food.ScrollableMapFragment;

import java.util.List;

/**
 * Created by kostya on 09.12.2016.
 */

public class ContactsFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    private ScrollView scrollView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.contacts_layout, null, false);
        scrollView = (ScrollView) rootView.findViewById(R.id.svContacts);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        ScrollableMapFragment scrollableMapFragment =
                (ScrollableMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        scrollableMapFragment.setListener(new ScrollableMapFragment.OnTouchListener() {
            @Override
            public void onTouch() {
                scrollView.requestDisallowInterceptTouchEvent(true);
            }
        });
        scrollableMapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        int permissionCheck = ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            Toast.makeText(getContext(), "Permissions for your location was not granted!", Toast.LENGTH_LONG).show();
        }

        LatLng address1 = getLocationFromAddress(getContext(), "95, Dzerzhinskogo, Minks, Belarus");
        if (address1 != null) {
            mMap.addMarker(new MarkerOptions().position(address1).title("Marker in Minsk"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(address1));
        }

        LatLng address2 = getLocationFromAddress(getContext(), "10b, Golubeva, Minks, Belarus");
        if (address2 != null) {
            mMap.addMarker(new MarkerOptions().position(address2).title("Golubeva 10b"));
        }

        LatLng address3 = getLocationFromAddress(getContext(), "14b, Golubeva, Minks, Belarus");
        if (address3 != null) {
            mMap.addMarker(new MarkerOptions().position(address3).title("Golubeva 14b"));
        }
    }

    public LatLng getLocationFromAddress(Context context, String strAddress)
    {
        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try
        {
            address = coder.getFromLocationName(strAddress, 5);
            if(address==null)
            {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return p1;

    }


}
