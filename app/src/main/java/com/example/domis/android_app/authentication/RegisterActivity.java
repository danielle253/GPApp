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

import com.example.domis.android_app.activity.MapsActivity;
import com.example.domis.android_app.repository.FirebaseController;
import com.example.domis.android_app.R;
import com.example.domis.android_app.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private EditText userEmailIn;
    private EditText userPass1In;
    private EditText userPass2In;

    private Button registerButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);

        userEmailIn = findViewById(R.id.editEmail);
        userPass1In = findViewById(R.id.editPass1);
        userPass2In = findViewById(R.id.editPass2);

        registerButton = findViewById(R.id.regButton);

        mAuth = FirebaseAuth.getInstance();
    }

    private void sendEmailVerification() {

        // Send verification email
        // [START send_email_verification]
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this,
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e("", "sendEmailVerification", task.getException());
                            Toast.makeText(RegisterActivity.this,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    public void registerClick(View v) {
        if (valid())
            registrate(userEmailIn.getText() + "", userPass1In.getText() + "");
    }

    //Validation Requires Here!!!
    private boolean valid() {

        return true;
    }

    private void registrate(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success
                            Log.d("", "createUserWithEmail:success");
                            Toast.makeText(RegisterActivity.this, "Authentication Succes.",
                                    Toast.LENGTH_SHORT).show();
                            successRegister();
                            //FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            failedRegister();
                        }
                    }
                });
    }

    private void successRegister() {
        AlertDialog ad = new AlertDialog.Builder(this).create();

        ad.setMessage("Registered Successfully");
        ad.setButton(DialogInterface.BUTTON_POSITIVE, "Continue",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       // startMapActivity();
                        sendEmailVerification();
                    }
                });

        ad.show();
    }

    private void failedRegister() {
        final AlertDialog ad = new AlertDialog.Builder(this).create();
        ad.setMessage("User Exists");
        ad.setButton(DialogInterface.BUTTON_POSITIVE, "Retry",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ad.cancel();
                    }
                });
        ad.show();
    }

    private void startMapActivity() {
        startActivity(new Intent(RegisterActivity.this, MapsActivity.class));
    }
}
