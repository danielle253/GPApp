package com.example.domis.android_app.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.domis.android_app.model.Booking;
import com.example.domis.android_app.model.User;
import com.example.domis.android_app.model.UserDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FirebaseRepository {

    private DatabaseReference myRef;

    public FirebaseRepository() {
        myRef = FirebaseDatabase.getInstance().getReference();
    }

    public void booking(final Booking booking) {
        final String bookingKey = myRef.child("BOOKINGS").push().getKey();
        myRef.child("BOOKINGS").push().setValue(booking, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                        UserDetails.currentUser.getBookings().add(bookingKey);
                        updateCurrentUserBookings();
                    }
                }
        );
        Log.e("Done", "Booking");
    }

    public void getBooking() {
        myRef.child("BOOKINGS").child("-LQxkKZYONj7ZyQcmSx1").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Booking booking = dataSnapshot.getValue(Booking.class);
                Log.e("Done", "Booking");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void updateCurrentUserBookings(){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("booking", UserDetails.currentUser.getBookings());
        myRef.child("USERS").child(UserDetails.UID).updateChildren(map);
    }

    public void getCurrentUserDetails(final String token) {
        final DatabaseReference users = myRef.child("USERS");
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user;

                if (dataSnapshot.hasChild(token)) {
                    user = dataSnapshot.child(token).getValue(User.class);
                } else {
                    user = new User(new ArrayList<String>(), 0.0);
                    users.child(token).setValue(user);
                }

                UserDetails.setCurrentUser(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
