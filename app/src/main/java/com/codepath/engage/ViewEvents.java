package com.codepath.engage;

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
import android.support.design.widget.NavigationView;
import android.support.v13.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.codepath.engage.models.Event;
import com.codepath.engage.models.Organizer;
import com.codepath.engage.models.Venue;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
public class  ViewEvents extends AppCompatActivity implements LocationListener,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener  {
    //Following counters are used to be abel to access the position of the events arraylist in functions where the position of the event is not passed.
    static int counterToGetPositionOfEvent;
    static int counterToSetOrganizer;
    //Variable that will reference the Search view/ Search bar icon
    private SearchView searchView;
    //Will hold the text that the user inputs to the search view
    private String valueOfQuery;

    //Handle the storage and populating the activity to show the activites around one.
    private EventbriteClient client;
    EventAdapter eventAdapter;
    ArrayList<Event> events;
    ArrayList<Venue> venues;
    RecyclerView rvEvents;
    //Holds the search term that is used to pass into the eventbrite api to look for events
    String query;
    //Checks if the async call is completed to ensure that data is not being accessed before its actually popualted
    Boolean eventRequestCompleted = false;

    //Used in aiding in retreiving hte current location of the user.
    final String TAG = "GPS";
    private long UPDATE_INTERVAL = 2 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */
    static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    GoogleApiClient gac;
    LocationRequest locationRequest;
    String tvLatitude, tvLongitude, tvTime;
    //Settting the view for U.I
    private DrawerLayout mDrawer;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar toolbar;
    private ImageView profileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_events);
        counterToSetOrganizer = 0;
        counterToGetPositionOfEvent = 0;
        client = new EventbriteClient();
        //Sets up the listners needed for the input text of search view.
        setUpSearchView();
        //find the recycler view
        rvEvents = (RecyclerView) findViewById(R.id.rvEvents);
        //initiating the arraylsit
        events = new ArrayList<>();
        venues = new ArrayList<>();
        //constructing the adapter from this datasoruce
        eventAdapter = new EventAdapter(events);
        //recycler view setup(layout manager, use adapter'
        rvEvents.setLayoutManager(new LinearLayoutManager(this));
        // set the adapter
        rvEvents.setAdapter(eventAdapter);
        Intent intent = getIntent();
        query = intent.getStringExtra("Query");
        populateEvents(query);

        //Getting the location for the user.
        //Setting up the location google maps
        isGooglePlayServicesAvailable();
        locationRequest = new LocationRequest();
        locationRequest.setInterval(UPDATE_INTERVAL);
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
        if(intentQuery != null){
            callSearchFromIntent(intentQuery);
        }
        //Refrencing the variables to their respective I.Ds for the xml style sheet
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        profileImage = (ImageView) findViewById(R.id.profileImage);
        searchView = (SearchView) findViewById(R.id.search);
        ActionBar actionbar = getSupportActionBar();
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        setSupportActionBar(toolbar);
        configureNavigationDrawer();
        //Checks to see if the the image has been clicked and to display sub menu
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawer.openDrawer(Gravity.RIGHT);
            }
        });
    }
    private void configureNavigationDrawer() {
        NavigationView navView = (NavigationView) findViewById(R.id.nvView);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                Fragment f = null;
                int itemId = menuItem.getItemId();

                if (f != null) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.rvIssues, f);
                    transaction.commit();
                    mDrawer.closeDrawers();
                    return true;
                }
                switch (itemId){
                    case R.id.nav_first_fragment:
                        Intent i = new Intent(ViewEvents.this, UserFeed.class);
                        i.putParcelableArrayListExtra("events",events);
                        startActivity(i);
                }
                return false;
            }
        });
    }
    private void callSearchFromIntent(Intent intent){
        query = intent.getStringExtra("Query");
        tvLongitude = intent.getStringExtra("Longitude");
        tvLatitude = intent.getStringExtra("Latitude");
        Log.i("Query"," " + query);

        onStart();
        Log.i("Latitude",""+tvLatitude);
        Log.i("Longitude",""+tvLongitude);
        events.clear();
        populateEvents(query);
    }
    //Closes the input search view after user has submitted the query
    private void closeSearchView(SearchView searchView){
        searchView.setIconified(true);
    }
    //Initializes all necessary values that will hold all the searchview values.
    private void setUpSearchView(){
        searchView = (SearchView) findViewById(R.id.search);
        // Sets searchable configuration defined in searchable.xml for this SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //ON a successful query submission the query is passed and api request call is made
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
        events.clear();
        counterToGetPositionOfEvent=0;
        closeSearchView(searchView);
        client.getInfoByQuery(valueOfQuery,tvLatitude,tvLongitude,new JsonHttpResponseHandler(){
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
                    //makes suere that the previous call is completed before moving onto assigning the rest of the values to the event object.
                    if(eventRequestCompleted) {
                        counterToSetOrganizer =0;
                        for (int i = 0; i < events.size(); i++) {
                            client.getOrganizerInfo(events.get(i).getOrganizerId(), new JsonHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    Organizer organizer = Organizer.fromJson(response);
                                    events.get(counterToSetOrganizer).setOrganizer(organizer);
                                    events.get(counterToSetOrganizer).setOrganizerName(organizer.getName());
                                    eventAdapter.notifyDataSetChanged();
                                    counterToSetOrganizer++;
                                }
                            });
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                finally {
                    //Once the event object has been populated with the organizer and event information another call is made to
                    //Retrieve the veneu for the event
                    for (int i = 0; i < events.size(); i++) {
                        if (eventRequestCompleted) {
                            client.getVenue(events.get(i).getVeneuId(), new JsonHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    try {
                                        Venue venue = Venue.fromJSON(response.getJSONObject("address"));
                                        venues.add(venue);
                                        events.get(counterToGetPositionOfEvent).setVenue(venue);
                                        String address ="";
                                        if(!venue.getAddress().equals("null"))
                                            address += venue.getAddress();
                                        if(!venue.getCity().equals("null"))
                                            address += ", "+venue.getCity();
                                        if(!venue.getCountry().equals("null"))
                                            address += ", "+ venue.getCountry();
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
                Log.i("info",client.finalUrl+responseString);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.i("info",client.finalUrl+errorResponse);
            }
        });
    }
    //START FUNCTIONS TO GET THE USER LOCATION WITH GOOGLE MAPS API
    //Functions that deal with user location
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
        Log.d(TAG, "onConnected");

        Location ll = LocationServices.FusedLocationApi.getLastLocation(gac);
        Log.d(TAG, "LastLocation: " + (ll == null ? "NO LastLocation" : ll.toString()));

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
                return;
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

}
