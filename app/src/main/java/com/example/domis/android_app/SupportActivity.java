package com.example.domis.android_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

public class SupportActivity extends AppCompatActivity {

    private Button bookingsButton;
    private Button suppButton;
    private Button logoutButton;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);

        bookingsButton = findViewById(R.id.bookingButton);
        suppButton = findViewById(R.id.suppButton);
        logoutButton = findViewById(R.id.logoutButton);
        listView = findViewById(R.id.listView);



    }
}
