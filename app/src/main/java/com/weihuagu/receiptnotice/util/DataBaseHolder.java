package com.weihuagu.receiptnotice.util;

import android.database.sqlite.SQLiteDatabase;

import com.weihuagu.receiptnotice.DatabaseHelper;
import com.weihuagu.receiptnotice.MainApplication;

public class DataBaseHolder {
    //创建 SingleObject 的一个对象
    private static DataBaseHolder instance = new DataBaseHolder();
    public DatabaseHelper dbHelper;
    public SQLiteDatabase sqliteDatabase;

    //让构造函数为 private，这样该类就不会被实例化
    private DataBaseHolder(){
        createDataBase();
    }

    //获取唯一可用的对象
    public static DataBaseHolder getInstance(){
        return instance;
    }

    private void createDataBase(){
        dbHelper = new DatabaseHelper(MainApplication.getAppContext(),"receiptnotice",null,1);
        sqliteDatabase = dbHelper.getWritableDatabase();
    }

    public SQLiteDatabase getDateBase(){
        return sqliteDatabase;
    }
}
