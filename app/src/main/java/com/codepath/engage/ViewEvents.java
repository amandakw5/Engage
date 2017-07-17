package com.codepath.engage;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;

import com.codepath.engage.models.Event;
import com.codepath.engage.models.Venue;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class ViewEvents extends AppCompatActivity  {
    static int counterToGetPositionOfEvent;
    //Variable that will refrence the Search view/ Search bar icon
    private SearchView searchView;
    //Will hold teh text that the user inputs to the serach view
    private String valueOfQuery;

    //Variables used to populate the timeline
    private EventbriteClient client;
    EventAdapter eventAdapter;
    ArrayList<Event> events;
    ArrayList<Venue> venues;
    RecyclerView rvEvents;
    Boolean eventRequestCompleted = false;
    Boolean venueRequestCompleted = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_events);
        counterToGetPositionOfEvent = 0;
        client = new EventbriteClient();
        //Sets up the listners needed for the input text of search view.
        setUpSearchView();

        //find the recycler view
        rvEvents = (RecyclerView) findViewById(R.id.rvEvents);
        //init the arraylsit
        events = new ArrayList<>();
        venues = new ArrayList<>();
        //construcct the adapter from this datasoruce
        eventAdapter = new EventAdapter(events);
        //recycler view setup(layout manager, use adapter'
        rvEvents.setLayoutManager(new LinearLayoutManager(this));
        // set the adapter
        rvEvents.setAdapter(eventAdapter);
    }
    //Perfoms The Searching Of Desired Event Category
    //TODO finish this function
    private void searchFor(String query){
    }
    private void closeSearchView(SearchView searchView){
        searchView.setIconified(true);
        searchView.setIconified(true);
    }
    //Initilalizes all necessary values that will hold all the searchview values.
    private void setUpSearchView(){
        searchView = (SearchView) findViewById(R.id.search);
        // Sets searchable configuration defined in searchable.xml for this SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                events.clear();
                populateEvents(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                //filterSearchFor(query);
                return true;
            }
        });
    }
    private void populateEvents(String query){
        valueOfQuery = query;
        searchFor(valueOfQuery);
        closeSearchView(searchView);
        client.getInfoByQuery(valueOfQuery,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray eventsObject = response.getJSONArray("events");
                    for (int i = 0 ; i < eventsObject.length();i++){
                        Event event = Event.fromJSON(eventsObject.getJSONObject(i));
                        events.add(event);
                        eventAdapter.notifyItemInserted(events.size() -1);
                        if(i == eventsObject.length() -1)
                            eventRequestCompleted = true;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                finally {
                    for (int i = 0; i < events.size(); i++) {
                        if (eventRequestCompleted) {
                            client.getVenue(events.get(i).getVeneuId(), new JsonHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    try {
                                        Venue venue = Venue.fromJSON(response.getJSONObject("address"));
                                        Log.i("INFO WORKS", venue.getSimpleAddress());
                                        venues.add(venue);
                                        events.get(counterToGetPositionOfEvent).setVenue(venue);
                                        String address ="";
                                        if(!venue.getAddress().equals("null"))
                                            address += venue.getAddress();
                                        if(!venue.getCity().equals("null"))
                                            address += ","+venue.getCity();
                                        if(!venue.getCountry().equals("null"))
                                            address += ","+ venue.getCountry();
                                        events.get(counterToGetPositionOfEvent).setTvEventInfo(events.get(counterToGetPositionOfEvent).getTvEventInfo() +"\n"+ address);
                                        counterToGetPositionOfEvent++;
                                        eventAdapter.notifyDataSetChanged();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                @Override
                                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                    super.onFailure(statusCode, headers, throwable, errorResponse);
                                }
                            });

                        }
                    }
                }
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

    }

}
