package com.codepath.engage;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.engage.models.User;
import com.codepath.engage.models.UserEvents;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

/**
 * Created by awestort on 8/2/17.
 */

public class MyEventsAdapter extends RecyclerView.Adapter<MyEventsAdapter.ViewHolder>{
    private List<UserEvents> mEvents;
    private Context context;
    private int recyclerType;
    private View recycleView;
    private User user;

    public MyEventsAdapter(List<UserEvents> events, User currentProfile){
        mEvents = events;
        user = currentProfile;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        recycleView = inflater.inflate(R.layout.item_myevents,parent,false);

        return new ViewHolder(recycleView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf");
        holder.tvHost.setTypeface(font);
        holder.tvEventName.setTypeface(font);
        holder.tvEventInfo.setTypeface(font);
        holder.time.setTypeface(font);
        position = holder.getAdapterPosition();
        UserEvents event = mEvents.get(position);
        holder.tvHost.setText(event.eventHost);
        holder.tvEventName.setText(event.eventName);
        holder.tvEventInfo.setText(event.eventLocation);
        holder.time.setText(event.eventTime);
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("photos").child(String.valueOf(event.getEventId()));
        Glide.with(context)
                .using(new FirebaseImageLoader())
                .load(storageReference)
                .into(holder.ivProfileImage);
    }

    @Override
    public int getItemCount() {
        return mEvents.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivProfileImage;
        public TextView tvEventName;
        public TextView tvEventInfo;
        public TextView time;
        public TextView tvHost;
        public ImageView profileImage;
        public TextView name;
        public ViewHolder(View itemView){
            super(itemView);
            tvHost = (TextView) itemView.findViewById(R.id.tvHost);
            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvEventName = (TextView) itemView.findViewById(R.id.tvEventName);
            tvEventInfo = (TextView) itemView.findViewById(R.id.tvLocationInfo);
            time = (TextView) itemView.findViewById(R.id.time);
           // tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);
            tvHost = (TextView) itemView.findViewById(R.id.tvHost);
            profileImage = (ImageView) itemView.findViewById(R.id.profileImage);
            name = (TextView) itemView.findViewById(R.id.name);
            //itemView.setOnClickListener(this);
        }
    }

    public UserEvents getEvent(int i){
        return mEvents.get(i);
    }

    void clear(){
        mEvents.clear();
        notifyDataSetChanged();
    }
}

