package com.example.domis.android_app.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.domis.android_app.R;
import com.example.domis.android_app.model.Booking;
import com.example.domis.android_app.model.User;
import com.example.domis.android_app.model.UserDetails;

import java.util.ArrayList;
import java.util.List;

public class SupportActivity extends AppCompatActivity {

    private Button historyButton;
    private ListView listView;
    private TextView ticketLabel;
    private EditText ticketQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);

        historyButton = findViewById(R.id.historyButton);
        listView = findViewById(R.id.listView);
        ticketLabel = findViewById(R.id.newTicketLabel);
        ticketQuery = findViewById(R.id.ticketText);




    }
}
