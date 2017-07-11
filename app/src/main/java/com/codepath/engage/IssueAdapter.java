package com.codepath.engage;

import android.content.Context;
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
        holder.specificIssue.setText(issue);
    }

    @Override
    public int getItemCount() {
        return issues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.specificIssue) TextView specificIssue;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
