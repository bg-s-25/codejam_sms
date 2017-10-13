package com.example.bobby.firstapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.telephony.SmsManager;

public class MainActivity extends AppCompatActivity {

    Button bt1;
    EditText etPhone, etMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etPhone = (EditText) findViewById(R.id.editTextPhone);
        etMessage = (EditText) findViewById(R.id.editTextMessage);
        bt1 = (Button) findViewById(R.id.button);

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Will now attempt to send an SMS", Toast.LENGTH_SHORT).show();

                String phoneNum = etPhone.getText().toString();
                String message = etMessage.getText().toString();

                sendSMS(phoneNum, message);
            }
        });

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
}
