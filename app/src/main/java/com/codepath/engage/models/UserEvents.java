package com.codepath.engage.models;

/**
 * Created by emilyz on 7/19/17.
 */

public class UserEvents {
    public String eventName;
    public String eventHost;
    public String eventInfo;
    public String eventId;

    public UserEvents(){ }

    public UserEvents(String eventName, String eventHost, String eventInfo,String eventId){
        this.eventName = eventName;
        this.eventHost = eventHost;
        this.eventInfo = eventInfo;
        this.eventId = eventId;
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
