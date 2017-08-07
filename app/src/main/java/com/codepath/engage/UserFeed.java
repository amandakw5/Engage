package com.codepath.engage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.codepath.engage.models.User;
import com.codepath.engage.models.UserEvents;
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

public class UserFeed extends AppCompatActivity {
    FeedAdapter adapter;
    public ArrayList<UserEvents> events;
    RecyclerView rvEvents;
    User currentProfile;
    ImageView profileImage;
    Context context;
    String uid;
    String whichprofile;
    public ArrayList<String> feedUsers;
    public ArrayList<Date> dates;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.toolbar_userfeed)
    Toolbar toolbar;
    @BindView(R.id.drawer_view)
    PlaceHolderView mDrawerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feed);
        ButterKnife.bind(this);
        setUpDrawer();
        events = new ArrayList<>();
        feedUsers = new ArrayList<>();
        dates = new ArrayList<>();
        profileImage = (ImageView) findViewById(R.id.profileImage);
        rvEvents = (RecyclerView) findViewById(R.id.rvEvents);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentProfile = dataSnapshot.child("users").child(Profile.getCurrentProfile().getId()).getValue(User.class);
                if (currentProfile != null) {
                    whichprofile = currentProfile.firstName + " " + currentProfile.lastName;
                    if (currentProfile.following != null) {
                        for (String ids : currentProfile.following.values()) {
                            //all the event IDS from someone user is following is followersSaved
                            DataSnapshot followersSaved = dataSnapshot.child("users").child(ids).child("eventsList");
                            // for each event ID
                            for (DataSnapshot followersEvents : followersSaved.getChildren()) {
                                // dataSnapshot.child("savedEvents").child(followersEvents.getValue(String));
                                String e = (String) followersEvents.getValue();
                                for (DataSnapshot findEvent : dataSnapshot.child("savedEvents").getChildren()) {
                                    if ((findEvent.getKey()).equals(e)) {
                                        UserEvents currente = findEvent.getValue(UserEvents.class);
                                        events.add(currente);
                                        feedUsers.add((dataSnapshot.child("users").child(ids).child("firstName").getValue() + " " + (dataSnapshot.child("users").child(ids).child("lastName").getValue())));
                                        dates.add(currente.date);
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                            }
                        }
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        adapter = new FeedAdapter(events, feedUsers, dates);

        rvEvents.setLayoutManager(new LinearLayoutManager(this));
        rvEvents.setAdapter(adapter);
    }

    private void setUpDrawer(){
        mDrawerView
                .addView(new DrawerHeader(this.getApplicationContext()))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_PROFILE))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_FEED))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_EVENTS))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_CREATE))
                .addView(new DrawerMenuItem(this.getApplicationContext(),DrawerMenuItem.DRAWER_MENU_ITEM_MESSAGE))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_LOGOUT));

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer){

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent (this, HomePage.class);
        startActivity(intent);
    }

}
