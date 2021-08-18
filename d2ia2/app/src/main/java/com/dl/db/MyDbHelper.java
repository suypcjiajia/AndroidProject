package com.dl.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDbHelper extends SQLiteOpenHelper {


    private static final String DB_NAME = "d2ia.db";
    public static final String TEST_RESULT_TABLE = "TestResult";
    public static final String SETTING_TABLE = "Setting";


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
        String sql = "create table if not exists "  + TEST_RESULT_TABLE + " " +
                "(" +
                "incrd INTEGER PRIMARY KEY AUTOINCREMENT," +
                "id varchar(64)," + //标本编号
                "project varchar(64)," + //检测项目
                "piChi varchar(64)," + //批次编码
                "cheLianZhi double," + //测量值
                "nonDu double," + //浓度
                "result varchar(32)," + //结论
                "testTime varchar(24)," + //检测时间
                "unit varchar(64)," + //单位
                "canKaoZhi double," + //参考值
                "name varchar(32)," + //姓名
                "sex varchar(8)," + //性别
                "age int," + //年龄
                "testMan varchar(32)," + //检测员
                "curver text" +  //曲线数据
                ")";
        try {
            System.out.println(sql);
            db.execSQL(sql);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return;
        }

        String sql2 = "create table if not exists "  + SETTING_TABLE + " " +
                "(" +
                "id varchar(64) PRIMARY KEY," + //项
                "value text" +  //值
                ")";
        try {
            System.out.println(sql);
            db.execSQL(sql2);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return;
        }
    }


}
