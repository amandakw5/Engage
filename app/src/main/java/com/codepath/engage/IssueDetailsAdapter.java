package com.codepath.engage;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.codepath.engage.models.Event;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by awestort on 7/11/17.
 */

public class IssueDetailsAdapter extends RecyclerView.Adapter<IssueDetailsAdapter.ViewHolder>{
    private ArrayList<String> issueSubsectionTitles;
    private String[] specificIssues;
    private String[] organizations;
    private List<String> upEvents;
    private List<Event> upEventIds;
    private String issue;

    Context context;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // create the view using the item_movie layout
        View issueView = inflater.inflate(R.layout.item_issue_subsection, parent, false);

        return new ViewHolder(issueView);
    }
    public IssueDetailsAdapter(String issue, ArrayList<String> issueSubsectionTitles, String[] specificIssues, String[] organizations, List<String> upEvents, List<Event> upEventIds) {
        this.issueSubsectionTitles = issueSubsectionTitles;
        this.specificIssues = specificIssues;
        this.organizations = organizations;
        this.upEvents = upEvents;
        this.issue = issue;
        this.upEventIds = upEventIds;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String title = issueSubsectionTitles.get(position);
        holder.subTitle.setText(title);
        ArrayList<String> specificIssueList = new ArrayList<String>();
        specificIssueList.addAll( Arrays.asList(specificIssues) );
        ArrayList<String> organizationList = new ArrayList<String>();
        organizationList.addAll( Arrays.asList(organizations) );
        // Create ArrayAdapter using the planet list.
        ArrayAdapter<String> listAdapter;
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
        holder.issueList.setAdapter(listAdapter);
    }

    @Override
    public int getItemCount() {
        return issueSubsectionTitles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.subTitle) TextView subTitle;
        @BindView(R.id.issueList) ListView issueList;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            issueList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        int rPosition = getAdapterPosition();
                        int innerPosition = issueList.getPositionForView(view);
                        if (rPosition == 2) {
                            Event event = upEventIds.get(innerPosition);
                            Intent intent = new Intent(context, EventDetailsActivity.class);
                            intent.putExtra(Event.class.getSimpleName(), Parcels.wrap(event));
                            context.startActivity(intent);
                    }
                }
            });
        }
    }
}
