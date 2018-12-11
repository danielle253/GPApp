package com.example.domis.android_app.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.example.domis.android_app.R;


public class MenuActivity extends AppCompatActivity {

    private Button bookingsButton;
    private Button suppButton;
    private Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        bookingsButton = findViewById(R.id.bookingButton);
        suppButton = findViewById(R.id.suppButton);
        logoutButton = findViewById(R.id.logoutButton);

        

    }
}
