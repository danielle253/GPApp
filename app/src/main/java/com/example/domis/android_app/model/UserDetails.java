package com.example.domis.android_app.model;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class UserDetails {

    public static User currentUser;
    public static String UID;

    public static void setCurrentUser(User user){
        if(user == null)
            currentUser = new User(new ArrayList<String>(), 0.0);
        else {
            if (user.getBookings() == null)
                user.setBookings(new ArrayList<String>());

            currentUser = user;
        }

        UID = FirebaseAuth.getInstance().getUid();
    }
}
