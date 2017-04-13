package softwareheadconsulting.krankiesscheduleapp;

import java.sql.Time;
import java.util.Calendar;


public class Weekday {
    private String month;
    private String dayOfMonth;
    private String dayOfWeek;
    private String shiftTimes;


    public Weekday(String Month, String DayOfMonth, String DayOfWeek, String ShiftTimes)
    {
        this.month = Month;
        this.dayOfMonth = DayOfMonth;
        this.dayOfWeek = DayOfWeek;
        this.shiftTimes = ShiftTimes;
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

    public String getShiftTimes() {
        return shiftTimes;
    }

    public void setShiftTimes(String shiftTimes) {
        this.shiftTimes = shiftTimes;
    }
}
