package com.example.domis.android_app.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.domis.android_app.model.Booking;
import com.example.domis.android_app.model.User;
import com.example.domis.android_app.model.UserDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.collect.ImmutableMap;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import com.example.domis.android_app.model.Entity;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class FirebaseRepository {

    private DatabaseReference myRef;
    public static String
            USERS_REF = "USERS",
            CARS_REF = "CARS",
            ADMIN_REF = "ADMIN_USERS",
            BOOKING_REF = "BOOKINGS",
            BOOKING_LOG_REF = "BOOKINGS_LOG";

    private final Map<String, Class> CLASS_REF = ImmutableMap.<String, Class>builder()
            .put(USERS_REF, User.class)
            //.put(CARS_REF, Car.class)
            //.put(ADMIN_REF, Admin.class)
            .put(BOOKING_REF, Booking.class)
            .put(BOOKING_LOG_REF, Booking.class)
            .build();

    public FirebaseRepository() {
        myRef = FirebaseDatabase.getInstance().getReference();
    }

    public void booking(final Booking booking) {
        myRef.child("BOOKINGS").push().setValue(booking, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                        UserDetails.currentUser.getBookings().add(databaseReference.getKey());
                        updateCurrentUserBookings();
                    }
                }
        );
        Log.e("Done", "Booking");
    }

    public Booking getBooking(String bookingID)
    {
        Booking b = getObject(BOOKING_LOG_REF, bookingID);
        if(b == null)
        {
            b = getObject(BOOKING_REF, bookingID);
        }
        return b;
    }

    public User getUser(String userID)
    {
        return getObject(USERS_REF, userID);
    }

    public ArrayList<Booking> getUserBookings(String userID) {
        List<Booking> list = getObjectList("BOOKINGS", Booking.class);
        list.addAll(getObjectList("BOOKINGS_LOG", Booking.class));
        ArrayList<Booking> usersBookings = new ArrayList<Booking>();
        for(Booking b : list)
        {
            if(b.getUserID().equals(userID))
            {
                usersBookings.add(b);
            }
        }
        return usersBookings;
    }

    private void updateCurrentUserBookings() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("bookings", UserDetails.currentUser.getBookings());
        myRef.child("USERS").child(UserDetails.UID).updateChildren(map);
    }

    public void getCurrentUserDetails(final String reference) {
        final DatabaseReference users = myRef.child("USERS");
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserDetails.setCurrentUser(dataSnapshot.child(reference).getValue(User.class));

                FirebaseInstanceId.getInstance().getInstanceId()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                User user = UserDetails.currentUser;
                                user.setToken(task.getResult().getToken());

                                Map<String, Object> map = new HashMap<String, Object>();
                                map.put(reference, user);
                                Log.e("User bookings: ", UserDetails.currentUser.getBookings().toString());
                                users.updateChildren(map);
                            }
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public <T extends Entity> List<T> getObjectList(String reference, Class<T> c) {
        final Waiter waiter = new Waiter();

        myRef.child(reference).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                snapshot.getChildren().forEach(i -> {
                    T item = i.getValue(c);
                    item.setKey(i.getKey());
                    waiter.getList().add(item);
                });
                waiter.respond();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.out.println("Query is Cancelled");
            }
        });

        waiter.waitRespond();

        return waiter.getList();
    }

    public <T extends Entity> T getObject(String reference, String child) {
        final Waiter waiter = new Waiter();
        myRef.child(reference).child(child).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if(snapshot.getValue() != null) {
                    waiter.setObject(snapshot.getValue(CLASS_REF.get(reference)));
                    ((T) waiter.getObject()).setKey(snapshot.getKey());
                }
                waiter.respond();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.out.println("Failed To Get " + child);
                waiter.respond();
            }

        });

        waiter.waitRespond();

        return (T) waiter.object;
    }

    private class Waiter
    {
        private final int TIMEOUT = 10000;

        private final List list;
        private Object object;

        private final FutureTask<Void> future;
        private final ExecutorService exec;

        public Waiter() {
            future = new FutureTask<Void>(() -> null);
            exec = Executors.newFixedThreadPool(1);
            list = new ArrayList();
        }

        public void waitRespond() {
            try {
                future.get(TIMEOUT, TimeUnit.MILLISECONDS);
            } catch (Throwable e) {System.out.println(e);}
        }

        public void respond() {
            exec.execute(future);
        }

        public void setObject(Object object) {
            this.object = object;
        }

        public List getList() {
            return list;
        }

        public Object getObject() {
            return object;
        }
    }


}

