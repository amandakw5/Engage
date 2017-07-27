package com.codepath.engage;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.engage.models.User;

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
        return null;
    }

    @Override
    public void onBindViewHolder(UserAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
        public TextView tvEventName;
        public ImageView profileImage;
        public TextView name;
        public ViewHolder(View itemView) {
            super(itemView);

        }

        @Override
        public void onClick(View v) {

        }
    }
}
