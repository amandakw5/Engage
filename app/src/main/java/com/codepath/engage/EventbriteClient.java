package com.codepath.engage;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * Created by calderond on 7/11/17.
 */

public class EventbriteClient {

    static final String baseApiUrl = "https://www.eventbriteapi.com/v3/";
    static final String searchBaseUrl="events/search/?q=";
    static final String endBaseUrl = "&token="+Integer.toString(R.string.EventBriteKey);

    public String finalUrl="";
    private static AsyncHttpClient client = new AsyncHttpClient();


    public void getInfoByQuery(String query, AsyncHttpResponseHandler asyncHttpResponseHandler){
        Log.i("key",R.string.EventBriteKey);
        finalUrl = baseApiUrl+searchBaseUrl+query
                +endBaseUrl;
        client.get(finalUrl,asyncHttpResponseHandler);
    }

}
