package com.codepath.engage.models;

import java.util.Date;

/**
 * Created by calderond on 8/7/17.
 */

public class DateProgram {

    long year;
    long month;
    long timezoneOffset;
    long time;
    long minutes;
    long seconds;
    long hours;
    long day;
    long date;

    Date dateConstructed;

    public DateProgram() {
    }

    public DateProgram(long year, long month, long timezoneOffset, long time, long minutes, long seconds, long hours, long day, long date) {
        this.year = year;
        this.month = month;
        this.timezoneOffset = timezoneOffset;
        this.time = time;
        this.minutes = minutes;
        this.seconds = seconds;
        this.hours = hours;
        this.day = day;
        this.date = date;
    }

    public long getYear() {
        return year;
    }

    public void setYear(long year) {
        this.year = year;
    }

    public long getMonth() {
        return month;
    }

    public void setMonth(long month) {
        this.month = month;
    }

    public long getTimezoneOffset() {
        return timezoneOffset;
    }

    public void setTimezoneOffset(long timezoneOffset) {
        this.timezoneOffset = timezoneOffset;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getMinutes() {
        return minutes;
    }

    public void setMinutes(long minutes) {
        this.minutes = minutes;
    }

    public long getSeconds() {
        return seconds;
    }

    public void setSeconds(long seconds) {
        this.seconds = seconds;
    }

    public long getHours() {
        return hours;
    }

    public void setHours(long hours) {
        this.hours = hours;
    }

    public long getDay() {
        return day;
    }

    public void setDay(long day) {
        this.day = day;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public Date getDateConstructed() {
        return dateConstructed;
    }

    public void setDateConstructed(long year, long month, long timezoneOffset, long time, long minutes, long seconds, long hours, long day, long date) {
        this.dateConstructed = new Date((int)year,(int)month,(int)date,(int)hours,(int) minutes,(int) seconds);

    }
}
