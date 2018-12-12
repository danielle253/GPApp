package com.example.domis.android_app.model;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.function.Supplier;

public class BookingDetails {

    public static Booking currentBooking;

    public static Supplier<Object> consumer;

    public static void setCurrentBooking(Booking booking){
        currentBooking = booking;
    }

    public static void setMethod(Supplier<Object> con){
        consumer = con;
    }

    public static void runConsumer(){
        consumer.get();
    }
}
