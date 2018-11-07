package com.example.domis.android_app;

import java.util.ArrayList;

public class User {

    private ArrayList<String> auth;
    private String password;
    private String username;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(ArrayList<String> auth, String username, String password) {
        this.auth = auth;
        this.password = password;
        this.username = username;
    }

    public void setAuth(ArrayList<String> auth)
    {
        this.auth = auth;
    }

    public ArrayList<String> getAuth()
    {
        return auth;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}