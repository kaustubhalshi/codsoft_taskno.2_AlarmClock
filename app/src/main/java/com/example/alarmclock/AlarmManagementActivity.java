package com.example.alarmclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class AlarmManagementActivity extends AppCompatActivity {

    private ListView alarmListView;
    private ArrayAdapter<String> alarmAdapter;
    private ArrayList<String> alarmList;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onResume() {
        super.onResume();
        loadAlarms();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_management);

        alarmListView = findViewById(R.id.alarmListView);
        sharedPreferences = getSharedPreferences("AlarmPrefs", Context.MODE_PRIVATE);

        loadAlarms();

        alarmListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String alarmTime = alarmList.get(position);
                cancelAlarm(alarmTime);
                removeAlarm(position);
            }
        });
    }

    private void loadAlarms() {
        Set<String> alarms = sharedPreferences.getStringSet("alarms", new HashSet<>());
        alarmList = new ArrayList<>(alarms);
        alarmAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, alarmList);
        alarmListView.setAdapter(alarmAdapter);
    }

    private void removeAlarm(int position) {
        String alarmTime = alarmList.get(position);
        alarmList.remove(position);
        alarmAdapter.notifyDataSetChanged();

        // Update SharedPreferences
        Set<String> updatedAlarms = new HashSet<>(alarmList);
        sharedPreferences.edit().putStringSet("alarms", updatedAlarms).apply();

        Toast.makeText(this, "Alarm for " + alarmTime + " removed", Toast.LENGTH_SHORT).show();
    }

    private void cancelAlarm(String alarmTime) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        int requestCode = alarmTime.hashCode();
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        alarmManager.cancel(pendingIntent);
    }
}
