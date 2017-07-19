package com.codepath.engage;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by awestort on 7/18/17.
 */

@IgnoreExtraProperties
public class User {
    public String uid;
    public String firstName;
    public String lastName;
    public String email;
    public String profilePicture;

    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");

    public User(){
    }

//    public User(String uid, String firstName, String lastName, String email, String profilePicture){
//        this.uid = uid;
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.email = email;
//        this.profilePicture = profilePicture;
//    }

    public User(String firstName, String lastName, String email, String profilePicture) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.profilePicture = profilePicture;
    }

    public String getUid() {
        return uid;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }
    public String getProfilePicture() {
        return profilePicture;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public void saveUser() {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference();
        userRef = userRef.child("users").child(getUid());
        userRef.setValue(this);
    }
//                new DatabaseReference.CompletionListener() {
//            @Override
//            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
//                if (databaseError != null){
//                    Log.d("Firebase failure", "Data send failed");
//                } else{
//                    Log.d("work", "werk");
//                }
//            }
//        }
}



//    public int userId;
//    public String first_name;
//    public String last_name;
//    public String email;
//    public String gender;
//    public String loc;
//    public String birthday;
//    public String followers;
//    public String following;
//
//    public int getUserId() {
//        return userId;
//    }
//
//    public void setUserId(int userId) {
//        this.userId = userId;
//    }
//
//    public String getFirst_name() {
//        return first_name;
//    }
//
//    public void setFirst_name(String first_name) {
//        this.first_name = first_name;
//    }
//
//    public String getLast_name() {
//        return last_name;
//    }
//
//    public void setLast_name(String last_name) {
//        this.last_name = last_name;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getGender() {
//        return gender;
//    }
//
//    public void setGender(String gender) {
//        this.gender = gender;
//    }
//
//    public String getLoc() {
//        return loc;
//    }
//
//    public void setLoc(String loc) {
//        this.loc = loc;
//    }
//
//    public String getBirthday() {
//        return birthday;
//    }
//
//    public void setBirthday(String birthday) {
//        this.birthday = birthday;
//    }
//    public String getFollowers() {
//        return followers;
//    }
//
//    public void setFollowers(String followers) {
//        this.followers = followers;
//    }
//
//    public String getFollowing() {
//        return following;
//    }
//
//    public void setFollowing(String following) {
//        this.following = following;
//    }
