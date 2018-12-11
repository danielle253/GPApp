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
        Toast.makeText(LoginActivity.this, "Logging in...",
            Toast.LENGTH_LONG).show();
        login(emailInput.getText() + "", passInput.getText() + "");
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void successLogin() {
        final AlertDialog ad = new AlertDialog.Builder(this).create();
        ad.setMessage("Login Successful");
        ad.setButton(DialogInterface.BUTTON_POSITIVE, "Continue",
                (dialog, which) -> {
                    startMenuActivity();
                    ad.cancel();
                });
        ad.show();
    }

    public void failedLogin() {
        final AlertDialog ad = new AlertDialog.Builder(this).create();
        ad.setMessage("Login Failed");
        ad.setButton(DialogInterface.BUTTON_POSITIVE, "Continue",
                (dialog, which) -> ad.cancel());
    }

    public void login(String email, String password) {
        Log.d("", "signIn:" + email);


        //UserDetails.setCurrentUser(rep.getUser(mAuth.getCurrentUser().getUid()));
        //successLogin();

        /*
        if (!validateForm()) {
            return;
        }
        */


        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success
                            Log.d(mAuth.getCurrentUser().getUid(), "signInWithEmail:success");
                            User user = rep.getUser(mAuth.getCurrentUser().getUid());
                            Log.e("USER: ", user.toString());
                            UserDetails.setCurrentUser(user);
                            //Log.e("USER TOKEN: ", UserDetails.currentUser.getToken());
                            Log.e("USER ACTIVE: ", UserDetails.currentUser.isActive() + "");
                            Log.e("USER BALANCE: ", UserDetails.currentUser.getBalance() + "");
                            UserDetails.currentUser.setEmail(email);
                            Log.e("User bookings: ", UserDetails.currentUser.getBookings().toString());
                            successLogin();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("", "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                            failedLogin();
                        }
                    }
                });
    }

    public boolean validateForm() {
        return true;
    }

    private void startMenuActivity() {
        startActivity(new Intent(LoginActivity.this, MenuActivity.class));
    }
}
