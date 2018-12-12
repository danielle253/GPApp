package com.example.domis.android_app.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.domis.android_app.R;
import com.example.domis.android_app.model.Booking;
import com.example.domis.android_app.model.ConfirmedBooking;
import com.example.domis.android_app.model.UserDetails;
import com.example.domis.android_app.repository.FirebaseRepository;

import java.util.ArrayList;
import java.util.List;

public class BookingLogActivity extends AppCompatActivity {

    private Button historyButton;
    private ListView listView;
    private ListView listView2;
    private List<ConfirmedBooking> confirmedBookings;
    private List<String> inprogressList;
    private List<String> completedList;
    private FirebaseRepository rep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_log);

        historyButton = findViewById(R.id.historyButton);
        listView = findViewById(R.id.listView);
        listView2 = findViewById(R.id.listView2);

        rep = new FirebaseRepository();

        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Button ", "clicked");
                List<String> bookings = UserDetails.currentUser.getBookings();
                Log.e("Bookings: ", bookings.toString());
                confirmedBookings = new ArrayList<>();
                for(String b : bookings)
                {
                    Object obj = rep.getBooking(b);
                    Log.e("Booking ", obj.toString());
                    if(obj instanceof ConfirmedBooking)
                    {
                        confirmedBookings.add((ConfirmedBooking) obj);
                    }
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
            }
        });
    }
}
