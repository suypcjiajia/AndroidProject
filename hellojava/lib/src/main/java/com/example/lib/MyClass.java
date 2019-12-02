package com.example.lib;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class MyClass {
    public static void main(String[] args){
        System.out.println("hell world");
        String a = "suyp";
        String b = "suyp";
        String c = new String("suyp");
        System.out.println(c == b);
        // 1 获取或创建JSON数据
        String json = "{\n" +
                "\t\"id\":2, \"name\":\"金鱼\", \n" +
                "\t\"price\":12.3, \n" +
                "\t\"imagePath\":\"http://blog.csdn.net/qq_29269233/L05_Server/images/f1.jpg\"\n" +
                "}\n";


        JsonElement element = JsonParser.parseString(json);


    }
}
