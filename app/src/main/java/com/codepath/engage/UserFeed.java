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
    UpdateAdapter adapter;
    public ArrayList<UserEvents> events;
    RecyclerView rvEvents;
    User currentProfile;
    ImageView profileImage;
    Context context;
    String uid;
    private DatabaseReference mDatabase;
    String whichprofile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feed);
//        //find the recycler view
        events = new ArrayList<>();
        profileImage = (ImageView) findViewById(R.id.profileImage);
        rvEvents = (RecyclerView) findViewById(R.id.rvEvents);
        //constructing the adapter from this datasoruce
//        //eventAdapter = new EventAdapter(events);
//        //recycler view setup(layout manager, use adapter'
//        rvEvents.setLayoutManager(new LinearLayoutManager(this));
//        // set the adapter
//        //rvEvents.setAdapter(eventAdapter);
//        //Linking Firebase Database to variable
//        mDataBase = FirebaseDatabase.getInstance().getReference();
//        setUserEvents();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        mDatabase.child(Profile.getCurrentProfile().getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentProfile = dataSnapshot.getValue(User.class);
                whichprofile = currentProfile.firstName + " " + currentProfile.lastName;
                if (currentProfile.following != null){
                    for (int i = 0; i < currentProfile.following.size(); i++){
                        //for each person they are following, get their eventlist
                        String id = currentProfile.following.get(i);
                        DataSnapshot followersSaved = dataSnapshot.child(id).child("eventList");
                        for (DataSnapshot followersEvents: followersSaved.getChildren()){
                            UserEvents e = followersEvents.getValue(UserEvents.class);
                            events.add(e);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        adapter = new UpdateAdapter(events, whichprofile);

        rvEvents.setLayoutManager(new LinearLayoutManager(this));
        rvEvents.setAdapter(adapter);
        //Glide.with(context).load(currentProfile.profilePicture).centerCrop().into(profileImage);


    }

}
