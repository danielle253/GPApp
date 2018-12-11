package com.example.domis.android_app.model;

import java.util.ArrayList;

public class User extends Entity {

    private ArrayList<String> bookings;
    private double balance;
    private String email;
    private boolean active;
    private String token;

    private User() {}

    public User(ArrayList<String> bookings, double balance) {
        this.bookings = bookings;
        this.balance = balance;

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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