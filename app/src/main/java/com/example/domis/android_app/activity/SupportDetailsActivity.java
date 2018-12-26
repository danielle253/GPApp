package com.example.domis.android_app.activity;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.domis.android_app.R;
import com.example.domis.android_app.game.GameActivity;
import com.example.domis.android_app.game.GameMainActivity;
import com.example.domis.android_app.model.Message;
import com.example.domis.android_app.model.SupportTicket;
import com.example.domis.android_app.model.TicketDetails;
import com.example.domis.android_app.repository.FirebaseRepository;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class SupportDetailsActivity extends AppCompatActivity {

    private TextView title;
    private ListView list;
    private SupportTicket ticket;
    private FirebaseRepository rep;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support_details);

        rep = new FirebaseRepository();

        title = findViewById(R.id.title);
        list = findViewById(R.id.list);

        ticket = TicketDetails.currentTicket;
        title.setText(ticket.getTitle());

        List<String> messages = new ArrayList<>();
        for(Message m : ticket.getMessages())
        {
            messages.add(m.getSenderID() + ": " + m.getMessage());
        }

        list.setAdapter(new ArrayAdapter<String>(
                SupportDetailsActivity.this,
                android.R.layout.simple_list_item_1,
                messages ));


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
                            startActivity(new Intent(SupportDetailsActivity.this, MapsActivity.class));
                        } /*else if(menuItem.getTitle().equals("Support")){
                            startActivity(new Intent(SupportDetailsActivity.this, SupportActivity.class));
                        }*/ else if(menuItem.getTitle().equals("Booking")){
                            startActivity(new Intent(SupportDetailsActivity.this, BookingLogActivity.class));
                        } else if(menuItem.getTitle().equals("Easter Egg")){
                            startActivity(new Intent(SupportDetailsActivity.this, GameMainActivity.class));
                        } else if(menuItem.getTitle().equals("Log Out")){
                            Log.e("Signing out", "");
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(SupportDetailsActivity.this, MainActivity.class));

//                            finish();
                        }

                        return true;
                    }
                });

    }
}
