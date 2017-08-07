package com.codepath.engage;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.Profile;

import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by awestort on 8/6/17.
 */

public class NotifAdapter extends RecyclerView.Adapter<NotifAdapter.ViewHolder> {
    public String uid;
    Context context;
    public ArrayList<Date> dates;
    public ArrayList<String> notifs;

    public NotifAdapter(ArrayList<String> not){
        notifs = not;
    }
    @Override
    public NotifAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // create the view using the item_movie layout
        View updateView = inflater.inflate(R.layout.item_update, parent, false);
        ViewHolder viewHolder = new ViewHolder(updateView);
        uid = Profile.getCurrentProfile().getId();

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NotifAdapter.ViewHolder holder, int position) {
        Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf");
        String n = notifs.get(position);
        holder.update.setTypeface(font);
        holder.update.setText(n);
    }

    @Override
    public int getItemCount() {
        return notifs.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.notification) TextView update;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
