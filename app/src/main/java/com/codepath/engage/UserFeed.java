package com.codepath.engage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.codepath.engage.models.Event;
import com.codepath.engage.models.UserEvents;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserFeed extends AppCompatActivity {
    EventAdapter eventAdapter;
    ArrayList<Event> events;
    ArrayList<Event> userEvents;
    RecyclerView rvEvents;
    private DatabaseReference mDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feed);
//        //find the recycler view
//        rvEvents = (RecyclerView) findViewById(R.id.rvEvents);
//        //constructing the adapter from this datasoruce
//        //eventAdapter = new EventAdapter(events);
//        //recycler view setup(layout manager, use adapter'
//        rvEvents.setLayoutManager(new LinearLayoutManager(this));
//        // set the adapter
//        //rvEvents.setAdapter(eventAdapter);
//        //Linking Firebase Database to variable
//        mDataBase = FirebaseDatabase.getInstance().getReference();
//        setUserEvents();
    }

//    public void setUserEvents() {
//        ValueEventListener postListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                DataSnapshot children = dataSnapshot.child("events");
//                UserEvents e = children.getValue(UserEvents.class);
//                Log.i("INFOID",e.getEventId());
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        };mDataBase.addValueEventListener(postListener);
//    }
}
