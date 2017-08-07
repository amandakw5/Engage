package com.codepath.engage;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.engage.models.DirectionFinder;
import com.codepath.engage.models.DirectionFinderListener;
import com.codepath.engage.models.Event;
import com.codepath.engage.models.Route;
import com.codepath.engage.models.UserEvents;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.location.LocationManager.NETWORK_PROVIDER;

public class MapFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, DirectionFinderListener {

    MapView mMapView;

    GoogleMap googleMap;
    GoogleApiClient mGoogleApiClient;
    Marker mCurrLocationMarker;

    UserEvents currentUpdate;
    Event event;
    Double destLat;
    Double destLng;

    @BindView(R.id.tvDuration)
    TextView tvDuration;
    @BindView(R.id.tvDistance)
    TextView tvDistance;
    @BindView(R.id.btnGoogleMaps)
    Button btnGoogleMaps;

    boolean isUserCreated;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;

    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();

    public static final int PERMISSIONS_REQUEST_LOCATION = 99;
    private static final int CONNECTION_RESOLUTION_REQUEST = 2;

    public static MapFragment newInstance(UserEvents currentUpdate, Event event, boolean isUserCreated) {
        MapFragment mapFragment = new MapFragment();

        Bundle args = new Bundle();

        if (event != null) {
            args.putParcelable("event", event);
        } else {
            args.putString("nothingE", "null");
        }
        if (currentUpdate != null){
            args.putParcelable("currentUpdate", currentUpdate);
        }
        args.putBoolean("isUserCreated", isUserCreated);

        mapFragment.setArguments(args);

        return mapFragment;
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        Bundle bundle = getArguments();
        try {
            event = bundle.getParcelable("event");

        } catch (Exception e) {
            e.printStackTrace();
            event = null;
        }

        try{
            currentUpdate = bundle.getParcelable("currentUpdate");
        } catch (Exception e){
            e.printStackTrace();
            currentUpdate = null;
        }

        isUserCreated = bundle.getBoolean("isUserCreated");

        if (event != null) {
            if (isUserCreated) {
                if (event.tvEventInfo == null){
                    noInfo(googleMap);
                } else {
                    getLocationFromAddress(getContext(), event.tvEventInfo);
                }
            } else if (event.tvEventInfo == null) {
                noInfo(googleMap);
        } else if (event.venue.getLatitude() == null || event.venue.getLongitude() == null){
                getLocationFromAddress(getContext(), event.tvEventInfo);
            } else {
                destLat = Double.parseDouble(event.venue.getLatitude());
                destLng = Double.parseDouble(event.venue.getLongitude());
            }
        } else if (currentUpdate!= null) {
            getLocationFromAddress(getContext(), currentUpdate.eventAddress);
        }

        buildGoogleApiClient();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        ButterKnife.bind(this, rootView);
        mMapView.onCreate(savedInstanceState);

        // needed to get the map to display immediately
        mMapView.onResume();

        try {
            MapsInitializer.initialize(this.getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(event!= null) {
            btnGoogleMaps.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + event.venue.getSimpleAddress());
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);
                }
            });
        }

        switch (GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity())) {
            case ConnectionResult.SUCCESS:
                mMapView.onCreate(savedInstanceState);
                // Gets to GoogleMap from the MapView and does initialization stuff

                if (mMapView != null) {
                    mMapView.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                                    //Location Permission already granted
                                    googleMap.setMyLocationEnabled(true);
                                        showMap(googleMap);
                                } else {
                                    //Request Location Permission
                                    checkLocationPermission();
                                }
                            } else {
                                buildGoogleApiClient();
                                googleMap.setMyLocationEnabled(true);
                            }
                        }
                    });
                }
                break;
            case ConnectionResult.SERVICE_MISSING:
                Toast.makeText(getActivity(), "SERVICE MISSING", Toast.LENGTH_SHORT).show();
                break;
            case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
                Toast.makeText(getActivity(), "UPDATE REQUIRED", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(getActivity(), GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity()), Toast.LENGTH_SHORT).show();
        }

        return rootView;
    }

    @Override
    public void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, (LocationListener) this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    public void showMap(GoogleMap googleMap) {

        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = locationManager.isProviderEnabled(NETWORK_PROVIDER);
        Location location;

        if (!isGPSEnabled && !isNetworkEnabled) {
            // no network provider is enabled
            Toast.makeText(getContext(), "Check GPS or Network", Toast.LENGTH_SHORT).show();
        } else {
            this.canGetLocation = true;
            checkLocationPermission();
            if (isNetworkEnabled) {
                location = locationManager.getLastKnownLocation(NETWORK_PROVIDER);
                if (location != null) {
                    locationChanged(location, googleMap);
                }
            }
            // if GPS Enabled get lat/long using GPS Services
            if (isGPSEnabled) {
                try {
                    locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    public void locationChanged(Location location, GoogleMap googleMap) {

        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        double originLat = location.getLatitude();
        double originLong = location.getLongitude();

        sendRequest(originLat, originLong, destLat, destLng, googleMap);
    }

    private void sendRequest(double originLat, double originLong, double destLat, double destLong, GoogleMap googleMap) {
        try {
            new DirectionFinder(this, originLat, originLong, destLat, destLong, googleMap).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDirectionFinderStart() {
        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline:polylinePaths ) {
                polyline.remove();
            }
        }
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {

    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes, final GoogleMap googleMap) {

        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (Route route : routes) {
            tvDuration.setText(route.duration.text);
            tvDistance.setText(route.distance.text);

            originMarkers.add(googleMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
                    .title("Current Location: " + route.startAddress)
                    .position(route.startLocation)));
            destinationMarkers.add(googleMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                    .title("Destination: " + route.endAddress)
                    .position(route.endLocation)));

            PolylineOptions polylineOptions = new PolylineOptions().geodesic(true).color(Color.BLUE).width(10);

            final LatLngBounds camera = new LatLngBounds(new LatLng(route.southWestLat, route.southWestLong), new LatLng(route.northEastLat, route.northEastLong));

            googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                @Override
                public void onMapLoaded() {
                    googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                            .target(googleMap.getCameraPosition().target)
                            .build()));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(camera, 100));
                }
            });

            googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                @Override
                public void onCameraIdle() {
                    googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                            .target(googleMap.getCameraPosition().target)
                            .zoom(15)
                            .build()));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(camera, 100));
                    googleMap.setOnCameraIdleListener(null);
                }
            });

            googleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
                @Override
                public void onCameraMove() {
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(camera, 100));
                    googleMap.setOnCameraMoveListener(null);
                }
            });

            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));

            polylinePaths.add(googleMap.addPolyline(polylineOptions));
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        if (mGoogleApiClient == null) { buildGoogleApiClient(); }

                        googleMap.setMyLocationEnabled(true);
                    }

                } else {
                    // permission denied! Disable the functionality that depends on this permission.
                    Toast.makeText(getActivity(), "Permission denied", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(getActivity())
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) { }

    @Override
    public void onConnectionSuspended(int i) { }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(getActivity(), CONNECTION_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                // There was an error with the resolution intent. Try again.
                mGoogleApiClient.connect();
            }
        } else {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), getActivity(), 1);
            dialog.show();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    public void getLocationFromAddress(Context context,String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return;
            }

            Address location = address.get(0);

            destLat = location.getLatitude();
            destLng = location.getLongitude();

        } catch (IOException ex) {

            ex.printStackTrace();
        }
    }

    public void noInfo(GoogleMap googleMap) {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = locationManager.isProviderEnabled(NETWORK_PROVIDER);
        Location location;

        if (!isGPSEnabled && !isNetworkEnabled) {
            // no network provider is enabled
            Toast.makeText(getContext(), "Check GPS and Network", Toast.LENGTH_SHORT).show();
        } else {
            this.canGetLocation = true;
            checkLocationPermission();
            if (isNetworkEnabled) {
                location = locationManager.getLastKnownLocation(NETWORK_PROVIDER);
                if (location != null) {
                    changeLocation(location, googleMap);
                }
            }
            // if GPS Enabled get lat/long using GPS Services
            if (isGPSEnabled) {
                try {
                    locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void changeLocation(Location location, GoogleMap googleMap) {

        double latitude = location.getLatitude();
        double longitude =location.getLongitude();

        LatLng loc = new LatLng(latitude, longitude);

        googleMap.addMarker(new MarkerOptions()
                .position(loc)
                .title("Current Location")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));

    }


}