package com.codepath.engage;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.engage.models.Event;

import org.parceler.Parcels;

import java.util.List;

/**
 * Created by calderond on 7/12/17.
 */

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder>{
    private List<Event> mEvents;
    private Context context;
    public EventAdapter(List<Event> events){
        mEvents = events;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View eventView = inflater.inflate(R.layout.event_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(eventView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        position = holder.getAdapterPosition();
        Event event = mEvents.get(position);
        holder.tvHost.setText(event.organizerName);
        holder.tvEventName.setText(event.tvEventName);
        holder.tvEventInfo.setText(event.tvEventInfo);
        holder.tvDescription.setText(event.tvDescription);
        if (event.ivEventImage == "null"){
            Glide.with(context).load(R.drawable.image_not_found).centerCrop().into(holder.ivProfileImage);
        } else {
            Glide.with(context).load(event.ivEventImage).centerCrop().into(holder.ivProfileImage);
//        holder.tvHost.setText(event.organizer.name);
        }
    }

    @Override
    public int getItemCount() {
        return mEvents.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView ivProfileImage;
        public TextView tvEventName;
        public TextView tvEventInfo;
        public TextView tvDescription;
        public TextView tvHost;
        public ViewHolder(View itemView){
            super(itemView);
            tvHost = (TextView) itemView.findViewById(R.id.tvHost);
            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvEventName = (TextView) itemView.findViewById(R.id.tvEventName);
            tvEventInfo = (TextView) itemView.findViewById(R.id.tvLocationInfo);
            tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);
            tvHost = (TextView) itemView.findViewById(R.id.tvHost);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            final int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                final Event event = mEvents.get(position);
                Intent intent = new Intent(context, EventDetailsActivity.class);
                intent.putExtra(Event.class.getSimpleName(), Parcels.wrap(event));
                context.startActivity(intent);
            }
        }
    }

    public Event getEvent(int i){
        return mEvents.get(i);
    }

    public void clear(){
        mEvents.clear();
        notifyDataSetChanged();
    }
}
