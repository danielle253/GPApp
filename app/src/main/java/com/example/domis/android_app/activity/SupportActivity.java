package com.example.domis.android_app.activity;

import android.content.Intent;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
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
import com.example.domis.android_app.model.Booking;
import com.example.domis.android_app.model.TicketDetails;
import com.example.domis.android_app.model.User;
import com.example.domis.android_app.model.UserDetails;
import com.example.domis.android_app.repository.FirebaseRepository;
import com.google.firebase.auth.FirebaseAuth;

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
            if(historyButton.getText().equals("HIDE"))
            {
                listView.setAdapter(null);
                historyButton.setText("SHOW");
            }
            else
            {
                ticketIDs = UserDetails.currentUser.getSupportTickets();
                List<String> tickets = new ArrayList<>();
                if(ticketIDs != null && !ticketIDs.isEmpty())
                {
                    for(String s : ticketIDs)
                    {
                        rep.getSupportTicket(s);
                        tickets.add(TicketDetails.currentTicket.getTitle());
                    }
                    ArrayAdapter<String> messageArray = new ArrayAdapter<>(
                        SupportActivity.this,
                        android.R.layout.simple_list_item_1,
                        tickets );
                    listView.setAdapter(messageArray);
                    historyButton.setText("HIDE");
                }
            }

        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            rep.getSupportTicket(ticketIDs.get(position));
            startActivity(new Intent(SupportActivity.this, SupportDetailsActivity.class));
        });

        sendButton.setOnClickListener(v -> {
            String message = ticketQuery.getText().toString();
            Log.e("Message: ", message);
            if(message != null && !message.isEmpty())
            {
                rep.createSupportTicket(message);
            }
            else
            {
                Toast.makeText(SupportActivity.this, "Invalid message",
                        Toast.LENGTH_LONG).show();
            }
        });


    }
}
