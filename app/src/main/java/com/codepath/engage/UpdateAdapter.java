package com.codepath.engage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codepath.engage.models.CreatedEvents;
import com.codepath.engage.models.UserEvents;
import com.facebook.Profile;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by awestort on 7/18/17.
 */

public class UpdateAdapter extends RecyclerView.Adapter<UpdateAdapter.ViewHolder> {
    public ArrayList<UserEvents> mEvents;
    public ArrayList<CreatedEvents> cEvents;
    private Context context;
    private String profilePage;
    public String uid;
    public String verb;
    public ArrayList<Date> dates;
    public ArrayList<UserEvents> finalEvents;


    public UpdateAdapter(ArrayList<UserEvents> events, String who, String v, ArrayList<Date> dates) {
        this.dates = dates;
        finalEvents = new ArrayList<>();
        mEvents = events;
        profilePage = who;
        verb = v;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // create the view using the item_movie layout
        View updateView = inflater.inflate(R.layout.item_update, parent, false);
        ViewHolder viewHolder = new ViewHolder(updateView);
        uid = Profile.getCurrentProfile().getId();
        if (!(dates.equals(null))) {
            Collections.sort(dates, Collections.reverseOrder());

        }
//            for(Date d: dates){
//                for (UserEvents ue: mEvents){
//                    if ((ue.date.equals(d)) && (!finalEvents.contains(ue))){
//                        finalEvents.add(ue);
//                    }
//                }
//            }

        return viewHolder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Date d = dates.get(position);
        Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf");
        for (UserEvents ue: mEvents) {
            if ((ue.date.equals(d))) {
                UserEvents e = ue;
                if (e.uid != null){
                    holder.update.setText(profilePage + " created the event " + e.eventName);
                    holder.update.setTypeface(font);
                }
                else{
                    holder.update.setTypeface(font);
                    holder.update.setText(profilePage + verb + "interested in " + e.eventName);
                }
            }
        }
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.notification) TextView update;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //get the position of row element
                    int position = getAdapterPosition();
                    // fire the listener callback
                    if (position != RecyclerView.NO_POSITION) {
                        UserEvents currentUpdate = mEvents.get(position);
                        // create intent for the new activity
                        Intent intent = new Intent(context, EventDetailsActivity.class);
                        intent.putExtra("current", Parcels.wrap(currentUpdate));
                        // serialize the movie using parceler, use its short name as a key
                        // show the activity
                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public void onClick(View v) {

        }
    }

    @Override
    public int getItemCount() {
        return mEvents.size();
    }


}
