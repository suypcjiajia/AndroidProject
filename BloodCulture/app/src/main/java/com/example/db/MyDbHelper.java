package com.example.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDbHelper extends SQLiteOpenHelper {


    private static final String DB_NAME = "BloodCulture.db";
    public static final String ATTENTION_TABLE = "attention";


    public MyDbHelper(Context context) {
        super(context, DB_NAME, null, 1);

    }

    public void onOpen(SQLiteDatabase db) {
        crateTable(db);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        crateTable(sqLiteDatabase);
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
    }


    private void crateTable(SQLiteDatabase db){
        String sql = "create table if not exists "  + ATTENTION_TABLE + " " +
                "(" +
                "incrd INTEGER PRIMARY KEY AUTOINCREMENT," +
                "id varchar(64)," + //标本编号
                "addTime varchar(24)" +  //添加时间
                ")";
        try {
            System.out.println(sql);
            db.execSQL(sql);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }


}
