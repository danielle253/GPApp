package com.example.domis.android_app.activity;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.domis.android_app.R;
import com.example.domis.android_app.game.GameMainActivity;
import com.example.domis.android_app.model.Booking;
import com.example.domis.android_app.model.ConfirmedBooking;
import com.example.domis.android_app.model.UserDetails;
import com.example.domis.android_app.repository.FirebaseRepository;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class BookingLogActivity extends AppCompatActivity {

    private ListView listView;
    private ListView listView2;
    private List<ConfirmedBooking> confirmedBookings;
    private List<String> inprogressList;
    private List<String> completedList;
    private FirebaseRepository rep;

    private DrawerLayout mDrawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_log);

        listView = findViewById(R.id.listView);
        listView2 = findViewById(R.id.listView2);

        rep = new FirebaseRepository();

        Log.e("Button ", "clicked");
        List<String> bookings = UserDetails.currentUser.getBookings();
        Log.e("Bookings: ", bookings.toString());
        confirmedBookings = new ArrayList<>();
        for(String b : bookings)
        {
            /*
            Object obj = rep.getBooking(b);
            Log.e("Booking ", obj.toString());
            if(obj instanceof ConfirmedBooking)
            {
                confirmedBookings.add((ConfirmedBooking) obj);
            }
            */
        }
        inprogressList = new ArrayList<>();
        completedList = new ArrayList<>();
        for(ConfirmedBooking cb : confirmedBookings)
        {
            if(cb.getState().equals("COMPLETED"))
            {
                completedList.add("From: " + cb.getSourceAddress()
                        + "\nTo: " + cb.getDestinationAddress()
                        + "\nDuration: " + cb.getDuration());
            }
            else
            {
                inprogressList.add("From: " + cb.getSourceAddress()
                        + "\nTo: " + cb.getDestinationAddress());
            }
        }
        ArrayAdapter<String> completedArray = new ArrayAdapter<String>(
                BookingLogActivity.this,
                android.R.layout.simple_list_item_1,
                completedList );
        ArrayAdapter<String> inprogressArray = new ArrayAdapter<String>(
                BookingLogActivity.this,
                android.R.layout.simple_list_item_1,
                completedList );
        listView.setAdapter(inprogressArray);
        listView.setAdapter(completedArray);




        //-------------------------
        //---------- MENU ---------
        //-------------------------
        mDrawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        if(menuItem.getTitle().equals("Map")){
                            startActivity(new Intent(BookingLogActivity.this, MapsActivity.class));
                        } else if(menuItem.getTitle().equals("Support")){
                            startActivity(new Intent(BookingLogActivity.this, SupportActivity.class));
                        } else if(menuItem.getTitle().equals("Easter Egg")){
                            startActivity(new Intent(BookingLogActivity.this, GameMainActivity.class));
                        } else if(menuItem.getTitle().equals("Log Out")){
                            Log.e("Signing out", "");
                            FirebaseAuth.getInstance().signOut();
                            finish();
                        }

                        return true;
                    }
                });


    }
}
