package com.example.tool;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

/**
 * http接口请求
 */
public final class Http {

    //private static final String ipaddr ="192.168.0.91:1874";
    private static final String ipaddr = "120.196.141.28:1874";

    /**
     * 登录验证
     * @param name
     * @param pwd
     * @return
     */
    public static JsonObject login(String name,String pwd){
        try {
            JsonObject json = new JsonObject();
            json.addProperty("name", name);
            json.addProperty("password",  Encrypt.md5(pwd));
            String response = sendPost("http://" + ipaddr + "/user/login", json.toString());
            System.out.println(response);
            JsonElement element = JsonParser.parseString(response);

            JsonObject jsonRes =  element.getAsJsonObject();
            return jsonRes;

        }catch (IllegalStateException e){
            return makeExpetion(e);
        }
    }

    /**
     * 在线设备个数信息
     * @return
     */
    public static JsonObject onlinesummary(){
        try {
            String response = sendPost("http://" + ipaddr + "/list/onlinesummary", "");
            System.out.println(response);
            JsonElement element = JsonParser.parseString(response);

            JsonObject jsonRes =  element.getAsJsonObject();
            return jsonRes;

        }catch (IllegalStateException e){
            return makeExpetion(e);
        }
    }

    /**
     * 异常统计
     * @return
     */
    public static JsonObject exceptionsummary(){
        try {
            String response = sendPost("http://" + ipaddr + "/exception/summary", "");
            System.out.println(response);
            JsonElement element = JsonParser.parseString(response);

            JsonObject jsonRes =  element.getAsJsonObject();
            return jsonRes;

        }catch (IllegalStateException e){
            return makeExpetion(e);
        }
    }

    /**
     * 获取合计全部设备硬盘空间信息
     * @return
     */
    public static JsonObject alldisksummary(){
        try {
            String response = sendPost("http://" + ipaddr + "/list/alldisksummary", "");
            System.out.println(response);
            JsonElement element = JsonParser.parseString(response);

            JsonObject jsonRes =  element.getAsJsonObject();
            return jsonRes;

        }catch (IllegalStateException e){
            return makeExpetion(e);
        }
    }

    /**
     * 获取血培养保阳列表（服务器按时间排序返回数据）
     * @param date
     * @return
     */
    public static JsonObject bchistory(String date){
        try {
            JsonObject json = new JsonObject();
            json.addProperty("datetime", date);
            String response = sendPost("http://" + ipaddr + "/alert/get/bchistory", json.toString());
            System.out.println(response);
            JsonElement element = JsonParser.parseString(response);

            JsonObject jsonRes =  element.getAsJsonObject();
            return jsonRes;

        }catch (IllegalStateException e){
            return makeExpetion(e);
        }
    }

    /**
     * 获取区域下面的医院列表
     * @param city
     * @return
     */
    public static JsonObject getHospitalByCity(String province,String city,String country){
        try {
            JsonObject json = new JsonObject();
            json.addProperty("Country", country);
            json.addProperty("Province", province);
            json.addProperty("City", city);
            String response = sendPost("http://" + ipaddr + "/hospital/listbycity", json.toString());
            System.out.println(response);
            JsonElement element = JsonParser.parseString(response);

            JsonObject jsonRes =  element.getAsJsonObject();
            return jsonRes;

        }catch (IllegalStateException e){
            return makeExpetion(e);
        }
    }

    /**
     * 向指定URL发送GET方法的请求
     * @param url 发送请求的URL
     * @param params 请求参数，请求参数应该是name1=value1&name2=value2的形式。
     * @return URL所代表远程资源的响应
     */
    public static String sendGet(String url, String params)
    {
        String result = "";
        BufferedReader in = null;
        try
        {
            String urlName = url + "?" + params;
            URL realUrl = new URL(urlName);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();

            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            // 建立实际的连接
            conn.connect();  //
            // 获取所有响应头字段
            Map<String, List<String>> map = conn.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet())
            {
                System.out.println(key + "--->" + map.get(key));
            }

            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null)
            {
                result += "\n" + line;
            }
        }
        catch (Exception e)
        {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally
        {
            try
            {
                if (in != null)
                {
                    in.close();
                }
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }
        return result;
    }
    /**
     * 向指定URL发送POST方法的请求
     * @param url 发送请求的URL
     * @param params
     * @return URL所代表远程资源的响应
     */
    public static String sendPost(String url, String params)
    {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try
        {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(params);  //
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null)
            {
                result += "\n" + line;
            }

        }
        catch (Exception e)
        {
            System.out.println("发送POST请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输出流、输入流
        finally
        {
            try
            {
                if (out != null)
                {
                    out.close();
                }
                if (in != null)
                {
                    in.close();
                }
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }
        return result;
    }

    private static JsonObject makeExpetion(Exception e){
        JsonObject jsonRes = new JsonObject();
        jsonRes.addProperty("code",-1);
        jsonRes.addProperty("status",e.getMessage());
        System.out.println( "Http:" + e.getMessage());
        return jsonRes;
    }

    public static  Boolean isSucess(JsonObject json){
        return json.get("code").getAsInt() == 0 ;
    }
    public static  String getErrorMsg(JsonObject json){
        return json.get("status").getAsString();
    }
}
