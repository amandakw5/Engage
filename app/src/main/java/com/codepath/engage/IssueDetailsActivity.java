package com.codepath.engage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;

public class IssueDetailsActivity extends AppCompatActivity {
    RecyclerView rvIssueSubsections;
    IssueDetailsAdapter adapter;
    ArrayList<String> issueSubsectionTitles;
    ArrayList<String> descriptions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_details);
        String[] strs = {"Specific Issues", "Organizations", "Popular Upcoming Events", "Past events"};
        issueSubsectionTitles = new ArrayList<>();
        adapter = new IssueDetailsAdapter(issueSubsectionTitles);
        issueSubsectionTitles.addAll(Arrays.asList(strs));
        rvIssueSubsections = (RecyclerView) findViewById(R.id.rvIssueSubsections);
        rvIssueSubsections.setLayoutManager(new LinearLayoutManager(this));
        rvIssueSubsections.setAdapter(adapter);
    }
}
