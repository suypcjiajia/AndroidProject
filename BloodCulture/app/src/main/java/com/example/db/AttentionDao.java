package com.example.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.bo.AttentionRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * 对关注表的读写(增删改查)
 */
public class AttentionDao {

    public static AttentionRecord attentionRecord = new AttentionRecord();

    private MyDbHelper myDbHelper;

    public AttentionDao(Context context){
        myDbHelper = new MyDbHelper(context);
    }


    /**
     * 插入一条
     * @param testRecord
     * @return
     */
    public  long insert(AttentionRecord testRecord){
        SQLiteDatabase db= myDbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", testRecord.id);
        contentValues.put("addTime",testRecord.addTime);
        return db.insert(MyDbHelper.ATTENTION_TABLE,null,contentValues);
    }

    /**
     * 获取一条
     * @param index 基于0的偏移
     * @return
     */
    public AttentionRecord getOne(int index) {


        String  sindex = Integer.toString(index);
        sindex +=",1";
        SQLiteDatabase db = myDbHelper.getReadableDatabase();

        Cursor cursor;
        try {
            cursor = db.query(MyDbHelper.ATTENTION_TABLE,
                    null, null,
                    null, null, null, "incrd DESC", sindex);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
        if (cursor.getCount() > 0) {

            AttentionRecord testRecord = new AttentionRecord();
            while (cursor.moveToNext()) {
                int pos = -1;
                pos = cursor.getColumnIndex("id");
                if(pos != -1){
                    testRecord.id =   cursor.getString(pos);
                }

                pos = cursor.getColumnIndex("addTime");
                if(pos != -1){
                    testRecord.addTime =   cursor.getString(pos);
                }

                break;
            }
            return testRecord;

        }
        return null;
    }

    /**
     * 获取所有
     * @return
     */
    public List<AttentionRecord> getAll(){

        List<AttentionRecord> tmp = new  ArrayList<>();

        SQLiteDatabase db = myDbHelper.getReadableDatabase();

        Cursor cursor;
        try {
            cursor = db.query(MyDbHelper.ATTENTION_TABLE,
                    null, null,
                    null, null, null, "addTime DESC", null);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return tmp;
        }
        if (cursor.getCount() > 0) {


            while (cursor.moveToNext()) {
                AttentionRecord testRecord = new AttentionRecord();
                int pos = -1;
                pos = cursor.getColumnIndex("id");
                if(pos != -1){
                    testRecord.id =   cursor.getString(pos);
                }

                pos = cursor.getColumnIndex("addTime");
                if(pos != -1){
                    testRecord.addTime =   cursor.getString(pos);
                }

                tmp.add(testRecord);

            }

        }
        return tmp;
    }

    /**
     * 获取总条数
     * @return
     */
    public int count(){
        SQLiteDatabase db = myDbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select count(*) as cnt from " + MyDbHelper.ATTENTION_TABLE ,null);
        if( cursor.getCount() > 0){

            while (cursor.moveToNext()) {
                int pos = cursor.getColumnIndex("cnt");
                if(pos != -1){
                    return   cursor.getInt(pos);
                }
            }


        }
        return  0;
    }

    public  boolean delete(String id){
        SQLiteDatabase db = myDbHelper.getReadableDatabase();
        String where = "id='" + id + "'";
        int num = db.delete(MyDbHelper.ATTENTION_TABLE,where,null) ;
        if(num >= 0){
            return true;
        }else{
            return false;
        }
    }
}
