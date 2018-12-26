package com.example.domis.android_app.game;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.example.domis.android_app.R;
import com.example.domis.android_app.activity.BookingLogActivity;
import com.example.domis.android_app.activity.MainActivity;
import com.example.domis.android_app.activity.MapsActivity;
import com.example.domis.android_app.activity.SupportActivity;
import com.google.firebase.auth.FirebaseAuth;

public class GameMainActivity extends AppCompatActivity implements View.OnClickListener {

    //image button
    private ImageButton buttonPlay;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_main);

        //setting the orientation to landscape
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //getting the button
        buttonPlay = (ImageButton) findViewById(R.id.buttonPlay);

        //adding a click listener
        buttonPlay.setOnClickListener(this);

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

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here
                        if(menuItem.getTitle().equals("Map")){
                            startActivity(new Intent(GameMainActivity.this, MapsActivity.class));
                        } else if(menuItem.getTitle().equals("Support")){
                            startActivity(new Intent(GameMainActivity.this, SupportActivity.class));
                        } else if(menuItem.getTitle().equals("Booking")){
                            startActivity(new Intent(GameMainActivity.this, BookingLogActivity.class));
                        } else if(menuItem.getTitle().equals("Log Out")){
                            Log.e("Signing out", "");
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(GameMainActivity.this, MainActivity.class));
                            // finish();
                        }

                        return true;
                    }
                });

    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(this, GameActivity.class));
    }
}
