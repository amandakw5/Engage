package com.codepath.engage;

import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.engage.models.Event;
import com.codepath.engage.models.UserEvents;
import com.facebook.Profile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class EventDetailsActivity extends AppCompatActivity{

    private Toolbar toolbar;
    private ImageView ivBackdrop;
    private CollapsingToolbarLayout collapsingToolbar;
    private TextView tvEventName;
    private FloatingActionButton fab;
    UserEvents currentUpdate;
    Event event;
    ViewPager vPager;
    TabLayout tabLayout;

    String uid;
    List<String> events;

    DatabaseReference users;
    DatabaseReference savedEvents;

    boolean isUserCreated = false;
    boolean savedEventsCreated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currentUpdate = Parcels.unwrap(getIntent().getParcelableExtra("current"));

        event = Parcels.unwrap(getIntent().getParcelableExtra(Event.class.getSimpleName()));

        isUserCreated = Parcels.unwrap(getIntent().getParcelableExtra("isCreated"));

        setContentView(R.layout.activity_event_details);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        setToolbar();
        Typeface font = Typeface.createFromAsset(this.getAssets(), "fonts/Roboto-Light.ttf");
        ivBackdrop = (ImageView) findViewById(R.id.ivBackdrop);
        tvEventName = (TextView) findViewById(R.id.tvEventName);
        tvEventName.setTypeface(font);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        events = new ArrayList<String>();
        users = FirebaseDatabase.getInstance().getReference("users");
        savedEvents = FirebaseDatabase.getInstance().getReference();
        uid = Profile.getCurrentProfile().getId();

        vPager = (ViewPager) findViewById(R.id.viewpager);
        vPager.setAdapter(new TabFragmentAdapter(getSupportFragmentManager(), EventDetailsActivity.this, currentUpdate, event, isUserCreated));

        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(vPager);

        if(event!=null){
            tvEventName.setText(event.tvEventName);
            if (isUserCreated){
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("photos").child(String.valueOf(event.getEventId()));
                    Glide.with(this)
                            .using(new FirebaseImageLoader())
                            .load(storageReference)
                            .error(R.drawable.image_not_found)
                            .into(ivBackdrop);
                } else if (!event.ivEventImage.equals("null")) {
                    Glide.with(this)
                            .load(event.ivEventImage)
                            .centerCrop()
                            .into(ivBackdrop);
                } else if (event.ivEventImage.equals("null")) {
                    Glide.with(this)
                            .load(R.drawable.image_not_found)
                            .centerCrop()
                            .into(ivBackdrop);
                }
        } else if (currentUpdate!=null){
            tvEventName.setText(currentUpdate.eventName);
            if (!currentUpdate.eventImage.equals("null")) {
                    Glide.with(this)
                            .load(currentUpdate.eventImage)
                            .centerCrop()
                            .into(ivBackdrop);
                } else if (currentUpdate.eventImage.equals("null")) {
                    Glide.with(this)
                            .load(R.drawable.image_not_found)
                            .centerCrop()
                            .into(ivBackdrop);
                }
        }

        View root = tabLayout.getChildAt(0);
        if (root instanceof LinearLayout) {
            ((LinearLayout) root).setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
            GradientDrawable drawable = new GradientDrawable();
            drawable.setColor(getResources().getColor(R.color.white));
            drawable.setSize(2, 1);
            ((LinearLayout) root).setDividerPadding(0);
            ((LinearLayout) root).setDividerDrawable(drawable);
        }

        if (event!=null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (event.ivEventImage == null) {
                        saveNewEvent(uid, event.getEventId(), event.getTvEventName(), event.organizer.getName(), event.getTimeStart(), event.getVenue().getAddress() + " " + event.getVenue().getCity() + " " + event.getVenue().getCountry(), "null", event.tvDescription);

                    } else {
                        saveNewEvent(uid, event.getEventId(), event.getTvEventName(), event.organizer.getName(), event.getTimeStart(), event.getVenue().getAddress() + " " + event.getVenue().getCity() + " " + event.getVenue().getCountry(), event.ivEventImage, event.tvDescription);
                    }
                    Toast.makeText(EventDetailsActivity.this, "Saved!", Toast.LENGTH_SHORT).show();
                }
            });
        } else if (currentUpdate!= null){
            fab.setVisibility(View.GONE);
        }
    }

    public void saveNewEvent(final String uid, final String eventId, String eventName, String eventHost, String eventTime, String eventAddress, String eventImage, String eventDescription) {
        savedEventsCreated = false;
        events.clear();
        Date date = new Date();
        Log.i("indo", date.toString());
        final UserEvents info = new UserEvents(eventName, eventHost, eventTime, eventAddress, eventId, eventImage, eventDescription, null, null);
        savedEvents.child("savedEvents").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(eventId)){

                }else{
                    savedEvents.child("savedEvents").child(eventId).setValue(info);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        savedEvents.child("savedEvents").child(eventId).child("date").child(uid).setValue(date);
        users.child(uid).child("eventsList").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    events.add(postSnapshot.getValue().toString());
                }
                if(!events.contains(eventId))
                    events.add(eventId);
                users.child(uid).child("eventsList").setValue(events, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError != null) {
                            System.out.println("Data could not be saved " + databaseError.getMessage());
                        } else {
                            System.out.println("Data saved successfully.");
                        }
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setToolbar() {
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

}
