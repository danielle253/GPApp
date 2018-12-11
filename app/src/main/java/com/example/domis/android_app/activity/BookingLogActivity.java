package com.example.domis.android_app.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.domis.android_app.R;
import com.example.domis.android_app.model.UserDetails;
import com.example.domis.android_app.repository.FirebaseRepository;

import java.util.ArrayList;
import java.util.List;

public class BookingLogActivity extends AppCompatActivity {

    private Button historyButton;
    private ListView listView;
    private List<String> bookingList;
    private FirebaseRepository rep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_log);

        historyButton = findViewById(R.id.historyButton);
        listView = findViewById(R.id.listView);

        rep = new FirebaseRepository();

        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Button ", "clicked");
                List<String> bookings = UserDetails.currentUser.getBookings();
                Log.e("Bookings: ", bookings.toString());
                bookingList = new ArrayList<>();
                for(String b : bookings)
                {
                    bookingList.add(rep.getBooking(b).toString());
                    Log.e("Booking ", rep.getBooking(b).toString());
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                        BookingLogActivity.this,
                        android.R.layout.simple_list_item_1,
                        bookingList );
                listView.setAdapter(arrayAdapter);
            }
        });
    }
}
