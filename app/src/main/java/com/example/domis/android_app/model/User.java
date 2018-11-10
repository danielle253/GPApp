package com.example.domis.android_app.model;

import java.util.ArrayList;

public class User {

    private ArrayList<String> bookings;
    private double balance;

    private User() {}

    public User(ArrayList<String> bookings, double balance) {
        this.bookings = bookings;
        this.balance = balance;
    }

    public ArrayList<String> getBookings() {
        return bookings;
    }

    public void setBookings(ArrayList<String> bookings) {
        this.bookings = bookings;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}