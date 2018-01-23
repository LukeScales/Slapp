/*
 * Created by Kris Vanhoutte.
 * Copyright (c) 2016. All rights reserved.
 *
 * Last modified 10/12/16 8:17 PM
 */

package com.getslapp.slapp;

/**
 * Class for storing data about the items displayed
 * in the timetable event list in the TimetableFragment
 */
public class TimetableListItem {
    private String timetable_day;
    private String timetable_starttime;
    private String timetable_endtime;
    private String timetable_module_name;
    private String timetable_module_code;
    private String timetable_location;
    private String timetable_lecturer;
    private String timetable_weeks;
    private String timetable_type;
    private int timetable_colour;

    public TimetableListItem(String day, String starttime, String endtime, String module_name, String module_code, String location, String lecturer, String weeks, String type, int colour)
    {
        super();
        timetable_day = day;
        timetable_starttime = starttime;
        timetable_endtime = endtime;
        timetable_module_name = module_name;
        timetable_module_code = module_code;
        timetable_location = location;
        timetable_lecturer = lecturer;
        timetable_weeks = weeks;
        timetable_type = type;
        timetable_colour = colour;
    }

    public String getDay() { return timetable_day; }
    public void setDay(String day) { timetable_day = day; }

    public String getTime() { return timetable_starttime + " - " + timetable_endtime; }
    public String getStartTime() { return timetable_starttime; }
    public String getEndTime() { return timetable_endtime; }
    public void setTime(String starttime, String endtime) {timetable_starttime = starttime; timetable_endtime = endtime;}

    public String getModule() { return timetable_module_name + " - " + timetable_module_code; }
    public String getModuleName() { return timetable_module_name; }
    public void setModuleName(String module_name) {timetable_module_name = module_name;}
    public String getModuleCode() { return timetable_module_code; }
    public void setModuleCode(String module_code) {timetable_module_code = module_code;}

    public String getLocation() { return timetable_location; }
    public void setLocation(String location) {timetable_location = location;}

    public String getLecturer() { return timetable_lecturer; }
    public void setLecturer(String lecturer) {timetable_lecturer = lecturer;}

    public String getWeeks() { return timetable_weeks; }
    public void setWeeks(String weeks) {timetable_weeks = weeks;}

    public String getType() { return timetable_type; }
    public void setType(String type) {timetable_type = type;}

    public int getColour() { return timetable_colour; }
    public void setColour(int colour) {timetable_colour = colour;}
}
