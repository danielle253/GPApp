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
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import java.util.Locale;

public class BookingActivity extends AppCompatActivity {

    FirebaseRepository rep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        rep = new FirebaseRepository();
    }

    public void makeBooking(View v){
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            //VALIDATION HERE !!!!!!!!!!!!!!!!!!!!
            String source = ((EditText) findViewById(R.id.editSource)).getText() + "";
            String destination = ((EditText) findViewById(R.id.editDestination)).getText() + "";

            List<Address> source_addresses = geocoder.getFromLocationName(source, 1);
            List<Address> destination_addresses = geocoder.getFromLocationName(destination, 1);

            Address source_address = source_addresses.get(0);
            Address destination_address = destination_addresses.get(0);

            Coordinate source_cord = new Coordinate(
                    source_address.getLatitude(),
                    source_address.getLongitude());

            Coordinate destination_cord = new Coordinate(
                    destination_address.getLatitude(),
                    destination_address.getLongitude());

            Booking booking = new Booking(source_cord, destination_cord, FirebaseAuth.getInstance().getUid());

            rep.booking(booking);

        } catch (Throwable e){}

    }
}
