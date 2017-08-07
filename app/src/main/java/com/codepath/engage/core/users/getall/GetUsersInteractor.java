package com.codepath.engage.core.users.getall;

import android.text.TextUtils;
import android.util.Log;

import com.codepath.engage.models.UserChat;
import com.codepath.engage.utils.Constants;
import com.facebook.Profile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


public class GetUsersInteractor implements GetUsersContract.Interactor {
    private static final String TAG = "GetUsersInteractor";
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");
    HashMap<String,String> following = new HashMap<>();
    private GetUsersContract.OnGetAllUsersListener mOnGetAllUsersListener;

    public GetUsersInteractor(GetUsersContract.OnGetAllUsersListener onGetAllUsersListener) {
        this.mOnGetAllUsersListener = onGetAllUsersListener;
    }


    @Override
    public void getAllUsersFromFirebase() {
        final String checkUid = Profile.getCurrentProfile().getId();
        FirebaseDatabase.getInstance().getReference().child(Constants.ARG_USERS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                following = (HashMap<String,String>) dataSnapshot.child(checkUid).child("following").getValue();

                Iterator<DataSnapshot> dataSnapshots = dataSnapshot.getChildren().iterator();
                List<UserChat> users = new ArrayList<>();
                while (dataSnapshots.hasNext()) {
                    DataSnapshot dataSnapshotChild = dataSnapshots.next();
                    UserChat user = dataSnapshotChild.getValue(UserChat.class);

                    try {
                        if (!TextUtils.equals(user.uid, checkUid) && following.containsValue(user.uid)) {
                            users.add(user);
                        }
                    }catch (NullPointerException e){
                        Log.i("USER HAS NO FOLLOWING",user.uid);
                    }
                }
                mOnGetAllUsersListener.onGetAllUsersSuccess(users);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                mOnGetAllUsersListener.onGetAllUsersFailure(databaseError.getMessage());
            }
        });
    }

    @Override
    public void getChatUsersFromFirebase() {

    }
}
