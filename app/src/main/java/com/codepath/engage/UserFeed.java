package com.codepath.engage;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.codepath.engage.models.User;
import com.codepath.engage.models.UserEvents;
import com.facebook.Profile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feed);
        events = new ArrayList<>();
        feedUsers = new ArrayList<>();
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
                                        events.add(findEvent.getValue(UserEvents.class));
                                        feedUsers.add((dataSnapshot.child("users").child(ids).child("firstName").getValue() + " " + (dataSnapshot.child("users").child(ids).child("lastName").getValue())));
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
        adapter = new FeedAdapter(events, feedUsers);

        rvEvents.setLayoutManager(new LinearLayoutManager(this));
        rvEvents.setAdapter(adapter);
    }

}
