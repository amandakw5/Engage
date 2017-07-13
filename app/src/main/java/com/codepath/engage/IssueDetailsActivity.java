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
    String[] specificIssues;
    String[] organizations;
    ArrayList<String> upcomingEvents;
    ArrayList<String> pastEvents;
    @BindView(R.id.issueTitle) TextView issueTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_details);
        String[] strs = {"Specific Issues", "Organizations", "Popular Upcoming Events", "Past events"};
        String[] womenSpecificIssues = new String[] {"Sexual and Reproductive Rights","Freedom from violence", "Economic and Political Empowerment"};
        String[] womenOrganizations = new String[] {"National Organization for Women","Planned Parenthood", "Association of Women's Rights in Development", "American Association of University Women"};
        String[] foodSpecificIssues = new String[] {"World Hunger", "Malnutrition"};
        String[] foodOrganizations = new String[] {"World Food Program", "World Bank", "The Food and Agriculture Organization of the United Nations"};
        String[] climateSpecificIssues = new String[] {"Global Warming", "Rising Sea Levels", "More Frequent Extreme Weather"};
        String[] climateOrganizations = new String[] {"350.org", "GreenPeace","Climate Reality Project", "iMatter"};
        String[] humanRightsSpecificIssues = new String[] {"LGBTQ Rights", "Disability Rights", "Racism", "Refugee Rights"};
        String[] humanRightsOrganizations = new String[] {"Amnesty International", "Human Rights Watch", "Human Rights Campaign"};
        String[] povertySpecificIssues = new String[] {"Education", "Homeslessness", "Poor Health"};
        String[] povertyOrganizations = new String[] {"ONE Campaign", "UNICEF", "Partners in Health"};
        Intent intent = getIntent();
        String issue = intent.getStringExtra("current");
        if (issue.equals("Women")){
            organizations = womenOrganizations;
            specificIssues = womenSpecificIssues;
        }
        else if(issue.equals("Food")){
            specificIssues = foodSpecificIssues;
            organizations = foodOrganizations;
        }
        else if(issue.equals("Climate Change")){
            organizations = climateOrganizations;
            specificIssues = climateSpecificIssues;
        }
        else if(issue.equals("Human Rights")){
            organizations = humanRightsOrganizations;
            specificIssues = humanRightsSpecificIssues;
        }
        else{
            organizations = povertyOrganizations;
            specificIssues = povertySpecificIssues;
        }
        ButterKnife.bind(this);
        issueTitle.setText(issue);
        issueSubsectionTitles = new ArrayList<>();
        upcomingEvents = new ArrayList<>();
        pastEvents = new ArrayList<>();
        issueSubsectionTitles.addAll(Arrays.asList(strs));
        adapter = new IssueDetailsAdapter(issue, issueSubsectionTitles, specificIssues, organizations, upcomingEvents, pastEvents);
        rvIssueSubsections = (RecyclerView) findViewById(R.id.rvIssueSubsections);
        rvIssueSubsections.setLayoutManager(new LinearLayoutManager(this));
        rvIssueSubsections.setAdapter(adapter);
    }
}
