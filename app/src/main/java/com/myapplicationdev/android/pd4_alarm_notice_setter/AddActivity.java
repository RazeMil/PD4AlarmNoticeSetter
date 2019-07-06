package com.myapplicationdev.android.pd4_alarm_notice_setter;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;

public class AddActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener{

    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;

    TextView tvTime;
    EditText etName, etDesc;
    Button btnAddTask, btnCancel, btnTime;
    Calendar calendar;
    int currentHour;
    int currentMinute;
    int reqCode = 12345;
    String amPM;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        etName = findViewById(R.id.editTextName);
        etDesc = findViewById(R.id.editTextDesc);
        tvTime = findViewById(R.id.tvTime);
        btnTime = findViewById(R.id.btnTime);
        btnAddTask = findViewById(R.id.buttonAdd);
        btnCancel = findViewById(R.id.buttonCancel);



        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }

        });



        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);

        updateTimeText(c);
        startAlarm(c);
    }
    private void updateTimeText(Calendar c) {
        String timeText = "Alarm set for: ";
        timeText += DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());

        tvTime.setText(timeText);
    }

    private void startAlarm (final Calendar c){
    btnAddTask.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String name = etName.getText().toString();
            String desc = etDesc.getText().toString();


            if (name.length() == 0) {
                Toast.makeText(getBaseContext(), "Name cannot be empty", Toast.LENGTH_LONG).show();
            } else if (desc.length() == 0) {
                Toast.makeText(getBaseContext(), "Description cannot be empty", Toast.LENGTH_LONG).show();
            } else {

                DBHelper db = new DBHelper(AddActivity.this);
                db.insertTask(name, desc);

                    Intent intent = new Intent(AddActivity.this,
                            ScheduledNotificationReceiver.class);
                    intent.putExtra("name", name);

                    PendingIntent pendingIntent = PendingIntent.getBroadcast(
                            AddActivity.this, reqCode,
                            intent, PendingIntent.FLAG_CANCEL_CURRENT);

                    AlarmManager am = (AlarmManager)
                            getSystemService(Activity.ALARM_SERVICE);
                    am.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
                    Toast.makeText(getBaseContext(), "Task Inserted", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });
    }
}

