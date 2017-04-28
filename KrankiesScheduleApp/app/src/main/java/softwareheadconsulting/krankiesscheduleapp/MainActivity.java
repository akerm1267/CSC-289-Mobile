package softwareheadconsulting.krankiesscheduleapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Calendar calendar;
    List<Weekday> weekdayList = new ArrayList<Weekday>();
    private ScheduleTask mAuthTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String username = getIntent().getStringExtra("EXTRA_USERNAME");
        String password = getIntent().getStringExtra("EXTRA_PASSWORD");

        List<Weekday> days = new ArrayList<>();
        days = sortedWeekdays();

        for(int index = 0; index < 7; index++)
        {
            mAuthTask = new ScheduleTask(username, days.get(index));
            mAuthTask.execute();
        }

        populateListview();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        switch (item.getItemId())
        {
            case R.id.action_info:
                BuildInfoAlert();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void BuildInfoAlert()
    {
        AlertDialog.Builder infoDialog = new AlertDialog.Builder(MainActivity.this);
        infoDialog.setMessage(R.string.app_info)
                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        infoDialog.show();
    }

    public void onBackPressed()
    {
        // code here to show dialog
        //super.onBackPressed();  // optional depending on your needs

        AlertDialog.Builder backAlert = new AlertDialog.Builder(MainActivity.this);
        backAlert.setMessage("Log out?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //nothing
                    }
                });
        backAlert.show();
    }

    private Weekday getWeekday(int dayIndex) {
        calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, dayIndex);
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        int month = calendar.get(Calendar.MONTH);
        int monthDay = calendar.get(Calendar.DAY_OF_MONTH);

        String strWeekday = "";
        switch(day)
        {
            case 1:
                strWeekday = "SUN";
                break;
            case 2:
                strWeekday = "MON";
                break;
            case 3:
                strWeekday = "TUE";
                break;
            case 4:
                strWeekday = "WED";
                break;
            case 5:
                strWeekday = "THU";
                break;
            case 6:
                strWeekday = "FRI";
                break;
            case 7:
                strWeekday = "SAT";
                break;
            default:
                strWeekday = "N/A";
                break;
        }

        String strMonth = "";
        switch (month)
        {
            case 1:
               strMonth = "JAN";
                break;
            case 2:
                strMonth = "FEB";
                break;
            case 3:
                strMonth = "MAR";
                break;
            case 4:
                strMonth = "APR";
                break;
            case 5:
                strMonth = "MAY";
                break;
            case 6:
                strMonth = "JUN";
                break;
            case 7:
                strMonth = "JUL";
                break;
            case 8:
                strMonth = "AUG";
                break;
            case 9:
                strMonth = "SEP";
                break;
            case 10:
                strMonth = "OCT";
                break;
            case 11:
                strMonth = "NOV";
                break;
            case 12:
                strMonth = "DEC";
                break;
            default:
                strMonth = "Err";
        }
        Weekday weekday = new Weekday(strMonth, String.valueOf(monthDay), strWeekday);
        return weekday;
    }

    private List<Weekday> sortedWeekdays() {
        List<Weekday> tempWeekdays = new ArrayList<Weekday>();

        for(int index = 0; index < 7; index++)
        {
            tempWeekdays.add(getWeekday(index));
        }

        return tempWeekdays;
    }

    private void populateListview() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ArrayAdapter<Weekday> adapter = new MyListAdapter();
        ListView lstWeekdays = (ListView) findViewById(R.id.weekListView);
        lstWeekdays.setAdapter(adapter);
        lstWeekdays.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder shiftDialog = new AlertDialog.Builder(MainActivity.this);
                shiftDialog.setTitle("Choose a shift");
                TextView tv = (TextView) view.findViewById(R.id.txtShift_Time);
                String text = tv.getText().toString();
                final String[] shifts = text.split("\n");
                shiftDialog.setItems(shifts, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Intent intent = new Intent(Intent.ACTION_SENDTO);
                        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                        intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"manager@167.160.84.186"});
                        intent.putExtra(Intent.EXTRA_SUBJECT, "[REQUEST TIME OFF] " + shifts[i] );
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivity(intent);
                        }
                    }
                });
                shiftDialog.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                shiftDialog.show();
            }
        });
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
            txtShift.setText(currentWeekday.getShiftTimes());

            return itemView;
        }
    }

    public class ScheduleTask extends AsyncTask<Void, Void, Boolean> {

        private String mUsername;
        String mWeekday;
        String mMonth;
        String mDayOfMonth;

        ScheduleTask(String user, Weekday day) {
            mUsername = user;
            mWeekday = day.getDayOfWeek();
            mMonth = day.getMonth();
            mDayOfMonth = day.getDayOfMonth();
        }

        String testM = "";

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            URL url = null;
            String login_URL = "http://167.160.84.186/schedule.php";

            try {
                url = new URL(login_URL);
                HttpURLConnection httpUrlConnection = (HttpURLConnection)url.openConnection();
                httpUrlConnection.setRequestMethod("POST");
                httpUrlConnection.setDoOutput(true);
                httpUrlConnection.setDoInput(true);
                OutputStream outputStream = httpUrlConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("user_name","UTF-8")+"="+URLEncoder.encode(mUsername,"UTF-8")+"&"+URLEncoder.encode("dayOfWeek","UTF-8")+
                        "="+URLEncoder.encode(mWeekday,"UTF-8");
                testM = post_data;
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                //int status = httpUrlConnection.getResponseCode();
                //testM = String.valueOf(status);

                InputStream inputStream = httpUrlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";
                while((line = bufferedReader.readLine()) != null)
                {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpUrlConnection.disconnect();
                testM = result;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                testM = e.toString();
            } catch (ProtocolException e) {
                e.printStackTrace();
                testM = e.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
                testM = e.toString();
            } catch (IOException e) {
                e.printStackTrace();
                testM = e.toString();
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            /*for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");

                testM = pieces[1];

                if (pieces[0].equals(mUsername)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }*/


            return false;

            // TODO: register the new account here.
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if(!testM.equals("---"))
            {
                String[] timeArray = testM.split("&");
                String[][] aryTimes = new String[3][2];
                for(int index = 0; index < timeArray.length; index++) {
                    aryTimes[index] = timeArray[index].split("-");

                    for(int aa = 0; aa < aryTimes[index].length; aa++)
                    {
                        String[] tempAry = aryTimes[index][aa].split(":");
                        int tempHour = Integer.valueOf(tempAry[0]);
                        String amPm = "AM";

                        if(tempHour == 12)
                            amPm = "PM";
                        else if(tempHour > 12){
                            tempHour -= 12;
                            amPm = "PM";
                        }

                        aryTimes[index][aa] = String.format("%d:%s %s", tempHour, tempAry[1], amPm);
                    }

                    timeArray[index] = String.format("%s - %s", aryTimes[index][0], aryTimes[index][1]);
                }

                String tempStr = "";
                for(int index = 0; index < timeArray.length; index++)
                {
                    if(index > 0)
                        tempStr = String.format("%s%n%s", tempStr, timeArray[index]);
                    else
                        tempStr = timeArray[index];
                }

                weekdayList.add(new Weekday(mMonth, mDayOfMonth, mWeekday, tempStr));
            }
            else {
                weekdayList.add(new Weekday(mMonth, mDayOfMonth, mWeekday, "---"));
            }
        }

        @Override
        protected void onCancelled() {

        }



    }
}
