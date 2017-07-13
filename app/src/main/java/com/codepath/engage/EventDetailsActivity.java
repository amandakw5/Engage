package com.codepath.engage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.engage.models.Event;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EventDetailsActivity extends AppCompatActivity {

    @Nullable @BindView(R.id.ivPicture) ImageView ivPicture;
    @BindView(R.id.tvHost) TextView tvHost;
    @BindView(R.id.tvLocation) TextView tvLocation;
    @BindView(R.id.tvTimeDate) TextView tvTimeDate;
    @BindView(R.id.tvEventDescription) TextView tvEventDescription;
    @BindView(R.id.tvPeopleParticipating) TextView tvPeopleParticipating;
    @BindView(R.id.btnSave) Button btnSave;
    @BindView(R.id.tvEventName) TextView tvEventName;
    @BindView(R.id.btnMap) Button btnMap;

    private EventbriteClient client;
    Event event;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        ButterKnife.bind(this);

        client = new EventbriteClient();
        event = Parcels.unwrap(getIntent().getParcelableExtra(Event.class.getSimpleName()));
//        position = Parcels.unwrap(getIntent().getParcelableExtra("Position"));

        Glide.with(this)
                .load(event.ivEventImage)
                .into(ivPicture);
        tvEventName.setText(event.tvEventName);
        tvEventDescription.setText(event.tvDescription);
        tvTimeDate.setText(event.tvEventInfo);

    }
    @OnClick(R.id.btnMap)
    public void getMap() {
//        Intent intent = new Intent(this, MapActivity.class);
//        startActivity(intent);
    }
}
