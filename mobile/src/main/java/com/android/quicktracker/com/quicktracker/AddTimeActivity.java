package com.android.quicktracker.com.quicktracker;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class AddTimeActivity extends ActionBarActivity {

    String TITLES[] = {"Job Details", "Calendar", "Settings"};
    int ICONS[] = {R.drawable.ic_clipboard,R.drawable.ic_calendar,R.drawable.ic_settings};




    private Toolbar toolbar;

    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    DrawerLayout Drawer;
    ListView mListView;
    ActionBarDrawerToggle mDrawerToggle;
    ImageButton b1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_time);
        b1 = (ImageButton) findViewById(R.id.addTimeButton);
        b1.setOnClickListener(myhandler1);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        Jobs.jobList = Jobs.listAll(Jobs.class);

        List<String> list = new ArrayList<String>();
        for (int i=0; i<Jobs.jobList.size();i++){
            list.add(Jobs.jobList.get(i).get_name());
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,list);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new NavDrawerAdapter(TITLES,ICONS);
        mRecyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mLayoutManager);

        final GestureDetector mGestureDetector = new android.view.GestureDetector(AddTimeActivity.this, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

        });
        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
                View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());


                if (child != null && mGestureDetector.onTouchEvent(motionEvent)) {
                    Drawer.closeDrawers();
                    onTouchDrawer(recyclerView.getChildPosition(child));
                    return true;

                }


                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {

            }
        });
        Drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);        // Drawer object Assigned to the view
        mDrawerToggle = new ActionBarDrawerToggle(this, Drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                // Code here will execute once drawer is closed
            }


        };
        Drawer.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
    }
    View.OnClickListener myhandler1 = new View.OnClickListener() {
        public void onClick(View v) {
            Intent intent = new Intent(AddTimeActivity.this, CalendarActivity.class);
            startActivity(intent);
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onTouchDrawer(final int position) {
        int lastMenu = 3;
        final int JOB_DETAILS_POSITION = 1;
        final int CALENDAR_POSITION = 2;
        final int SETTINGS_POSITION = 3;

        if (lastMenu == position) return;

        switch (lastMenu = position) {
            case JOB_DETAILS_POSITION:
                Intent intent0 = new Intent(AddTimeActivity.this, JobDetailsActivity.class);
                startActivity(intent0);
                break;
            case CALENDAR_POSITION:
                Intent intent = new Intent(AddTimeActivity.this, CalendarActivity.class);
                startActivity(intent);
                break;
            case SETTINGS_POSITION:
                Intent intent2 = new Intent(AddTimeActivity.this, SettingsActivity.class);
                startActivity(intent2);
                break;
            default:
                return;
        }
    }
}
