package com.codepath.engage;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v13.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

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
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

import static com.codepath.engage.ViewEvents.counterToGetPositionOfEvent;
import static com.codepath.engage.ViewEvents.counterToSetOrganizer;

public class IssueDetailsActivity extends AppCompatActivity implements LocationListener,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    RecyclerView rvIssueSubsections;
    IssueDetailsAdapter adapter;
    ArrayList<String> issueSubsectionTitles;
    String[] specificIssues;
    String[] organizations;
    String[] upcomingEvents;
    String[] urlIssues;
    String[] urlOrgs;
    ArrayList<Venue> venues;
    List<String> upEvents;
    @BindView(R.id.issueTitle) TextView issueTitle;
    ArrayList<Event> events;
    ArrayList<User> allUsers;
    GoogleApiClient gac;
    LocationRequest locationRequest;
    String tvLatitude, tvLongitude, tvTime;
    private EventbriteClient client;
    Boolean eventRequestCompleted = false;
    int yes = 0;
    ProgressDialog progress;
    //Following
    final String TAG = "GPS";
    static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    String distance = "15mi";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_details);
        progress  = new ProgressDialog(IssueDetailsActivity.this);
        Typeface font = Typeface.createFromAsset(this.getAssets(), "fonts/Roboto-Light.ttf");
        String[] strs = {"Specific Issues", "Organizations", "Upcoming Events"}; //, "Past events"
        String[] womenSpecificIssues = new String[] {"Sexual and Reproductive Rights","Freedom from violence", "Economic and Political Empowerment"};
        String[] womenOrganizations = new String[] {"National Organization for Women","Planned Parenthood", "Association of Women's Rights in Development", "American Association of University Women"};
        String[] foodSpecificIssues = new String[] {"World Hunger", "Malnutrition", "Food Contamination and Disease"};
        String[] foodOrganizations = new String[] {"World Food Program", "Action Against Hunger", "The Food and Agriculture Organization of the United Nations"};
        String[] climateSpecificIssues = new String[] {"Global Warming/Climate Change", "Rising Sea Levels", "More Frequent Extreme Weather"};
        String[] climateOrganizations = new String[] {"350.org", "GreenPeace","Climate Reality Project", "iMatter"};
        String[] humanRightsSpecificIssues = new String[] {"LGBTQ Rights", "Disability Rights", "Racism", "Refugee Rights"};
        String[] humanRightsOrganizations = new String[] {"Amnesty International", "Human Rights Watch", "Human Rights Campaign"};
        String[] povertySpecificIssues = new String[] {"Education", "Homelessness", "Poor Health"};
        String[] povertyOrganizations = new String[] {"ONE Campaign", "UNICEF", "Partners in Health"};
        String[] wSpecificIssues = new String[] {"http://www.ohchr.org/EN/Issues/Women/WRGS/Pages/HealthRights.aspx", "http://www.ohchr.org/EN/Issues/Women/WRGS/Pages/VAW.aspx", "http://www.unwomen.org/en/what-we-do/economic-empowerment", "http://www.unwomen.org/en"};
        String[] wOrganizations = new String[] {"http://now.org/about/", "https://www.plannedparenthood.org/learn", "https://www.awid.org/about-us", "http://salsa4.salsalabs.com/o/50796/p/dia/action4/common/public/?action_KEY=22954", "https://en.wikipedia.org/wiki/List_of_women%27s_organizations"};
        String[] fSpecificIssues = new String[] {"http://www.worldhunger.org/learn-about-hunger/", "https://data.unicef.org/topic/nutrition/malnutrition/", "http://www.who.int/foodsafety/about/FOSSP-En.pdf?ua=1", "http://www.un.org/en/sections/issues-depth/food/index.html"};
        String[] fOrganizations = new String[] {"http://www1.wfp.org/overview", "http://www.actionagainsthunger.org/about", "http://www.fao.org/about/en/", "https://mswonlineprograms.org/poverty-hunger/"};
        String[] cSpecificIssues = new String[] {"http://www.un.org/en/sections/issues-depth/climate-change/", "http://www.nationalgeographic.com/environment/global-warming/sea-level-rise/", "http://nca2014.globalchange.gov/highlights/report-findings/extreme-weather", "http://www.un.org/sustainabledevelopment/climatechange/"};
        String[] cOrganizations = new String[] {"https://350.org/about/", "http://greenpeacefund.org/our-projects/", "https://www.climaterealityproject.org/our-mission", "http://www.imatteryouth.org/about-us/", "https://en.wikipedia.org/wiki/Category:Climate_change_organizations"};
        String[] hRSpecificIssues = new String[] {"https://www.unfe.org/about/", "https://www.ada.gov/cguide.htm", "http://www.un.org/en/events/racialdiscriminationday/index.shtml", "http://www.unhcr.org/afr/publications/brochures/3b779dfe2/protecting-refugees-questions-answers.html", "http://www.ohchr.org/EN/Issues/Pages/WhatareHumanRights.aspx"};
        String[] hROrganizations = new String[] {"https://www.amnesty.org/en/", "https://www.hrw.org/topic/lgbt-rights", "http://www.hrc.org/", "http://www.humanrights.com/voices-for-human-rights/human-rights-organizations/non-governmental.html"};
        String[] pSpecificIssues = new String[] {"http://www.un.org/sustainabledevelopment/wp-content/uploads/2017/02/ENGLISH_Why_it_Matters_Goal_4_QualityEducation.pdf", "http://www.ohchr.org/EN/Issues/Housing/Pages/Homelessnessandhumanrights.aspx", "https://www.healthpovertyaction.org/info-and-resources/the-cycle-of-poverty-and-poor-health/key-facts/", "http://www.un.org/sustainabledevelopment/poverty/"};
        String[] pOrganizations = new String[] {"https://www.one.org/international/about/", "https://www.unicef.org/about-us", "https://www.pih.org/pages/our-mission", "https://borgenproject.org/top-10-global-poverty-nonprofits/"};
        Intent intent = getIntent();
        String issue = intent.getStringExtra("current");

        tvLatitude = intent.getStringExtra("tvLatitude");
        tvLongitude = intent.getStringExtra("tvLongitude");

        counterToSetOrganizer = 0;
        counterToGetPositionOfEvent = 0;

        client = new EventbriteClient();

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

        switch (issue) {
            case "Women":
                organizations = womenOrganizations;
                specificIssues = womenSpecificIssues;
                urlIssues = wSpecificIssues;
                urlOrgs = wOrganizations;
                break;
            case "Food Insecurity":
                specificIssues = foodSpecificIssues;
                organizations = foodOrganizations;
                urlIssues = fSpecificIssues;
                urlOrgs = fOrganizations;
                break;
            case "Climate Change":
                organizations = climateOrganizations;
                specificIssues = climateSpecificIssues;
                urlIssues = cSpecificIssues;
                urlOrgs = cOrganizations;
                break;
            case "Human Rights":
                organizations = humanRightsOrganizations;
                specificIssues = humanRightsSpecificIssues;
                urlIssues = hRSpecificIssues;
                urlOrgs = hROrganizations;
                break;
            default:
                organizations = povertyOrganizations;
                specificIssues = povertySpecificIssues;
                urlIssues = pSpecificIssues;
                urlOrgs = pOrganizations;
                break;
        }

        ButterKnife.bind(this);
        issueTitle.setTypeface(font);
        issueTitle.setText(issue);
        issueSubsectionTitles = new ArrayList<>();
        upcomingEvents = new String[3];
        issueSubsectionTitles.addAll(Arrays.asList(strs));
        upEvents = new ArrayList<String>();
        allUsers = new ArrayList<>();
        venues = new ArrayList<>();
        events = new ArrayList<>();
        adapter = new IssueDetailsAdapter(issue, issueSubsectionTitles, specificIssues, organizations, upEvents, events, urlIssues, urlOrgs);
        rvIssueSubsections = (RecyclerView) findViewById(R.id.rvIssueSubsections);
        rvIssueSubsections.setLayoutManager(new LinearLayoutManager(this));
        rvIssueSubsections.setAdapter(adapter);
        //construct the adapter from this data source

    }
    private void getEventsInfo(String issue){
        progress.setMessage("Updating");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.show();
        counterToGetPositionOfEvent = 0;
        if (issue.equals("Food Insecurity")){
            issue = "Food";
        }
        client.getInfoByQuery(issue,tvLatitude,tvLongitude,distance,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray eventsObject = response.getJSONArray("events");
                    for (int i = 0 ; i < 3; i++){
                        Event event = Event.fromJSON(eventsObject.getJSONObject(i));
                        events.add(event);
                        upEvents.add(event.tvEventName);
                        adapter.notifyItemInserted(upEvents.size() - 1);
                        adapter.notifyItemInserted(events.size() - 1);
                        adapter.notifyDataSetChanged();
                        if(i == 2)
                            eventRequestCompleted = true;
                    }

                    if(eventRequestCompleted) {
                        counterToSetOrganizer = 0;
                        for (int i = 0; i < events.size(); i++) {
                            client.getOrganizerInfo(events.get(i).getOrganizerId(), new JsonHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    Organizer organizer = Organizer.fromJson(response);
                                    events.get(counterToSetOrganizer).setOrganizer(organizer);
                                    counterToSetOrganizer++;
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                finally {
                    for (int i = 0; i < events.size(); i++) {
                        if (eventRequestCompleted) {
                            client.getVenue(events.get(i).getVenueId(), new JsonHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    try {
                                        Venue venue = Venue.fromJSON(response);
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
                                        adapter.notifyDataSetChanged();
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
                    progress.dismiss();
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
            ActivityCompat.requestPermissions(IssueDetailsActivity.this,
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
                    Toast.makeText(IssueDetailsActivity.this, "Permission was granted!", Toast.LENGTH_LONG).show();

                    try{
                        LocationServices.FusedLocationApi.requestLocationUpdates(
                                gac, locationRequest, this);
                    } catch (SecurityException e) {
                        Toast.makeText(IssueDetailsActivity.this, "SecurityException:\n" + e.toString(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(IssueDetailsActivity.this, "Permission denied!", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(IssueDetailsActivity.this, "onConnectionFailed: \n" + connectionResult.toString(),
                Toast.LENGTH_LONG).show();
        Log.d("DDD", connectionResult.toString());
    }

    private void updateUI(Location loc) {
        Log.d(TAG, "updateUI");
        tvLatitude = Double.toString(loc.getLatitude());
        tvLongitude = Double.toString(loc.getLongitude());
        yes++;
        if (yes == 1){
            Intent intent = getIntent();
            String issue = intent.getStringExtra("current");
            getEventsInfo(issue);
        }

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
