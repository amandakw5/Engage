package com.codepath.engage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;

import com.codepath.engage.models.Event;
import com.codepath.engage.models.UserEvents;

import java.util.ArrayList;

import static com.codepath.engage.EventDetailsFragment.newInstance;

/**
 * Created by emilyz on 8/1/17.
 */

public class TabFragmentAdapter extends FragmentPagerAdapter {
    private Context context;
    private Event event;
    private UserEvents currentUpdate;
    private Boolean isUserCreated;

    public TabFragmentAdapter(FragmentManager fm, Context context, UserEvents currentUpdate, Event event, Boolean isUserCreated) {
        super(fm);
        this.context = context;
        this.event = event;
        this.currentUpdate = currentUpdate;
        this.isUserCreated = isUserCreated;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                new EventDetailsFragment();
                return EventDetailsFragment.newInstance(currentUpdate, event, isUserCreated);
            case 1:
                new MapFragment();
                return  MapFragment.newInstance(currentUpdate, event, isUserCreated);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case 0:
                return "Event Details";
            case 1:
                return "Map";
            default:
                return null;
        }
    }
}
