package com.codepath.engage;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * Created by emilyz on 7/12/17.
 */

public class EventBriteClient {
    static final String baseApiUrl = "https://www.eventbriteapi.com/v3/";
    static final String searchBaseUrl = "events/search/?q=";
    static final String eventFindUrl = "events/";
    //TODO fix secret key with authentication token
    static final String endBaseUrl = "&token=ZVEFG6DHKZONNTXLL5HM";
    public String finalUrl="";
    private static AsyncHttpClient client = new AsyncHttpClient();

    public void getInfoByQuery(String query, AsyncHttpResponseHandler asyncHttpResponseHandler){
        finalUrl = baseApiUrl + searchBaseUrl + query + endBaseUrl;
        client.get(finalUrl,asyncHttpResponseHandler);
    }

    public void getEventInfo(String eventId, AsyncHttpResponseHandler asynchHttpResponseHandler){
        finalUrl = baseApiUrl + eventFindUrl + eventId + endBaseUrl;
        client.get(finalUrl, asynchHttpResponseHandler);
    }
}
