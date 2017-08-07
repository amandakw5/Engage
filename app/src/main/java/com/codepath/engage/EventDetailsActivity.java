package com.codepath.engage;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.engage.models.Event;
import com.codepath.engage.models.UserEvents;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.parceler.Parcels;
import org.w3c.dom.Text;


public class EventDetailsActivity extends AppCompatActivity{

    private Toolbar toolbar;
    private ImageView ivBackdrop;
    private CollapsingToolbarLayout collapsingToolbar;
    private TextView tvTitle;
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
        collapsingToolbar=(CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        setToolbar();

        ivBackdrop = (ImageView) findViewById(R.id.ivBackdrop);
        tvTitle = (TextView) findViewById(R.id.tvTitle);

        vPager = (ViewPager) findViewById(R.id.viewpager);
        vPager.setAdapter(new TabFragmentAdapter(getSupportFragmentManager(), EventDetailsActivity.this, currentUpdate, event, isUserCreated));

        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(vPager);

        if(event!=null){
            tvTitle.setText(event.tvEventName);
            if (isUserCreated){
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("photos").child(String.valueOf(event.getEventId()));
                    Glide.with(this)
                            .using(new FirebaseImageLoader())
                            .load(storageReference)
                            .error(R.drawable.image_not_found)
                            .into(ivBackdrop);
                } else if (!event.ivEventImage.equals("null")) {
                    Glide.with(this)
                            .load(event.ivEventImage)
                            .centerCrop()
                            .into(ivBackdrop);
                } else if (event.ivEventImage.equals("null")) {
                    Glide.with(this)
                            .load(R.drawable.image_not_found)
                            .centerCrop()
                            .into(ivBackdrop);
                }
        } else if (currentUpdate!=null){
            tvTitle.setText(currentUpdate.eventName);
            if (!currentUpdate.eventImage.equals("null")) {
                    Glide.with(this)
                            .load(currentUpdate.eventImage)
                            .centerCrop()
                            .into(ivBackdrop);
                } else if (currentUpdate.eventImage.equals("null")) {
                    Glide.with(this)
                            .load(R.drawable.image_not_found)
                            .centerCrop()
                            .into(ivBackdrop);
                }
        }

        View root = tabLayout.getChildAt(0);
        if (root instanceof LinearLayout) {
            ((LinearLayout) root).setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
            GradientDrawable drawable = new GradientDrawable();
            drawable.setColor(getResources().getColor(R.color.white));
            drawable.setSize(2, 1);
            ((LinearLayout) root).setDividerPadding(0);
            ((LinearLayout) root).setDividerDrawable(drawable);
        }

    }

    private void setToolbar() {
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

}
