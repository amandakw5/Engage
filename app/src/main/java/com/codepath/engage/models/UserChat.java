package com.codepath.engage.models;

/**
 * Created by calderond on 7/31/17.
 */
public class UserChat {
    public String uid;
    public String email;
    public String firebaseToken;

    public UserChat(){

    }

    public UserChat(String uid, String email, String firebaseToken){
        this.uid = uid;
        this.email = email;
        this.firebaseToken = firebaseToken;
    }
}