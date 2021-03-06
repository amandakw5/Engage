package com.codepath.engage;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.engage.models.User;
import com.facebook.Profile;
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

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.codepath.engage.R.id.emailTxt;
import static com.codepath.engage.R.id.nameTxt;

/**
 * Created by emilyz on 7/27/17.
 */

@NonReusable
@Layout(R.layout.drawer_header)
public class DrawerHeader{

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");
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
                    Typeface font = Typeface.createFromAsset(mContext.getAssets(), "fonts/Roboto-Light.ttf");
                    nameTxt.setTypeface(font);
                    nameTxt.setText(user.getFirstName()+ " "+ user.getLastName());
                    emailTxt.setTypeface(font);
                    emailTxt.setText(user.getEmail());
                    Glide.with(mContext)
                            .load(user.getProfilePicture())
                            .bitmapTransform(new RoundedCornersTransformation(mContext, 100, 0))
                            .into(profileImage);
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
