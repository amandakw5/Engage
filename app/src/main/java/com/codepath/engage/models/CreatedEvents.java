package com.codepath.engage.models;

import java.util.Date;

/**
 * Created by calderond on 7/26/17.
 */

public class CreatedEvents {

    private String eventAddress;
    private String eventDescription;
    private String eventHost;
    private String eventName;
    private String eventTime;
    private String uid;

    private String eventLocation;
    private String eventHour;
    private String eventMinute;
    private String eventDay;
    private String eventMonth;
    private String eventYear;
    private Date date;


    public CreatedEvents() {
    }

    public CreatedEvents(String eventName, String eventLocation, String eventDescription, String eventHour, String eventMinute, String eventDay, String eventMonth, String eventYear, String uid, Date dateCreated) {
        this.eventName = eventName;
        this.eventLocation = eventLocation;
        this.eventDescription = eventDescription;
        this.eventHour = eventHour;
        this.eventMinute = eventMinute;
        this.eventDay = eventDay;
        this.eventMonth = eventMonth;
        this.eventYear = eventYear;
        this.uid = uid;
        this.date = dateCreated;
    }
    public void setDateByValues(){
        eventDay = String.valueOf(date.getDay());
        eventHour = String.valueOf(date.getHours());
        eventMinute = String.valueOf(date.getMinutes());
        eventMonth =String.valueOf(date.getMonth());
        eventYear = String.valueOf(date.getYear());
        eventLocation = eventAddress;

    }
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getEventHour() {
        return eventHour;
    }

    public void setEventHour(String eventHour) {
        this.eventHour = eventHour;
    }

    public String getEventMinute() {
        return eventMinute;
    }

    public void setEventMinute(String eventMinute) {
        this.eventMinute = eventMinute;
    }

    public String getEventDay() {
        return eventDay;
    }

    public void setEventDay(String eventDay) {
        this.eventDay = eventDay;
    }

    public String getEventMonth() {
        return eventMonth;
    }

    public void setEventMonth(String eventMonth) {
        this.eventMonth = eventMonth;
    }

    public String getEventYear() {
        return eventYear;
    }

    public void setEventYear(String eventYear) {
        this.eventYear = eventYear;
    }

    public String getEventAddress() {
        return eventAddress;
    }

    public void setEventAddress(String eventAddress) {
        this.eventAddress = eventAddress;
    }

    public String getEventHost() {
        return eventHost;
    }

    public void setEventHost(String eventHost) {
        this.eventHost = eventHost;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }
}
