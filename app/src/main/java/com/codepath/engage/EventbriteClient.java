package com.codepath.engage;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * Created by calderond on 7/11/17.
 */

public class EventbriteClient {

    static final String baseApiUrl = "https://www.eventbriteapi.com/v3/";
    static final String searchBaseUrl="events/search/?q=";
    //TODO fix secret key wiht authentication token
    static final String endBaseUrl = "&token=ZVEFG6DHKZONNTXLL5HM";

    public String finalUrl="";
    private static AsyncHttpClient client = new AsyncHttpClient();


    public void getInfoByQuery(String query, AsyncHttpResponseHandler asyncHttpResponseHandler){

        finalUrl = baseApiUrl+searchBaseUrl+query
                +endBaseUrl;
        client.get(finalUrl,asyncHttpResponseHandler);
    }

}
