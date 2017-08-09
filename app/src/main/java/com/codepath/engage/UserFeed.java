package com.codepath.engage;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.engage.models.User;
import com.codepath.engage.models.UserEvents;
import com.facebook.Profile;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mindorks.placeholderview.PlaceHolderView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

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
//    TextView feed;
    Context context;
    String uid;
    String whichprofile;
    public ArrayList<String> feedUsers;
    public ArrayList<Date> dates;
    ImageView header;
    final int REQUEST_CODE = 1;
    ProgressDialog progress;
    StorageReference storage;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.toolbar_userfeed)
    Toolbar toolbar;
    @BindView(R.id.drawer_view)
    PlaceHolderView mDrawerView;
    @BindView(R.id.tvMyFeed) TextView tvMyFeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feed);
        ButterKnife.bind(this);
        setUpDrawer();
        events = new ArrayList<>();
        feedUsers = new ArrayList<>();
        dates = new ArrayList<>();
        progress = new ProgressDialog(this);
        storage = FirebaseStorage.getInstance().getReference();
        context = this;
        Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf");
        tvMyFeed.setTypeface(font);
//        feed = (TextView) findViewById(R.id.feed);
//        feed.setTypeface(font);
        header = (ImageView) findViewById(R.id.header);
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
        Glide.with(context).using(new FirebaseImageLoader())
                .load(storage.child("headers").child(Profile.getCurrentProfile().getId())).error(R.color.red_300).centerCrop().into(header);
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(UserFeed.this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(UserFeed.this);
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
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            progress.setMessage("uploading");
            progress.show();
            Uri uri = data.getData();
            final StorageReference path = storage.child("headers").child(uid);
            path.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progress.dismiss();
                    Toast.makeText(getApplicationContext(),"Successfully uploaded image",Toast.LENGTH_LONG).show();
                    Glide.with(context).using(new FirebaseImageLoader())
                            .load(path).centerCrop().into(header);
                }
            });
        }
    }
    private void setUpDrawer(){
        mDrawerView
                .addView(new DrawerHeader(this.getApplicationContext()))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_PROFILE))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_FEED))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_EVENTS))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_CREATE))
                .addView(new DrawerMenuItem(this.getApplicationContext(),DrawerMenuItem.DRAWER_MENU_ITEM_MESSAGE))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_NOTIF))
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

    public void goHome(View view) {
        Intent i = new Intent(UserFeed.this,HomePage.class);
        startActivity(i);
    }
}
