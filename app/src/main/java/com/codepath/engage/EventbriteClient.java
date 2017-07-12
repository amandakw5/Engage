package com.codepath.engage;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * Created by calderond on 7/11/17.
 */

public class EventbriteClient {
    //Intial url for the base url for eventbrite
    static final String baseApiUrl = "https://www.eventbriteapi.com/v3/";
    //Endpint for seraching with a string input
    static final String searchBaseUrl="events/search/?q=";
    //TODO fix secret key wiht authentication token. Was getting werid erros when refrencing from a R.id.string
    static final String endBaseUrl = "&token=ZVEFG6DHKZONNTXLL5HM";
    //variable to hold the complete url.
    public String finalUrl="";
    //Client to handle network calls
    private static AsyncHttpClient client = new AsyncHttpClient();
    //Searches for events based on the query passed into the function. Will return a list of objects that fall under the search query
    public void getInfoByQuery(String query, AsyncHttpResponseHandler asyncHttpResponseHandler){

        finalUrl = baseApiUrl+searchBaseUrl+query
                +endBaseUrl;
        client.get(finalUrl,asyncHttpResponseHandler);
    }

}
