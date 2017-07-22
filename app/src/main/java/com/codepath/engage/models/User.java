package com.codepath.engage.models;

import com.google.firebase.database.IgnoreExtraProperties;

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
    public int followers;
    public int following;


    public User(){
    }

    public User(String firstName, String lastName, String email, String profilePicture, int followers, int following) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.profilePicture = profilePicture;
        this.followers = followers;
        this.following = following;
    }

    public String getUid() { return uid; }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() { return lastName; }

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

    public int getFollowers() { return followers; }

    public void setFollowers(int followers) { this.followers = followers; }

    public int getFollowing() { return following; }

    public void setFollowing(int following) { this.following = following; }
}