package com.codepath.engage;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.engage.models.Event;

import java.util.List;

/**
 * Created by calderond on 7/12/17.
 */

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder>{
    private List<Event> mEvents;
    Context context;
    public EventAdapter(List<Event> events){
        mEvents = events;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View eventView= inflater.inflate(R.layout.event_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(eventView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Event event = mEvents.get(position);
        holder.tvEventName.setText(event.tvEventName);
        holder.tvEventInfo.setText(event.tvEventInfo);
        holder.tvDescription.setText(event.tvDescription);
        Glide.with(context).load(event.ivEventImage).into(holder.ivProfileImage);
    }

    @Override
    public int getItemCount() {
        return mEvents.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView ivProfileImage;
        public TextView tvEventName;
        public TextView tvEventInfo;
        public TextView tvDescription;
        public ViewHolder(View itemView){
            super(itemView);
            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProileImage);
            tvEventName = (TextView) itemView.findViewById(R.id.tvEventName);
            tvEventInfo = (TextView) itemView.findViewById(R.id.tvLocationInfo);
            tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);
        }

    }
    public void clear(){
        mEvents.clear();
        notifyDataSetChanged();
    }
}
