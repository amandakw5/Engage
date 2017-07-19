package com.codepath.engage;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by awestort on 7/11/17.
 */

public class IssueDetailsAdapter extends RecyclerView.Adapter<IssueDetailsAdapter.ViewHolder>{
    ArrayList<String> issueSubsectionTitles;
    String[] specificIssues;
    String[] organizations;
    List<String> upEvents;
    ArrayList<String> pastEvents;
    private ArrayAdapter<String> listAdapter ;
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
    public IssueDetailsAdapter(String issue, ArrayList<String> issueSubsectionTitles, String[] specificIssues, String[] organizations, List<String> upEvents, ArrayList<String> pastEvents) {
        this.issueSubsectionTitles = issueSubsectionTitles;
        this.specificIssues = specificIssues;
        this.organizations = organizations;
        this.upEvents = upEvents;
        this.pastEvents = pastEvents;
        this.issue = issue;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String title = issueSubsectionTitles.get(position);
        holder.subTitle.setText(title);
        ArrayList<String> specificIssueList = new ArrayList<String>();
        specificIssueList.addAll( Arrays.asList(specificIssues) );
        ArrayList<String> organizationList = new ArrayList<String>();
        organizationList.addAll( Arrays.asList(organizations) );
//       List<String> upcomingEventsList = new ArrayList<String>();
//        upcomingEventsList.addAll( Arrays.asList(upcomingEvents));
        // Create ArrayAdapter using the planet list.
        if (position == 0 ) {
            listAdapter = new ArrayAdapter<String>(context, R.layout.simplerow, specificIssueList);
        }
        else if (position == 1){
            listAdapter = new ArrayAdapter<String>(context, R.layout.simplerow, organizationList);
        }
        else{
            listAdapter = new ArrayAdapter<String>(context, R.layout.simplerow, upEvents);
        }
        // Add more planets. If you passed a String[] instead of a List<String>
        // into the ArrayAdapter constructor, you must not add more items.
        // Otherwise an exception will occur.
        // Set the ArrayAdapter as the ListView's adapter.
        holder.issueList.setAdapter( listAdapter );
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
