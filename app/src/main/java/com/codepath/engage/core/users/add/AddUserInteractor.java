package com.codepath.engage.core.users.add;

import android.content.Context;
import android.support.annotation.NonNull;

import com.codepath.engage.R;
import com.codepath.engage.models.UserChat;
import com.codepath.engage.utils.Constants;
import com.facebook.Profile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class AddUserInteractor implements AddUserContract.Interactor {
    private AddUserContract.OnUserDatabaseListener mOnUserDatabaseListener;

    public AddUserInteractor(AddUserContract.OnUserDatabaseListener onUserDatabaseListener) {
        this.mOnUserDatabaseListener = onUserDatabaseListener;
    }

    @Override
    public void addUserToDatabase(final Context context, final FirebaseUser firebaseUser) {
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child("users").child(Profile.getCurrentProfile().getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserChat userChat = dataSnapshot.getValue(UserChat.class);
                database.child(Constants.ARG_USERS)
                        .child(firebaseUser.getUid())
                        .setValue(userChat)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    mOnUserDatabaseListener.onSuccess(context.getString(R.string.user_successfully_added));
                                } else {
                                    mOnUserDatabaseListener.onFailure(context.getString(R.string.user_unable_to_add));
                                }
                            }
                        });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
