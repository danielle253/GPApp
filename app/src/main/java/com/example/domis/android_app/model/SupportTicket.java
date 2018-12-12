package com.example.domis.android_app.model;

import com.example.domis.android_app.activity.SupportActivity;

import java.util.List;

public class SupportTicket extends Entity {

    private String id;
    private List<Message> messages;

    public SupportTicket()
    {

    }

    public SupportTicket(String id, List<Message> messages)
    {
        this.id = id;
        this.messages = messages;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
