package softwareheadconsulting.krankiesscheduleapp;

import android.content.Context;
import android.database.Cursor;
import android.provider.CalendarContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<Weekday> weekdayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Calendar testCalendar = Calendar.getInstance();


        weekdayList.add(new Weekday("MAR", "26", "Sunday", "5:00 AM", "11:00 AM"));
        weekdayList.add(new Weekday("MAR", "27", "Monday", "6:00 AM", "12:00 PM"));
        weekdayList.add(new Weekday("MAR", "28", "Tuesday", "10:00 AM", "5:30 PM"));
        weekdayList.add(new Weekday("MAR", "29", "Wednesday", "12:45 PM", "11:35 PM"));
        weekdayList.add(new Weekday("MAR", "30", "Thursday", "5:00 AM", "11:00 AM"));
        weekdayList.add(new Weekday("MAR", "31", "Friday", "3:00 PM", "7:00 PM"));
        weekdayList.add(new Weekday("APR", "1", "Saturday", "7:30 AM", "1:00 PM"));


        populateListview();

        testCalendar.set(testCalendar.DAY_OF_MONTH, 24);


    }

    private void populateListview() {
        ArrayAdapter<Weekday> adapter = new MyListAdapter();
        ListView lstWeekdays = (ListView) findViewById(R.id.weekListView);
        lstWeekdays.setAdapter(adapter);
    }

    private class MyListAdapter extends ArrayAdapter<Weekday> {

        public MyListAdapter() {
            super(MainActivity.this, R.layout.item_view, weekdayList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            View itemView = convertView;
            if(itemView == null)
                itemView = getLayoutInflater().inflate(R.layout.item_view, parent, false);

            Weekday currentWeekday = weekdayList.get(position);

            TextView txtMonth = (TextView) itemView.findViewById(R.id.txtMonth);
            txtMonth.setText(currentWeekday.getMonth());

            TextView txtMonthDay = (TextView) itemView.findViewById(R.id.txtMonthDay);
            txtMonthDay.setText(currentWeekday.getDayOfMonth());

            TextView txtWeekDay = (TextView) itemView.findViewById(R.id.txtWeekday);
            txtWeekDay.setText(currentWeekday.getDayOfWeek());

            TextView txtShift = (TextView) itemView.findViewById(R.id.txtShift_Time);
            txtShift.setText(currentWeekday.getStartTime() + " - " + currentWeekday.getEndTime());

            return itemView;
        }
    }
}
