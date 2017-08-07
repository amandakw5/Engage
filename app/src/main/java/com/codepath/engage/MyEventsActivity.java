package com.codepath.engage;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import com.codepath.engage.models.User;
import com.codepath.engage.models.UserEvents;
import com.codepath.engage.models.Venue;
import com.facebook.Profile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mindorks.placeholderview.PlaceHolderView;

import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by awestort on 8/1/17.
 */

public class MyEventsActivity extends AppCompatActivity {

    //Setting the view for U.I
    @BindView(R.id.btnFilter)
    ImageButton btnFilter;
   // @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.drawerView)
    PlaceHolderView mDrawerView;
    @BindView(R.id.rvEvents)
    RecyclerView rvEvents;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawer;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    static int counterToGetPositionOfEvent;
    static int counterToSetOrganizer;
    ArrayList<Venue> venues;
    MyEventsAdapter eventAdapter;
    ArrayList<UserEvents> events;
    String uid;
    ProgressDialog progress;
    public ArrayList<Date> dates;
    User currentProfile;
    //Firebase Variables
    private DatabaseReference mDatabase;
    private DatabaseReference createdEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_events);

        progress = new ProgressDialog(MyEventsActivity.this);

        ButterKnife.bind(this);

        counterToSetOrganizer = 0;
        counterToGetPositionOfEvent = 0;
        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        //find the recycler view
        rvEvents = (RecyclerView) findViewById(R.id.rvEvents);
        //initiating the array list
        events = new ArrayList<>();
        dates = new ArrayList<>();
        venues = new ArrayList<>();
        final DatabaseReference createdEvents = FirebaseDatabase.getInstance().getReference("CreatedEvents");
        uid = Profile.getCurrentProfile().getId();

        setUpDrawer();

        //constructing the adapter from this datasource
        //constructing the adapter from this data source
        //recycler view setup(layout manager, use adapter)

        rvEvents.setLayoutManager(new LinearLayoutManager(this));
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentProfile = dataSnapshot.child(Profile.getCurrentProfile().getId()).getValue(User.class);
                eventAdapter = new MyEventsAdapter(events, currentProfile);
                rvEvents.setAdapter(eventAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        createdEvents.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot evSnapshot : dataSnapshot.getChildren()) {
                    if (uid.equals((String) evSnapshot.child("uid").getValue())){
                        UserEvents e = evSnapshot.getValue(UserEvents.class);
                        events.add(e);
                        dates.add(e.date);
                        eventAdapter.notifyItemInserted(events.size() -1);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void setUpDrawer(){
        mDrawerView
                .addView(new DrawerHeader(this.getApplicationContext()))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_PROFILE))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_FEED))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_EVENTS))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_CREATE))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_LOGOUT))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_MESSAGE));

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.open_drawer, R.string.close_drawer){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

            }
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

        mDrawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }

}