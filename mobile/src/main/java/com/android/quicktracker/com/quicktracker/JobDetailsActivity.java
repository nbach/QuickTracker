package com.android.quicktracker.com.quicktracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.enums.SnackbarType;
import com.nispok.snackbar.listeners.ActionClickListener;

import java.util.ArrayList;
import java.util.List;


public class JobDetailsActivity extends ActionBarActivity {

    String TITLES[] = {"Job Details","Calendar","Settings"};
    int ICONS[] = {R.drawable.ic_clipboard,R.drawable.ic_calendar,R.drawable.ic_settings};



    private Toolbar toolbar;

    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    DrawerLayout Drawer;
    private MyCustomAdapter mAdapterJob;
    ListView mListView;
    ActionBarDrawerToggle mDrawerToggle;
    ImageButton b1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_details);
        mListView = (ListView) findViewById(R.id.jobDetailListView);
        b1 = (ImageButton) findViewById(R.id.addTaskButton);
        b1.setOnClickListener(myhandler1);
        mAdapterJob = new MyCustomAdapter(this);
        mListView.setAdapter(mAdapterJob);




 

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);






        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView);

        mRecyclerView.setHasFixedSize(true);
        mAdapter = new NavDrawerAdapter(TITLES,ICONS);
        mRecyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mLayoutManager);

        final GestureDetector mGestureDetector = new GestureDetector(JobDetailsActivity.this, new GestureDetector.SimpleOnGestureListener() {

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

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

            }




        };
        Drawer.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

    }

    public void onTouchDrawer(final int position) {
        int lastMenu=1;
        final int CALENDAR_POSITION = 2;
        final int SETTINGS_POSITION = 3;
        final int MANAGE_JOBS_POSITION = 1;
        if (lastMenu == position) return;

        switch (lastMenu = position) {
            case CALENDAR_POSITION:
                Intent intent = new Intent(JobDetailsActivity.this, CalendarActivity.class);
                startActivity(intent);
                break;
            case SETTINGS_POSITION:
                Intent intent2 = new Intent(JobDetailsActivity.this, SettingsActivity.class);
                startActivity(intent2);
                break;
            case MANAGE_JOBS_POSITION:
                Intent intent3 = new Intent(JobDetailsActivity.this, AddJobActivity.class);
                startActivity(intent3);
                break;
            default:
                return;
        }
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


        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    View.OnClickListener myhandler1 = new View.OnClickListener() {
        public void onClick(View v) {
            Intent intent = new Intent(JobDetailsActivity.this, AddJobActivity.class);
            intent.putExtra("callingActivity", getBaseContext().toString());
            startActivity(intent);
        }
    };

    private class MyCustomAdapter extends BaseAdapter {

        private List<Jobs> mData = Jobs.listAll(Jobs.class);
        private LayoutInflater mInflater;
        private Context mContext;

        public MyCustomAdapter(Context context) {
            mInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mContext = context;
        }

        public void addItem(final Jobs item) {
            mData.add(item);
            notifyDataSetChanged();
        }

        @Override
        public int getItemViewType(int position) {

            return 1;
        }



        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Jobs getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            int type = getItemViewType(position);

            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.job_detail_row, null);
                holder.deleteButton = (ImageButton)convertView.findViewById(R.id.deleteButton);
                holder.textView = (TextView)convertView.findViewById(R.id.jobDetailRowTextView);
                holder.editButton = (ImageButton)convertView.findViewById(R.id.editButton);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }
            holder.textView.setText(mData.get(position).get_name());
            holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    // ShowPlacePref(places, position);
                    AlertDialog.Builder adb=new AlertDialog.Builder(JobDetailsActivity.this);
                    adb.setMessage("Are you sure you want to delete " + mData.get(position).get_name().toString().trim());
                    final int positionToRemove = position;
                    adb.setNegativeButton("Cancel", null);
                    adb.setPositiveButton("Delete", new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            final Jobs revertDelete = mData.get(positionToRemove);
                            final Geofence revertFence = AddJobActivity.mGeofenceList.get(positionToRemove);


                            mData.get(positionToRemove).delete();
                            AddJobActivity.mGeofenceList.remove(positionToRemove);
                            mData.remove(positionToRemove);
                            if (mData.isEmpty()){
                                mData.add(new Jobs("None", "None"));
                            }
                            notifyDataSetChanged();

                                SnackbarManager.show(
                                        Snackbar.with(getApplicationContext()) // context
                                                .text("Job Deleted")
                                                .actionLabel("Undo")
                                                .actionColor(Color.YELLOW)

                                                .actionListener(new ActionClickListener() {
                                                    @Override
                                                    public void onActionClicked(Snackbar snackbar) {
                                                        AddJobActivity.mGeofenceList.add(revertFence);
                                                        if (mData.get(0).get_name().equals("None")) mData.remove(0);
                                                        Jobs newJob = new Jobs(revertDelete);
                                                        newJob.save();
                                                        mData.add(revertDelete);
                                                        notifyDataSetChanged();
                                                    }
                                                })
                                        , (Activity) mContext);

                        }});
                    adb.show();
                }
            });
            holder.editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    // ShowPlacePref(places, position);
                    AddJobActivity.editPosition=position;
                    Intent intent = new Intent(JobDetailsActivity.this, AddJobActivity.class);
                    startActivity(intent);
                }
            });
            return convertView;
        }

    }

    public static class ViewHolder {
        public TextView textView;
        public ImageButton deleteButton;
        public ImageButton editButton;
    }
}