package com.example.alarmclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

public class AlarmRingActivity extends AppCompatActivity {

    private static final int SNOOZE_DELAY_MINUTES = 5;
    private MediaPlayer mediaPlayer;
    private String currentAlarmTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_ring);

        mediaPlayer = MediaPlayer.create(this, R.raw.alarm_tone);
        mediaPlayer.start();

        currentAlarmTime = getIntent().getStringExtra("alarmTime");

        Button btnStopAlarm = findViewById(R.id.btnStop);
        Button btnSnoozeAlarm = findViewById(R.id.btnSnoozeAlarm);

        btnStopAlarm.setOnClickListener(v -> {
            stopAlarm();
            Toast.makeText(AlarmRingActivity.this, "Alarm Stopped", Toast.LENGTH_SHORT).show();
            finish();
        });

        btnSnoozeAlarm.setOnClickListener(v -> {
            snoozeAlarm();
            Toast.makeText(AlarmRingActivity.this, "Alarm Snoozed for 5 minutes", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void stopAlarm() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void snoozeAlarm() {
        stopAlarm();

        Calendar snoozeTime = Calendar.getInstance();
        snoozeTime.add(Calendar.MINUTE, SNOOZE_DELAY_MINUTES);

        int hour = snoozeTime.get(Calendar.HOUR_OF_DAY);
        int minute = snoozeTime.get(Calendar.MINUTE);
        String newAlarmTime = String.format("%02d:%02d", hour, minute);

        SharedPreferences sharedPreferences = getSharedPreferences("AlarmPrefs", Context.MODE_PRIVATE);
        Set<String> alarms = sharedPreferences.getStringSet("alarms", new HashSet<>());

        if (currentAlarmTime != null && alarms != null) {
            alarms.remove(currentAlarmTime);
            alarms.add(newAlarmTime);
            sharedPreferences.edit().putStringSet("alarms", alarms).apply();
        }

        Intent intent = new Intent(this, AlarmRingActivity.class);
        intent.putExtra("alarmTime", newAlarmTime);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, snoozeTime.getTimeInMillis(), pendingIntent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopAlarm();
    }
}
