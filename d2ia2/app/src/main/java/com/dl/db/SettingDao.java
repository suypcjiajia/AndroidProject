package com.dl.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dl.com.dl.bo.SettingBo;
import com.dl.com.dl.bo.TestRecord;

/**
 * 对参数配置表的读写(增删改查)
 */
public class SettingDao {


    private MyDbHelper myDbHelper;

    public SettingDao(Context context){
        myDbHelper = new MyDbHelper(context);
    }


    /**
     * 插入一条
     * @param obj
     * @return
     */
    public  long insert(SettingBo obj){
        SQLiteDatabase db= myDbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", obj.id);
        contentValues.put("value",obj.value);
        return db.replace(MyDbHelper.SETTING_TABLE,null,contentValues);
    }

    /**
     * 获取一条
     * @param id 项
     * @return
     */
    public SettingBo getOne(String id) {


        SQLiteDatabase db = myDbHelper.getReadableDatabase();
        String args[] = new String[1];
        args[0] = id;

        Cursor cursor;
        try {
            cursor = db.query(MyDbHelper.SETTING_TABLE,
                    null, "id=?",
                    args, null, null, null, null);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
        if (cursor.getCount() > 0) {

            SettingBo obj = new SettingBo();
            while (cursor.moveToNext()) {
                int pos = -1;
                pos = cursor.getColumnIndex("id");
                if(pos != -1){
                    obj.id =   cursor.getString(pos);
                }

                pos = cursor.getColumnIndex("value");
                if(pos != -1){
                    obj.value =   cursor.getString(pos);
                }


                break;
            }
            return obj;

        }
        return null;
    }

}
