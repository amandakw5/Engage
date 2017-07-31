package com.codepath.engage;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.engage.models.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by awestort on 7/31/17.
 */

public class FollowAdapter extends RecyclerView.Adapter<FollowAdapter.ViewHolder> {
    List<User> users;
    Context context;

    public FollowAdapter(ArrayList<User> follows) {
        users = follows;

    }
    @Override
    public FollowAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View followView = inflater.inflate(R.layout.item_user, parent, false);
        FollowAdapter.ViewHolder viewHolder = new FollowAdapter.ViewHolder(followView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FollowAdapter.ViewHolder holder, int position) {
        User user = users.get(position);
        // populate the views according to this data
        holder.name.setText(user.firstName + " " + user.lastName);
        Glide.with(context).load(user.profilePicture).into(holder.profileImage);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView profileImage;
        public TextView name;
    ;

        public ViewHolder(View itemView) {
            super(itemView);
            profileImage = (ImageView) itemView.findViewById(R.id.profileImage);
            name = (TextView) itemView.findViewById(R.id.name);

        }
    }
}


