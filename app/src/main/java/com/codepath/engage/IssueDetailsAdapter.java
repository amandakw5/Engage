package com.codepath.engage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
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
    private String[] urlIssues;
    private String[] urlOrgs;
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
    public IssueDetailsAdapter(String issue, ArrayList<String> issueSubsectionTitles, String[] specificIssues, String[] organizations, List<String> upEvents, List<Event> upEventIds, String[] urlIssues, String[] urlOrgs) {
        this.issueSubsectionTitles = issueSubsectionTitles;
        this.specificIssues = specificIssues;
        this.organizations = organizations;
        this.upEvents = upEvents;
        this.issue = issue;
        this.upEventIds = upEventIds;
        this.urlIssues = urlIssues;
        this.urlOrgs = urlOrgs;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String title = issueSubsectionTitles.get(position);
        Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf");
        holder.subTitle.setTypeface(font);
        holder.subTitle.setText(title);
        ArrayList<String> specificIssueList = new ArrayList<String>();
        specificIssueList.addAll(Arrays.asList(specificIssues) );
        ArrayList<String> organizationList = new ArrayList<String>();
        organizationList.addAll(Arrays.asList(organizations) );
        // Create ArrayAdapter using the planet list.
        ArrayAdapter<String> listAdapter;
        if (position == 0) {
            listAdapter = new ArrayAdapter<String>(context, R.layout.simplerow, specificIssueList);
        }
        else if (position == 1){
            listAdapter = new ArrayAdapter<String>(context, R.layout.simplerow, organizationList);
        } else {
            listAdapter = new ArrayAdapter<String>(context, R.layout.simplerow, upEvents);
        }

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
        @BindView(R.id.readmore) TextView readMore;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf");
            readMore.setTypeface(font);
            issueList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    int rPosition = getAdapterPosition();
                    int innerPosition = issueList.getPositionForView(view);
                    if (rPosition == 0){
                        String url = urlIssues[innerPosition];
                        goToUrl(url);
                    } else if (rPosition == 1){
                        String url = urlOrgs[innerPosition];
                        goToUrl(url);
                    } else if (rPosition == 2) {
                        Event event = upEventIds.get(innerPosition);
                        Intent intent = new Intent(context, EventDetailsActivity.class);
                        intent.putExtra(Event.class.getSimpleName(), Parcels.wrap(event));
                        intent.putExtra("isCreated", Parcels.wrap(false));
                        context.startActivity(intent);
                    }
                }
            });
            readMore.setOnClickListener(new AdapterView.OnClickListener(){
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    int listSize = urlIssues.length;
                    if (position == 0) {
                        String url = urlIssues[listSize-1];
                        goToUrl(url);
                    } else if (position == 1){
                        String url = urlIssues[listSize-1];
                        goToUrl(url);
                    } else if (position == 2){
                        String query = issue;
                        Intent intent = new Intent(context, ViewEvents.class);
                        intent.putExtra("Query", query);
                        context.startActivity(intent);
                    }
                }
            });
        }
    }

    private void goToUrl (String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        context.startActivity(launchBrowser);
    }
}
