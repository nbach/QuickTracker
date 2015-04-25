package com.android.quicktracker.com.quicktracker;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;


public class QuickTracker extends Activity implements ResultCallback<Status>, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    Button b1;
    Button b2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_tracker);
        b1 = (Button) findViewById(R.id.addJobButton);
        b2 = (Button) findViewById(R.id.learnMoreButton);
        b1.setOnClickListener(myhandler1);
        b2.setOnClickListener(myhandler2);


    }

    View.OnClickListener myhandler1 = new View.OnClickListener() {
        public void onClick(View v) {
            Intent intent = new Intent(QuickTracker.this, AddJobActivity.class);
            startActivity(intent);
        }
    };

    View.OnClickListener myhandler2 = new View.OnClickListener() {
        public void onClick(View v) {

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_quick_tracker, menu);
        return true;
    }
    public void onResult(Status status) {
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(AddJobActivity.mGeofenceList);
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent() {
        if (AddJobActivity.mGeofencePendingIntent != null) {
            return AddJobActivity.mGeofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
        return PendingIntent.getService(this, 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
    }
    @Override
    public void onConnected(Bundle connectionHint) {
        Jobs.jobList = Jobs.listAll(Jobs.class);
        for (int i=0; i<Jobs.jobList.size(); i++) {
            if (Jobs.jobList.get(i).get_address()!=null && Jobs.jobList.get(i).get_name()!=null && getLocationFromAddress(Jobs.jobList.get(i).get_address())!=null){
            AddJobActivity.mGeofenceList.add(new Geofence.Builder()

                    .setRequestId(Jobs.jobList.get(i).get_name())
                    .setCircularRegion(
                            getLocationFromAddress(Jobs.jobList.get(i).get_address()).latitude,
                            getLocationFromAddress(Jobs.jobList.get(i).get_address()).longitude,
                            200
                    )
                    .setExpirationDuration(-1)
                    .setNotificationResponsiveness(5000)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                            Geofence.GEOFENCE_TRANSITION_EXIT)
                    .build());
            LocationServices.GeofencingApi.addGeofences(
                    AddJobActivity.mGoogleApiClient,
                    getGeofencingRequest(),
                    getGeofencePendingIntent()
            );
        }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        AddJobActivity.mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();



        AddJobActivity.mGoogleApiClient.connect();
    }

    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to

        AddJobActivity.mGoogleApiClient.connect();
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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

}
