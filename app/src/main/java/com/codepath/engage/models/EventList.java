package com.codepath.engage.models;

import java.util.ArrayList;

/**
 * Created by awestort on 7/21/17.
 */

public class EventList {
    public ArrayList<UserEvents> events;

    public EventList (ArrayList<UserEvents> events){
        this.events = events;
    }

    public ArrayList<UserEvents> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<UserEvents> events) {
        this.events = events;
    }
}
