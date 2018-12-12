package com.example.domis.android_app.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.domis.android_app.R;
import com.example.domis.android_app.model.Message;
import com.example.domis.android_app.model.SupportTicket;
import com.example.domis.android_app.repository.FirebaseRepository;

import java.util.ArrayList;
import java.util.List;

public class SupportDetailsActivity extends AppCompatActivity {

    private TextView title;
    private ListView list;
    private SupportTicket ticket;
    private FirebaseRepository rep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support_details);

        rep = new FirebaseRepository();

        title = findViewById(R.id.title);
        list = findViewById(R.id.list);

        Intent in = getIntent();
        ticket = rep.getSupportTicket(in.getStringExtra("id"));
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
    }
}
