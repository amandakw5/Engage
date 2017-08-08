package com.codepath.engage.models;

import android.os.Parcelable;

import org.parceler.Parcel;

import java.util.Date;

/**
 * Created by emilyz on 7/19/17.
 */
@Parcel
public class UserEvents implements Parcelable {
    public String eventName;
    public String eventHost;
    public String eventDescription;
    public String eventVenue;
    public String eventInfo;
    public String eventId;
    public String eventImage;
    public String eventTime;
    public String eventLocation;
    public String eventAddress;
    public boolean createdByUser;
    public String uid;
    public Date date;

    public UserEvents(){ }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) { this.eventDescription = eventDescription; }

    public String getEventVenue() {
        return eventVenue;
    }

    public void setEventVenue(String eventVenue) {
        this.eventVenue = eventVenue;
    }

    public String getEventImage() {
        return eventImage;
    }

    public void setEventImage(String eventImage) {
        this.eventImage = eventImage;
    }
    public void updateLocations(){
        if(eventAddress == null || eventAddress.equals("null")){
            eventAddress = eventLocation;
        }
        else{
            eventLocation = eventAddress;
        }
    }
    public UserEvents(String eventName, String eventHost, String eventTime, String eventAddress, String eventId, String eventImage, String eventDescription, String uid, Date date){
        this.eventName = eventName;
        this.eventTime = eventTime;
        this.eventAddress = eventAddress;
        this.eventHost = eventHost;
        this.eventId = eventId;
        this.eventImage = eventImage;
        this.eventDescription = eventDescription;
        this.uid = uid;
        this.date = date;
        createdByUser = false;

    }
    public void setUserDateByValues(String date, String day, String hours, String minutes, String months, String seconds, String time, String timezoneOffset, String year){
        this.date = new Date(Integer.parseInt(year),Integer.parseInt(months), Integer.parseInt(date),Integer.parseInt(hours),Integer.parseInt(minutes),Integer.parseInt(seconds));
    }

    public boolean isCreatedByUser() {
        return createdByUser;
    }

    public void setCreatedByUser(boolean createdByUser) {
        this.createdByUser = createdByUser;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getEventAddress() {
        return eventAddress;
    }

    public void setEventAddress(String eventAddress) {
        this.eventAddress = eventAddress;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
}

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventName() { return eventName; }

    public void setEventName(String eventName) { this.eventName = eventName; }

    public String getEventHost() { return eventHost; }

    public void setEventHost(String eventHost) { this.eventHost = eventHost; }

    public String getEventInfo() { return eventInfo; }

    public void setEventInfo(String eventInfo) { this.eventInfo = eventInfo; }

    @Override
    public int describeContents() {
        return 0;
    }

    private UserEvents(android.os.Parcel in) {
        eventName = in.readString();
        eventHost = in.readString();
        eventDescription = in.readString();
        eventVenue = in.readString();
        eventInfo = in.readString();
        eventId = in.readString();
        eventImage = in.readString();
        eventTime = in.readString();
        eventAddress = in.readString();
    }

    public static final Creator<UserEvents> CREATOR = new Creator<UserEvents>() {
        @Override
        public UserEvents createFromParcel(android.os.Parcel in) {
            return new UserEvents(in);
        }

        @Override
        public UserEvents[] newArray(int size) {
            return new UserEvents[size];
        }
    };

    @Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeString(eventName);
        dest.writeString(eventHost);
        dest.writeString(eventDescription);
        dest.writeString(eventVenue);
        dest.writeString(eventInfo);
        dest.writeString(eventId);
        dest.writeString(eventImage);
        dest.writeString(eventTime);
        dest.writeString(eventAddress);
    }
}
