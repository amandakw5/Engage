package com.codepath.engage.models;

/**
 * Created by calderond on 7/31/17.
 */
//TODO model to be used for allowing chat rooms in implementation of this feature is reached.
public class UsersChat {

    private String emailId;
    private String lastMessage;
    private int notifCount;

    public String getEmailId(){ return emailId; }

    public void setEmailId(){ this.emailId = emailId; }

    public String getLastMessage(){ return lastMessage; }

    public void setLastMessage(){ this.lastMessage = lastMessage; }

    public int getNotifCount(){ return notifCount; }

    public void setNotifCount(int notifCount){ this.notifCount = notifCount; }
}