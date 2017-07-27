package com.codepath.engage;

import android.content.Context;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.engage.R;
import com.codepath.engage.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.NonReusable;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;

import com.facebook.Profile;

import static com.codepath.engage.R.id.emailTxt;
import static com.codepath.engage.R.id.nameTxt;

/**
 * Created by emilyz on 7/27/17.
 */

@NonReusable
@Layout(R.layout.drawer_header)
public class DrawerHeader{

    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");
    User user;
    private Context mContext;

    @View(R.id.nameTxt)
    private TextView nameTxt;

    @View(R.id.emailTxt)
    private TextView emailTxt;

    @View(R.id.profileImageView)
    private ImageView profileImage;

    public DrawerHeader(Context context) {
        mContext = context;
    }

    public void getInfo(){
        mDatabase.child(Profile.getCurrentProfile().getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                if (user != null){
                    nameTxt.setText(user.getFirstName()+user.getLastName());
                    emailTxt.setText(user.getEmail());
                    Glide.with(mContext).load(user.getProfilePicture()).into(profileImage);
                } else {
                    Log.d(":(", ":(((((");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

    @Resolve
    private void onResolved() {
        getInfo();
    }
}
