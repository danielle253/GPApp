package com.example.domis.android_app.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.domis.android_app.model.Booking;
import com.example.domis.android_app.model.BookingDetails;
import com.example.domis.android_app.model.Message;
import com.example.domis.android_app.model.SupportTicket;
import com.example.domis.android_app.model.TicketDetails;
import com.example.domis.android_app.model.User;
import com.example.domis.android_app.model.UserDetails;
import com.google.common.collect.ImmutableMap;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.example.domis.android_app.model.Entity;

public class FirebaseRepository implements Repository{

    private DatabaseReference myRef;
    public static String
            USERS_REF = "USERS",
            //CARS_REF = "CARS",
            BOOKING_REF = "BOOKINGS",
            BOOKING_LOG_REF = "BOOKINGS_LOG",
            SUPPORT_TICKET_REF = "SUPPORT_TICKETS";

    private final Map<String, Class> CLASS_REF = ImmutableMap.<String, Class>builder()
            .put(USERS_REF, User.class)
            //.put(CARS_REF, Car.class)
            //.put(ADMIN_REF, Admin.class)
            .put(BOOKING_REF, Booking.class)
            .put(BOOKING_LOG_REF, Booking.class)
            .put(SUPPORT_TICKET_REF, SupportTicket.class)
            .build();

    public FirebaseRepository() {
        myRef = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public <T extends Entity> void getObjectList(String reference, Consumer<List<T>> consumer) {
        myRef.child(reference).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<T> list = new ArrayList<T>();

                snapshot.getChildren().forEach(i -> {
                    T item = ((T) i.getValue(CLASS_REF.get(reference)));
                    item.setKey(i.getKey());
                    list.add(item);
                });

                consumer.accept(list);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.out.println("Query is Cancelled");
            }
        });
    }

    @Override
    public <T extends Entity> void getObject(String reference, String child, Consumer<T> consumer) {
        myRef.child(reference).child(child).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.e("", snapshot.child(reference).child(child).toString());
                if(snapshot.getValue() != null) {
                    T obj = (T) snapshot.getValue(CLASS_REF.get(reference));
                    obj.setKey(snapshot.getKey());
                    consumer.accept(obj);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("Failed To Get ", child);
            }
        });
    }

    @Override
    public void add(String referece, String child, Object obj) {
        myRef.child(USERS_REF).child(child).setValue(obj);
    }

    @Override
    public <T extends Entity> String push(String reference, T obj) {
        DatabaseReference pushRef = myRef.child(reference).push();
        String key = pushRef.getKey();
        pushRef.setValue(obj);

        return key;
    }

    @Override
    public <T extends Entity> void set(String reference, T obj) {
        myRef.child(reference).setValue(obj);
    }

    @Override
    public void delete(String reference, String child) {
        myRef.child(reference).child(child).removeValue();
    }

    @Override
    public void update(String reference, Map map) {
        myRef.child(reference).updateChildren(map);
    }

}

