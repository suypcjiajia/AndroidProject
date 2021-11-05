package db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDbHelper extends SQLiteOpenHelper {


    private static final String DB_NAME = "bcphone.db";
    public static final String DEVICE_TABLE = "device";


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


        String sql = "create table if not exists "  + DEVICE_TABLE + " " +
                "(" +
                "id varchar(64) PRIMARY KEY," +
                "ip varchar(64) ," +
                "msg text" +  //值
                ")";
        try {
            System.out.println(sql);
            db.execSQL(sql);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return;
        }
    }


}
