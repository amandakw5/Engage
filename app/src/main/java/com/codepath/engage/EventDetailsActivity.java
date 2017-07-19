package com.codepath.engage;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.engage.models.Event;
import com.codepath.engage.models.User;
import com.codepath.engage.models.UserEvents;
import com.facebook.Profile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;


public class EventDetailsActivity extends AppCompatActivity{

    @Nullable
    @BindView(R.id.ivPicture) ImageView ivPicture;
    @BindView(R.id.tvHost) TextView tvHost;
    @BindView(R.id.tvLocation) TextView tvLocation;
    @BindView(R.id.tvTimeDate) TextView tvTimeDate;
    @BindView(R.id.tvEventDescription) TextView tvEventDescription;
    @BindView(R.id.tvPeopleParticipating) TextView tvPeopleParticipating;
    @BindView(R.id.btnSave) Button btnSave;
    @BindView(R.id.tvEventName) TextView tvEventName;
    @BindView(R.id.btnMap) Button btnMap;

    Event event;
    String uid;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        ButterKnife.bind(this);

        firebaseDatabase = FirebaseDatabase.getInstance();
        users = firebaseDatabase.getReference("users");
        uid = Profile.getCurrentProfile().getId();

        event = Parcels.unwrap(getIntent().getParcelableExtra(Event.class.getSimpleName()));

        if (ivPicture != null) {
            Glide.with(this)
                    .load(event.ivEventImage)
                    .into(ivPicture);
        }
        tvEventName.setText(event.tvEventName);
        tvEventDescription.setText(event.tvDescription);
        tvTimeDate.setText(event.tvEventInfo);
        tvLocation.setText(event.venue.address);
    }

    public void openMap(View view){
        Intent intent = new Intent(EventDetailsActivity.this, MapActivity.class);
        intent.putExtra("latitude", 34.8098080980);
        intent.putExtra("longitude", 67.09098898);
        startActivity(intent);
    }
    public void saveEvent(View view){
        saveNewEvent(uid, event.getEventId(), event.getTvEventName(), event.organizer.getName(), event.tvEventInfo);
    }

    public void saveNewEvent(String uid, String eventId, String eventName, String eventHost, String eventInformation){
        UserEvents info = new UserEvents(eventName, eventHost, eventInformation);
        users.child(uid).child("events").child(eventId).setValue(info, new DatabaseReference.CompletionListener(){

            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    System.out.println("Data could not be saved " + databaseError.getMessage());
                } else {
                    System.out.println("Data saved successfully.");
                    Intent intent = new Intent(EventDetailsActivity.this, ProfileActivity.class);
                    intent.putExtra(Event.class.getSimpleName(), Parcels.wrap(event));
                    startActivity(intent);
                }
            }
        });
    }
}
