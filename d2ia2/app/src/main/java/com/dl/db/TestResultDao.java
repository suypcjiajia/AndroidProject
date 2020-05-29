package com.dl.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dl.com.dl.bo.TestRecord;

/**
 * 对检测结果表的读写(增删改查)
 */
public class TestResultDao {

    public static TestRecord testRecord = new TestRecord();

    private MyDbHelper myDbHelper;

    public TestResultDao(Context context){
        myDbHelper = new MyDbHelper(context);
    }


    /**
     * 插入一条检测结果
     * @param testRecord
     * @return
     */
    public  long insert(TestRecord testRecord){
        SQLiteDatabase db= myDbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", testRecord.id);
        contentValues.put("name",testRecord.name);
        contentValues.put("testMan",testRecord.testMan);
        contentValues.put("age",testRecord.age);
        contentValues.put("sex",testRecord.sex);
        String tmp = new String();
        for( int i = 0; i < testRecord.curver.size();  i++){
            tmp += testRecord.curver.get(i);
            if( i != testRecord.curver.size() -1){
                tmp += ";";
            }
        }
        contentValues.put("curver",tmp);
        return db.insert(MyDbHelper.TEST_RESULT_TABLE,null,contentValues);
    }

    /**
     * 获取一条检测结果
     * @param index 基于0的偏移
     * @return
     */
    public TestRecord getOne(int index) {


        String  sindex = Integer.toString(index);
        sindex +=",1";
        SQLiteDatabase db = myDbHelper.getReadableDatabase();

        Cursor cursor;
        try {
            cursor = db.query(MyDbHelper.TEST_RESULT_TABLE,
                    null, null,
                    null, null, null, "incrd DESC", sindex);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
        if (cursor.getCount() > 0) {

            TestRecord testRecord = new TestRecord();
            while (cursor.moveToNext()) {
                int pos = -1;
                pos = cursor.getColumnIndex("id");
                if(pos != -1){
                    testRecord.id =   cursor.getString(pos);
                }

                pos = cursor.getColumnIndex("name");
                if(pos != -1){
                    testRecord.name =   cursor.getString(pos);
                }


                pos = cursor.getColumnIndex("testMan");
                if(pos != -1){
                    testRecord.testMan =   cursor.getString(pos);
                }


                pos = cursor.getColumnIndex("age");
                if(pos != -1){
                    testRecord.age =   cursor.getString(pos);
                }

                pos = cursor.getColumnIndex("sex");
                if(pos != -1){
                    testRecord.sex =   cursor.getString(pos);
                }

                pos = cursor.getColumnIndex("curver");
                if(pos != -1){
                    String tmp =   cursor.getString(pos);
                    String[] elems  = tmp.split(";");
                    for( int i = 0; i < elems.length; i++){
                        if( elems[i].isEmpty()){
                            continue;
                        }
                        testRecord.curver.add(elems[i]);
                    }
                }
                break;
            }
            return testRecord;

        }
        return null;
    }

    /**
     * 获取总条数
     * @return
     */
    public int count(){
        SQLiteDatabase db = myDbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select count(*) as cnt from " + MyDbHelper.TEST_RESULT_TABLE ,null);
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
}
