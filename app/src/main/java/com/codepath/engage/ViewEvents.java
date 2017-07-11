package com.codepath.engage;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ViewEvents extends AppCompatActivity {
    private SearchView searchView;
    private String valueOfQuery;
    EventbriteClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_events);
        client = new EventbriteClient();
        setUpSearchView();
    }
    //Perfoms The Searching Of Desired Event Category
    private void searchFor(String query){
    }
    private void closeSearchView(SearchView searchView){
        searchView.setIconified(true);
        searchView.setIconified(true);
    }
    private void setUpSearchView(){
        searchView = (SearchView) findViewById(R.id.search);
        // Sets searchable configuration defined in searchable.xml for this SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                valueOfQuery = query;
                searchFor(valueOfQuery);
                closeSearchView(searchView);
                client.getInfoByQuery(valueOfQuery,new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.i("Inof","T"+response);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.i("info","T"+client.finalUrl+responseString);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.i("info","T"+client.finalUrl+errorResponse);
                    }
                });
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                //filterSearchFor(query);
                return true;
            }
        });
    }
}
