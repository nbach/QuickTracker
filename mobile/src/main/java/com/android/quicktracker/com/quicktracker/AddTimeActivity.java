package com.android.quicktracker.com.quicktracker;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.internal.widget.TintSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;


public class AddTimeActivity extends ActionBarActivity {

    String TITLES[] = {"Job Details", "Calendar", "Settings"};
    int ICONS[] = {R.drawable.ic_clipboard,R.drawable.ic_calendar,R.drawable.ic_settings};




    private Toolbar toolbar;
    private Jobs selectedJob;

    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    DrawerLayout Drawer;
    ListView mListView;
    ActionBarDrawerToggle mDrawerToggle;
    final Calendar userSelection = Calendar.getInstance();
    final Calendar defaultSelection = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener dateStart = new DatePickerDialog.OnDateSetListener() {

       @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            userSelection.set(Calendar.YEAR, year);
            userSelection.set(Calendar.MONTH, monthOfYear);
            userSelection.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            selectedJob.set_start_date(userSelection.get(Calendar.DAY_OF_MONTH));
            selectedJob.set_start_month(userSelection.get(Calendar.MONTH));
            selectedJob.set_start_year(userSelection.get(Calendar.YEAR));
            TextView startDate = (TextView) findViewById(R.id.startDateTextView);
            String dayOfWeek = getDay(userSelection.get(Calendar.DAY_OF_WEEK));
            String month = getMonth(userSelection.get(Calendar.MONTH));
            String startDateString = dayOfWeek + ", " + month + " " + Integer.toString(userSelection.get(Calendar.DAY_OF_MONTH)) + ", " + Integer.toString(2015);
            startDate.setText(startDateString);
            selectedJob.save();
        }

    };
    DatePickerDialog.OnDateSetListener dateEnd = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            userSelection.set(Calendar.YEAR, year);
            userSelection.set(Calendar.MONTH, monthOfYear);
            userSelection.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            selectedJob.set_end_date(userSelection.get(Calendar.DAY_OF_MONTH));
            selectedJob.set_end_month(userSelection.get(Calendar.MONTH));
            selectedJob.set_end_year(userSelection.get(Calendar.YEAR));
            TextView endDate = (TextView) findViewById(R.id.endDateTextView);
            String dayOfWeek = getDay(userSelection.get(Calendar.DAY_OF_WEEK));
            String month = getMonth(userSelection.get(Calendar.MONTH));
            String endDateString = dayOfWeek + ", " + month + " " + Integer.toString(userSelection.get(Calendar.DAY_OF_MONTH)) + ", " + Integer.toString(2015);
            endDate.setText(endDateString);
            selectedJob.save();
        }

    };
    TimePickerDialog.OnTimeSetListener timeStart = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hour, int minute
                              ) {
            userSelection.set(Calendar.HOUR_OF_DAY, hour);
            userSelection.set(Calendar.MINUTE, minute);
            String startTimeString = Integer.toString(userSelection.get(Calendar.HOUR)) + ":" +((userSelection.get(Calendar.MINUTE) > 9) ? Integer.toString(userSelection.get(Calendar.MINUTE)) : "0"+ Integer.toString(userSelection.get(Calendar.MINUTE))) + " " + ((userSelection.get(Calendar.AM_PM) == 1) ? "PM" : "AM");
            TextView startTime = (TextView) findViewById(R.id.startTimeTextView);
            startTime.setText(startTimeString);
            selectedJob.set_start_hours(userSelection.get(Calendar.HOUR_OF_DAY));
            selectedJob.set_start_minutes(userSelection.get(Calendar.MINUTE));
            selectedJob.save();

        }

    };
    TimePickerDialog.OnTimeSetListener timeEnd = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hour, int minute
        ) {
            userSelection.set(Calendar.HOUR_OF_DAY, hour);
            userSelection.set(Calendar.MINUTE, minute);
            String endTimeString = Integer.toString(userSelection.get(Calendar.HOUR)) + ":" +((userSelection.get(Calendar.MINUTE) > 9) ? Integer.toString(userSelection.get(Calendar.MINUTE)) : "0"+ Integer.toString(userSelection.get(Calendar.MINUTE))) + " " + ((userSelection.get(Calendar.AM_PM) == 1) ? "PM" : "AM");
            TextView endTime = (TextView) findViewById(R.id.endTimeTextView);
            endTime.setText(endTimeString);
            selectedJob.set_end_hours(userSelection.get(Calendar.HOUR_OF_DAY));
            selectedJob.set_end_minutes(userSelection.get(Calendar.MINUTE));
            selectedJob.save();

        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_time);
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
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                selectedJob = Jobs.jobList.get(position);
                            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });
        Calendar rightNow = Calendar.getInstance();
        String startTimeString = Integer.toString(rightNow.get(Calendar.HOUR)) + ":" +((rightNow.get(Calendar.MINUTE) > 10) ? Integer.toString(rightNow.get(Calendar.MINUTE)) : "0"+ Integer.toString(rightNow.get(Calendar.MINUTE))) + " " + ((rightNow.get(Calendar.AM_PM) == 1) ? "PM" : "AM");
        TextView startTime = (TextView) findViewById(R.id.startTimeTextView);
        TextView startDate = (TextView) findViewById(R.id.startDateTextView);
        TextView endTime = (TextView) findViewById(R.id.endTimeTextView);
        TextView endDate = (TextView) findViewById(R.id.endDateTextView);
        startTime.setText(startTimeString);
        endTime.setText(startTimeString);
        String dayOfWeek = getDay(rightNow.get(Calendar.DAY_OF_WEEK));
        String month = getMonth(rightNow.get(Calendar.MONTH));
        String startDateString = dayOfWeek + ", " + month + " " + Integer.toString(rightNow.get(Calendar.DAY_OF_MONTH)) + ", " + Integer.toString(2015);
        startDate.setText(startDateString);
        endDate.setText(startDateString);

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


    public void onClickStartTime(View v){
        new TimePickerDialog(AddTimeActivity.this, timeStart, userSelection
                .get(Calendar.HOUR_OF_DAY), userSelection.get(Calendar.MINUTE), true).show();
    }
    public void onClickEndTime(View v){
        new TimePickerDialog(AddTimeActivity.this, timeEnd, userSelection
                .get(Calendar.HOUR_OF_DAY), userSelection.get(Calendar.MINUTE), true).show();
    }

    public void onClickStartDate(View v){
        new DatePickerDialog(AddTimeActivity.this, dateStart, userSelection
                .get(Calendar.YEAR), userSelection.get(Calendar.MONTH),
                userSelection.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void onClickEndDate(View v){
        new DatePickerDialog(AddTimeActivity.this, dateEnd, userSelection
                .get(Calendar.YEAR), userSelection.get(Calendar.MONTH),
                userSelection.get(Calendar.DAY_OF_MONTH)).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_time, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.save_add_time) {
            selectedJob.add_time();
            selectedJob.save();
            Toast.makeText(getBaseContext(), selectedJob.get_time_string(), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(AddTimeActivity.this, CalendarActivity.class);
            startActivity(intent);
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

    public String getDay(int day){
        final int SUNDAY = 1;
        final int MONDAY = 2;
        final int TUESDAY = 3;
        final int WEDNESDAY = 4;
        final int THURSDAY = 5;
        final int FRIDAY = 6;
        final int SATURDAY = 7;
        switch (day){
            case SUNDAY:
                return "Sunday";
            case MONDAY:
                return "Monday";
            case TUESDAY:
                return "Tuesday";
            case WEDNESDAY:
                return "Wednesday";
            case THURSDAY:
                return "Thursday";
            case FRIDAY:
                return "Friday";
            case SATURDAY:
                return "Saturday";
            default:
                return "";
       }
    }


    public String getMonth(int month){
        final int JANUARY = 0;
        final int FEBRUARY = 1;
        final int MARCH = 2;
        final int APRIL = 3;
        final int MAY = 4;
        final int JUNE = 5;
        final int JULY = 6;
        final int AUGUST = 7;
        final int SEPTEMBER = 8;
        final int OCTOBER = 9;
        final int NOVEMBER = 10;
        final int DECEMBER = 11;
        switch (month){
            case JANUARY:
                return "January";
            case FEBRUARY:
                return "February";
            case MARCH:
                return "March";
            case APRIL:
                return "April";
            case MAY:
                return "May";
            case JUNE:
                return "June";
            case JULY:
                return "July";
            case AUGUST:
                return "August";
            case SEPTEMBER:
                return "September";
            case OCTOBER:
                return "October";
            case NOVEMBER:
                return "November";
            case DECEMBER:
                return "December";
            default:
                return "";
        }
    }
}
