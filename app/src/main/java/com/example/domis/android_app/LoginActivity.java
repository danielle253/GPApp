package com.example.domis.android_app;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    private FirebaseController firebaseController;
    private AlertDialog ad;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        firebaseController = FirebaseController.getInstance();
        PersonalData.LOGIN_ACTIVITY = this;

        final EditText emailInput = findViewById(R.id.editEmail);
        final EditText passInput = findViewById(R.id.editPass);
        Button loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Editable userEmail = emailInput.getText();
                Editable userPass = passInput.getText();

                User user = new User(null, userEmail.toString(), userPass.toString());
                System.out.println(userEmail);
                System.out.println(userPass);
                //do some weird firebase shit to make map appear
                //successLogin(firebaseController.getUser(user));
                firebaseController.checkUserLogin(user);
            }
        });
    }

    public void successLogin(boolean success) {
        ad = new AlertDialog.Builder(this).create();
        if(success)
        {
            ad.setMessage("Login Successful");
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
            ad.setMessage("Login Failed");
            ad.setButton(DialogInterface.BUTTON_POSITIVE, "Continue",
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
        startActivity( new Intent(LoginActivity.this, MapsActivity.class));
    }
}
