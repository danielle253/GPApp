package com.example.domis.android_app.model;


import java.util.ArrayList;

public class Booking {

    private Coordinate source;
    private Coordinate destination;
    private String userID;

    private Booking() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Booking(Coordinate source, Coordinate destination, String userID) {
        this.source = source;
        this.destination = destination;
        this.userID = userID;
    }

    public Coordinate getSource() {
        return source;
    }

    public void setSource(Coordinate source) {
        this.source = source;
    }

    public Coordinate getDestination() {
        return destination;
    }

    public void setDestination(Coordinate destination) {
        this.destination = destination;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}