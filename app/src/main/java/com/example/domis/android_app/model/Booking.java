package com.example.domis.android_app.model;


import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class Booking {

    private LatLng source;
    private LatLng destination;
    private String userID;
    private boolean wheelchair;

    public Booking() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Booking(LatLng source, LatLng destination, String userID, boolean wheelchair) {
        this.source = source;
        this.destination = destination;
        this.userID = userID;
        this.wheelchair = wheelchair;
    }

    public LatLng getSource() {
        return source;
    }

    public void setSource(LatLng source) {
        this.source = source;
    }

    public LatLng getDestination() {
        return destination;
    }

    public void setDestination(LatLng destination) {
        this.destination = destination;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public boolean getWheelchair() { return wheelchair; }

    public void setWheelchair(boolean wheelchair) { this.wheelchair = wheelchair; }
}