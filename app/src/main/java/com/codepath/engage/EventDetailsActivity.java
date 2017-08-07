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
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.engage.models.Event;
import com.codepath.engage.models.UserEvents;
import com.facebook.Profile;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.parceler.Parcels;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.codepath.engage.R.id.btnMap;
import static com.codepath.engage.R.id.ivPicture;
import static com.codepath.engage.R.id.map;
import static com.codepath.engage.R.id.start;
import static com.codepath.engage.R.id.tvEventDescription;
import static com.codepath.engage.R.id.tvEventInfo;
import static com.codepath.engage.R.id.tvEventName;
import static com.codepath.engage.R.id.view;


public class EventDetailsActivity extends AppCompatActivity{

    ArrayList<String> createdEventInfo;
    UserEvents currentUpdate;
    Event event;

    ViewPager vPager;
    TabLayout tabLayout;

    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createdEventInfo = Parcels.unwrap(getIntent().getParcelableExtra("createdEventInfo"));

        currentUpdate = Parcels.unwrap(getIntent().getParcelableExtra("current"));

        event = Parcels.unwrap(getIntent().getParcelableExtra(Event.class.getSimpleName()));

        setContentView(R.layout.activity_event_details);

        vPager = (ViewPager) findViewById(R.id.viewpager);

        vPager.setAdapter(new TabFragmentAdapter(getSupportFragmentManager(), EventDetailsActivity.this, createdEventInfo, currentUpdate, event));

        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);

        tabLayout.setupWithViewPager(vPager);
        vPager.setCurrentItem(0);

        final ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener(){
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                Log.d("position","position = " + position);
                fragment = null;
                switch(position){
                    case 0:
                        EventDetailsFragment eventDetailsFragment = EventDetailsFragment.newInstance(createdEventInfo, currentUpdate, event);
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.details_container, eventDetailsFragment);
                        ft.commit();
                        break;
                    case 1:
                        MapFragment mapFragment= MapFragment.newInstance(createdEventInfo, currentUpdate, event);
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
