package com.codepath.engage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.codepath.engage.models.Event;
import com.codepath.engage.models.UpdateAdapter;

import org.parceler.Parcels;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {
    RecyclerView rvUpdates;
    UpdateAdapter adapter;
    public ArrayList<Update> updates;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        updates = new ArrayList<Update>();
        adapter = new UpdateAdapter(updates);

        rvUpdates = (RecyclerView) findViewById(R.id.rvUpdates);
        rvUpdates.setLayoutManager(new LinearLayoutManager(this));
        rvUpdates.setAdapter(adapter);

        Event event = Parcels.unwrap(getIntent().getParcelableExtra(Event.class.getSimpleName()));
    }
}
