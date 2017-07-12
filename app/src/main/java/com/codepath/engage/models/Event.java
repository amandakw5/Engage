package com.codepath.engage.models;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by calderond on 7/11/17.
 */

public class Event {
    public String tvEventName;
    public String tvEventInfo;
    public String tvDescription;
    public String ivEventImage;
    public String eventId;

    public Event(String tvEventName, String tvEventInfo, String tvDescription, String ivEventImage, String eventId) {
        this.tvEventName = tvEventName;
        this.tvEventInfo = tvEventInfo;
        this.tvDescription = tvDescription;
        this.ivEventImage = ivEventImage;
        this.eventId = eventId;
    }

    public Event() {
    }

    public static Event fromJSON(JSONObject jsonObject)throws JSONException {
        Event event = new Event();
        //Getting the name of the event
        JSONObject nameEvent = jsonObject.getJSONObject("name");
        event.tvEventName = nameEvent.getString("text");
        //Getting the description for the event Time and location only
        JSONObject eventInfo = jsonObject.getJSONObject("start");
        event.tvEventInfo = eventInfo.getString("local");
        //Getting the description of the event
        JSONObject eventDescription = jsonObject.getJSONObject("description");
        event.tvDescription = eventDescription.getString("text");
        //Getting teh id of the event for future use
        event.eventId = jsonObject.getString("id");
        //Getting a thumbnail of the image for futer use.
        JSONObject logo = jsonObject.getJSONObject("logo");
        JSONObject original= logo.getJSONObject("original");
        event.ivEventImage = original.getString("url");
        Log.i("Ingo",event.ivEventImage);
        return event;
    }
    public String getTvEventName() {
        return tvEventName;
    }

    public void setTvEventName(String tvEventName) {
        this.tvEventName = tvEventName;
    }

    public String getTvEventInfo() {
        return tvEventInfo;
    }

    public void setTvEventInfo(String tvEventInfo) {
        this.tvEventInfo = tvEventInfo;
    }

    public String getTvDescription() {
        return tvDescription;
    }

    public void setTvDescription(String tvDescription) {
        this.tvDescription = tvDescription;
    }

    public String getIvEventImage() {
        return ivEventImage;
    }

    public void setIvEventImage(String ivEventImage) {
        this.ivEventImage = ivEventImage;
    }
}
