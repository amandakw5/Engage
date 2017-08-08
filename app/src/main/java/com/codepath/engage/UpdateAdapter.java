package com.codepath.engage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codepath.engage.models.CreatedEvents;
import com.codepath.engage.models.UserEvents;
import com.facebook.Profile;

import org.parceler.Parcels;

import java.util.ArrayList;
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
//        if (!(dates.equals(null))) {
//            Collections.sort(dates, Collections.reverseOrder());
//        }
        Log.d("UE All", mEvents.toString());
        Log.d("UE Dates", dates.toString());

        return viewHolder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Date d = dates.get(position);
        Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf");
        holder.update.setTypeface(font);
        for (UserEvents ue : mEvents) {
            Log.d("UE", ue.toString());
            Log.d("UE d", d.toString());
            Log.d("UE Date", ue.date.toString());
            if ((ue.date.equals(d))) {
                UserEvents e = ue;
                Log.d("UE Event", ue.eventName);
                if (e.uid != null) {
                    holder.update.setText(profilePage + " created the event " + e.eventName);
                } else {
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
//                        ArrayList<UserEvents> helpme = new ArrayList<UserEvents>(mEvents);
//                        Collections.reverse(helpme);
//                        UserEvents currentUpdate = helpme.get(position);
                        UserEvents currentUpdate = mEvents.get(position);

                        // create intent for the new activity
                        Intent intent = new Intent(context, EventDetailsActivity.class);
                        intent.putExtra("current", Parcels.wrap(currentUpdate));
                        intent.putExtra("isCreated", Parcels.wrap(currentUpdate.isCreatedByUser()));
                        // serialize the update using parceler, use its short name as a key
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
