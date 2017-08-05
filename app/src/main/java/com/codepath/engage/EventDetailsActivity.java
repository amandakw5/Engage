package com.codepath.engage;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.codepath.engage.models.Event;
import com.codepath.engage.models.UserEvents;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.parceler.Parcels;


public class EventDetailsActivity extends AppCompatActivity{

    UserEvents currentUpdate;
    Event event;
    ViewPager vPager;
    TabLayout tabLayout;

    boolean isUserCreated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currentUpdate = Parcels.unwrap(getIntent().getParcelableExtra("current"));

        event = Parcels.unwrap(getIntent().getParcelableExtra(Event.class.getSimpleName()));

        isUserCreated = Parcels.unwrap(getIntent().getParcelableExtra("isCreated"));

        setContentView(R.layout.activity_event_details);

        vPager = (ViewPager) findViewById(R.id.viewpager);
        vPager.setAdapter(new TabFragmentAdapter(getSupportFragmentManager(), EventDetailsActivity.this, currentUpdate, event, isUserCreated));

        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(vPager);

        final ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener(){
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                Log.d("position","position = " + position);
                switch(position){
                    case 0:
                        EventDetailsFragment eventDetailsFragment = EventDetailsFragment.newInstance(currentUpdate, event, isUserCreated);
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.details_container, eventDetailsFragment);
                        ft.commit();
                        break;
                    case 1:
                        MapFragment mapFragment= MapFragment.newInstance(currentUpdate, event, isUserCreated);
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.details_container, mapFragment);
                        fragmentTransaction.commit();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };

        vPager.addOnPageChangeListener(pageChangeListener);

        pageChangeListener.onPageSelected(vPager.getCurrentItem());

    }

}
