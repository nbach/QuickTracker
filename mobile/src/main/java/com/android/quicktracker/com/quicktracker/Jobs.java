package com.android.quicktracker.com.quicktracker;

import android.graphics.Color;

import com.orm.SugarRecord;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Time;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

/**
 * Job class to record all the details about each job.
 */
public class Jobs extends SugarRecord<Jobs> {
    private String _name;
    private String _address;
    /*private boolean _salary;
    private double _wage;
    private boolean _weekly;
    private Date _first_timesheet;
    private Color _job_color;*/
    public static int _start_hours = 0;
    public static int _start_minutes = 0;
    public static int _end_hours = 0;
    public static int _end_minutes = 0;
    public static List<Jobs> jobList;

    public Jobs(){}


    public Jobs(String name, String address){
        _name = name;
        _address = address;
        List<int[]> inputArray = new ArrayList<int[]>();
    }

    /* Getters and Setters */
    public String get_name() {

        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_address() {
        return _address;
    }

    public void set_address(String _address) {
        this._address = _address;
    }

    /*public boolean is_salary() {
        return _salary;
    }

    public void set_salary(boolean _salary) {
        this._salary = _salary;
    }

    public double get_wage() {
        return _wage;
    }

    public void set_wage(double _wage) {
        this._wage = _wage;
    }

    public Date get_first_timesheet() {
        return _first_timesheet;
    }

    public void set_first_timesheet(Date _first_timesheet) {
        this._first_timesheet = _first_timesheet;
    }

    public boolean is_weekly() {
        return _weekly;
    }

    public void set_weekly(boolean _weekly) {
        this._weekly = _weekly;
    }

    public Color get_job_color() {
        return _job_color;
    }

    public void set_job_color(Color _job_color) {
        this._job_color = _job_color;
    }
*/
    public List<int[]> get_hours() {
        List<int[]> _hours= new ArrayList<int[]>();
        _hours.add(new int[]{_start_hours,_start_minutes, _end_hours, _end_minutes});
        return _hours;
    }


    public void add_start_hours(int begin_hours){
         this._start_hours = begin_hours;
    }
    public void add_start_minutes(int begin_minutes){
        this._start_minutes = begin_minutes;
    }
    public void add_end_hours(int end_hours){


        _end_hours= end_hours;
    }
    public void add_end_minutes(int end_minutes){
       _end_minutes= end_minutes;
    }
}
