package com.example.note.model.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DB extends SQLiteOpenHelper {
    public static final String TABLE_NAME ="notes";
    public static final String ID ="_id";                 //习惯，表明主键
    public static final String CONTENT ="content";
    public static final String TIME ="time";
    public static final String TAG ="tag";

    public DB(Context context){
        super(context,TABLE_NAME,null,1);  //调用父类的构造方法
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME              //SQL语句一定要注意 空格
                + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + CONTENT + " TEXT NOT NULL,"
                + TIME + " TEXT NOT NULL,"
                + TAG + " INTEGER DEFAULT 1)"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
