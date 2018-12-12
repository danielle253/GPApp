package com.example.domis.android_app.activity;

import android.content.Intent;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
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
import com.example.domis.android_app.repository.FirebaseRepository;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class SupportActivity extends AppCompatActivity {

    private Button historyButton;
    private ListView listView;
    private TextView ticketLabel;
    private EditText ticketQuery;
    private FirebaseRepository rep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);

        rep = new FirebaseRepository();

        historyButton = findViewById(R.id.historyButton);
        listView = findViewById(R.id.listView);
        ticketLabel = findViewById(R.id.newTicketLabel);
        ticketQuery = findViewById(R.id.ticketText);

        Display display = getWindowManager().getDefaultDisplay();

        //Getting the screen resolution into point object
        Point size = new Point();
        display.getSize(size);

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = size.y / 2;
        listView.setLayoutParams(params);

        ArrayAdapter<String> messageArray = new ArrayAdapter<String>(
                SupportActivity.this,
                android.R.layout.simple_list_item_1,
                completedList );



    }
}
