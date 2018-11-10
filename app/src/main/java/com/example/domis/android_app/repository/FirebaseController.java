package com.example.domis.android_app.repository;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.domis.android_app.model.User;
import com.google.firebase.database.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class FirebaseController {
/*
    private static final FirebaseController ourInstance = new FirebaseController();
    private FirebaseDatabase database;


    public static FirebaseController getInstance() {
        return ourInstance;
    }

    private FirebaseController() {
        database = FirebaseDatabase.getInstance();
    }

  *//*  public boolean registerUser(User user)
    {


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
    }*//*

     */
}

