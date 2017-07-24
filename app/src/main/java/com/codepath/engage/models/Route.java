package com.codepath.engage.models;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by emilyz on 7/19/17.
 */

public class Route {
    public Distance distance;
    public Duration duration;
    public String endAddress;
    public LatLng endLocation;
    public String startAddress;
    public LatLng startLocation;
    public int northEastLat;
    public int northEastLong;
    public int southWestLat;
    public int southWestLong;

    public List<LatLng> points;
}
