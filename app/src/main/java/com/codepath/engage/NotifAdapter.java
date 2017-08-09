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
import com.facebook.Profile;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by awestort on 8/6/17.
 */

public class NotifAdapter extends RecyclerView.Adapter<NotifAdapter.ViewHolder> {
    public String uid;
    Context context;
    public ArrayList<String> proPics;
    public ArrayList<String> notifs;

    public NotifAdapter(ArrayList<String> not, ArrayList<String> profPics){
        notifs = not;
        proPics = profPics;
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

       String currentProPic = proPics.get(position);
        holder.update.setTypeface(font);
        holder.update.setText(n);
        Glide.with(context).load(currentProPic).bitmapTransform(new RoundedCornersTransformation(context, 100, 0)).into(holder.profPic);


    }

    @Override
    public int getItemCount() {
        return notifs.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.notification) TextView update;
        @BindView(R.id.profPic) ImageView profPic;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
