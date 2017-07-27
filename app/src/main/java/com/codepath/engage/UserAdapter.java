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
import com.codepath.engage.models.User;

import org.parceler.Parcels;

import java.util.List;

/**
 * Created by calderond on 7/27/17.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{
    private List<User> users;
    private Context context;
    private int recyclerType;
    View recyclerView;

    public UserAdapter(List<User> users,int i){
        this.users = users;
        recyclerType = i;
    }

    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        recyclerView = inflater.inflate(R.layout.user_item,parent,false);
        ViewHolder viewHolder = new ViewHolder (recyclerView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(UserAdapter.ViewHolder holder, int position) {
        User u = users.get(position);
        holder.tvUserName.setText(u.firstName + " " + u.lastName);
        Glide.with(context).load(u.profilePicture).centerCrop().into(holder.ivProfileImage);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
        public TextView tvUserName;
        public ImageView ivProfileImage;

        public ViewHolder(View itemView) {
            super(itemView);
            tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            final int position = getAdapterPosition();
            final User u = users.get(position);
            Intent i = new Intent(context, ProfileActivity.class);
            i.putExtra(User.class.getSimpleName(), Parcels.wrap(u));
            i.putExtra("whichProfile", u.firstName + " " + u.lastName + " is ");
            context.startActivity(i);
        }
    }
    public void clear(){
        users.clear();
        notifyDataSetChanged();
    }
}
