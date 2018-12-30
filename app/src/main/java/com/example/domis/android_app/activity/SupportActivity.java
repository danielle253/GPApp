package com.example.domis.android_app.activity;

import android.content.Intent;
import android.graphics.Point;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.domis.android_app.R;
import com.example.domis.android_app.authentication.LoginActivity;
import com.example.domis.android_app.game.GameActivity;
import com.example.domis.android_app.game.GameMainActivity;
import com.example.domis.android_app.model.Booking;
import com.example.domis.android_app.model.TicketDetails;
import com.example.domis.android_app.model.User;
import com.example.domis.android_app.model.UserDetails;
import com.example.domis.android_app.repository.FirebaseRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.internal.ObjectConstructor;

import java.util.ArrayList;
import java.util.List;

public class SupportActivity extends AppCompatActivity {

    private Button historyButton;
    private ListView listView;
    private TextView ticketLabel;
    private EditText ticketQuery;
    private Button sendButton;
    private FirebaseRepository rep;
    private List<String> ticketIDs;
    private DrawerLayout mDrawerLayout;
    private ArrayAdapter messageArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);

        rep = new FirebaseRepository();

        historyButton = findViewById(R.id.historyButton);
        listView = findViewById(R.id.listView);
        ticketLabel = findViewById(R.id.newTicketLabel);
        ticketQuery = findViewById(R.id.ticketText);
        sendButton = findViewById(R.id.sendButton);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = size.y / 2;
        listView.setLayoutParams(params);

        historyButton.setOnClickListener(v -> {
            if (historyButton.getText().equals("HIDE")) {
                listView.setAdapter(null);
                historyButton.setText("SHOW");
            } else {
                ticketIDs = UserDetails.currentUser.getTickets();
                List<String> tickets = new ArrayList<>();
                if (ticketIDs != null && !ticketIDs.isEmpty()) {
                    for (String s : ticketIDs) {
                        //rep.getSupportTicket(s);
                        TicketDetails.setMethod(this::method);
                        TicketDetails.runConsumer();
                        tickets.add(TicketDetails.currentTicket.getTitle());
                    }
                    messageArray = new ArrayAdapter<>(
                            SupportActivity.this,
                            android.R.layout.simple_list_item_1,
                            tickets);
                    listView.setAdapter(messageArray);
                    historyButton.setText("HIDE");
                }
            }

        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            //rep.getSupportTicket(ticketIDs.get(position));
            startActivity(new Intent(SupportActivity.this, SupportDetailsActivity.class));
        });

        sendButton.setOnClickListener(v -> {
            String message = ticketQuery.getText().toString();
            Log.e("Message: ", message);
            if (message != null && !message.isEmpty()) {
                messageArray.add(message);
                //rep.set("SUPPORT");
                //rep.createSupportTicket(message);
            } else {
                Toast.makeText(SupportActivity.this, "Invalid message",
                        Toast.LENGTH_LONG).show();
            }
        });


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
                            startActivity(new Intent(SupportActivity.this, MapsActivity.class));
                        } /*else if(menuItem.getTitle().equals("Support")){
                            startActivity(new Intent(SupportActivity.this, SupportActivity.class));
                        }*/ else if(menuItem.getTitle().equals("Booking")){
                            startActivity(new Intent(SupportActivity.this, BookingLogActivity.class));
                        } else if(menuItem.getTitle().equals("Easter Egg")){
                            startActivity(new Intent(SupportActivity.this, GameMainActivity.class));
                        } else if(menuItem.getTitle().equals("Log Out")){
                            Log.e("Signing out", "");
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(SupportActivity.this, MainActivity.class));

//                            finish();
                        }

                        return true;
                    }
                });


    }

    private Object method() {
        return null;
    }
}
