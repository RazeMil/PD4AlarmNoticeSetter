
package com.myapplicationdev.android.pd4_alarm_notice_setter;

import android.app.Activity;
import android.app.AlarmManager;
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

public class ModifyAlarm extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener{

    EditText /*etID,*/ etTitle, etDesc;
    Button btnUpdate, btnDelete, btnCancel, btnTimer;
    TextView tvTimeShow;
    Calendar calendar;
    int currentHour;
    int currentMinute;
    int reqCode = 12345;
    String amPM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_alarm);


      /*  etID = findViewById(R.id.etID);*/
        etTitle = findViewById(R.id.etTitle);
        etDesc = findViewById(R.id.etDesc);
        btnTimer = findViewById(R.id.btnTimer);
        btnCancel = findViewById(R.id.btnCancel);
        btnDelete = findViewById(R.id.btnDelete);
        btnUpdate = findViewById(R.id.btnUpdate);
        tvTimeShow = findViewById(R.id.tvTimeshow);


        Intent intentReceived = getIntent();
        final Task task = (Task) intentReceived.getSerializableExtra("alarm");

     /*   etID.setEnabled(false);
        etID.setText(task.getId() + "");*/
        etTitle.setText(task.getTask());
        etDesc.setText(task.getDescription());


        btnTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker 2");
            }

        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Title = etTitle.getText().toString();
                String Desc = etDesc.getText().toString();
                if (Title.length() == 0 || Desc.length() == 0) {
                    Toast.makeText(getBaseContext(), "All fields cannot be empty", Toast.LENGTH_SHORT).show();
                } else {
                    DBHelper db = new DBHelper(ModifyAlarm.this);
                    db.updateTask(new Task(task.getId(), Title, Desc));
                    Toast.makeText(getBaseContext(), "Updated", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent();
                    setResult(RESULT_OK, i);
                    finish();
                }
            }

        });


        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper db = new DBHelper(ModifyAlarm.this);
                db.deleteTask(task.getId());
                Intent i = new Intent();
                setResult(RESULT_OK, i);
                finish();
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

        tvTimeShow.setText(timeText);
    }

    private void startAlarm (final Calendar c){
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etTitle.getText().toString();
                String desc = etDesc.getText().toString();


                if (name.length() == 0) {
                    Toast.makeText(getBaseContext(), "Name cannot be empty", Toast.LENGTH_LONG).show();
                } else if (desc.length() == 0) {
                    Toast.makeText(getBaseContext(), "Description cannot be empty", Toast.LENGTH_LONG).show();
                } else {

                    DBHelper db = new DBHelper(ModifyAlarm.this);
                    db.insertTask(name, desc);

                    Intent intent = new Intent(ModifyAlarm.this,
                            ScheduledNotificationReceiver.class);
                    intent.putExtra("name", name);

                    PendingIntent pendingIntent = PendingIntent.getBroadcast(
                            ModifyAlarm.this, reqCode,
                            intent, PendingIntent.FLAG_CANCEL_CURRENT);

                    AlarmManager am = (AlarmManager)
                            getSystemService(Activity.ALARM_SERVICE);
                    am.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
                    Toast.makeText(getBaseContext(), "Task Inserted", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });
}}
