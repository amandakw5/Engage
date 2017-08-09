package com.codepath.engage.ui.activities.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.engage.R;
import com.codepath.engage.models.UserChat;

import java.util.List;
import java.util.Random;

/**
 * Created by calderond on 7/31/17.
 */

public class UserListingRecyclerAdapter extends RecyclerView.Adapter<UserListingRecyclerAdapter.ViewHolder> {
    private List<UserChat> mUsers;
    private Context context;
    public UserListingRecyclerAdapter(List<UserChat> users) {
        this.mUsers = users;
    }

    public void add(UserChat user) {
        mUsers.add(user);
        notifyItemInserted(mUsers.size() - 1);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_user_listing, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UserChat user = mUsers.get(position);

        holder.txtUsername.setText(user.firstName + " " + user.lastName);
        holder.lastMessage.setText(user.lastMessage);
        Random random = new Random();
        int R = (int)(Math.random()*256);
        int G = (int)(Math.random()*256);
        int B= (int)(Math.random()*256);
        int randomColor = Color.rgb(R,G,B);
        holder.imgBlock.setBackgroundColor(randomColor);
        Glide.with(context)
                .load(user.getProfilePicture())
                .fitCenter()
                .into(holder.imgUser);
    }

    @Override
    public int getItemCount() {
        if (mUsers != null) {
            return mUsers.size();
        }
        return 0;
    }

    public UserChat getUser(int position) {
        return mUsers.get(position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView  txtUsername,lastMessage;
        private ImageView imgUser,imgBlock;
        ViewHolder(View itemView) {
            super(itemView);
            imgBlock =(ImageView) itemView.findViewById(R.id.imgBlock);
            imgUser = (ImageView) itemView.findViewById(R.id.user_image);
            txtUsername = (TextView) itemView.findViewById(R.id.text_view_username);
            lastMessage = (TextView) itemView.findViewById(R.id.text_view_last_message);
        }
    }
}