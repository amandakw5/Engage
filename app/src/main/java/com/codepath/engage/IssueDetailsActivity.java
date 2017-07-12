package com.codepath.engage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IssueDetailsActivity extends AppCompatActivity {
    RecyclerView rvIssueSubsections;
    IssueDetailsAdapter adapter;
    ArrayList<String> issueSubsectionTitles;
    ArrayList<String> specificIssues;
    ArrayList<String> organizations;
    ArrayList<String> upcomingEvents;
    ArrayList<String> pastEvents;
    @BindView(R.id.issueTitle) TextView issueTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_details);
        String[] strs = {"Specific Issues", "Organizations", "Popular Upcoming Events", "Past events"};
        Intent intent = getIntent();
        String issue = intent.getStringExtra("current");
        ButterKnife.bind(this);
        issueTitle.setText(issue);
        issueSubsectionTitles = new ArrayList<>();
        specificIssues = new ArrayList<>();
        organizations = new ArrayList<>();
        upcomingEvents = new ArrayList<>();
        pastEvents = new ArrayList<>();
        issueSubsectionTitles.addAll(Arrays.asList(strs));
        adapter = new IssueDetailsAdapter(issue, issueSubsectionTitles, specificIssues, organizations, upcomingEvents, pastEvents);
        rvIssueSubsections = (RecyclerView) findViewById(R.id.rvIssueSubsections);
        rvIssueSubsections.setLayoutManager(new LinearLayoutManager(this));
        rvIssueSubsections.setAdapter(adapter);
    }
}
