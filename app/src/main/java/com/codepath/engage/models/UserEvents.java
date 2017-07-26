package com.codepath.engage.models;

import org.parceler.Parcel;

/**
 * Created by emilyz on 7/19/17.
 */
@Parcel
public class UserEvents {
    public String eventName;
    public String eventHost;
    public String eventDescription;
    public String eventVenue;
    public String eventInfo;
    public String eventId;
    public String eventImage;
    public String eventTime;
    public String eventAddress;

    public UserEvents(){ }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

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

    public UserEvents(String eventName, String eventHost, String eventTime, String eventAddress, String eventId, String eventImage, String eventDescription){
        this.eventName = eventName;
        this.eventTime = eventTime;
        this.eventAddress = eventAddress;
        this.eventHost = eventHost;
        this.eventId = eventId;
        this.eventImage = eventImage;
        this.eventDescription = eventDescription;

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
}
