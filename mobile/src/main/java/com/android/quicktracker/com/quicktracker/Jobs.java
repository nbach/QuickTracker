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
    private int _start_hours = 0;
    private int _start_minutes = 0;
    private int _end_hours = 0;
    private int _end_minutes = 0;
    private String _time = "";
    public static List<Jobs> jobList;

    public Jobs(){}


    public Jobs(String name, String address){
        _name = name;
        _address = address;
        _time = "";
    }
    public Jobs(String name, String address, int startHour, int startMinute, int endHour, int endMinute){
        _name = name;
        _address = address;
        _start_hours = startHour;
        _start_minutes = startMinute;
        _end_hours = endHour;
        _end_minutes = endMinute;
    }

    /* Getters and Setters */
    public String get_name() {

        return _name;
    }

    public ArrayList<int[]> _get_time(){
        return stringToArray(_time);
    }
    public ArrayList<int[]> stringToArray(String input) {
        //Parse string and eliminate delimiters
        if (input.equals("")){
            return null;
        }
        String[] tokens = input.split(",");
        int[] values = new int[tokens.length];
        //Parse strings chars into ints
        for (int i = 0; i < values.length; i++) {
            if(tokens[i] != "") {
                values[i] = Integer.parseInt(tokens[i]);
            }
        }
        int valuesIndex = 0;
        ArrayList<int[]> returnList = new ArrayList<int[]>();
        while (valuesIndex < values.length) {
            int[] toAdd = new int[4];
            for (int j = 0; j < 4; j++) {
                toAdd[j] = values[valuesIndex];
                valuesIndex++;
            }
            returnList.add(toAdd);
        }
        return returnList;
    }

    public String arrayToString(ArrayList<int[]> input){
        String returnString="";
        for (int i=0; i<input.size(); i++){
            for (int j=0; j<4; j++){
                returnString += Integer.toString(input.get(i)[j]);
                returnString += ",";
            }
        }

        return returnString;
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

    public List<int[]> get_hours() {
        List<int[]> _hours= new ArrayList<int[]>();
        _hours.add(new int[]{_start_hours,_start_minutes, _end_hours, _end_minutes});
        return _hours;
    }


    public void set_start_hours(int begin_hours){
         this._start_hours = begin_hours;
    }
    public void set_start_minutes(int begin_minutes){
        this._start_minutes = begin_minutes;
    }
    public void set_end_hours(int end_hours){
       this. _end_hours= end_hours;
    }
    public void set_end_minutes(int end_minutes){
       this._end_minutes= end_minutes;
    }
    public int get_start_hours(){
        return this._start_hours;
    }
    public int get_start_minutes(){
        return this._start_minutes;
    }
    public int get_end_hours(){
        return this._end_hours;
    }
    public int get_end_minutes(){
        return this._end_minutes;
    }

    public String get_time_string(){
        return _time;
    }

    //Add new info to time String
    public void add_time(){
        int[] transferIntegers = new int[]{_start_hours,_start_minutes,_end_hours,_end_minutes};
        //If time elapsed is zero, return
        if (_start_hours == _end_hours &&_start_minutes == _end_minutes){
            return;
        }
        ArrayList<int[]> allTimes = new ArrayList<int[]>();
        if(!_time.equals("")) {
            allTimes = stringToArray(_time);
        }

        allTimes.add(transferIntegers);
        _time = arrayToString(allTimes);
        _start_hours=0;
        _start_minutes=0;
        _end_hours=0;
        _end_minutes=0;
    }
}
