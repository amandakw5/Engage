package com.codepath.engage;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by calderond on 7/11/17.
 */

public class EventbriteClient {
    //Intial url for the base url for eventbrite
    static final String baseApiUrl = "https://www.eventbriteapi.com/v3/";
    //Endpint for seraching with a string input
    static final String searchBaseUrl="events/search/?q=";
    static final String eventFindUrl = "events/";
    static final String venueUrl = "venues/";
    static final String organizerUrl = "organizers/";
    static final String endBaseUrl = "&token=ZVEFG6DHKZONNTXLL5HM";
    static final String endVenueUrl = "/?token=ZVEFG6DHKZONNTXLL5HM";
    static final String endOrganizerUrl = "/?token=ZVEFG6DHKZONNTXLL5HM";
    //variable to hold the complete url.
    public String finalUrl="";
    //Client to handle network calls
    private static AsyncHttpClient client = new AsyncHttpClient();
    //Searches for events based on the query passed into the function. Will return a list of objects that fall under the search query
    public void getInfoByQuery(String query,String latitude,String longitude, AsyncHttpResponseHandler asyncHttpResponseHandler){
        RequestParams requestParams= new RequestParams();
        requestParams.put("location.longitude",longitude);

        requestParams.put("location.latitude",latitude);
        requestParams.put("sort_by","distance");
        requestParams.put("location.within","15mi");

                finalUrl = baseApiUrl + searchBaseUrl + query + endBaseUrl;
        Log.i("FINAL URL",finalUrl);

        client.get(finalUrl,requestParams,asyncHttpResponseHandler);

    }
    public void getOrganizerInfo(String id, AsyncHttpResponseHandler asyncHttpResponseHandler){
        finalUrl = baseApiUrl + venueUrl+id+endOrganizerUrl;
        client.get(finalUrl,asyncHttpResponseHandler);
    }
    public void getEventInfo(String eventId, AsyncHttpResponseHandler asynchHttpResponseHandler){
        finalUrl = baseApiUrl + eventFindUrl + eventId + endBaseUrl;
        client.get(finalUrl, asynchHttpResponseHandler);
    }
    public void getVenue(String id, AsyncHttpResponseHandler asyncHttpResponseHandler){
        finalUrl = baseApiUrl + venueUrl + id+ endVenueUrl;
        client.get(finalUrl,asyncHttpResponseHandler);
    }

}
