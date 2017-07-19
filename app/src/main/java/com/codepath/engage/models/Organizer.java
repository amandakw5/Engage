package com.codepath.engage.models;

import android.util.Log;

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
    public String numPastEvents;
    public String numFutureEvents;
    public String website;
    public String facebookUsername;
    public String twitter;
    public String name;

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
            organizer.organizerId = "NA";
            e.printStackTrace();
        }
        try {
            organizer.numPastEvents =  jsonObject.getString("num_past_events");
        } catch (JSONException e) {
            organizer.numPastEvents = "Na";
            e.printStackTrace();
        }
        try {
            organizer.numFutureEvents = jsonObject.getString("num_future_events");
        } catch (JSONException e) {
            organizer.numFutureEvents = "NA";
            e.printStackTrace();
        }
        try {
            organizer.website = jsonObject.getString("website");
        } catch (JSONException e) {
            Log.i("Error",e.getMessage());
            organizer.website = "NA";
            e.printStackTrace();
        }
        try {
            organizer.facebookUsername = jsonObject.getString("facebook");
        } catch (JSONException e) {
            organizer.facebookUsername = "NA";
            e.printStackTrace();
        }
        try {
            organizer.name = jsonObject.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            organizer.twitter = jsonObject.getString("twitter");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return organizer;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
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

    public String getNumPastEvents() {
        return numPastEvents;
    }

    public void setNumPastEvents(String numPastEvents) {
        this.numPastEvents = numPastEvents;
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
