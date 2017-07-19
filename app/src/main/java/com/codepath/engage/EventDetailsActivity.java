package com.codepath.engage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.engage.models.Event;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.codepath.engage.R.id.tvEventInfo;
import static com.codepath.engage.R.id.tvEventName;


public class EventDetailsActivity extends AppCompatActivity{

    @Nullable
    @BindView(R.id.ivPicture) ImageView ivPicture;
    @BindView(R.id.tvHost) TextView tvHost;
    @BindView(R.id.tvEventInfo) TextView tvEventInfo;
    @BindView(R.id.tvEventDescription) TextView tvEventDescription;
    @BindView(R.id.tvPeopleParticipating) TextView tvPeopleParticipating;
    @BindView(R.id.btnSave) Button btnSave;
    @BindView(R.id.tvEventName) TextView tvEventName;
    @BindView(R.id.btnMap) Button btnMap;

    Event event;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        ButterKnife.bind(this);

        firebaseDatabase = FirebaseDatabase.getInstance();
        users = firebaseDatabase.getReference("users");

        event = Parcels.unwrap(getIntent().getParcelableExtra(Event.class.getSimpleName()));

        if (ivPicture != null) {
            Glide.with(this)
                    .load(event.ivEventImage)
                    .into(ivPicture);
        }
        tvEventName.setText(event.tvEventName);
        tvEventDescription.setText(event.tvDescription);
        tvEventInfo.setText(event.tvEventInfo);
        tvHost.setText(event.organizer.name);
    }

    public void openMap(View view){
        Intent intent = new Intent(EventDetailsActivity.this, MapActivity.class);
        intent.putExtra("latitude", 34.8098080980);
        intent.putExtra("longitude", 67.09098898);
        startActivity(intent);
    }
    public void saveEvent(View view){
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra(Event.class.getSimpleName(), Parcels.wrap(event));
        startActivity(intent);
    }
}
