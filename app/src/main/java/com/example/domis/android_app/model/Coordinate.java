package com.example.domis.android_app.model;

public class Coordinate {
    private double lng;
    private double lat;

    private Coordinate(){}

    public Coordinate(double lat, double lng ){
        this.lng = lng;
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }
}
