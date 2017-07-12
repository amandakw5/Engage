package com.codepath.engage;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by emilyz on 7/12/17.
 */

public class Event {
    String eventName;
    String eventDescription;
    String eventUrl;
    String eventStartTime;
    String organizerId;
    String eventEndTime;
    int capacity;


    public Event() { super(); }

    public static Event fromJSON(JSONObject jsonObject) throws JSONException {
        Event event = new Event();

        event.capacity = jsonObject.getInt("capacity");
        event.eventUrl = jsonObject.getString("url");
        event.organizerId = jsonObject.getString("organizer_id");
        JSONObject startArray = jsonObject.getJSONObject("start");
        if(startArray != null){
            event.eventStartTime = startArray.getString("local");
        }
        JSONObject endArray = jsonObject.getJSONObject("end");
        if(endArray != null){
            event.eventEndTime = endArray.getString("local");
        }
        JSONObject nameArray = jsonObject.getJSONObject("name");
        if (nameArray != null){
            event.eventName = nameArray.getString("name");
        }
        JSONObject descriptionArray = jsonObject.getJSONObject("description");
        if (descriptionArray != null){
            event.eventDescription = descriptionArray.getString("text");
        }

        return event;
    }
}
