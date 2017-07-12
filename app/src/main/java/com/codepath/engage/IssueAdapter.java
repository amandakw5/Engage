package com.codepath.engage;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Config;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by awestort on 7/11/17.
 */

public class IssueAdapter extends RecyclerView.Adapter<IssueAdapter.ViewHolder> {
    ArrayList<String> issues;
    Context context;
    Config config;
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // create the view using the item_movie layout
        View issueView = inflater.inflate(R.layout.item_issue, parent, false);
        ViewHolder viewHolder = new ViewHolder(issueView);
        return viewHolder;
    }
    public IssueAdapter(ArrayList<String> issues) {
        this.issues = issues;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String issue = issues.get(position);
        holder.issueTitle.setText(issue);
    }

    @Override
    public int getItemCount() {
        return issues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.issueTitle) TextView issueTitle;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view){
                        //get the position of row element
                    int position = getAdapterPosition();
                    // fire the listnener callback
                    if (position != RecyclerView.NO_POSITION) {
                        String currentIssue = issues.get(position);
                        // create intent for the new activity
                        Intent intent = new Intent(context, IssueDetailsActivity.class);
                        intent.putExtra("current", currentIssue);
                        // serialize the movie using parceler, use its short name as a key
                        // show the activity
                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            // fire the listnener callback
            if (position != RecyclerView.NO_POSITION) {
                String currentIssue = issues.get(position);
                // create intent for the new activity
                Intent intent = new Intent(context, IssueDetailsActivity.class);
                intent.putExtra("current", currentIssue);
                // serialize the movie using parceler, use its short name as a key
                // show the activity
                context.startActivity(intent);
            }

        }
    }
}
