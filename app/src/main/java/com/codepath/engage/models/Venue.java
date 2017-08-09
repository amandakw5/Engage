package com.codepath.engage.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by calderond on 7/17/17.
 */


public class Venue implements Parcelable {

    public String address;
    public String city;
    public String region;
    public String postalCode;
    public String country;
    public String latitude;
    public String longitude;
    public String simpleAddress;
    public String id;
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

    protected Venue(Parcel in) {
        address = in.readString();
        city = in.readString();
        region = in.readString();
        postalCode = in.readString();
        country = in.readString();
        latitude = in.readString();
        longitude = in.readString();
        simpleAddress = in.readString();
        id = in.readString();
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static final Creator<Venue> CREATOR = new Creator<Venue>() {
        @Override
        public Venue createFromParcel(Parcel in) {
            return new Venue(in);
        }

        @Override
        public Venue[] newArray(int size) {
            return new Venue[size];
        }
    };

    public static Venue fromJSON(JSONObject jsonObject)throws JSONException {
        Venue venue = new Venue();
        JSONObject address = jsonObject.getJSONObject("address");
        if(address.getString("address_1") == null){
            if(address.getString("address_2") == null)
                venue.address = "No Location Available";
            else
                venue.address = address.getString("address_2");
        }
        else
            venue.address = address.getString("address_1");
        venue.city = address.getString("city");
        venue.region = address.getString("region");
        venue.postalCode = address.getString("postal_code");
        venue.country = address.getString("country");
        venue.latitude = address.getString("latitude");
        venue.longitude = address.getString("longitude");
        venue.simpleAddress =venue.address +", "+ venue.city +", "+ venue.country;
        venue.id = jsonObject.getString("id");
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(address);
        dest.writeString(city);
        dest.writeString(region);
        dest.writeString(postalCode);
        dest.writeString(country);
        dest.writeString(latitude);
        dest.writeString(longitude);
        dest.writeString(simpleAddress);
        dest.writeString(id);
    }
}
