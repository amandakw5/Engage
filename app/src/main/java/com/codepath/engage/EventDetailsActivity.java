package com.codepath.engage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class EventDetailsActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Event Name");
        setContentView(R.layout.activity_event_details);

    }
}
