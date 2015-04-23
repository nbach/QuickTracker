package com.android.quicktracker.com.quicktracker;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import android.support.v4.app.TaskStackBuilder;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by nbach_000 on 4/16/2015.
 */
public class GeofenceTransitionsIntentService extends IntentService {

    public GeofenceTransitionsIntentService() {
        super("GeofenceTransitionsIntentService");
    }

    protected void onHandleIntent(Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        Jobs.jobList = Jobs.listAll(Jobs.class);

        if (geofencingEvent.hasError()) {
            Jobs.jobList.add(new Jobs("ERROR","TRIG"));
            return;
        }


        int geofenceTransition = geofencingEvent.getGeofenceTransition();


        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
                geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {


            List triggeringGeofences = geofencingEvent.getTriggeringGeofences();
            Calendar rightNow = Calendar.getInstance();

            for (int i=0; i<triggeringGeofences.size(); i++) {
                //AddJobActivity.jobList.add(new Jobs("TEST", "TRIG"));
                int triggeringGeofenceLocation = -1;
                for (int j=0; j<Jobs.jobList.size(); j++){
                    if (((Geofence) triggeringGeofences.get(i)).getRequestId().equals(AddJobActivity.mGeofenceList.get(j).getRequestId())){
                        triggeringGeofenceLocation = j;
                    }
                }
                if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
                    Jobs job = Jobs.jobList.get(triggeringGeofenceLocation);
                    job.set_start_hours(rightNow.HOUR_OF_DAY);
                    job.set_start_minutes(rightNow.MINUTE);
                    job.save();
                    Jobs.jobList = Jobs.listAll(Jobs.class);
                                   }
                if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
                    Jobs job = Jobs.jobList.get(triggeringGeofenceLocation);
                    job.set_end_hours(rightNow.HOUR_OF_DAY);
                    job.set_end_minutes(rightNow.MINUTE);
                    job.save();
                    Jobs.jobList = Jobs.listAll(Jobs.class);
                }
                //AddJobActivity.jobList.add(new Jobs(AddJobActivity.jobList.get(AddJobActivity.jobList.size() - 1).get_hours().lastElement()[0].toString(), "TRIG"));
            }
            //AddJobActivity.jobList.add(new Jobs("TEST2", "TRIG"));
            // Get the transition details as a String.
            String geofenceTransitionDetails = getGeofenceTransitionDetails(
                    this,
                    geofenceTransition,
                    triggeringGeofences
            );
        sendNotification(geofenceTransitionDetails);
        }
    }

    private String getGeofenceTransitionDetails(
            Context context,
            int geofenceTransition,
            List<Geofence> triggeringGeofences) {

        String geofenceTransitionString = getTransitionString(geofenceTransition);


        ArrayList triggeringGeofencesIdsList = new ArrayList();
        for (Geofence geofence : triggeringGeofences) {
            triggeringGeofencesIdsList.add(geofence.getRequestId());
        }
        String triggeringGeofencesIdsString = TextUtils.join(", ", triggeringGeofencesIdsList);

        return "You have " + geofenceTransitionString + " " + triggeringGeofencesIdsString;
    }

    private String getTransitionString(int transitionType) {
        switch (transitionType) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                return "entered";
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                return "exited";
            default:
                return "unknown";
        }
    }

    private void sendNotification(String notificationDetails) {

        int notificationId = 001;

        Intent viewIntent = new Intent(this, AddJobActivity.class);
        PendingIntent viewPendingIntent =
                PendingIntent.getActivity(this, 0, viewIntent, 0);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_logo)
                        .setContentTitle("QuickTracker Alert")
                        .setContentText(notificationDetails)
                        .setContentIntent(viewPendingIntent);

// Get an instance of the NotificationManager service
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(this);

// Build the notification and issues it with notification manager.
        notificationManager.notify(notificationId, notificationBuilder.build());
    }



}