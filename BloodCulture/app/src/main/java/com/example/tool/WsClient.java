/**
 * WebSocket客户端
 */

package com.example.tool;
import android.app.Activity;

import com.example.bloodculture.MyService;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import java.net.URI;
import java.net.URISyntaxException;


public class WsClient extends WebSocketClient {
   // static String uri = "ws://192.168.0.91:1877";
    static String uri = "ws://120.196.141.28:1877";

    private MyService mMyService;
    private Boolean rooter = true;
    private WsClient me = this;
    private Boolean mNeedOpen  = true;//保证只connect一次



    public static void main(String[] args) throws URISyntaxException {
        WsClient client = new WsClient();
        client.rooter = false;
        client.start();


    }

    public WsClient() throws URISyntaxException{

        super(new URI(uri));

    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println( "opened webscoket connection" );
    }

    @Override
    public void onMessage(String message) {
        System.out.println( "onMessage rev:" +  message);
        if( rooter) {
            JsonElement element = JsonParser.parseString(message);
            JsonObject jsonRes =  element.getAsJsonObject();
            if( jsonRes.get("status") != null){
                if( jsonRes.get("status").getAsString().equals("connected") ){//过滤连接后服务器返回的消息
                    return;
                }
            }

            if( mMyService != null){
                mMyService.mynotify(message);
            }

        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println( "websocket Connection closed by " + ( remote ? "remote peer" : "us" ) );
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }

    public void setService(MyService service){
        mMyService = service;
    }

    public  void start(){
        if( !mNeedOpen){
            return;
        }
        mNeedOpen = false;
        me.connect();//初始化连接
        thread.start();//线程：起到重连的作用
    }

    private Thread  thread = new Thread()
    {
        private int counts;
        private  int sleepMill = 2000;//一次循环睡多久，必须是1000的倍数
        private int seconds = 10;//秒，时间到就重连
        public void run() {
            counts = 0;
            while(true) {

                counts++;
                try {
                    sleep(sleepMill);
                } catch (Exception e) {
                    System.out.println(e.getMessage()  );
                }

                try{
                    //System.out.println("WsClient run isOpen:" + me.isOpen() + ",isClosed:" + me.isClosed());
                    if( me.isClosed()  ) {//被关闭就重连
                        me.reconnect();
                        counts = 0;
                    }
                }catch (Exception e){
                    System.out.println(e  );
                }

                if(counts*1000/sleepMill * (sleepMill/1000) >= seconds){//时间到就重连
                    me.reconnect();
                    counts = 0;
                }

            }
        }
    };
}
