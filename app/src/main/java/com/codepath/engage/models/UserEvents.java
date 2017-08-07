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
    public String eventAddress;
    public String uid;
    public Date date;

    public UserEvents(){ }

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
