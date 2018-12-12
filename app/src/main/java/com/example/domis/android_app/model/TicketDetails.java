package com.example.domis.android_app.model;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class TicketDetails {

    public static SupportTicket currentTicket;
    public static List<Message> currentMessages;

    public static Supplier<Object> consumer;

    public static void setCurrentTicket(SupportTicket ticket){
        currentTicket = ticket;
    }

    public static void setMessages(List<Message> messages)
    {
        currentMessages = messages;
    }

    public static void setMethod(Supplier<Object> con){
        consumer = con;
    }

    public static void runConsumer(){
        consumer.get();
    }

}
