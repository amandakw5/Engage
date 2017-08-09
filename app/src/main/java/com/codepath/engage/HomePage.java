package com.codepath.engage;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Profile;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.mindorks.placeholderview.PlaceHolderView;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HomePage extends AppCompatActivity implements LocationListener,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    IssueAdapter adapter;
    ArrayList<String> issues;
    ArrayList<Integer> images;
    @BindView(R.id.rvIssues) RecyclerView rvIssues;
    @BindView(R.id.drawerView) PlaceHolderView mDrawerView;
    @BindView(R.id.drawer_layout) DrawerLayout mDrawer;
    @BindView(R.id.toolbar) Toolbar toolbar;
    //Variable that will reference the Search view/Search bar icon
    @BindView(R.id.search) SearchView searchView;
    @BindView(R.id.btnFilter) ImageButton btnFilter;
    @BindView(R.id.hpIssues) TextView hpIssues;
    String distance;
    String query;
    Context context;

    //Will hold the text that the user inputs to the search view

    //Following variables are for maps
    final String TAG = "GPS";
    static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    GoogleApiClient gac;
    LocationRequest locationRequest;
    String tvLatitude, tvLongitude, tvTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        context = getApplicationContext();
        FirebaseDatabase.getInstance().getReference("users").child(Profile.getCurrentProfile().getId()).child("firebasetoken").setValue(FirebaseInstanceId.getInstance().getToken());
        ButterKnife.bind(this);
        Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Medium.ttf");
        setSupportActionBar(toolbar);
        hpIssues.setTypeface(font);

        //Getting user location and setting location in google maps
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

        String[] strs = {"Women", "Food Insecurity", "Climate Change", "Human Rights", "Poverty"};
        ActionBar actionbar = getSupportActionBar();
        Integer[] issueImages = {R.drawable.womenmarch,R.drawable.desert, R.drawable.melting, R.drawable.pride, R.drawable.poverty};
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        URL profile_picture = null;
        EditText searchEditText = (EditText)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(Color.WHITE);
        searchEditText.setHintTextColor(Color.WHITE);

        try {
            profile_picture = new URL("https://graph.facebook.com/" + Profile.getCurrentProfile().getId() + "/picture?width=200&height=200");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        images = new ArrayList<>();
        images.addAll(Arrays.asList(issueImages));
        issues = new ArrayList<>();
        issues.addAll(Arrays.asList(strs));

        rvIssues.setLayoutManager(new LinearLayoutManager(this));

        setUpSearchView();

        adapter = new IssueAdapter(issues, tvLatitude, tvLongitude, images);
        rvIssues.setAdapter(adapter);
        setUpDrawer();
        toolbar.setTitle("");
        btnFilter = (ImageButton) findViewById(R.id.btnFilter);
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(HomePage.this,btnFilter);
                popup.getMenuInflater().inflate(R.menu.poupup_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        item.setChecked(true);
                        if(query != null) {
                            if (!query.equals("null")) {
                                Intent searchInt = new Intent(HomePage.this, ViewEvents.class);
                                searchInt.putExtra("Query", query);
                                searchInt.putExtra("Latitude", tvLatitude);
                                searchInt.putExtra("Longitude", tvLongitude);
                                searchInt.putExtra("distance", distance);
                                startActivity(searchInt);
                                overridePendingTransition(0, 0);
                            }
                        }
                        return true;
                    }
                });
                //show popup menu
                popup.show();
            }
        });
    }

    private void setUpDrawer(){
        mDrawerView
                .addView(new DrawerHeader(this.getApplicationContext()))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_PROFILE))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_FEED))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_EVENTS))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_CREATE))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_MESSAGE))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_NOTIF))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_LOGOUT));

        ActionBarDrawerToggle  drawerToggle = new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.open_drawer, R.string.close_drawer){

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

        mDrawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }

    private void closeSearchView(SearchView searchView){
        searchView.setIconified(true);
    }

    //Initializes all necessary values that will hold all the searchview values.
    private void setUpSearchView(){
        // Sets searchable configuration defined in searchable.xml for this SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() { //searchView, SearchView
            @Override
            public boolean onQueryTextSubmit(String query) {
               Intent i = new Intent(HomePage.this, ViewEvents.class);
                i.putExtra("Query", query);
                i.putExtra("Latitude",tvLatitude);
                i.putExtra("Longitude",tvLongitude);
                i.putExtra("distance", distance);
                startActivity(i);
                overridePendingTransition(0, 0);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                //filterSearchFor(query);
                return true;
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
            ActivityCompat.requestPermissions(HomePage.this,
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
                    Toast.makeText(HomePage.this, "Permission was granted!", Toast.LENGTH_LONG).show();

                    try{
                        LocationServices.FusedLocationApi.requestLocationUpdates(
                                gac, locationRequest, this);
                    } catch (SecurityException e) {
                        Toast.makeText(HomePage.this, "SecurityException:\n" + e.toString(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(HomePage.this, "Permission denied!", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(HomePage.this, "onConnectionFailed: \n" + connectionResult.toString(),
                Toast.LENGTH_LONG).show();
        Log.d("Connection", connectionResult.toString());
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
                Toast.makeText(this, "This device is not supported.", Toast.LENGTH_SHORT).show();
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
    public void onBackPressed() {
        overridePendingTransition(0, 0);
    }
}
