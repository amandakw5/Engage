package com.codepath.engage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.codepath.engage.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FollowActivity extends AppCompatActivity {
    FollowAdapter FollowAdapter; // data source
    ArrayList<User> follows;
    ArrayList<String> uids;
    @BindView(R.id.rvFollow) RecyclerView rvFollow;
    @BindView(R.id.followTitle) TextView followTitle;
    User user;
    DatabaseReference mDatabase;
    public String whichView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow);
        ButterKnife.bind(this);
        user = (User) Parcels.unwrap(getIntent().getParcelableExtra(User.class.getSimpleName()));
        whichView = (String) getIntent().getStringExtra("f");
        follows = new ArrayList<>();
        FollowAdapter = new FollowAdapter(follows);
        rvFollow.setAdapter(FollowAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvFollow.setLayoutManager(new LinearLayoutManager(this));
        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        loadFollow(whichView);
        uids = new ArrayList<>();
        if (whichView.equals("followers")){
            followTitle.setText("Followers");
        }
        else{
            followTitle.setText("Following");
        }
    }

    private void loadFollow(final String whichView) {
            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    HashMap<String, String> followList = (HashMap<String, String>) dataSnapshot.child(user.uid).child(whichView).getValue();
                    if (followList != null) {
                        for (Object value : followList.values()) {
                            uids.add((String) value);
                        }
                    }
                    for(String oneUid: uids){
                        for(DataSnapshot evSnapshot : dataSnapshot.getChildren()) {
                            if (((String) evSnapshot.getKey()).equals(oneUid)){
                                follows.add(evSnapshot.getValue(User.class));
                                FollowAdapter.notifyItemInserted(follows.size() - 1);
                            }
                        }
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

}
