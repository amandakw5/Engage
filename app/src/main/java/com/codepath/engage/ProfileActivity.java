package com.codepath.engage;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.engage.models.CreatedEvents;
import com.codepath.engage.models.DateProgram;
import com.codepath.engage.models.User;
import com.codepath.engage.models.UserEvents;
import com.facebook.Profile;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mindorks.placeholderview.PlaceHolderView;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileActivity extends AppCompatActivity {

    public UpdateAdapter adapter;
    public ArrayList<UserEvents> events;
    public ArrayList<CreatedEvents> createdEventList;
    String uid;
    String whichProfile;
    DatabaseReference mDatabase;
    boolean isFollowing;
    User u;
    User currentProfile;
    ProgressDialog progress;
    Context context;
    List<String> eventIDs;
    String verb;
    public ArrayList<Date> dates;
    final int REQUEST_CODE = 1;

    @BindView(R.id.rvUpdates) RecyclerView rvUpdates;
    @BindView(R.id.profileImage) ImageView profileImage;
    @BindView(R.id.profileHeader) TextView profileHeader;
    @BindView(R.id.following) TextView following;
    @BindView(R.id.followers) TextView followers;
    @BindView(R.id.floatingActionButton) FloatingActionButton floatingActionButton;
    @BindView(R.id.header) ImageView header;
    StorageReference storage;
    @BindView(R.id.Home) ImageView home;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.toolbar_profile)
    Toolbar toolbar;
    @BindView(R.id.drawer_view)
    PlaceHolderView mDrawerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ButterKnife.bind(this);
        dates = new ArrayList<>();
        eventIDs = new ArrayList<>();
        whichProfile = getIntent().getStringExtra("whichProfile");
        verb = getIntent().getStringExtra("verb");
        events = new ArrayList<>();
        //createdEventList = new ArrayList<>();
        eventIDs = new ArrayList<>();

        progress = new ProgressDialog(this);

        adapter = new UpdateAdapter(events, whichProfile, verb, dates);

        rvUpdates.setLayoutManager(new LinearLayoutManager(this));
        rvUpdates.setAdapter(adapter);
        storage = FirebaseStorage.getInstance().getReference();

        isFollowing = false;
        u = new User();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf");

        setUpDrawer();

        profileHeader.setTypeface(font);

        if (Parcels.unwrap(getIntent().getParcelableExtra(User.class.getSimpleName())) != null) {
            u = Parcels.unwrap(getIntent().getParcelableExtra(User.class.getSimpleName()));
            uid = u.getUid();
            profileHeader.setText(u.firstName + " " + u.lastName);

        }
        else {
            uid = Profile.getCurrentProfile().getId();
        }

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf");
                currentProfile = dataSnapshot.child(Profile.getCurrentProfile().getId()).getValue(User.class);
                if (uid.equals(Profile.getCurrentProfile().getId())){
                    u = currentProfile;
                }
                Glide.with(context).load(u.profilePicture).centerCrop().into(profileImage);
                followers.setTypeface(font);
                following.setTypeface(font);
                following.setText(u.numFollowing + " following");
                followers.setText(u.numFollowers + " followers");
                HashMap<String, String> followingList = (HashMap<String, String>) dataSnapshot.child(currentProfile.uid).child("following").getValue();
                if (followingList != null) {
                    for (Object value : followingList.values()) {
                        if (((String) (value)).equals(uid)) {
                            isFollowing = true;
                        }
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        Glide.with(context).load(u.profilePicture).centerCrop().into(profileImage);
        following.setText(u.numFollowing + " following");
        followers.setText(u.numFollowers + " followers");
        followers.setTypeface(font);
        following.setTypeface(font);

        final DatabaseReference savedEvents = FirebaseDatabase.getInstance().getReference("savedEvents");
        final DatabaseReference createdEvents = FirebaseDatabase.getInstance().getReference("CreatedEvents");
        final DatabaseReference evDatabase = FirebaseDatabase.getInstance().getReference("users").child(uid).child("eventsList");
        final StorageReference path = storage.child("headers").child(uid);
        Glide.with(context).using(new FirebaseImageLoader())
                .load(storage.child("headers").child(uid)).error(R.drawable.search_gradient).centerCrop().into(header);
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(ProfileActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(ProfileActivity.this);
                }
                builder.setTitle("Upload image")
                        .setMessage("Do you want to upload a header?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                pick();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                                finish();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

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
                        for (final DataSnapshot evSnapshot : dataSnapshot.getChildren()) {
                            if (id.equals(evSnapshot.getKey())) {
                                savedEvents.child(id).child("date").child(Profile.getCurrentProfile().getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot2) {
                                        UserEvents e = evSnapshot.getValue(UserEvents.class);
                                        DateProgram date = dataSnapshot2.getValue(DateProgram.class);
                                        date.setDateConstructed(date.getYear(),date.getMonth(),date.getTimezoneOffset(),date.getTime(),date.getMinutes(),date.getSeconds(),date.getHours(),date.getDay(),date.getDate());
                                        e.setDate(date.getDateConstructed());
                                        events.add(e);
                                        dates.add(e.date);
                                        adapter.notifyItemInserted(events.size() - 1);
                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
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
        createdEvents.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot evSnapshot : dataSnapshot.getChildren()) {
                    if (uid.equals((String) evSnapshot.child("uid").getValue())){
                        UserEvents e = evSnapshot.getValue(UserEvents.class);
                        e.setCreatedByUser(true);
                        events.add(e);
                        dates.add(e.date);
                        adapter.notifyItemInserted(events.size() -1);
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
                            HashMap<String, String> followingList = (HashMap<String, String>) dataSnapshot.child(currentProfile.uid).child("following").getValue();
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
                        DatabaseReference addNotif = mDatabase.child(uid).child("notifList").push();
                        addNotif.setValue(currentProfile.firstName + " " + currentProfile.lastName + " followed you.");
                        isFollowing = true;
                    }
                }
            }
        });
        followers.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent i = new Intent(ProfileActivity.this, FollowActivity.class);
                i.putExtra(User.class.getSimpleName(), Parcels.wrap(u));
                i.putExtra("f", "followers");
                startActivity(i);
            }
        });
        following.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent i = new Intent(ProfileActivity.this, FollowActivity.class);
                i.putExtra(User.class.getSimpleName(), Parcels.wrap(u));
                i.putExtra("f", "following");
                startActivity(i);
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

    public void pick(){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            progress.setMessage("uploading");
            progress.show();
            Uri uri = data.getData();
            final StorageReference path = storage.child("headers").child(uid);
            path.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progress.dismiss();
                    Toast.makeText(getApplicationContext(), "Successfully uploaded image", Toast.LENGTH_LONG).show();
                    Glide.with(context).using(new FirebaseImageLoader())
                            .load(path).centerCrop().into(header);
                }
            });
        }
    }

    public void goHome(View view) {
        Intent i = new Intent(ProfileActivity.this,HomePage.class);
        startActivity(i);
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
}
