package com.codepath.engage.models;

import java.util.List;

/**
 * Created by emilyz on 7/19/17.
 */

public interface DirectionFinderListener {
    void onDirectionFinderStart();
    void onDirectionFinderSuccess(List<Route> route);
}