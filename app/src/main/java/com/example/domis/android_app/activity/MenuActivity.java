package com.example.domis.android_app.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.domis.android_app.R;
import com.example.domis.android_app.authentication.LoginActivity;
import com.example.domis.android_app.game.GameMainActivity;
import com.example.domis.android_app.model.UserDetails;
import com.google.firebase.auth.FirebaseAuth;


public class MenuActivity extends AppCompatActivity {

    private TextView userView;
    private Button bookingsButton;
    private Button suppButton;
    private Button logoutButton;
    private Button mapButton;
    private Button gameButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        mAuth = FirebaseAuth.getInstance();

        userView = findViewById(R.id.userLogin);
        userView.setText("User: " + UserDetails.currentUser.getEmail());

        bookingsButton = findViewById(R.id.bookingButton);
        suppButton = findViewById(R.id.suppButton);
        logoutButton = findViewById(R.id.logoutButton);
        mapButton = findViewById(R.id.mapButton);
        gameButton = findViewById(R.id.gameButton);

        bookingsButton.setOnClickListener(v -> startActivity(new Intent(MenuActivity.this, BookingLogActivity.class)));

        suppButton.setOnClickListener(v -> startActivity(new Intent(MenuActivity.this, SupportActivity.class)));

        mapButton.setOnClickListener(v -> startActivity(new Intent(MenuActivity.this, MapsActivity.class)));

        gameButton.setOnClickListener(v -> startActivity(new Intent(MenuActivity.this, GameMainActivity.class)));

        logoutButton.setOnClickListener(v -> {
            Log.e("Signing out", "");
            FirebaseAuth.getInstance().signOut();
            finish();
        });
    }

    @Override
    public void onBackPressed()
    {
        Log.e("Signing out", "");
        FirebaseAuth.getInstance().signOut();
        finish();
    }
}
