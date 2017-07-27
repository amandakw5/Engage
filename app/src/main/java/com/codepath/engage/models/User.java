package com.codepath.engage.models;

import com.google.firebase.database.IgnoreExtraProperties;

import org.parceler.Parcel;

import java.util.HashMap;
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
<<<<<<< HEAD
    public HashMap<String,String> followers;
    public HashMap<String,String>  following;
    public List<String> eventsList;
=======
    public HashMap<String,String> following;
    public HashMap<String,String> followers;

    public List<String> followersL;
    public List<String>  followingL;
>>>>>>> c207e1a185fdc5f5dab48fe45f989d943fbae415


    public User(){
    }

<<<<<<< HEAD
    public User(String uid, String firstName, String lastName, String email, String profilePicture, int numFollowers, int numFollowing, HashMap<String,String> followers, HashMap<String,String> following) { //, List<String> eventsList
=======
    public User(String uid, String firstName, String lastName, String email, String profilePicture, int numFollowers, int numFollowing, HashMap<String,String>followers, HashMap<String,String> following) { //
>>>>>>> c207e1a185fdc5f5dab48fe45f989d943fbae415
        this.uid = uid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.profilePicture = profilePicture;
        this.numFollowers = numFollowers;
        this.numFollowing = numFollowing;
        this.following = following;
        this.followers = followers;
<<<<<<< HEAD
       // this.eventsList = eventsList;
    }

    public List<String> getEventsList() {
        return eventsList;
    }

    public void setEventsList(List<String> eventsList) {
        this.eventsList = eventsList;
    }

    public HashMap<String,String> getFollowers() {
        return followers;
    }

    public void setFollowers(HashMap<String,String> followers) {
        this.followers = followers;
    }

    public HashMap<String,String> getFollowing() {
        return following;
    }

    public void setFollowing(HashMap<String,String> following) {
        this.following = following;
=======
        for(String str : following.values())
            followingL.add(str);
        for(String str : followers.values())
            followersL.add(str);

    }
    public User(String uid, String firstName, String lastName, String email, String profilePicture, int numFollowers, int numFollowing, List<String> followers,List<String> following) { //
        this.uid = uid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.profilePicture = profilePicture;
        this.numFollowers = numFollowers;
        this.numFollowing = numFollowing;
        this.followingL = following;
        this.followersL = followers;

    }
    public HashMap<String, String> getFollowing() {
        return following;
    }

    public void setFollowing(HashMap<String, String> following) {
        this.following = following;
    }

    public HashMap<String, String> getFollowers() {
        return followers;
    }

    public void setFollowers(HashMap<String, String> followers) {
        this.followers = followers;
    }

    public List<String> getFollowersL() {
        return followersL;
    }

    public void setFollowersL(List<String> followersL) {
        this.followersL = followersL;
    }

    public List<String> getFollowingL() {
        return followingL;
    }

    public void setFollowingL(List<String> followingL) {
        this.followingL = followingL;
>>>>>>> c207e1a185fdc5f5dab48fe45f989d943fbae415
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