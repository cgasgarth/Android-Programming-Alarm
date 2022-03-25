package com.example.alarm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.times_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {

        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
         String item = (String) parent.getItemAtPosition(pos);
         Log.i("item selected is", item);
    }

    public void newAlarm(View view){
        TimePicker tp = findViewById(R.id.timePicker1);
        Calendar calendar = Calendar.getInstance();
        int hr = tp.getHour();
        int m = tp.getMinute();

        TextView dateET = findViewById(R.id.editTextDate);
        String[] date = String.valueOf(dateET.getText()).split("-");
        if (date.length > 1) {
            String month = date[0];
            String day = date[1];
            String year = date[2];
            calendar.set(Calendar.MONTH, Integer.valueOf(date[0]));
            calendar.set(Calendar.DAY_OF_MONTH, Integer.valueOf(date[1]));
            calendar.set(calendar.YEAR, Integer.valueOf(date[2]));
            Log.i("Month, day, year", month + day + year);
        }

        Log.i("Hour and minuite", hr + ":" + m);
        calendar.set(Calendar.HOUR_OF_DAY,hr);
        calendar.set(Calendar.MINUTE,m);
        Date time = calendar.getTime();
        Log.i("set cal time", String.valueOf(time));


        //Creating a pending intent for sendNotification class.
        Intent intent = new Intent(this, sendNotification.class);
        PendingIntent pendingIntent = null;
        pendingIntent = PendingIntent.getBroadcast(this, 40, intent, PendingIntent.FLAG_IMMUTABLE);

        //Generating object of alarmManager using getSystemService method. Here ALARM_SERVICE is used to receive alarm manager with intent at a time.
        AlarmManager alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);
        long millTime = calendar.getTimeInMillis();
        Log.i("cal time", String.valueOf(millTime));
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        Log.d("===Sensing alarm===", "One time alert alarm has been created. This alarm will send to a broadcast sensing receiver.");

        Toast.makeText(this, "Sensing alert alarm has been created. This alarm will send to a broadcast start sensing receiver.", Toast.LENGTH_LONG).show();
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
}