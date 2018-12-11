package com.example.domis.android_app.model;

import java.util.ArrayList;
import java.util.List;

public class User extends Entity {

    private List<String> bookings;
    private double balance;
    private String email;
    private boolean active;
    private String token;

    private User() {}

    public User(List<String> bookings, double balance) {
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

    public List<String> getBookings() {
        return bookings;
    }

    public void setBookings(List<String> bookings) {
        this.bookings = bookings;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}