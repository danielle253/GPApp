package com.example.domis.android_app.authentication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.domis.android_app.activity.BookingActivity;
import com.example.domis.android_app.R;
import com.example.domis.android_app.activity.MapsActivity;
import com.example.domis.android_app.activity.MenuActivity;
import com.example.domis.android_app.model.User;
import com.example.domis.android_app.model.UserDetails;
import com.example.domis.android_app.repository.FirebaseRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseRepository rep;

    private EditText emailInput;
    private EditText passInput;

    private Button loginButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        //Views
        emailInput = findViewById(R.id.editEmail);
        passInput = findViewById(R.id.editPass);

        //Buttons
        loginButton = findViewById(R.id.loginButton);

        mAuth = FirebaseAuth.getInstance();
        rep = new FirebaseRepository();
    }

    public void loginClick(View v){
        if(!emailInput.getText().toString().isEmpty() && !passInput.getText().toString().isEmpty()) {
            Toast.makeText(LoginActivity.this, "Logging in...",
                    Toast.LENGTH_LONG).show();
            login(emailInput.getText() + "", passInput.getText() + "");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public Object successLogin() {
        final AlertDialog ad = new AlertDialog.Builder(this).create();
        ad.setMessage("Login Successful");
        ad.setButton(DialogInterface.BUTTON_POSITIVE, "Continue",
                (dialog, which) -> {
                    ad.cancel();
                    startMenuActivity();
                });
        ad.show();
        return null;
    }

    public Object failedLogin() {
        final AlertDialog ad = new AlertDialog.Builder(this).create();
        ad.setMessage("Login Failed");
        ad.setButton(DialogInterface.BUTTON_POSITIVE, "Continue",
                (dialog, which) -> ad.cancel());
        ad.show();
        return null;
    }


    public void login(String email, String password) {
        Log.d("", "signIn:" + email);
        //User user = rep.getObject(FirebaseRepository.USERS_REF, "D1B6KN45KRahDUvF5IzZIQ3YSFx2");
        //emailInput.setText(user.getToken());


        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success
                            Log.d(mAuth.getCurrentUser().getUid(), "signInWithEmail:success");
                            UserDetails.setMethod(LoginActivity.this::startMenuActivity);
                            rep.getObject(rep.USERS_REF, mAuth.getCurrentUser().getUid());
                            UserDetails.setMethod(LoginActivity.this::successLogin);
                            UserDetails.runConsumer();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("", "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            UserDetails.setMethod(LoginActivity.this::failedLogin);
                            failedLogin();
                        }
                    }
                });
    }

    public boolean validateForm() {
        return true;
    }

    private Object startMenuActivity() {
        startActivity(new Intent(LoginActivity.this, MenuActivity.class));
        return null;
    }
}
