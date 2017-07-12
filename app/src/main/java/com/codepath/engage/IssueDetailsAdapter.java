package com.codepath.engage;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by awestort on 7/11/17.
 */

public class IssueDetailsAdapter extends RecyclerView.Adapter<IssueDetailsAdapter.ViewHolder>{
    ArrayList<String> issueSubsectionTitles;
    ArrayList<String> specificIssues;
    ArrayList<String> organizations;
    ArrayList<String> upcomingEvents;
    ArrayList<String> pastEvents;
    String issue;

    Context context;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // create the view using the item_movie layout
        View issueView = inflater.inflate(R.layout.item_issue_subsection, parent, false);
        ViewHolder viewHolder = new ViewHolder(issueView);
        return viewHolder;
    }
    public IssueDetailsAdapter(String issue, ArrayList<String> issueSubsectionTitles,  ArrayList<String> specificIssues, ArrayList<String> organizations, ArrayList<String> upcomingEvents, ArrayList<String> pastEvents) {
        this.issueSubsectionTitles = issueSubsectionTitles;
        this.specificIssues = specificIssues;
        this.organizations = organizations;
        this.upcomingEvents = upcomingEvents;
        this.pastEvents = pastEvents;
        this.issue = issue;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String title = issueSubsectionTitles.get(position);
        holder.subTitle.setText(title);
    }

    @Override
    public int getItemCount() {
        return issueSubsectionTitles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.subTitle) TextView subTitle;
        @BindView(R.id.issueList) ListView issueList;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
