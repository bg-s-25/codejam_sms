package com.example.bobby.sms;

import android.icu.util.Calendar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.telephony.SmsManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.widget.DatePicker;
import android.widget.TimePicker;

public class MainActivity extends AppCompatActivity {

    Button bt1;
    EditText etPhone, etMessage;
    Button bt2;
    static final int DATE_DIALOG_ID = 1;
    static final int TIME_DIALOG_ID = 2;
    Calendar cal = Calendar.getInstance();
    int year_x, month_x, date_x, hour_x, minute_x; // year, month, day of month, hour of day, and minute set by user
    String dateStr, timeStr;
    String[] saveData;
    Thread t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        etPhone = (EditText) findViewById(R.id.editTextPhone);
        etMessage = (EditText) findViewById(R.id.editTextMessage);
        bt1 = (Button) findViewById(R.id.button);
        bt2 = (Button) findViewById(R.id.buttonSetDateTime);
        year_x = cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.MONTH);
        date_x = cal.get(Calendar.DAY_OF_MONTH);

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Will now attempt to send an SMS", Toast.LENGTH_SHORT).show();

                String phoneNum = etPhone.getText().toString();
                String message = etMessage.getText().toString();

                sendSMS(phoneNum, message);
            }
        });

        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Picking date/time", Toast.LENGTH_SHORT).show();

                Toast.makeText(getApplicationContext(), "Set Date button clicked", Toast.LENGTH_SHORT).show();

                showDialog(TIME_DIALOG_ID);

                showDialog(DATE_DIALOG_ID);

                String phoneNum = etPhone.getText().toString();
                String message = etMessage.getText().toString();

                // Saves phone number, specified message, and selected date/time to String[]

                saveData = new String[]{phoneNum, message, dateStr, timeStr};

                cal = Calendar.getInstance();
                System.out.println(year_x);
                System.out.println(month_x);
                System.out.println(date_x);
                System.out.println(hour_x);
                System.out.println(minute_x);
                System.out.println(cal.get(Calendar.YEAR));
                System.out.println(cal.get(Calendar.MONTH) + 1);
                System.out.println(cal.get(Calendar.DAY_OF_MONTH));
                System.out.println(cal.get(Calendar.HOUR_OF_DAY));
                System.out.println(cal.get(Calendar.MINUTE));

                // Start thread to check for auto send SMS

                t.start();
            }
        });

        t = new Thread() {
            @Override
            public void run() {
                while (!isInterrupted()) {
                    try {
                        Thread.sleep(5000);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                cal = Calendar.getInstance();
                                timerTask();
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

    }

    // Send an SMS message
    private void sendSMS(String phoneNumber, String message) {
        if (phoneNumber == "") {return;}
        try {
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(phoneNumber, null, message, null, null);
            Toast.makeText(getApplicationContext(), "SMS Sent!", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "SMS failed to send", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public DatePickerDialog.OnDateSetListener dpickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            year_x = year;
            month_x = monthOfYear + 1;
            date_x = dayOfMonth;
            dateStr = month_x + "/" + date_x + "/" + year_x;
            // System.out.println(dateStr);
        }
    };

    // Dialogs for date and time
    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 1) {
            return new DatePickerDialog(this, dpickerListener, year_x, month_x, date_x);
        } else if (id == 2) {
            return new TimePickerDialog(this, tpickerListener, hour_x, minute_x, false);
        }
        return null;
    }

    public TimePickerDialog.OnTimeSetListener tpickerListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            hour_x = hourOfDay;
            minute_x = minute;
            timeStr = hour_x + ":" + minute_x;
            // System.out.println(timeStr);
        }
    };

    // Method repeated by timer on regular interval
    public void timerTask() {
        if (year_x == cal.get(Calendar.YEAR) && month_x == (cal.get(Calendar.MONTH) + 1) && date_x == cal.get(Calendar.DAY_OF_MONTH) && hour_x == cal.get(Calendar.HOUR_OF_DAY) && minute_x == cal.get(Calendar.MINUTE)) {
            sendSMS(saveData[0], saveData[1]);
        }
    }
}
