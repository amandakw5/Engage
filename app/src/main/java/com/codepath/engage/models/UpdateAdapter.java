package com.codepath.engage.models;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.engage.IssueDetailsActivity;
import com.codepath.engage.R;
import com.codepath.engage.Update;

import java.util.ArrayList;

/**
 * Created by awestort on 7/18/17.
 */

public class UpdateAdapter extends RecyclerView.Adapter<UpdateAdapter.ViewHolder> {
    public ArrayList<Update> updates;
    Context context;
    public UpdateAdapter(ArrayList<Update> updates) {
        this.updates = updates;
    }
    @Override
    public UpdateAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // create the view using the item_movie layout
        View updateView = inflater.inflate(R.layout.item_update, parent, false);
        ViewHolder viewHolder = new ViewHolder(updateView);
        return viewHolder;
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //get the position of row element
                    int position = getAdapterPosition();
                    // fire the listener callback
                    if (position != RecyclerView.NO_POSITION) {
                        Update currentUpdate = updates.get(position);
                        // create intent for the new activity
                        Intent intent = new Intent(context, IssueDetailsActivity.class);
                        intent.putExtra("current", (Parcelable) currentUpdate);
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
    public void onBindViewHolder(UpdateAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

}
