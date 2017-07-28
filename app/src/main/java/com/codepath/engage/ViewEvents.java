package com.codepath.engage;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v13.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.codepath.engage.models.CreatedEvents;
import com.codepath.engage.models.Event;
import com.codepath.engage.models.Organizer;
import com.codepath.engage.models.User;
import com.codepath.engage.models.Venue;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mindorks.placeholderview.PlaceHolderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

import static com.codepath.engage.R.string.location;

public class  ViewEvents extends AppCompatActivity implements LocationListener,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener  {

    //Setting the view for U.I
    @BindView(R.id.btnFilter) ImageButton btnFilter;
    @BindView(R.id.drawerView) PlaceHolderView mDrawerView;
    //Variable that will reference the Search view/ Search bar icon
    @BindView(R.id.search) SearchView searchView;
    @BindView(R.id.rvEvents) RecyclerView rvEvents;
    @BindView(R.id.drawer_layout) DrawerLayout mDrawer;
    @BindView(R.id.toolbar) Toolbar toolbar;

    //Following counters are used to be abel to access the position of the events arraylist in functions where the position of the event is not passed.
    static int counterToGetPositionOfEvent;
    static int counterToSetOrganizer;

    //Will hold the text that the user inputs to the search view
    private String valueOfQuery;
    ProgressDialog progress;
    //Handle the storage and populating the activity to show the activites around one.
    private EventbriteClient client;

    EventAdapter eventAdapter;
    UserAdapter userAdapter;
    ArrayList<Event> events;
    ArrayList<CreatedEvents> createdEventsList;
    ArrayList<Venue> venues;
    ArrayList<User> users;

    String tvLatitude, tvLongitude, tvTime;
    String distance;
    //Holds the search term that is used to pass into the Eventbrite api to look for events
    String query;

    //Checks if the async call is completed to ensure that data is not being accessed before its actually popualted
    Boolean eventRequestCompleted = false;

    //Used in aiding in retrieving the current location of the user.
    final String TAG = "GPS";
    static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    GoogleApiClient gac;
    LocationRequest locationRequest;
    //Firebase Variables
    private DatabaseReference mDatabase;
    private DatabaseReference createdEvents;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_events);

        progress  = new ProgressDialog(ViewEvents.this);

        ButterKnife.bind(this);

        counterToSetOrganizer = 0;
        counterToGetPositionOfEvent = 0;

        client = new EventbriteClient();

        //Sets up the listeners needed for the input text of search view.
        setUpSearchView();

        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        //find the recycler view
        rvEvents = (RecyclerView) findViewById(R.id.rvEvents);
        //initiating the array list
        createdEventsList = new ArrayList<>();
        events = new ArrayList<>();
        venues = new ArrayList<>();
        users = new ArrayList<>();

        //constructing the adapter from this datasource
        //constructing the adapter from this data source
        //recycler view setup(layout manager, use adapter)
        rvEvents.setLayoutManager(new LinearLayoutManager(this));

        createdEvents = FirebaseDatabase.getInstance().getReference("CreatedEvents");

        //Getting the location for the user.
        //Setting up the location google maps
        isGooglePlayServicesAvailable();
        locationRequest = new LocationRequest();

        long UPDATE_INTERVAL = 2 * 1000;
        locationRequest.setInterval(UPDATE_INTERVAL);

        long FASTEST_INTERVAL = 2000;
        locationRequest.setFastestInterval(FASTEST_INTERVAL);

        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        gac = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        if(!isLocationEnabled())
            showAlert();
        Intent intentQuery = getIntent();
        if(intentQuery != null && intentQuery.getStringExtra("Query") != null ){
            if(intentQuery.getStringExtra("distance") != null){
                distance = intentQuery.getStringExtra("distance");
            }
            callSearchFromIntent(intentQuery);
        }
        //Referencing the variables to their respective I.Ds for the xml style sheet
        ActionBar actionbar = getSupportActionBar();
        setSupportActionBar(toolbar);
        setUpDrawer();

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(ViewEvents.this,btnFilter);
                popup.getMenuInflater().inflate(R.menu.poupup_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        distance = item.getTitle().toString();
                        if(query != null || !query.equals("null"))
                            populateEvents(query);
                        return true;
                    }
                });
                popup.show(); //show popup menu

            }
        });
        searchView.setQuery(query, false);
    }

    private void setUpDrawer(){
        mDrawerView
                .addView(new DrawerHeader(this.getApplicationContext()))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_PROFILE))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_FEED))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_EVENTS))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_CREATE))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_LOGOUT));

        ActionBarDrawerToggle  drawerToggle = new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.open_drawer, R.string.close_drawer){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

            }
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

        mDrawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }

    private void callSearchFromIntent(Intent intent){
        query = intent.getStringExtra("Query");
        tvLongitude = intent.getStringExtra("Longitude");
        tvLatitude = intent.getStringExtra("Latitude");

        onStart();
        events.clear();
        if (query.startsWith("~")){
            userAdapter = new UserAdapter(users,0);
            rvEvents.setAdapter(userAdapter);
            populateUsers(query);
        }
        else{
            eventAdapter = new EventAdapter(events, users, 1);
            rvEvents.setAdapter(eventAdapter);
            populateEvents(query);
        }
    }

    //Closes the input search view after user has submitted the query
    private void closeSearchView(SearchView searchView){
        searchView.setIconified(true);
    }

    //Initializes all necessary values that will hold all the search view values.
    private void setUpSearchView(){
        // Sets searchable configuration defined in searchable.xml for this SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //ON a successful query submission the query is passed and api request call is made

                if (query.startsWith("~")){
                    userAdapter = new UserAdapter(users,0);
                    rvEvents.setAdapter(userAdapter);
                    populateUsers(query);
                } else {
                    eventAdapter = new EventAdapter(events, users, 1);
                    rvEvents.setAdapter(eventAdapter);
                    populateEvents(query);
                }
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
        progress.setMessage("Retrieving Events");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.show();
        eventAdapter.clear();
        events.clear();
        venues.clear();
        valueOfQuery = query;
        //Getting events stored from in firabse database
        createdEvents.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i = 1;
                int includeEvent =0;
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){

                    CreatedEvents userEvents = snapshot.getValue(CreatedEvents.class);
                    if (userEvents != null) {
                        Event event = new Event(userEvents.getEventName(), userEvents.getEventLocation() + "\n" + userEvents.getEventDay() + "/" + userEvents.getEventMonth() + " " + userEvents.getEventHour() + ":" + userEvents.getEventMinute(), userEvents.getEventDescription(), "null", String.valueOf(i));
                        if (userEvents.getEventName().contains(valueOfQuery)) {
                            event.setCreatedEvent(true);
                            events.add(event);
                            eventAdapter.notifyItemInserted(events.size() - 1);
                        }
                        String[] split = userEvents.getEventDescription().split("\\s+");
                        for (String aSplit : split) {
                            if (aSplit.contains(valueOfQuery))
                                includeEvent++;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //End getting data from stored firebase database
        counterToGetPositionOfEvent=0;
        eventRequestCompleted = false;
        client.getInfoByQuery(valueOfQuery,tvLatitude,tvLongitude,distance,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    //Retrieving all the events that are related to the search query
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
                    //makes sure that the previous call is completed before moving onto assigning the rest of the values to the event object.
                    if(eventRequestCompleted) {
                        counterToSetOrganizer =0;
                        for (int i = 0; i < events.size(); i++) {
                            client.getOrganizerInfo(events.get(i).getOrganizerId(), new JsonHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    Organizer organizer = Organizer.fromJson(response);

                                    for(int i = 0; i < events.size();i++){
                                        if(events.get(i).getOrganizerId().equals(organizer.getOrganizerId())){
                                            events.get(i).setOrganizer(organizer);
                                            events.get(i).setOrganizerName(organizer.getName());
                                            eventAdapter.notifyDataSetChanged();
                                        }
                                    }
                                }
                            });

                        }
                    }
                    //Once the event object has been populated with the organizer and event information another call is made to
                    //Retrieve the venue for the event
                    if (eventRequestCompleted) {
                        for (int i = 0; i < events.size(); i++) {
                            client.getVenue(events.get(i).getVeneuId(), new JsonHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    try {
                                        Venue venue = Venue.fromJSON(response);
                                        for(int i = 0; i  < events.size();i++) {
                                            if(events.get(i).getVeneuId().equals(venue.getId())) {
                                                venues.add(venue);
                                                events.get(i).setVenue(venue);
                                                String address = "";
                                                if (!venue.getAddress().equals("null"))
                                                    address += venue.getAddress();
                                                if (!venue.getCity().equals("null"))
                                                    address += ", " + venue.getCity();
                                                if (!venue.getCountry().equals("null"))
                                                    address += ", " + venue.getCountry();
                                                events.get(i).setTvEventInfo(events.get(i).getTvEventInfo() + "\n" + address);
                                                eventAdapter.notifyDataSetChanged();
                                            }
                                        }
                                        progress.dismiss();
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
                Log.i("info",client.finalUrl+responseString);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.i("info",client.finalUrl+errorResponse);
            }
        });
    }

    private void populateUsers(final String query){
        progress.setMessage("Retrieving Users");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.show();
        users.clear();
        userAdapter.clear();
        counterToGetPositionOfEvent=0;
        Log.i("Info", query);
        eventRequestCompleted = false;
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                int i = query.indexOf(' ');
                String first = query.substring(1, i);
                String last = query.substring(i+1);
                Log.i("Info",last);
                for (DataSnapshot evSnapshot : dataSnapshot.getChildren()) {
                    String f = (String) evSnapshot.child("firstName").getValue();
                    String l = (String) evSnapshot.child("lastName").getValue();
                    if (f != null && l != null) {
                        if (f.equals(first) && l.equals(last)) {
                            User u = evSnapshot.getValue(User.class);
                            if (u != null) {
                                u.setUid(evSnapshot.getKey());
                            }
                            users.add(u);
                            Log.i("Info", "Added user");
                            userAdapter.notifyItemInserted(users.size() - 1);
                        }
                    }
                }
                progress.dismiss();
            }
            @Override public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //START FUNCTIONS TO GET THE USER LOCATION WITH GOOGLE MAPS API, deal with user location
    @Override
    protected void onStart() {
        gac.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        gac.disconnect();
        super.onStop();
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            updateUI(location);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ViewEvents.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

            return;
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(gac, locationRequest, this);
    }
    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(ViewEvents.this, "Permission was granted!", Toast.LENGTH_LONG).show();

                    try{
                        LocationServices.FusedLocationApi.requestLocationUpdates(
                                gac, locationRequest, this);
                    } catch (SecurityException e) {
                        Toast.makeText(ViewEvents.this, "SecurityException:\n" + e.toString(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(ViewEvents.this, "Permission denied!", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(ViewEvents.this, "onConnectionFailed: \n" + connectionResult.toString(),
                Toast.LENGTH_LONG).show();
        Log.d("DDD", connectionResult.toString());
    }
    private void updateUI(Location loc) {
        Log.d(TAG, "updateUI");
        tvLatitude = Double.toString(loc.getLatitude());
        tvLongitude = Double.toString(loc.getLongitude());

    }
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
    private boolean isGooglePlayServicesAvailable() {
        final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.d(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        Log.d(TAG, "This device is supported.");
        return true;
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    }
                });
        dialog.show();
    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }
}
