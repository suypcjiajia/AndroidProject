package entity;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class ShareData extends Object {


    /**http返回值
     *
     */
    public static JsonArray getBaoYang;
    public static JsonArray getBoard;
    public static JsonObject getState;
    public static JsonArray getHistory;
    public static JsonObject getTongji;
    public static JsonObject getHouse;
    public static JsonObject getCurve = new JsonObject();


    //传递给曲线界面的参数
    public static  String PutInTime;
    public static  int HoleNum;
    public static  int ExtensionNum;

    public static String getMaching(int i){
        if(i == 0){
            return "主机";
        }else{
            return i + "仓";
        }
    }
}
