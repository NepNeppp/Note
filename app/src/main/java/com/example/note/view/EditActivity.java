package com.example.note.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.note.R;
import com.example.note.model.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;


public class EditActivity extends AppCompatActivity {

    EditText editText;
    int OpenMode;
    private String old_content;
    private long id;
    private Toolbar mytoolbar;
    private int Tag;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_layout);

        Intent intent = getIntent();
        OpenMode = intent.getIntExtra("mode",0);          //OpenMode为0表示新建，为1表示打开已存在

        editText = findViewById(R.id.edit);

        if(OpenMode == 1){                                                      //该条note已经存在,让其显示在editview中
            old_content = intent.getStringExtra("content");
            id = intent.getLongExtra("id",0);
            editText.setText(old_content);
            editText.setSelection(old_content.length());                        //光标在最后一行
        }

        mytoolbar = findViewById(R.id.myToolbar);

        setSupportActionBar(mytoolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);          //设置toolbar代替actionbar
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);     //透明，半沉浸式体验（笑）
//        mytoolbar.setPadding(0,mytoolbar.getPaddingTop()+50,mytoolbar.getPaddingRight(),mytoolbar.getPaddingBottom());

        mytoolbar.setNavigationOnClickListener(new View.OnClickListener() {            //设置菜单栏的edit返回main按钮
            @Override
            public void onClick(View v) {
                if (OpenMode == 1){                                                                 //参考onKeydown的操作，修改note
                    AlertDialog alertDialog = new AlertDialog.Builder(EditActivity.this).create();
                    alertDialog.setIcon(R.drawable.neptune);
                    alertDialog.setTitle("提示");
                    alertDialog.setMessage("是否保存为当前记录？");
                    alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "保存", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent();
                            intent.putExtra("id",id);
                            intent.putExtra("content",editText.getText().toString());
                            intent.putExtra("time",dateToStr());
                            intent.putExtra("mode",1);
                            setResult(RESULT_OK,intent);
                            finish();
                        }
                    });
                    alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "直接退出", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent();
                            intent.putExtra("content",editText.getText().toString());
                            intent.putExtra("time",dateToStr());
                            intent.putExtra("mode",-1);
                            setResult(RESULT_OK,intent);
                            finish();
                        }
                    });
                    alertDialog.show();
                }else  if(OpenMode == 0){                            //新建note
                    Intent intent = new Intent();
                    intent.putExtra("content",editText.getText().toString());
                    intent.putExtra("time",dateToStr());
                    intent.putExtra("mode",0);
                    setResult(RESULT_OK,intent);
                    finish();
                }else {                                             //直接退出
                    Intent intent = new Intent();
                    intent.putExtra("content",editText.getText().toString());
                    intent.putExtra("time",dateToStr());
                    intent.putExtra("mode",-1);
                    setResult(RESULT_OK,intent);
                    finish();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {                                                 //对菜单栏进行处理
        getMenuInflater().inflate(R.menu.edit_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==R.id.menu_delete){                                                     //删除功能
            AlertDialog alertDialog = new AlertDialog.Builder(EditActivity.this).create();
            alertDialog.setIcon(R.drawable.neptune);
            alertDialog.setMessage("确定要删除当前日志吗？");
            alertDialog.setTitle("删除");
            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "删除", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent();
                    intent.putExtra("id",id);
                    intent.putExtra("content",editText.getText().toString());
                    intent.putExtra("time",dateToStr());
                    if(OpenMode == 1){
                        intent.putExtra("mode",2);                 //2表示删除
                    }else {
                        intent.putExtra("mode",-1);
                    }
                    setResult(RESULT_OK,intent);
                    finish();
                }
            });
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alertDialog.show();
        }else if (item.getItemId()==R.id.menu_share){                                               //分享功能
            String msg = editText.getText().toString();
            startActivity(Intent.createChooser(new Utils().shareText(msg), "分享"));
        }else{                                                                                       //提醒功能,打TimeSetaAtivity
            Intent intent = new Intent(EditActivity.this,TimeSetActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_HOME){
            return true;
        }else  if(keyCode == KeyEvent.KEYCODE_BACK && OpenMode == 0){                             //新建note
            Intent intent = new Intent();
            intent.putExtra("content",editText.getText().toString());
            intent.putExtra("time",dateToStr());
            intent.putExtra("mode",0);
            setResult(RESULT_OK,intent);
            finish();
            return true;
        }else if (keyCode == KeyEvent.KEYCODE_BACK && OpenMode == 1){                               //打开已存在的note
            AlertDialog alertDialog = new AlertDialog.Builder(EditActivity.this).create();
            alertDialog.setIcon(R.drawable.neptune);
            alertDialog.setTitle("提示");
            alertDialog.setMessage("是否保存为当前记录？");
            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "保存", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent();
                    intent.putExtra("id",id);
                    intent.putExtra("content",editText.getText().toString());
                    intent.putExtra("time",dateToStr());
                    intent.putExtra("mode",1);
                    setResult(RESULT_OK,intent);
                    finish();
                }
            });
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "直接退出", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent();
                    intent.putExtra("content",editText.getText().toString());
                    intent.putExtra("time",dateToStr());
                    intent.putExtra("mode",-1);
                    setResult(RESULT_OK,intent);
                    finish();
                }
            });
            alertDialog.show();
        }
        return super.onKeyDown(keyCode,event);
    }

    public String dateToStr(){
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }
}
