package com.myapplicationdev.android.pd4_alarm_notice_setter;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class AddActivity extends AppCompatActivity {

    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;

    EditText etName, etDesc, etTime;
    Button btnAddTask, btnCancel;
    Calendar calendar;
    int currentHour;
    int currentMinute;
    int reqCode = 12345;
    String amPM;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        etName = findViewById(R.id.editTextName);
        etDesc = findViewById(R.id.editTextDesc);

        etTime = findViewById(R.id.etTime);

        btnAddTask = findViewById(R.id.buttonAdd);
        btnCancel = findViewById(R.id.buttonCancel);


        btnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();
                String desc = etDesc.getText().toString();
                String time = etTime.getText().toString();
                if (name.length() == 0) {
                    Toast.makeText(getBaseContext(), "Name cannot be empty", Toast.LENGTH_LONG).show();
                } else if (desc.length() == 0) {
                    Toast.makeText(getBaseContext(), "Description cannot be empty", Toast.LENGTH_LONG).show();
                } else {
                    DBHelper db = new DBHelper(AddActivity.this);
                    db.insertTask(name, desc);
                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.SECOND,( Integer.parseInt(time))*60);

                    Intent intent = new Intent(AddActivity.this,
                            ScheduledNotificationReceiver.class);
                    intent.putExtra("name", name);

                    PendingIntent pendingIntent = PendingIntent.getBroadcast(
                            AddActivity.this, reqCode,
                            intent, PendingIntent.FLAG_CANCEL_CURRENT);

                    AlarmManager am = (AlarmManager)
                            getSystemService(Activity.ALARM_SERVICE);
                    am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
                    Toast.makeText(getBaseContext(), "Task Inserted", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
