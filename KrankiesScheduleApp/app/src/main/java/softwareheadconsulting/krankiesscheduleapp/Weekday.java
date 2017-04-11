package softwareheadconsulting.krankiesscheduleapp;

import java.sql.Time;
import java.util.Calendar;


public class Weekday {
    private String month;
    private String dayOfMonth;
    private String dayOfWeek;
    private String startTime;
    private String endTime;


    public Weekday(String Month, String DayOfMonth, String DayOfWeek, String StartTime, String EndTime)
    {
        this.month = Month;
        this.dayOfMonth = DayOfMonth;
        this.dayOfWeek = DayOfWeek;
        this.startTime = StartTime;
        this.endTime = EndTime;
    }

    public Weekday(String Month, String DayOfMonth, String DayOfWeek)
    {
        this.month = Month;
        this.dayOfMonth = DayOfMonth;
        this.dayOfWeek = DayOfWeek;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(String dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
