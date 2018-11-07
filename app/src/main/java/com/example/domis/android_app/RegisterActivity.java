package com.example.domis.android_app;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {

    private EditText userEmailIn;
    private EditText userPass1In;
    private EditText userPass2In;
    private Button registerButton;
    private AlertDialog ad;
    private FirebaseController fbController;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);

        fbController = FirebaseController.getInstance();

        userEmailIn = findViewById(R.id.editEmail);
        userPass1In = findViewById(R.id.editPass1);
        userPass2In = findViewById(R.id.editPass2);
        registerButton = findViewById(R.id.regButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = userEmailIn.getText().toString();
                if(userPass1In.getText().toString().equals(userPass2In.getText().toString()))
                {
                    String pass = userPass1In.getText().toString();
                    ArrayList<String> userTypes = new ArrayList<String>();
                    userTypes.add("USER");
                    User user = new User(userTypes, email, pass);
                    //boolean success = fbController.registerUser(user);
                    //successRegister(success);
                }
            }
        });

    }

    private void successRegister(boolean success) {
        ad = new AlertDialog.Builder(this).create();
        if(success)
        {
            ad.setMessage("Registered Successfully");
            ad.setButton(DialogInterface.BUTTON_POSITIVE, "Continue",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startMapActivity();
                        }
                    });
        }
        else
        {
            ad.setMessage("User Exists");
            ad.setButton(DialogInterface.BUTTON_POSITIVE, "Retry",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ad.cancel();
                        }
                    });
        }
        ad.show();
    }

    private void startMapActivity()
    {
        startActivity( new Intent(RegisterActivity.this, MapsActivity.class));
    }
}
