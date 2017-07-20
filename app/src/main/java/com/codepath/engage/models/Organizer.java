package com.codepath.engage.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by calderond on 7/18/17.
 */
public class Organizer implements Parcelable{
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

    protected Organizer(Parcel in) {
        description = in.readString();
        organizerId = in.readString();
        numPastEvents = in.readString();
        numFutureEvents = in.readString();
        website = in.readString();
        facebookUsername = in.readString();
        twitter = in.readString();
        name = in.readString();
    }

    public static final Creator<Organizer> CREATOR = new Creator<Organizer>() {
        @Override
        public Organizer createFromParcel(Parcel in) {
            return new Organizer(in);
        }

        @Override
        public Organizer[] newArray(int size) {
            return new Organizer[size];
        }
    };

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(description);
        dest.writeString(organizerId);
        dest.writeString(numPastEvents);
        dest.writeString(numFutureEvents);
        dest.writeString(website);
        dest.writeString(facebookUsername);
        dest.writeString(twitter);
        dest.writeString(name);
    }
}
