package com.codepath.engage.models;

import com.google.firebase.database.IgnoreExtraProperties;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by awestort on 7/18/17.
 */

@IgnoreExtraProperties
@Parcel
public class User {
    public String uid;
    public String firstName;
    public String lastName;
    public String email;
    public String profilePicture;
    public int numFollowers;
    public int numFollowing;
    public List<User> followers;
    public List<User>  following;


    public User(){
    }

    public User(String uid, String firstName, String lastName, String email, String profilePicture, int numFollowers, int numFollowing, List<User> followers, List<User> following) { //
        this.uid = uid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.profilePicture = profilePicture;
        this.numFollowers = numFollowers;
        this.numFollowing = numFollowing;
        this.following = following;
        this.followers = followers;
    }

    public List<User> getFollowers() {
        return followers;
    }

    public void setFollowers(ArrayList<User> followers) {
        this.followers = followers;
    }

    public List<User> getFollowing() {
        return following;
    }

    public void setFollowing(ArrayList<User> following) {
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

    public int getNumFollowers() { return numFollowers; }

    public void setNumFollowers(int numFollowers) { this.numFollowers = numFollowers; }

    public int getNumFollowing() { return numFollowing; }

    public void setNumFollowing(int numFollowing) { this.numFollowing = numFollowing; }
}