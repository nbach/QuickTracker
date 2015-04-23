package com.android.quicktracker.com.quicktracker;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Outline;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.common.ConnectionResult;

import com.google.android.gms.common.GooglePlayServicesClient;
import android.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.plus.Plus;

import java.util.ArrayList;
import java.util.List;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.NotificationCompat.WearableExtender;

public class AddJobActivity extends ActionBarActivity implements ResultCallback<Status>, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    String TITLES[] = {"Job Details","Calendar","Settings"};
    int ICONS[] = {R.drawable.ic_clipboard,R.drawable.ic_calendar,R.drawable.ic_settings};
    private LocationRequest mLocationRequest;

    public static ArrayList<Geofence> mGeofenceList = new ArrayList<Geofence>();
    public static PendingIntent mGeofencePendingIntent;
    /**
     * Provides the entry point to Google Play services.
     */
    public static GoogleApiClient mGoogleApiClient;

    /**
     * Represents a geographical location.
     */
    public Location mLastLocation;


                                // Declaring the Toolbar Object
    public static int editPosition=-1;
    private Toolbar toolbar;
    RecyclerView mRecyclerView;                           // Declaring RecyclerView
    RecyclerView.Adapter mAdapter;                        // Declaring Adapter For Recycler View
    RecyclerView.LayoutManager mLayoutManager;            // Declaring Layout Manager as a linear layout manager
    DrawerLayout Drawer;                                  // Declaring DrawerLayout
    ImageButton b1;
    ActionBarDrawerToggle mDrawerToggle;                  // Declaring Action Bar Drawer Toggle

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_job);
        Jobs.jobList = Jobs.listAll(Jobs.class);
        if(editPosition != -1){
            Jobs tempJob = Jobs.jobList.get(editPosition);
            EditText taskEdit = (EditText) findViewById(R.id.jobNameEditText);
            EditText addressEdit = (EditText) findViewById(R.id.addressEditText);
            taskEdit.setText(tempJob.get_name());
            addressEdit.setText(tempJob.get_address());
        }
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        b1 = (ImageButton) findViewById(R.id.OkBt);
        b1.setOnClickListener(myhandler1);





        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView); // Assigning the RecyclerView Object to the xml View

        mRecyclerView.setHasFixedSize(true);                            // Letting the system know that the list objects are of fixed size
        mAdapter = new NavDrawerAdapter(TITLES, ICONS);  // Letting the system know that the list objects are of fixed size
        mRecyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(this);                 // Creating a layout Manager

        mRecyclerView.setLayoutManager(mLayoutManager);                 // Setting the layout Manager

        final GestureDetector mGestureDetector = new android.view.GestureDetector(AddJobActivity.this, new GestureDetector.SimpleOnGestureListener() {

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
        Drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);        // Drawer object Assigned to the view
        mDrawerToggle = new ActionBarDrawerToggle(this,Drawer,toolbar,R.string.openDrawer,R.string.closeDrawer){

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // code here will execute once the drawer is opened( As I dont want anything happened whe drawer is
                // open I am not going to put anything here)
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                // Code here will execute once drawer is closed
            }



        }; // Drawer Toggle Object Made
        Drawer.setDrawerListener(mDrawerToggle); // Drawer Listener set to the Drawer toggle
        mDrawerToggle.syncState();
    }

    View.OnClickListener myhandler1 = new View.OnClickListener() {
        public void onClick(View v) {
            if (editPosition != -1){
                Jobs currentJob = Jobs.jobList.get(editPosition);
                EditText taskEdit = (EditText) findViewById(R.id.jobNameEditText);
                EditText addressEdit = (EditText) findViewById(R.id.addressEditText);
                String taskName = taskEdit.getText().toString().trim();
                String addressName = addressEdit.getText().toString().trim();
                currentJob.set_name(taskName);
                currentJob.set_address(addressName);
                currentJob.save();
                editPosition=-1;
                Intent intent = new Intent(AddJobActivity.this, JobDetailsActivity.class);
                startActivity(intent);
            }
            else {
                EditText taskEdit = (EditText) findViewById(R.id.jobNameEditText);
                EditText addressEdit = (EditText) findViewById(R.id.addressEditText);
                String taskName = taskEdit.getText().toString().trim();
                String addressName = addressEdit.getText().toString().trim();
                Jobs job = new Jobs(taskName, addressName);
                job.save();
                Jobs.jobList = Jobs.listAll(Jobs.class);
                mGeofenceList.add(new Geofence.Builder()
                        .setRequestId(taskName)
                        .setCircularRegion(
                                getLocationFromAddress(addressName).latitude,
                                getLocationFromAddress(addressName).longitude,
                                400
                        )
                        .setExpirationDuration(-1)
                        .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                                Geofence.GEOFENCE_TRANSITION_EXIT)
                        .setNotificationResponsiveness(5000)
                        .build());
                LocationServices.GeofencingApi.addGeofences(
                        mGoogleApiClient,
                        getGeofencingRequest(),
                        getGeofencePendingIntent()
                );
                Intent intent = new Intent(AddJobActivity.this, JobDetailsActivity.class);
                startActivity(intent);
            }
        }
    };

    public void onResult(Status status) {
        if (status.isSuccess()) {
            Toast.makeText(this, "LOLSUCCESS", Toast.LENGTH_LONG).show();

            // Update the UI. Adding geofences enables the Remove Geofences button, and removing
            // geofences enables the Add Geofences button.


        }
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        //Toast.makeText(this, "TEST1", Toast.LENGTH_LONG).show();
        builder.addGeofences(mGeofenceList);
        //Toast.makeText(this, "TEST2", Toast.LENGTH_LONG).show();
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);

        return PendingIntent.getService(this, 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
    }
    @Override
    public void onConnected(Bundle connectionHint) {

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
       //Toast.makeText(this, "onConnceted", Toast.LENGTH_LONG).show();


        Toast.makeText(this, Integer.toString(mGeofenceList.size()), Toast.LENGTH_LONG).show();

    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();



        mGoogleApiClient.connect();
        //Toast.makeText(this, "CONNECT", Toast.LENGTH_LONG).show();
    }

    public void onConnectionSuspended(int cause) {
        //Toast.makeText(this, "SUSPENDED", Toast.LENGTH_LONG).show();
        mGoogleApiClient.connect();
    }


    @Override
    protected void onStop() {
        super.onStop();
        //Toast.makeText(this, "STOP", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Toast.makeText(this, "LOL", Toast.LENGTH_SHORT).show();

    }

    public void onTouchDrawer(final int position) {
        int lastMenu=0;
        editPosition=-1;
        final int JOB_DETAILS_POSITION=1;
        final int CALENDAR_POSITION = 2;
        final int SETTINGS_POSITION = 3;
        if (lastMenu == position) return;

        switch (lastMenu = position) {
            case JOB_DETAILS_POSITION:
                Intent intent0 = new Intent(AddJobActivity.this, JobDetailsActivity.class);
                startActivity(intent0);
                break;
            case CALENDAR_POSITION:
                Intent intent = new Intent(AddJobActivity.this, CalendarActivity.class);
                startActivity(intent);
                break;
            case SETTINGS_POSITION:
                Intent intent2 = new Intent(AddJobActivity.this, SettingsActivity.class);
                startActivity(intent2);
                break;
            default:
                return;
        }
    }

    public LatLng getLocationFromAddress(String strAddress) {

        Geocoder coder = new Geocoder(this);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 1);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        return p1;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_job_details, menu);
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
}
