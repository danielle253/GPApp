package com.example.domis.android_app.model;

import com.example.domis.android_app.activity.SupportActivity;

import java.util.List;

public class SupportTicket extends Entity {

    private List<Message> messages;
    private String state;
    private String title;

    public SupportTicket()
    {

    }

    public SupportTicket(List<Message> messages, String title)
    {
        this.messages = messages;
        this.title = title;
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

    public void addMessage(Message message)
    {
        messages.add(message);
    }
}
