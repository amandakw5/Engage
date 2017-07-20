package com.codepath.engage.models;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

/**
 * Created by calderond on 7/17/17.
 */

@Parcel
public class Venue {
    public String address;
    public String city;
    public String region;
    public String postalCode;
    public String country;
    public String latitude;
    public String longitude;
    public String simpleAddress;

    public Venue() {
    }

    public Venue(String address, String city, String region, String postalCode, String country, String latitude, String longitude, String simpleAddress) {
        this.address = address;
        this.city = city;
        this.region = region;
        this.postalCode = postalCode;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
        this.simpleAddress = simpleAddress;
    }
    public static Venue fromJSON(JSONObject jsonObject)throws JSONException {
        Venue venue = new Venue();
        if(jsonObject.getString("address_1") == null){
            if(jsonObject.getString("address_2") == null)
                venue.address = "No Location Available";
            else
                venue.address = jsonObject.getString("address_2");
        }
        else
            venue.address = jsonObject.getString("address_1");
        venue.city = jsonObject.getString("city");
        venue.region = jsonObject.getString("region");
        venue.postalCode = jsonObject.getString("postal_code");
        venue.country = jsonObject.getString("country");
        venue.latitude = jsonObject.getString("latitude");
        venue.longitude = jsonObject.getString("longitude");
        venue.simpleAddress =venue.address +","+ venue.city +","+ venue.country;
        Log.i("SIMPLE", venue.simpleAddress);
        return venue;
    }
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getSimpleAddress() {
        return simpleAddress;
    }

    public void setSimpleAddress(String simpleAddress) {
        this.simpleAddress = simpleAddress;
    }
}
