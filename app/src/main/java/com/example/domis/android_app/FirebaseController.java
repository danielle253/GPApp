package com.example.domis.android_app;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.database.*;

import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class FirebaseController {

    private static final FirebaseController ourInstance = new FirebaseController();
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FutureTask<Void> ft;
    private ExecutorService es;

    public static FirebaseController getInstance() {
        return ourInstance;
    }

    private FirebaseController() {
        database = FirebaseDatabase.getInstance();
        ft = new FutureTask<Void>(new Runnable() {
            @Override
            public void run() {

            }
        }, null);
        es = Executors.newFixedThreadPool(1);
        //getUserList();
    }

    public void checkUserLogin(final User user) {
        String userID = user.getUsername();
        Log.d("Search for: ", userID);
        myRef = database.getReference().child("USERS").child(user.getUsername());
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                PersonalData.USER = dataSnapshot.getValue(User.class);
                //Log.e("USER.toString(): ", "===============================================\n");
                PersonalData.validate(user.getPassword());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Error", "Failed To Get the user");
            }
        });
    }

    /**
    public boolean registerUser(User user)
    {
        //getUserList();
        System.out.println("Number of users: " + userList.size());
        int indexOfAt = user.getUsername().indexOf("@");
        String userID = user.getUsername().substring(0, indexOfAt);
        System.out.println("UserID: " + userID);
        userID = userID.replace(".", "");
        System.out.println("UserID: " + userID);
        myRef = database.getReference().child("USERS");
        if(!userList.containsKey(user.getUsername()))
        {
            myRef.child(userID).setValue(user);
            return true;
        }
        else
        {
            return false;
        }
    }
     */
}

