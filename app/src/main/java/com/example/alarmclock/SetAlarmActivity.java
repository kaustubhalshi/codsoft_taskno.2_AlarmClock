package com.example.alarmclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

public class SetAlarmActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private TimePicker timePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alarm);

        timePicker = findViewById(R.id.timePicker);
        Button btnSaveAlarm = findViewById(R.id.btnSaveAlarm);
        Button btnChooseTone = findViewById(R.id.btnChooseTone);
        sharedPreferences = getSharedPreferences("AlarmPrefs", Context.MODE_PRIVATE);

        btnSaveAlarm.setOnClickListener(v -> setAlarm());
        btnChooseTone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SetAlarmActivity.this, "Coming soon", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setAlarm() {
        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();

        String alarmTime = String.format("%02d:%02d", hour, minute);

        Set<String> alarms = sharedPreferences.getStringSet("alarms", new HashSet<>());
        alarms.add(alarmTime);
        sharedPreferences.edit().putStringSet("alarms", alarms).apply();


        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);


        int requestCode = alarmTime.hashCode();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }

        Toast.makeText(this, "Alarm set for " + alarmTime, Toast.LENGTH_SHORT).show();
        finish();
    }
}
