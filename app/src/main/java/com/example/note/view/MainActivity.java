package com.example.note.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.note.controller.Compare_tag;
import com.example.note.controller.Compare_time;
import com.example.note.model.pojo.Note;
import com.example.note.controller.NoteAdapter;
import com.example.note.R;
import com.example.note.model.dao.DB;
import com.example.note.model.dao.dbOperate;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private DB db;                       //数据库

    private Context context = this;
    final String TAG = "tag";
    FloatingActionButton btn;
    private ListView listView;
    private NoteAdapter adapter;        //适配器
    private List<Note> noteList = new ArrayList<>();
    private Toolbar mytoolbar;         //顶部工具栏
    private boolean timeRank = false;  //时间逆序排序标记(默认不按时间逆序排序)


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        listView = findViewById(R.id.lv);
        listView.setOnItemClickListener(this);                     //给listview添加单击长按listener
        listView.setOnItemLongClickListener(this);
        adapter = new NoteAdapter(getApplicationContext(),noteList);     //创建note适配器并给listview使用
        RefreshListView();                                               //每次显示的时候刷新
        listView.setAdapter(adapter);

        mytoolbar = findViewById(R.id.myToolbar);

        setSupportActionBar(mytoolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);          //设置toolbar代替actionbar
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);     //透明，半沉浸式体验（笑）
//        mytoolbar.setPadding(0,mytoolbar.getPaddingTop()+50,mytoolbar.getPaddingRight(),mytoolbar.getPaddingBottom());


        mytoolbar.setNavigationIcon(R.drawable.ic_sort_black_24dp);

        mytoolbar.setNavigationOnClickListener(new View.OnClickListener() {               //左上角的按钮：设置mote排序方式（时间顺序和新建顺序）
            @Override
            public void onClick(View v) {
                timeRank = !timeRank;
                if (!timeRank){
                    Toast.makeText(MainActivity.this, "按最初创建时间排序", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MainActivity.this, "按最后修改时间排序", Toast.LENGTH_SHORT).show();
                }
                RefreshListView();
            }
        });


        btn =findViewById(R.id.floatingActionButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,EditActivity.class);
                intent.putExtra("mode",0);                                            //表示新建note
                startActivityForResult(intent,0);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {                                                 //对查找菜单栏进行处理
        getMenuInflater().inflate(R.menu.main_menu,menu);

        MenuItem mSearch = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) mSearch.getActionView();

        searchView.setQueryHint("查找日志");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_clean){
            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setIcon(R.drawable.neptune);
            alertDialog.setMessage("全部删除吗？");
            alertDialog.setTitle("清空");
            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "全部删除", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    db = new DB(context);
                    SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
                    sqLiteDatabase.delete("notes",null,null);
                    RefreshListView();
                }
            });
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alertDialog.show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {       //将编辑完返回的note添加到数据库
        super.onActivityResult(requestCode, resultCode, data);
        String content = data.getStringExtra("content");
        String time = data.getStringExtra("time");
        int CloseMode = data.getIntExtra("mode",-1);                             //CloseMode为0表示需要新增（顺序插入），1表示需要修改（id不变），默认为-1（什么都不干）
        Note note = new Note(content,time,1);
        if(note.getContent().trim().length()!=0 && CloseMode == 0){                                                   //拒绝 空格串！！！！
            dbOperate operate = new dbOperate(context);
            operate.open();                                                                         //打开插入关闭一套操作行云流水
            operate.addNote(note);
            operate.close();
            RefreshListView();
        }else if(note.getContent().trim().length()!=0 && CloseMode == 1){                           //需要保存当前note（不改变id）
            note.setId(data.getLongExtra("id",0));
            dbOperate operate = new dbOperate(context);
            operate.open();
            operate.updateNote(note);
            operate.close();
            RefreshListView();
        }else if (CloseMode == 2){                                                                  //删除一条note
            note.setId(data.getLongExtra("id",0));
            dbOperate operate = new dbOperate(context);
            operate.open();
            operate.ReomveNote(note);
            operate.close();
            RefreshListView();
        }else if(CloseMode != -1){                     //CloseCode为-1表示不进行如何操作，此时不是-1，只能为空格串
            Toast.makeText(MainActivity.this,"输入信息不能为空哦~",Toast.LENGTH_SHORT).show();
        }
    }

    public void RefreshListView(){                                                                  //刷新
        dbOperate operate = new dbOperate(context);
        if(noteList.size()>0) noteList.clear();
        operate.open();
        noteList.addAll(operate.getAllNotes());
        operate.close();
        if(timeRank){                                                                   //比较time进行时间排序（要先于tag排序，因为tag排序优先级更高）
            Compare_time compare_time = new Compare_time();
            Collections.sort(noteList,compare_time);
        }
        Compare_tag compare_tag = new Compare_tag();                                                   //比较tag进行优先级排序
        Collections.sort(noteList,compare_tag);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {              //单击打开 position为排列位置  id主键
        Note curNote = (Note) parent.getItemAtPosition(position);
        Intent intent = new Intent(MainActivity.this,EditActivity.class);
        intent.putExtra("content",curNote.getContent());
        intent.putExtra("time",curNote.getTime());
        intent.putExtra("id",curNote.getId());
        intent.putExtra("mode",1);                                                     //表示打开已有的note
        startActivityForResult(intent,1);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {           //设置长按置顶
        if(noteList.get(position).getTag() == 1){
            noteList.get(position).setTag(-1);
            Toast.makeText(MainActivity.this, "成功置顶~", Toast.LENGTH_SHORT).show();
        }else {
            noteList.get(position).setTag(1);
            Toast.makeText(MainActivity.this, "取消置顶", Toast.LENGTH_SHORT).show();
        }
        dbOperate operate = new dbOperate(context);
        operate.open();
        operate.updateNote(noteList.get(position));
        operate.close();
        RefreshListView();
        return false;
    }

}
