package com.example.domis.android_app.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.domis.android_app.R;
import com.example.domis.android_app.authentication.LoginActivity;


public class MenuActivity extends AppCompatActivity {

    private String emailIn;
    private TextView userView;
    private Button bookingsButton;
    private Button suppButton;
    private Button logoutButton;
    private Button mapButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Intent in = getIntent();
        emailIn = in.getStringExtra("emailInsert");

        userView = findViewById(R.id.userLogin);
        userView.setText("User: "+emailIn);

        bookingsButton = findViewById(R.id.bookingButton);
        suppButton = findViewById(R.id.suppButton);
        logoutButton = findViewById(R.id.logoutButton);
        mapButton = findViewById(R.id.mapButton);

        suppButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(MenuActivity.this, SupportActivity.class));
            }
        });

        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(MenuActivity.this, MapsActivity.class));
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                finish();
                startActivity(new Intent(MenuActivity.this, LoginActivity.class));
            }
        });




    }
}
