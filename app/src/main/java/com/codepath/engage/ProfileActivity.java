package com.codepath.engage;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.codepath.engage.models.User;
import com.codepath.engage.models.UserEvents;
import com.facebook.Profile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import org.parceler.Parcel;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.codepath.engage.R.id.floatingActionButton;

public class ProfileActivity extends AppCompatActivity {
    RecyclerView rvUpdates;
    public UpdateAdapter adapter;
    public ArrayList<UserEvents> events;
    String uid;
    String whichprofile;
    List<String> eventIDs;
    DatabaseReference mDatabase;
    TextView profileHeader;
    boolean following;
    User u;
    User currentProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        whichprofile = getIntent().getStringExtra("whichProfile");
        events = new ArrayList<>();
        eventIDs = new ArrayList<>();

        adapter = new UpdateAdapter(events, whichprofile);
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        profileHeader = (TextView) findViewById(R.id.profileHeader);
        rvUpdates = (RecyclerView) findViewById(R.id.rvUpdates);
        rvUpdates.setLayoutManager(new LinearLayoutManager(this));
        rvUpdates.setAdapter(adapter);

        following = false;
        u = new User();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        if ((User) Parcels.unwrap(getIntent().getParcelableExtra(User.class.getSimpleName())) != null){
            u = (User) Parcels.unwrap(getIntent().getParcelableExtra(User.class.getSimpleName()));
            uid = u.getUid();
            profileHeader.setText(u.firstName + " " + u.lastName + "'s Profile");
        }
        else {
            uid = Profile.getCurrentProfile().getId();
        }
        mDatabase.child(Profile.getCurrentProfile().getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentProfile = dataSnapshot.getValue(User.class);
                if (uid.equals( Profile.getCurrentProfile().getId())){
                    u = currentProfile;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        uid = Profile.getCurrentProfile().getId();
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users").child(uid).child("eventsList");
        DatabaseReference savedEvents = FirebaseDatabase.getInstance().getReference("savedEvents");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>(){};
                eventIDs = dataSnapshot.getValue(t);
                if (eventIDs == null) {
                    Log.d("did not work", "lol");
                } else {
                    Log.d("eventIds", eventIDs.toString());
                    }
                }
            @Override
            public void onCancelled(DatabaseError databaseError) { int i = 0; }
        });

        savedEvents.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (eventIDs != null){
                    for (String id : eventIDs) {
                        for (DataSnapshot evSnapshot : dataSnapshot.getChildren()) {
                            if (id.equals(evSnapshot.getKey())){
                                UserEvents e = evSnapshot.getValue(UserEvents.class);
                                events.add(e);
                                adapter.notifyItemInserted(events.size()-1);
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
                if (!uid.equals(currentProfile.uid)){
                    if (following){
                        mDatabase.child(uid).child("numFollowers").setValue((u.numFollowers - 1));
                        mDatabase.child(currentProfile.uid).child("numFollowing").setValue(currentProfile.numFollowing - 1);
                        DatabaseReference deleteFollow = mDatabase.child(uid).child("following").child(currentProfile.uid).push();
                        deleteFollow.setValue(null);
                        DatabaseReference deleteFollowing = mDatabase.child(currentProfile.uid).child("followers").child(uid).push();
                        deleteFollowing.setValue(null);
                    }
                    else{
                        mDatabase.child(uid).child("numFollowers").setValue((u.numFollowers + 1));
                        mDatabase.child(currentProfile.uid).child("numFollowing").setValue(currentProfile.numFollowing + 1);
                        DatabaseReference addFollow = mDatabase.child(currentProfile.uid).child("followers").push();
                        addFollow.setValue(u);
                        DatabaseReference addFollowing = mDatabase.child(uid).child("following").push();
                        addFollowing.setValue(currentProfile);
                    }
                }
            }
        });

    }

}
