package db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import entity.DeviceEntity;

/**
 * 对设备表的读写(增删改查)
 */
public class DeviceDao {


    private MyDbHelper myDbHelper;

    public DeviceDao(Context context){
        myDbHelper = new MyDbHelper(context);
    }


    /**
     * 插入一条
     * @param obj
     * @return
     */
    public  long insert(DeviceEntity obj){
        SQLiteDatabase db= myDbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", obj.Id);
        contentValues.put("ip",obj.ip);
        contentValues.put("msg",obj.msg);
        return db.replace(MyDbHelper.DEVICE_TABLE,null,contentValues);
    }

    /**
     * 获取一条
     * @param id 项
     * @return
     */
    public DeviceEntity getOne(String id) {


        SQLiteDatabase db = myDbHelper.getReadableDatabase();
        String args[] = new String[1];
        args[0] = id;

        Cursor cursor;
        try {
            cursor = db.query(MyDbHelper.DEVICE_TABLE,
                    null, "id=?",
                    args, null, null, null, null);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
        if (cursor.getCount() > 0) {

            DeviceEntity obj = new DeviceEntity();
            while (cursor.moveToNext()) {
                int pos = -1;
                pos = cursor.getColumnIndex("id");
                if(pos != -1){
                    obj.Id =   cursor.getString(pos);
                }

                pos = cursor.getColumnIndex("ip");
                if(pos != -1){
                    obj.ip =   cursor.getString(pos);
                }

                pos = cursor.getColumnIndex("msg");
                if(pos != -1){
                    obj.msg =   cursor.getString(pos);
                }


                break;
            }
            return obj;

        }
        return null;
    }

    public void clear(){
        SQLiteDatabase db = myDbHelper.getReadableDatabase();
        db.execSQL("delete from " + MyDbHelper.DEVICE_TABLE);
    }

}
