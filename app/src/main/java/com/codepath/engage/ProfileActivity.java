package com.codepath.engage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.codepath.engage.models.Event;
import com.codepath.engage.models.UpdateAdapter;
import com.codepath.engage.models.UserEvents;
import com.facebook.Profile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.parceler.Parcels;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {
    RecyclerView rvUpdates;
    public UpdateAdapter adapter;
    public ArrayList<UserEvents> events;
    private DatabaseReference mDatabase;
    String uid;

// ...

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        events = new ArrayList<>();
        adapter = new UpdateAdapter(events);

        rvUpdates = (RecyclerView) findViewById(R.id.rvUpdates);
        rvUpdates.setLayoutManager(new LinearLayoutManager(this));
        rvUpdates.setAdapter(adapter);
        uid = Profile.getCurrentProfile().getId();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        Event event = Parcels.unwrap(getIntent().getParcelableExtra(Event.class.getSimpleName()));
        mDatabase.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot contactChildren = dataSnapshot.child("events");
                String x;
               for (DataSnapshot evSnapshot: contactChildren.getChildren()){
                   UserEvents e = evSnapshot.getValue(UserEvents.class);
                   events.add(e);
                   adapter.notifyItemInserted(events.size() - 1);
               }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
int i =0;
            }
        });
    }

}
