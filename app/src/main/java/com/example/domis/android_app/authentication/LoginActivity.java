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

import com.example.domis.android_app.R;
import com.example.domis.android_app.activity.MapsActivity;
import com.example.domis.android_app.model.User;
import com.example.domis.android_app.repository.FirebaseRepository;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.function.Consumer;

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

    public void login(String email, String password) {
        Log.d("", "signIn:" + email);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            rep.getObject(rep.USERS_REF, currentUser.getUid(), new Consumer<User>(){

                                @Override
                                public void accept(User user){
                                    if(user.isActive() && currentUser.isEmailVerified()) {
                                        //Over here I am getting an Instance Token to be able to send notification using it later!!
                                        Task<InstanceIdResult> task = FirebaseInstanceId.getInstance().getInstanceId();
                                        task.addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                                            @Override
                                            public void onSuccess(InstanceIdResult authResult) {
                                                user.setToken(authResult.getToken());
                                                rep.set(FirebaseRepository.USERS_REF + "/" +
                                                        mAuth.getCurrentUser().getUid(), user);
                                            }
                                        });

                                        successLogin(user);

                                        // Sign in success
                                        Log.d(currentUser.getUid(), "signInWithEmail:success");

                                    } else {
                                        failedLogin();
                                    }
                                }
                            });
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

    private Object startMenuActivity() {
        startActivity(new Intent(LoginActivity.this, MapsActivity.class));
        return null;
    }

    public Object successLogin(User user) {
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





}
