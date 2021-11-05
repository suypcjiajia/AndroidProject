package tool;

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


    private static String  ipaddr;//血培养设备代理器的IP地址和端口


    public static void setIp(String ip){
        ipaddr = ip + ":6665";
    }


    /**
     * 获取最新的报阳
     * @return
     */
    public static JsonObject getBaoYang() {
        try {
            JsonObject json = new JsonObject();
            System.out.println("Http getBaoYang call");
            String response = sendPost("http://" + ipaddr + "/query/baoyang", json.toString());
            System.out.println("Http getBaoYang request:" + json.toString() + " response:" + response);
            JsonElement element = JsonParser.parseString(response);

            JsonObject jsonRes = element.getAsJsonObject();
            return jsonRes;

        } catch (IllegalStateException e) {
            return makeExpetion(e);
        }
    }

    /**
     * 获取当前有瓶子的孔
     * @return
     */
    public static JsonObject getBoard() {
        try {
            JsonObject json = new JsonObject();
            System.out.println("Http getBoard call");
            String response = sendPost("http://" + ipaddr + "/query/board", json.toString());
            System.out.println("Http getBoard request:" + json.toString() + " response:" + response);
            JsonElement element = JsonParser.parseString(response);

            JsonObject jsonRes = element.getAsJsonObject();
            return jsonRes;

        } catch (IllegalStateException e) {
            return makeExpetion(e);
        }
    }

    /**
     * 获取设备状态
     * @return
     */
    public static JsonObject getState() {
        try {
            JsonObject json = new JsonObject();
            System.out.println("Http getState call");
            String response = sendPost("http://" + ipaddr + "/query/state", json.toString());
            System.out.println("Http getState request:" + json.toString() + " response:" + response);
            JsonElement element = JsonParser.parseString(response);

            JsonObject jsonRes = element.getAsJsonObject();
            return jsonRes;

        } catch (IllegalStateException e) {
            return makeExpetion(e);
        }
    }

    /**
     * 获取历史数据
     * @return
     */
    public static JsonObject getHistory(String start,String end,List<Integer> BottleType,List<Integer> FinalState) {
        try {
            JsonObject json = new JsonObject();
            json.addProperty("start",start);
            json.addProperty("end",end);
            String strBottleType = new String();
            for(int i = 0; i < BottleType.size(); i++){

                strBottleType += BottleType.get(i) ;
                if(i != BottleType.size() - 1){
                    strBottleType += ",";
                }
            }
            json.addProperty("BottleType",strBottleType);
            String strFinalState = new String();
            for(int i = 0; i < FinalState.size(); i++){

                strFinalState += FinalState.get(i) ;
                if(i != FinalState.size() - 1){
                    strFinalState += ",";
                }
            }
            json.addProperty("FinalState",strFinalState);


            System.out.println("Http getHistory call");
            String response = sendPost("http://" + ipaddr + "/query/history", json.toString());
            System.out.println("Http getHistory request:" + json.toString() + " response:" + response);
            JsonElement element = JsonParser.parseString(response);

            JsonObject jsonRes = element.getAsJsonObject();
            return jsonRes;

        } catch (IllegalStateException e) {
            return makeExpetion(e);
        }
    }


    /**
     * 获取统计
     * @return
     */
    public static JsonObject getTongji(String year) {
        try {
            JsonObject json = new JsonObject();
            json.addProperty("year",year);

            System.out.println("Http getTongji call");
            String response = sendPost("http://" + ipaddr + "/query/tongji", json.toString());
            System.out.println("Http getTongji request:" + json.toString() + " response:" + response);
            JsonElement element = JsonParser.parseString(response);

            JsonObject jsonRes = element.getAsJsonObject();
            return jsonRes;

        } catch (IllegalStateException e) {
            return makeExpetion(e);
        }
    }

    /**
     * 遥控
     * @return
     */
    public static JsonObject cmd(String arg) {
        try {
            JsonObject json = new JsonObject();
            json.addProperty("arg",arg);

            System.out.println("Http cmd call");
            String response = sendPost("http://" + ipaddr + "/query/cmd", json.toString());
            System.out.println("Http cmd request:" + json.toString() + " response:" + response);
            JsonElement element = JsonParser.parseString(response);

            JsonObject jsonRes = element.getAsJsonObject();
            return jsonRes;

        } catch (IllegalStateException e) {
            return makeExpetion(e);
        }
    }


    /**
     * 获取仓室数量和每仓坑数
     * @return
     */
    public static JsonObject getHouse() {
        try {
            JsonObject json = new JsonObject();

            System.out.println("Http getHouseCount call");
            String response = sendPost("http://" + ipaddr + "/query/house", json.toString());
            System.out.println("Http getHouseCount request:" + json.toString() + " response:" + response);
            JsonElement element = JsonParser.parseString(response);

            JsonObject jsonRes = element.getAsJsonObject();
            return jsonRes;

        } catch (IllegalStateException e) {
            return makeExpetion(e);
        }
    }

    /**
     * 获取生长数据
     * @return
     */
    public static JsonObject getCurve(String PutInTime,int HoleNum ) {
        try {
            JsonObject json = new JsonObject();
            json.addProperty("PutInTime",PutInTime);
            json.addProperty("HoleNum",HoleNum);

            System.out.println("Http getCurve call");
            String response = sendPost("http://" + ipaddr + "/query/curve", json.toString());
            System.out.println("Http getCurve request:" + json.toString() + " response:" + response);
            JsonElement element = JsonParser.parseString(response);

            JsonObject jsonRes = element.getAsJsonObject();
            return jsonRes;

        } catch (IllegalStateException e) {
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
        jsonRes.addProperty("state",-1);
        jsonRes.addProperty("msg",e.getMessage());
        System.out.println( "Http:" + e.getMessage());
        return jsonRes;
    }

    public static  Boolean isSucess(JsonObject json){
        return json.get("state").getAsInt() == 0 ;
    }
    public static  String getErrorMsg(JsonObject json){
        return json.get("msg").getAsString();
    }
}
