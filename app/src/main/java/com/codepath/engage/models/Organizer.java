package com.codepath.engage.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

/**
 * Created by calderond on 7/18/17.
 */
@Parcel
public class Organizer {
    public String description;
    public String organizerId;
    public String numePastEvents;
    public String numFutureEvents;
    public String website;
    public String facebookUsername;

    public Organizer() {
    }
    public static Organizer fromJson(JSONObject jsonObject){
        Organizer organizer = new Organizer();
        try {
            organizer.description = jsonObject.getJSONObject("description").getString("text");
        } catch (JSONException e) {
            organizer.description ="NA";
            e.printStackTrace();
        }
        try {
            organizer.organizerId = jsonObject.getString("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            organizer.numePastEvents =  jsonObject.getString("num_past_events");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            organizer.numFutureEvents = jsonObject.getString("num_future_events");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            organizer.website = jsonObject.getString("website");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            organizer.facebookUsername = jsonObject.getString("facebook");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return organizer;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOrganizerId() {
        return organizerId;
    }

    public void setOrganizerId(String organizerId) {
        this.organizerId = organizerId;
    }

    public String getNumePastEvents() {
        return numePastEvents;
    }

    public void setNumePastEvents(String numePastEvents) {
        this.numePastEvents = numePastEvents;
    }

    public String getNumFutureEvents() {
        return numFutureEvents;
    }

    public void setNumFutureEvents(String numFutureEvents) {
        this.numFutureEvents = numFutureEvents;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getFacebookUsername() {
        return facebookUsername;
    }

    public void setFacebookUsername(String facebookUsername) {
        this.facebookUsername = facebookUsername;
    }
}
