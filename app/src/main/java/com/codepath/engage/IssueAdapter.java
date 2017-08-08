package com.codepath.engage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by awestort on 7/11/17.
 */

public class IssueAdapter extends RecyclerView.Adapter<IssueAdapter.ViewHolder> {
    ArrayList<String> issues;
    Context context;
    final String TAG = "GPS";
    private long UPDATE_INTERVAL = 2 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */
    static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    GoogleApiClient gac;
    LocationRequest locationRequest;
    String tvLatitude, tvLongitude, tvTime;
    private EventbriteClient client;
    Boolean eventRequestCompleted = false;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // create the view using the item_movie layout
        View issueView = inflater.inflate(R.layout.item_issue, parent, false);
        ViewHolder viewHolder = new ViewHolder(issueView);

        return viewHolder;
    }

    public IssueAdapter(ArrayList<String> issues, String tvLatitude, String tvLongitude) {
        this.issues = issues;
        this.tvLatitude = tvLatitude;
        this.tvLongitude = tvLongitude;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String issue = issues.get(position);
        Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Medium.ttf");
        holder.issueTitle.setTypeface(font);
        holder.issueTitle.setTextColor(Color.BLACK);
        holder.issueTitle.setText(issue);
    }

    @Override
    public int getItemCount() {
        return issues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.issueTitle) TextView issueTitle;

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
                        String currentIssue = issues.get(position);
                        // create intent for the new activity
                        Intent intent = new Intent(context, IssueDetailsActivity.class);
                        intent.putExtra("current", currentIssue);
                        intent.putExtra("tvLongitude", tvLongitude);
                        intent.putExtra("tvLatitude", tvLatitude);
                        // serialize the movie using parceler, use its short name as a key
                        // show the activity
                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            // fire the listener callback
            if (position != RecyclerView.NO_POSITION) {
                String currentIssue = issues.get(position);
                // create intent for the new activity
                Intent intent = new Intent(context, IssueDetailsActivity.class);
                intent.putExtra("current", currentIssue);
                // serialize the movie using parceler
                // show the activity
                context.startActivity(intent);
            }
        }
    }
}