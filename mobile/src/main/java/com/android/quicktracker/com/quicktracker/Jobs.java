package com.android.quicktracker.com.quicktracker;

import android.graphics.Color;
import android.text.format.Time;

import java.util.Date;
import java.util.Vector;

/**
 * Job class to record all the details about each job.
 */
public class Jobs {
    private String _name;
    private String _address;
    private boolean _salary;
    private double _wage;
    private boolean _weekly;
    private Date _first_timesheet;
    private Color _job_color;
    private Vector<Time[]> _hours;

    public Jobs(String name, String address, boolean salary, double wage, boolean weekly, Date first_timesheet, Color job_color){
        _name = name;
        _address = address;
        _salary = salary;
        _wage = wage;
        _weekly = weekly;
        _first_timesheet = first_timesheet;
        _job_color = job_color;
        _hours = new Vector<Time[]>();
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

    public boolean is_salary() {
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

    public Vector<Time[]> get_hours() {
        return _hours;
    }

    public void set_hours(Vector<Time[]> _hours) {
        this._hours = _hours;
    }

    public void add_hours(Time[] new_hours){
        if(new_hours.length == 2) {
            this._hours.add(new_hours);
        }
    }
}
