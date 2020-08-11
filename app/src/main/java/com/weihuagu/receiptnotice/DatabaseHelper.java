package com.weihuagu.receiptnotice;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    //数据库版本号
    private static Integer Version = 1;

    public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String plat = "create table plat(id integer primary key autoincrement,name varchar(64),address varchar(64))";
        String pushrecord = "create table pushrecord(id integer primary key autoincrement,type varchar(64),time varchar(64),title varchar(64),money varchar(64),content varchar(64))";

        db.execSQL(plat);
        db.execSQL(pushrecord);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
