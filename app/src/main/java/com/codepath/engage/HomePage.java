package com.codepath.engage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;

public class HomePage extends AppCompatActivity {
    RecyclerView rvIssues;
    IssueAdapter adapter;
    ArrayList<String> issues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        String[] strs = {"Women", "Food", "Climate Change", "Human Rights", "Poverty"};
        issues = new ArrayList<>();
        adapter = new IssueAdapter(issues);
        issues.addAll(Arrays.asList(strs));
        rvIssues = (RecyclerView) findViewById(R.id.rvIssues);
        rvIssues.setLayoutManager(new LinearLayoutManager(this));
        rvIssues.setAdapter(adapter);
    }
}
