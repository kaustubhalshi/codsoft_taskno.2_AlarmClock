package com.example.alarmclock;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.alarmclock.CustomClockView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView tvCurrentTime;
    private TextView tvCurrentDate;
    private CustomClockView clockView;
    private Handler handler = new Handler();
    private Runnable timeUpdater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvCurrentTime = findViewById(R.id.tvCurrentTime);
        tvCurrentDate = findViewById(R.id.tvCurrentDate);
        clockView = findViewById(R.id.clockView);
        Button btnSetAlarm = findViewById(R.id.btnSetAlarm);
        Button btnViewAlarms = findViewById(R.id.btnViewAlarms);

        updateTimeAndDate();

        btnSetAlarm.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SetAlarmActivity.class);
            startActivity(intent);
        });

        btnViewAlarms.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AlarmManagementActivity.class);
            startActivity(intent);
        });
    }

    private void updateTimeAndDate() {
        timeUpdater = new Runnable() {
            @Override
            public void run() {
                String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
                tvCurrentTime.setText(currentTime);

                String currentDate = new SimpleDateFormat("EEEE, d MMMM", Locale.getDefault()).format(new Date());
                tvCurrentDate.setText(currentDate);

                if (clockView != null) {
                    clockView.invalidate();
                }

                handler.postDelayed(this, 60000);
            }
        };
        handler.post(timeUpdater);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(timeUpdater);
    }
}
