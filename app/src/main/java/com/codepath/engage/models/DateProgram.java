package com.codepath.engage.models;

import java.util.Date;

/**
 * Created by calderond on 8/7/17.
 */

public class DateProgram {
    String year;
    String month;
    String dateString;
    String hrs;
    String min;
    String seconds;
    Date date;

    public DateProgram(String year, String month, String dateString, String hrs, String min, String seconds) {
        this.year = year;
        this.month = month;
        this.dateString = dateString;
        this.hrs = hrs;
        this.min = min;
        this.seconds = seconds;
        this.date = new Date(Integer.parseInt(year),Integer.parseInt(month),Integer.parseInt(dateString),Integer.parseInt(hrs),Integer.parseInt(min),Integer.parseInt(seconds));
    }

    public Date getDate() {
        return date;
    }
}
