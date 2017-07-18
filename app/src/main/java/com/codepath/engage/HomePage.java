package com.codepath.engage;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomePage extends AppCompatActivity implements View.OnClickListener {
    RecyclerView rvIssues;
    IssueAdapter adapter;
    ArrayList<String> issues;
    private DrawerLayout mDrawer;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle mDrawerToggle;
    //Variable that will refrence the Search view/ Search bar icon
    private SearchView searchView;
    //Will hold teh text that the user inputs to the serach view
    private String valueOfQuery;
    private Toolbar toolbar;
    private ImageView profileImage;
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        //mDrawerList = (ListView) findViewById(R.id.navList);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        profileImage = (ImageView) findViewById(R.id.profileImage);
        ActionBar actionbar = getSupportActionBar();
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        setSupportActionBar(toolbar);

        configureNavigationDrawer();

       // drawerToggle = setupDrawerToggle();

        // Tie DrawerLayout events to the ActionBarToggle
       // mDrawer.addDrawerListener(drawerToggle);

        // Find our drawer view

        // Setup drawer view
//        setupDrawerContent(nvDrawer);
        //
        // View headerLayout = nvDrawer.getHeaderView(0);
      //  searchView = (SearchView) headerLayout.findViewById(R.id.search);
       // profileImage = (ImageView) headerLayout.findViewById(R.id.profileImage);
        //setUpSearchView();
        String[] strs = {"Women", "Food", "Climate Change", "Human Rights", "Poverty"};
        issues = new ArrayList<>();
        adapter = new IssueAdapter(issues);
        issues.addAll(Arrays.asList(strs));
        rvIssues = (RecyclerView) findViewById(R.id.rvIssues);
        rvIssues.setLayoutManager(new LinearLayoutManager(this));
        List<String> sideItems = new ArrayList<String>();
        sideItems.add("foo");
        sideItems.add("bar");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                sideItems );

        //mDrawerList.setAdapter(arrayAdapter);
        //   nvDrawer.setItemIconTintList(null);
       // addDrawerItems();
       // setupDrawer();

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawer.openDrawer(Gravity.RIGHT);
            }
        });

    }
    private void configureNavigationDrawer() {

        NavigationView navView = (NavigationView) findViewById(R.id.nvView);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                Fragment f = null;
                int itemId = menuItem.getItemId();


                if (f != null) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.rvIssues, f);
                    transaction.commit();
                    mDrawer.closeDrawers();
                    return true;
                }

                return false;
            }
        });
    }

    //Perfoms The Searching Of Desired Event Category
    //TODO finish this function
    private void searchFor(String query){
    }
    private void addDrawerItems() {
        String[] osArray = { "Android", "iOS", "Windows", "OS X", "Linux" };
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(HomePage.this, "Time for an upgrade!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void closeSearchView(SearchView searchView){
        searchView.setIconified(true);
    }
    //@Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.topbar, menu);
//        return true;
//    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        switch(itemId) {
            // Android home
            case R.id.profileImage:
                mDrawer.openDrawer(GravityCompat.START);
                return true;

            // manage other entries if you have it ...
        }

        return true;
    }


    //Initilalizes all necessary values that will hold all the searchview values.
    private void setUpSearchView(){
        // Sets searchable configuration defined in searchable.xml for this SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
               Intent i = new Intent(HomePage.this, ViewEvents.class);
                i.putExtra("Query", query);
                startActivity(i);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                //filterSearchFor(query);
                return true;
            }
        });
    }
//    private void setupDrawer() {
//        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer, R.string.drawer_open, R.string.drawer_close) {
//
//            /** Called when a drawer has settled in a completely open state. */
//            public void onDrawerOpened(View drawerView) {
//                super.onDrawerOpened(drawerView);
//                getSupportActionBar().setTitle("Navigation!");
//                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
//            }
//
//            /** Called when a drawer has settled in a completely closed state. */
//            public void onDrawerClosed(View view) {
//                super.onDrawerClosed(view);
//                getSupportActionBar().setTitle("hi");
//                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
//            }
//        };
//
//        mDrawerToggle.setDrawerIndicatorEnabled(true);
//        mDrawer.setDrawerListener(mDrawerToggle);
//    }
//    private void setupDrawerContent(NavigationView navigationView) {
//        navigationView.setNavigationItemSelectedListener(
//                new NavigationView.OnNavigationItemSelectedListener() {
//                    @Override
//                    public boolean onNavigationItemSelected(MenuItem menuItem) {
//                        selectDrawerItem(menuItem);
//                        return true;
//                    }
//                });
//    }
//    private ActionBarDrawerToggle setupDrawerToggle() {
//        // NOTE: Make sure you pass in a valid toolbar reference.  ActionBarDrawToggle() does not require it
//        // and will not render the hamburger icon without it.
//        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open,  R.string.drawer_close);
//    }
//    @Override
//    protected void onPostCreate(Bundle savedInstanceState) {
//        super.onPostCreate(savedInstanceState);
//        // Sync the toggle state after onRestoreInstanceState has occurred.
//        mDrawerToggle.syncState();
//    }
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        mDrawerToggle.onConfigurationChanged(newConfig);
//    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass = Navigation.class;

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.rvIssues, fragment).commit();
        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        mDrawer.closeDrawers();
    }

    @Override
    public void onClick(View v) {

    }
}
