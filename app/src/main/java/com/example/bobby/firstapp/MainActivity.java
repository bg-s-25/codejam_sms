package com.example.bobby.firstapp;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.telephony.SmsManager;

public class MainActivity extends AppCompatActivity {

    Button bt1; Button bt3;
    EditText et1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et1 = (EditText) findViewById(R.id.editText);
        bt1 = (Button) findViewById(R.id.button);
        bt3 = (Button) findViewById(R.id.button3);

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Will now attempt to send an SMS", Toast.LENGTH_SHORT).show();

                String phoneNum = et1.getText().toString();
                sendSMS(phoneNum, "SMS Message");
            }
        });

        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Input Data button clicked
                startActivity(new Intent(MainActivity.this, InputDataActivity.class));
            }
        });
    }

    // Send an SMS message
    private void sendSMS(String phoneNumber, String message) {
        try {
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(phoneNumber, null, message, null, null);
            Toast.makeText(getApplicationContext(), "SMS Sent!", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "SMS failed to send", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}
