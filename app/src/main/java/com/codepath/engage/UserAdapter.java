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

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by calderond on 7/27/17.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{
    private List<User> users;
    private Context context;

    public UserAdapter(List<User> users, int i){
        this.users = users;
    }

    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View recyclerView = inflater.inflate(R.layout.user_item, parent, false);
        return new ViewHolder (recyclerView);
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
        @BindView(R.id.tvUserName) TextView tvUserName;
        @BindView(R.id.ivProfileImage) ImageView ivProfileImage;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
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
    void clear(){
        users.clear();
        notifyDataSetChanged();
    }
}
