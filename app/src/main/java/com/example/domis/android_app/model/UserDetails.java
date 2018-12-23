package com.example.domis.android_app.model;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class UserDetails {

    public static User currentUser;
    public static String UID;


    public static Supplier<Object> consumer;

    public static void setCurrentUser(User user){
        if(user == null)
            currentUser = new User(0.0);
        else {
            if (user.getBookings() == null)
                user.setBookings(new ArrayList<String>());

            currentUser = user;
        }

        UID = FirebaseAuth.getInstance().getUid();
    }

    public static void setMethod(Supplier<Object> con){
        consumer = con;
    }

    public static void runConsumer(){
        consumer.get();
    }
}
