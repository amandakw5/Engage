package com.codepath.engage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.codepath.engage.models.Event;

import java.util.ArrayList;

public class UserFeed extends AppCompatActivity {
    EventAdapter eventAdapter;
    ArrayList<Event> events;
    ArrayList<Event> userEvents;
    RecyclerView rvEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feed);
        Intent i = getIntent();
        setUserEvents();
        events = i.getParcelableArrayListExtra("events");
        //find the recycler view
        rvEvents = (RecyclerView) findViewById(R.id.rvEvents);
        //constructing the adapter from this datasoruce
        eventAdapter = new EventAdapter(events);
        //recycler view setup(layout manager, use adapter'
        rvEvents.setLayoutManager(new LinearLayoutManager(this));
        // set the adapter
        rvEvents.setAdapter(eventAdapter);
    }

    public void setUserEvents() {
        
    }
}
