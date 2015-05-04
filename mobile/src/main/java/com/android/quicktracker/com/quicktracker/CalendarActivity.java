package com.android.quicktracker.com.quicktracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.RectF;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.google.android.gms.location.Geofence;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.listeners.ActionClickListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;


public class CalendarActivity extends ActionBarActivity implements WeekView.MonthChangeListener,
        WeekView.EventClickListener, WeekView.EventLongPressListener{

    private static final int TYPE_DAY_VIEW = 1;
    private static final int TYPE_THREE_DAY_VIEW = 2;
    private static final int TYPE_WEEK_VIEW = 3;
    private int mWeekViewType = TYPE_WEEK_VIEW;
    private Context mContext = this;

    String TITLES[] = {"Job Details","Calendar","Settings"};
    int ICONS[] = {R.drawable.ic_clipboard,R.drawable.ic_calendar,R.drawable.ic_settings};
    private static int x = 0;
    private Toolbar toolbar;
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    DrawerLayout Drawer;
    ImageButton b1;
    ActionBarDrawerToggle mDrawerToggle;
    public static WeekView mWeekView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        x=0;
        Jobs.jobList = Jobs.listAll(Jobs.class);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        // Get a reference for the week view in the layout.
        mWeekView = (WeekView) findViewById(R.id.weekView);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        b1 = (ImageButton) findViewById(R.id.addTimeButton);
        b1.setOnClickListener(myhandler1);

        mWeekView.setOnEventClickListener(this);
        mWeekView.setMonthChangeListener(this);
        mWeekView.setEventLongPressListener(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new NavDrawerAdapter(TITLES, ICONS);
        mRecyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mLayoutManager);


        final GestureDetector mGestureDetector = new android.view.GestureDetector(CalendarActivity.this, new GestureDetector.SimpleOnGestureListener() {

            @Override public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

        });
        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
                View child = recyclerView.findChildViewUnder(motionEvent.getX(),motionEvent.getY());
                if(child!=null && mGestureDetector.onTouchEvent(motionEvent)){
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
        Drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);
        mDrawerToggle = new ActionBarDrawerToggle(this,Drawer,toolbar,R.string.openDrawer,R.string.closeDrawer){

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

            }



        };
        Drawer.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
    }



    @Override
    public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();
        Jobs.jobList = Jobs.listAll(Jobs.class);
        if (Jobs.jobList != null && x<1) {
            x++;
            Toast.makeText(this, Integer.toString(x), Toast.LENGTH_LONG).show();
            for (int i = 0; i < Jobs.jobList.size(); i++) {
                Jobs currentJob = Jobs.jobList.get(i);
                Random rand = new Random();
                int r = rand.nextInt(255);
                int g = rand.nextInt(255);
                int b = rand.nextInt(255);
                int randomColor = Color.rgb(r, g, b);
                ArrayList<int[]> currentJobEvents = currentJob._get_time();
                if (currentJobEvents!=null && currentJobEvents.size() > 0) {
                    for (int j = 0; j < currentJobEvents.size(); j++) {
                        int[] times = currentJobEvents.get(j);

                        Calendar startTime = Calendar.getInstance();
                        startTime.set(Calendar.HOUR_OF_DAY, times[0]);
                        startTime.set(Calendar.MINUTE, times[1]);
                        startTime.set(Calendar.DAY_OF_MONTH, times[4]);
                        startTime.set(Calendar.MONTH, times[5]);
                        startTime.set(Calendar.YEAR, times[6]);
                        Calendar endTime = (Calendar) startTime.clone();

                            endTime.set(Calendar.HOUR_OF_DAY, times[2]);
                            endTime.set(Calendar.MINUTE, times[3]);
                            endTime.set(Calendar.DAY_OF_MONTH, times[7]);
                            endTime.set(Calendar.MONTH, times[8]);
                            endTime.set(Calendar.YEAR, times[9]);
                            WeekViewEvent event = new WeekViewEvent(j, currentJob.get_name() + " from " + Integer.toString(times[0]) + ":" + Integer.toString(times[1]) +" to " + Integer.toString(times[2]) + ":" +Integer.toString(times[3]), startTime, endTime);

                            event.setColor(randomColor);
                            events.add(event);

                    }
                }
            }
        }

        return events;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String id = item.toString();
        switch (id){
            case "Go To Today":
                mWeekView.goToToday();
                return true;
            case "Day View":
                if (mWeekViewType != TYPE_DAY_VIEW) {
                    item.setChecked(!item.isChecked());
                    mWeekViewType = TYPE_DAY_VIEW;
                    mWeekView.setNumberOfVisibleDays(1);
                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                }
                return true;
            case "Five Day View":
                if (mWeekViewType != TYPE_THREE_DAY_VIEW) {
                    item.setChecked(!item.isChecked());
                    mWeekViewType = TYPE_THREE_DAY_VIEW;
                    mWeekView.setNumberOfVisibleDays(5);


                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics()));
                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                }
                return true;
            case "Week View":
                if (mWeekViewType != TYPE_WEEK_VIEW) {
                    item.setChecked(!item.isChecked());
                    mWeekViewType = TYPE_WEEK_VIEW;
                    mWeekView.setNumberOfVisibleDays(7);


                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    View.OnClickListener myhandler1 = new View.OnClickListener() {

        public void onClick(View v) {
            Jobs.jobList = Jobs.listAll(Jobs.class);
            if (Jobs.jobList.size() < 1){
                Toast.makeText(getBaseContext(), "Please create a job before trying to add hours", Toast.LENGTH_LONG).show();
                return;
            }

            Intent intent = new Intent(CalendarActivity.this, AddTimeActivity.class);
            startActivity(intent);
        }
    };
    public void onTouchDrawer(final int position) {

        int lastMenu=2;
        final int JOB_DETAILS_POSITION=1;
        final int CALENDAR_POSITION = 2;
        final int SETTINGS_POSITION = 3;
        if (lastMenu == position) return;

        switch (lastMenu = position) {
            case JOB_DETAILS_POSITION:

                Intent intent0 = new Intent(CalendarActivity.this, JobDetailsActivity.class);
                startActivity(intent0);
                break;
            case CALENDAR_POSITION:

                Intent intent = new Intent(CalendarActivity.this, CalendarActivity.class);
                startActivity(intent);
                break;
            case SETTINGS_POSITION:

                Intent intent2 = new Intent(CalendarActivity.this, SettingsActivity.class);
                startActivity(intent2);
                break;
            default:
                return;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_calendar, menu);
        return true;
    }


    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {
        x=0;
        Intent intent = new Intent(CalendarActivity.this, AddTimeActivity.class);
        startActivity(intent);
    }

    @Override
    public void onEventLongPress(WeekViewEvent event, RectF eventRect) {
        AlertDialog.Builder adb=new AlertDialog.Builder(CalendarActivity.this);
        adb.setMessage("Are you sure you want to delete this event?");
        adb.setNegativeButton("Cancel", null);
        adb.setPositiveButton("Delete", null);
        adb.show();}
}


