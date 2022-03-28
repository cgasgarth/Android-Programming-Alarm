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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private ArrayList<Alarm> alarms = new ArrayList<Alarm>();
    private ArrayAdapter<Alarm> adapter2;
    private Alarm selectedAlarm;
    private int requestCode = 0;


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

        Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);
        adapter2 = new ArrayAdapter<Alarm>(this, android.R.layout.simple_spinner_item, alarms);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(this);

    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {

        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        switch (parent.getId()){
            case R.id.spinner:
                String time = (String) parent.getItemAtPosition(pos);
                Log.i("time selected is", time);
                break;
            case R.id.spinner2:
                Alarm alarm = (Alarm) parent.getItemAtPosition(pos);
                selectedAlarm = alarm;
                Log.i("alarm selected is", alarm.toString());
                break;
        }
    }

    public void removeAlarm(View view){
        if (selectedAlarm != null){
            PendingIntent pendingIntent = selectedAlarm.getPendingIntent();
            AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
            alarmManager.cancel(pendingIntent);
            alarms.remove(selectedAlarm);
            adapter2.notifyDataSetChanged();
            Toast.makeText(this, "Alarm has been removed.", Toast.LENGTH_LONG).show();
        }
    }

    public void newAlarm(View view){
        TimePicker tp = findViewById(R.id.timePicker1);
        Calendar calendar = Calendar.getInstance();
        int hr = tp.getHour();
        int m = tp.getMinute();

        EditText alarmNameET = findViewById(R.id.alarmName);
        String name = String.valueOf(alarmNameET.getText());

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

        Log.i("Hour and minute", hr + ":" + m);
        calendar.set(Calendar.HOUR_OF_DAY,hr);
        calendar.set(Calendar.MINUTE,m);
        Date time = calendar.getTime();
        Log.i("set cal time", String.valueOf(time));


        //Creating a pending intent for sendNotification class.
        Intent intent = new Intent(this, sendNotification.class);
        intent.putExtra("AlarmName", name);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, requestCode, intent, PendingIntent.FLAG_MUTABLE);
        requestCode++;
        //Generating object of alarmManager using getSystemService method. Here ALARM_SERVICE is used to receive alarm manager with intent at a time.
        AlarmManager alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);
        long millTime = calendar.getTimeInMillis();
        Log.i("cal time", String.valueOf(millTime));
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        Alarm newAlarm = new Alarm(pendingIntent, millTime, name);
        alarms.add(newAlarm);
        adapter2.notifyDataSetChanged();

        Toast.makeText(this, "Alarm has been created.", Toast.LENGTH_LONG).show();
    }

    public void updateAlarm(View view){
        //remove the current pending intent
        PendingIntent pendingIntent = selectedAlarm.getPendingIntent();
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);

        TimePicker tp = findViewById(R.id.timePicker1);
        Calendar calendar = Calendar.getInstance();
        int hr = tp.getHour();
        int m = tp.getMinute();

        EditText alarmNameET = findViewById(R.id.alarmName);
        String name = String.valueOf(alarmNameET.getText());


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

        Log.i("Hour and minute", hr + ":" + m);
        calendar.set(Calendar.HOUR_OF_DAY,hr);
        calendar.set(Calendar.MINUTE,m);
        Date time = calendar.getTime();
        Log.i("set cal time", String.valueOf(time));

        //Creating a pending intent for sendNotification class.
        Intent intent = new Intent(this, sendNotification.class);
        pendingIntent = PendingIntent.getBroadcast(this, requestCode, intent, PendingIntent.FLAG_MUTABLE);
        intent.putExtra("AlarmName", name);
        requestCode++;
        //Generating object of alarmManager using getSystemService method. Here ALARM_SERVICE is used to receive alarm manager with intent at a time.
        long millTime = calendar.getTimeInMillis();
        Log.i("cal time", String.valueOf(millTime));
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        //update the current alarms params
        selectedAlarm.setPendingIntent(pendingIntent);
        selectedAlarm.setMilli(millTime);
        selectedAlarm.setAlarmName(name);
        adapter2.notifyDataSetChanged();
        Toast.makeText(this, "Alarm has been updated.", Toast.LENGTH_LONG).show();

    }



    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
}

class Alarm{
    private PendingIntent pendingIntent;
    private long milli;
    private String alarmName;

    public Alarm(PendingIntent pendingIntent, long milli, String alarmName){
        this.pendingIntent = pendingIntent;
        this.milli = milli;
        this.alarmName = alarmName;
    }

    public PendingIntent getPendingIntent(){ return this.pendingIntent; }
    public void setPendingIntent(PendingIntent pendingIntent){ this.pendingIntent = pendingIntent; }

    public long getMilli(){ return this.milli; }
    public void setMilli(long milli){ this.milli = milli; }

    public String getAlarmName(){ return this.alarmName; }
    public void setAlarmName(String alarmName){ this.alarmName = alarmName; }

    @Override
    public String toString(){
        Date date = new Date(this.getMilli());
        DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy 'at' hh:mm:ss");
        //formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        String dateFormatted = formatter.format(date);
        dateFormatted = dateFormatted + " " + this.getAlarmName();
        return dateFormatted;

    }
}