package com.codepath.engage.models;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by calderond on 7/11/17.
 */

@Parcel
public class Event {
    public String tvEventName;
    public String tvEventInfo;
    public String tvDescription;
    public String ivEventImage;
    public String organizerName;
    public String eventId;
    public String veneuId;
    public String organizerId;
    public Venue venue;
    public Organizer organizer;


    public Event(String tvEventName, String tvEventInfo, String tvDescription, String ivEventImage, String eventId, String veneuId,Venue venue,Organizer organizer) {
        this.tvEventName = tvEventName;
        this.tvEventInfo = tvEventInfo;
        this.tvDescription = tvDescription;
        this.ivEventImage = ivEventImage;
        this.eventId = eventId;
        this.veneuId = veneuId;
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
        event.tvEventInfo = eventInfo.getString("utc");
        SimpleDateFormat existingUTCFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat requiredFormat = new SimpleDateFormat("MM-dd hh:mm a");
        event.eventId = jsonObject.getString("id");
        try{
            Date getDate = existingUTCFormat.parse(event.tvEventInfo);
            String mydate = requiredFormat.format(getDate);
            event.tvEventInfo = mydate;
        }
        catch(ParseException e){
            e.printStackTrace();
        }
        //Getting the description of the event
        JSONObject eventDescription = jsonObject.getJSONObject("description");
        event.tvDescription = eventDescription.getString("text");
        //Getting a thumbnail of the image for futer use.
        try{
            jsonObject.getJSONObject("logo");
            JSONObject logo = jsonObject.getJSONObject("logo");
            JSONObject original= logo.getJSONObject("original");
            event.ivEventImage = original.getString("url");
            Log.i("Ingo",event.ivEventImage);
        }
        catch (Exception exception){
            event.ivEventImage ="@drawable/tree";
        }
        event.veneuId = jsonObject.getString("venue_id");
        event.organizerId = jsonObject.getString("organizer_id");
        return event;
    }

    public Organizer getOrganizer() {
        return organizer;
    }

    public void setOrganizer(Organizer organizer) {
        this.organizer = organizer;
    }

    public String getOrganizerId() {
        return organizerId;
    }

    public void setOrganizerId(String organizerId) {
        this.organizerId = organizerId;
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

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
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

    public String getVeneuId() {
        return veneuId;
    }

    public void setVeneuId(String veneuId) {
        this.veneuId = veneuId;
    }

    public String getOrganizerName() {
        return organizerName;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public void setOrganizerName(String organizerName) {
        this.organizerName = organizerName;
    }
}
