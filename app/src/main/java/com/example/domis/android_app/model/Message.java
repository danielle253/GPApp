package com.example.domis.android_app.model;

public class Message extends Entity {

    private String senderID;
    private String message;

    public Message()
    {

    }

    public Message(String senderID, String message)
    {
        this.senderID = senderID;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }
}
