package com.codepath.engage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
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

import com.codepath.engage.models.User;
import com.facebook.Profile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mindorks.placeholderview.PlaceHolderView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationsActivity extends AppCompatActivity {

    Context context;
    @BindView(R.id.notifHeader) TextView notifHeader;
    DatabaseReference mDatabase;
    User currentProfile;
    public NotifAdapter adapter;
    public ArrayList<String> notifs;
    @BindView(R.id.nRecylerView) RecyclerView nRecyclerView;
    @BindView(R.id.Home) ImageView home;
    public ArrayList<String> profPics;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.toolbar_notif)
    Toolbar toolbar;
    @BindView(R.id.drawer_view)
    PlaceHolderView mDrawerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        ButterKnife.bind(this);
        setUpDrawer();
        context = this;
        Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf");
        notifHeader.setTypeface(font);
        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        notifs = new ArrayList<>();
        profPics = new ArrayList<>();
        adapter = new NotifAdapter(notifs, profPics);
        nRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        nRecyclerView.setAdapter(adapter);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(Profile.getCurrentProfile().getId()).hasChild("notifList")){
                    currentProfile = dataSnapshot.child(Profile.getCurrentProfile().getId()).getValue(User.class);
                    for (String n : currentProfile.notifList.values()){
                        notifs.add(n);
                        profPics.add(currentProfile.profilePicture);
                        adapter.notifyItemInserted(notifs.size() -1);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void goHome(View view) {
        Intent i = new Intent(NotificationsActivity.this,HomePage.class);
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
                .addView(new DrawerMenuItem(this.getApplicationContext(),DrawerMenuItem.DRAWER_MENU_ITEM_NOTIF))
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
