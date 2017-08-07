package com.codepath.engage;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.codepath.engage.models.User;
import com.facebook.Profile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        ButterKnife.bind(this);
        context = this;
        Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf");
        notifHeader.setTypeface(font);
        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        notifs = new ArrayList<>();
        adapter = new NotifAdapter(notifs);
        nRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        nRecyclerView.setAdapter(adapter);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(Profile.getCurrentProfile().getId()).hasChild("notifList")){
                    currentProfile = dataSnapshot.child(Profile.getCurrentProfile().getId()).getValue(User.class);
                    for (String n : currentProfile.notifList.values()){
                        notifs.add(n);
                        adapter.notifyItemInserted(notifs.size() -1);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
