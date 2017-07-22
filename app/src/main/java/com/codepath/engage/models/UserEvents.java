package com.codepath.engage.models;

/**
 * Created by emilyz on 7/19/17.
 */

public class UserEvents {
    public String eventName;
    public String eventHost;
    public String eventInformation;
    public String eventVenue;
    public String eventId;

    public UserEvents(){ }

    public UserEvents(String eventId, String eventName, String eventHost, String eventInformation){
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventHost = eventHost;
        this.eventInformation = eventInformation;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventHost() {
        return eventHost;
    }

    public void setEventHost(String eventHost) {
        this.eventHost = eventHost;
    }

    public String getEventInformation() {
        return eventInformation;
    }

    public void setEventInformation(String eventInformation) {
        this.eventInformation = eventInformation;
    }

    public String getEventVenue() {
        return eventVenue;
    }

    public void setEventVenue(String eventVenue) {
        this.eventVenue = eventVenue;
    }
}