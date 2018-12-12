package com.example.domis.android_app.model;

import com.example.domis.android_app.activity.SupportActivity;

import java.util.List;

public class SupportTicket extends Entity {

    private String id;
    private List<Message> messages;
    private String state;
    private String title;

    public SupportTicket()
    {

    }

    public SupportTicket(String id, List<Message> messages, String state, String title)
    {
        this.id = id;
        this.messages = messages;
        this.state = state;
        this.title = title;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
