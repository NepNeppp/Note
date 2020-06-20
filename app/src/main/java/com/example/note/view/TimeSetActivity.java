package com.example.note.view;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.note.R;

import java.util.Calendar;

public class TimeSetActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timeset_layout);
        final TimePicker timePicker = findViewById(R.id.timepicker);
        timePicker.setIs24HourView(true);                        //设置24小时制

        Button button = findViewById(R.id.btn_settime);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //设置闹钟
                Intent intent = new Intent(TimeSetActivity.this, AlarmActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(TimeSetActivity.this,0,intent,0);  //获取显示闹钟的PendingIntent对象
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                long test = calendar.getTimeInMillis();
                calendar.set(Calendar.HOUR_OF_DAY,timePicker.getHour());
                calendar.set(Calendar.MINUTE,timePicker.getMinute());
                calendar.set(Calendar.SECOND,0);                                           //设置闹钟的秒数为0秒，不然默认为创建calendar
                alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
                Toast.makeText(TimeSetActivity.this, "涅普！我会准时提醒你的！", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
