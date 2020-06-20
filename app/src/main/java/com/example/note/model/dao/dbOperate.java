package com.example.note.model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.note.model.pojo.Note;

import java.util.ArrayList;
import java.util.List;

public class dbOperate {
    SQLiteOpenHelper dbhandler;
    SQLiteDatabase db;


    private static final String[] columns = {                     //以string的形式来保存传递信息
            DB.ID,
            DB.CONTENT,
            DB.TIME,
            DB.TAG
    };


    public dbOperate(Context context) {
        dbhandler = new DB(context);
    }


    public void open() {
        db = dbhandler.getWritableDatabase();                                           //(漏掉了db = 导致空指针异常)
    }               //打开和关闭数据库


    public void close() {
        dbhandler.close();
    }



    public Note addNote(Note note) {                               //将一个note添加到数据库
        ContentValues contentValues = new ContentValues();        //一个专门处理数据的类
        contentValues.put(DB.CONTENT, note.getContent());
        contentValues.put(DB.TIME, note.getTime());
        contentValues.put(DB.TAG, note.getTag());
        long ID = db.insert(DB.TABLE_NAME, null, contentValues);    //将信息插入数据库并返回id
        note.setId(ID);
        return note;
    }


    public Note getNote(long id) {                            //从数据库取出一条信息
        Cursor cursor = db.query(DB.TABLE_NAME, columns, DB.ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null) cursor.moveToFirst();
        Note e = new Note(cursor.getString(1), cursor.getString(2), cursor.getInt(3));
        return e;
    }


    public List<Note> getAllNotes(){                              //从数据库取出所有信息
        Cursor cursor = db.query(DB.TABLE_NAME,columns,null,null,null,null,null,null);
        List<Note> notes = new ArrayList<>();
        if(cursor.getCount()>0){
            while (cursor.moveToNext()){
                Note note = new Note();
                note.setId(cursor.getLong(cursor.getColumnIndex(DB.ID)));
                note.setContent(cursor.getString(cursor.getColumnIndex(DB.CONTENT)));
                note.setTime(cursor.getString(cursor.getColumnIndex(DB.TIME)));
                note.setTag(cursor.getInt(cursor.getColumnIndex(DB.TAG)));
                notes.add(note);                                      //将从数据库中取出的信息保存在notes中
            }
        }
        return notes;
    }


    public int updateNote(Note note){                            //更新一条数据
        ContentValues contentValues = new ContentValues();
        contentValues.put(DB.CONTENT,note.getContent());
        contentValues.put(DB.TIME,note.getTime());
        contentValues.put(DB.TAG,note.getTag());
        return db.update(DB.TABLE_NAME,contentValues,DB.ID + "=?", new String[]{ String.valueOf(note.getId()) });
    }

    public void ReomveNote(Note note){
        db.delete(DB.TABLE_NAME,DB.ID + "=" + note.getId(),null);
    }
}
