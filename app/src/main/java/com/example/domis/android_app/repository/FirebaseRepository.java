package com.example.domis.android_app.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.domis.android_app.model.Booking;
import com.example.domis.android_app.model.User;
import com.example.domis.android_app.model.UserDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

    public void getBooking() {
        /*myRef.child("BOOKINGS").child("-LQxkKZYONj7ZyQcmSx1").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Booking booking = dataSnapshot.getValue(Booking.class);
                Log.e("Done", "Booking");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
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
                        .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                            @Override
                            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                if (task.isSuccessful()) {
                                    User user = UserDetails.currentUser;
                                    user.setToken(task.getResult().getToken());

                                    Map<String, Object> map = new HashMap<String, Object>();
                                    map.put(reference, user);

                                    users.updateChildren(map);
                                }
                            }
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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

