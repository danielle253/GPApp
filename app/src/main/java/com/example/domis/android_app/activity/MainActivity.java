package com.example.domis.android_app.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.domis.android_app.R;
import com.example.domis.android_app.authentication.LoginActivity;
import com.example.domis.android_app.authentication.RegisterActivity;
import com.example.domis.android_app.model.Booking;
import com.example.domis.android_app.model.Coordinate;
import com.example.domis.android_app.repository.FirebaseRepository;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button loginButton = findViewById(R.id.loginButton);
        Button registerButton = findViewById(R.id.registerButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLoginActivity();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRegisterActivity();
            }
        });

        /*new FirebaseRepository().booking(
                new Booking(
                        new Coordinate(123, 123), new Coordinate(123, 125), "userId"));
        new FirebaseRepository().getBooking();*/
    }

    private void startLoginActivity() {
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
    }

    private void startRegisterActivity() {
        startActivity(new Intent(MainActivity.this, RegisterActivity.class));
    }

    @Override
    public void onBackPressed() {

    }
}
