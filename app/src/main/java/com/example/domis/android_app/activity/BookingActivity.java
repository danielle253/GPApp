package com.example.domis.android_app.activity;

import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.domis.android_app.R;
import com.example.domis.android_app.model.Booking;
import com.example.domis.android_app.model.Coordinate;
import com.example.domis.android_app.repository.FirebaseRepository;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import java.util.Locale;

public class BookingActivity extends AppCompatActivity {

    FirebaseRepository rep;
    private SupportPlaceAutocompleteFragment pafSrc;
    private SupportPlaceAutocompleteFragment pafDest;
    private LatLng src;
    private LatLng dest;
    private Booking booking;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        booking = new Booking();
        booking.setUserID(FirebaseAuth.getInstance().getUid());

        rep = new FirebaseRepository();

        pafSrc = (SupportPlaceAutocompleteFragment) getSupportFragmentManager().findFragmentById(R.id.place_autocomplete_src);
        pafSrc.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                //addMarker(place);
                booking.setSource(place.getLatLng());
            }

            @Override
            public void onError(Status status) {
                Log.d("Maps", "An error occurred: " + status);
            }
        });

        pafDest = (SupportPlaceAutocompleteFragment) getSupportFragmentManager().findFragmentById(R.id.place_autocomplete_dest);
        pafDest.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                //addMarker(place);
                booking.setDestination(place.getLatLng());
            }

            @Override
            public void onError(Status status) {
                Log.d("Maps", "An error occurred: " + status);
            }
        });


    }

    public void makeBooking(View v){
        try {

            //Booking booking = new Booking(src, dest, FirebaseAuth.getInstance().getUid());
            if(!(booking.getSource() == null || booking.getDestination() == null))
            {
                rep.booking(booking);
            }


        } catch (Throwable e){}

    }

}
