package com.example.bobby.firstapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.FileOutputStream;

public class InputDataActivity extends AppCompatActivity {

    Button buttonAdd; Button buttonSetDate; Button buttonSetTime;
    EditText editTextName; EditText editTextPhone;
    int year_x, month_x, date_x, hour_x, minute_x;
    boolean hr24_x;
    static final int DATE_DIALOG_ID = 1;
    static final int TIME_DIALOG_ID = 2;
    String dateStr, timeStr;
    String[] saveData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_data);

        editTextName = (EditText) findViewById(R.id.editTextName); editTextPhone = (EditText) findViewById(R.id.editTextPhone);
        buttonSetDate = (Button) findViewById(R.id.buttonSetDate); buttonSetTime = (Button) findViewById(R.id.buttonSetTime);
        buttonAdd = (Button) findViewById(R.id.buttonAdd);

        buttonSetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set Date button clicked
                Toast.makeText(getApplicationContext(), "Set Date button clicked", Toast.LENGTH_SHORT).show();
                showDialog(DATE_DIALOG_ID);
            }
        });

        buttonSetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set Time button clicked
                Toast.makeText(getApplicationContext(), "Set Time button clicked", Toast.LENGTH_SHORT).show();

                showDialog(TIME_DIALOG_ID);
            }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add button clicked
                String add_name = editTextName.getText().toString();
                String add_phone = editTextPhone.getText().toString();

                saveData = new String[]{add_name, add_phone, dateStr, timeStr};

                // createFile("FirstAppSaveFile", add_name + "|" + add_phone + "|" + dateStr + "|" + timeStr);

                // Save to shared preferences using a new key based on the current naming key stored in preferences key 0

                SharedPreferences sharedPref = getPreferences(MODE_PRIVATE);
                int newNum = getPrefCurNum() + 1;

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt("0", newNum);
                editor.putString(Integer.toString(newNum), add_name + "|" + add_phone + "|" + dateStr + "|" + timeStr);
                editor.commit();

                String s = sharedPref.getString(Integer.toString(newNum), "DEFAULT");

                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();

                System.out.println(sharedPref.getString("0", "DEFAULT"));
                System.out.println(sharedPref.getString("1", "DEFAULT"));
                System.out.println(sharedPref.getString("2", "DEFAULT"));
            }
        });
    }

    public int getPrefCurNum() {
        SharedPreferences sharedPref = getPreferences(MODE_PRIVATE);
        return sharedPref.getInt("0", 0); // current naming key
    }

    // Dialog codes for date and time

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 1) {
            return new DatePickerDialog(this, dpickerListener, year_x, month_x, date_x);
        } else if (id == 2) {
            return new TimePickerDialog(this, tpickerListener, hour_x, minute_x, false);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener dpickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            year_x = year;
            month_x = monthOfYear;
            date_x = dayOfMonth;
            dateStr = month_x + "/" + date_x + "/" + year_x;
            // Convert to date type for comparison?
            System.out.println(dateStr);
        }
    };

    private TimePickerDialog.OnTimeSetListener tpickerListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            hour_x = hourOfDay;
            minute_x = minute;
            timeStr = hour_x + ":" + minute_x;
            // Convert to time type for comparison?
            System.out.println(timeStr);
        }
    };

    public void createFile(String filename, String string) {
        FileOutputStream outputStream;

        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(string.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
