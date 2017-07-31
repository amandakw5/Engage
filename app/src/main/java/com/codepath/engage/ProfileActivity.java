package com.codepath.engage;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.engage.models.User;
import com.codepath.engage.models.UserEvents;
import com.facebook.Profile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class ProfileActivity extends AppCompatActivity {

    public UpdateAdapter adapter;
    public ArrayList<UserEvents> events;
    String uid;
    String whichProfile;
    DatabaseReference mDatabase;
    boolean isFollowing;
    User u;
    User currentProfile;
    Context context;
    List<String> eventIDs;

    @BindView(R.id.rvUpdates) RecyclerView rvUpdates;
    @BindView(R.id.profileImage) ImageView profileImage;
    @BindView(R.id.profileHeader) TextView profileHeader;
    @BindView(R.id.following) TextView following;
    @BindView(R.id.followers) TextView followers;
    @BindView(R.id.floatingActionButton) FloatingActionButton floatingActionButton;
    @BindView(R.id.Home) ImageView home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ButterKnife.bind(this);

        eventIDs = new ArrayList<>();
        whichProfile = getIntent().getStringExtra("whichProfile");
        events = new ArrayList<>();
        eventIDs = new ArrayList<>();

        adapter = new UpdateAdapter(events, whichProfile);

        rvUpdates.setLayoutManager(new LinearLayoutManager(this));
        rvUpdates.setAdapter(adapter);

        isFollowing = false;
        u = new User();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        if (Parcels.unwrap(getIntent().getParcelableExtra(User.class.getSimpleName())) != null) {
            u = Parcels.unwrap(getIntent().getParcelableExtra(User.class.getSimpleName()));
            uid = u.getUid();
            profileHeader.setText(u.firstName + " " + u.lastName);
        } else {
            uid = Profile.getCurrentProfile().getId();
        }
        mDatabase.child(Profile.getCurrentProfile().getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentProfile = dataSnapshot.getValue(User.class);
                if (uid.equals(Profile.getCurrentProfile().getId())) {
                    u = currentProfile;
                }
                Glide.with(context)
                        .load(u.profilePicture)
                        .centerCrop()
                        .into(profileImage);
                following.setText(u.numFollowing + " following");
                followers.setText(u.numFollowers + " followers");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference savedEvents = FirebaseDatabase.getInstance().getReference("savedEvents");

        final DatabaseReference evDatabase = FirebaseDatabase.getInstance().getReference("users").child(uid).child("eventsList");

        evDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {
                };
                eventIDs = dataSnapshot.getValue(t);
                if (eventIDs == null) {
                    Log.d("Event IDs", "null");
                } else {
                    Log.d("eventIds", eventIDs.toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        savedEvents.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (eventIDs != null) {
                    for (String id : eventIDs) {
                        for (DataSnapshot evSnapshot : dataSnapshot.getChildren()) {
                            if (id.equals(evSnapshot.getKey())) {
                                UserEvents e = evSnapshot.getValue(UserEvents.class);
                                events.add(e);
                                adapter.notifyItemInserted(events.size() - 1);
                                break;
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!uid.equals(currentProfile.uid)) {
                    mDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            HashMap<String, String> followingList = (HashMap<String, String>) dataSnapshot.child("following").getValue();
                            if (followingList!= null){
                                for (Object value : followingList.values()){
                                    if (((String)(value)).equals(uid)){
                                        isFollowing = true;
                                    }
                                }
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            }
                    });
                    if (isFollowing) {
                        mDatabase.child(uid).child("numFollowers").setValue((u.numFollowers - 1));
                        mDatabase.child(currentProfile.uid).child("numFollowing").setValue(currentProfile.numFollowing - 1);
                        DatabaseReference deleteFollow = mDatabase.child(uid).child("following").child(currentProfile.uid).push();
                        deleteFollow.setValue(null);
                        DatabaseReference deleteFollowing = mDatabase.child(currentProfile.uid).child("followers").child(uid).push();
                        deleteFollowing.setValue(null);
                        isFollowing = false;
                    } else {
                        mDatabase.child(uid).child("numFollowers").setValue((u.numFollowers + 1));
                        mDatabase.child(currentProfile.uid).child("numFollowing").setValue(currentProfile.numFollowing + 1);
                        DatabaseReference addFollow = mDatabase.child(uid).child("followers").push();
                        addFollow.setValue(currentProfile.uid);
                        DatabaseReference addFollowing = mDatabase.child(currentProfile.uid).child("following").push();
                        addFollowing.setValue(uid);
                        isFollowing = true;
                    }
                }
            }
        });

        home.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, HomePage.class);
                startActivity(intent);
            }
        });


    }

}
