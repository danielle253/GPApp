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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private EditText userEmailIn;
    private EditText userPass1In;
    private EditText userPass2In;

    private Button registerButton;

    private FirebaseRepository rep;
    private SignInButton mGoogleSignInButton;
    private GoogleApiClient mGoogleApiClient;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);

        rep = new FirebaseRepository();

        userEmailIn = findViewById(R.id.editEmail);
        userPass1In = findViewById(R.id.editPass1);
        userPass2In = findViewById(R.id.editPass2);

        registerButton = findViewById(R.id.regButton);

        mAuth = FirebaseAuth.getInstance();

        mGoogleSignInButton = (SignInButton)findViewById(R.id.google_sign_in_button);
        mGoogleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithGoogle();
            }
        });
    }

    private void sendEmailVerification() {

        // Send verification email
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
            register(userEmailIn.getText() + "", userPass1In.getText() + "");
    }

    //Validation Requires Here!!!
    private boolean valid() {

        return true;
    }

    private void register(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success
                            Log.d("createUserWithEmail", "success");
                            Toast.makeText(RegisterActivity.this, "Authentication Succes.",
                                    Toast.LENGTH_SHORT).show();

                            User user = new User(0);
                            user.setEmail(mAuth.getCurrentUser().getEmail());
                            user.setActive(true);

                            rep.add(FirebaseRepository.USERS_REF,
                                    mAuth.getCurrentUser().getUid(), user);

                            successRegister();
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
                        sendEmailVerification();
                        finish();
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

    private static final int RC_SIGN_IN = 9001;

    private void signInWithGoogle() {
        if(mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        final Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            if(result.isSuccess()) {
                final GoogleApiClient client = mGoogleApiClient;
                register(result.getSignInAccount().getEmail(), result.getSignInAccount().getId());
                Intent myIn = new Intent(RegisterActivity.this, MapsActivity.class);
                //successRegister();

            } else {
                failedRegister();

            }
        }
    }
}
