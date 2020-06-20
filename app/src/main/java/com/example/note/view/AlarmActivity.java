package com.example.note.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.note.R;

public class AlarmActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_layout);
        View view = findViewById(R.id.alarmlayout);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setIcon(R.drawable.neptune);
        dialog.setTitle("涅普普的准时提醒！");
        dialog.setMessage("时间到！");
        dialog.setButton(DialogInterface.BUTTON_POSITIVE,"知道了",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                finish();
            }
        });
        dialog.show();
    }
}
