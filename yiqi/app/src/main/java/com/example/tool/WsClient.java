/**
 * WebSocket客户端
 */

package com.example.tool;
import android.app.Activity;

import com.example.yiqi.MainActivity;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import java.net.URI;
import java.net.URISyntaxException;

public class WsClient extends WebSocketClient {
    static String uri = "ws://192.168.0.91:1877";
    MainActivity mActivity;
    Boolean rooter = false;


    public static void main(String[] args) throws URISyntaxException {
        WebSocketClient client = new WsClient();
        client.connect();


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
        mActivity.mynotify(message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println( "websocket Connection closed by " + ( remote ? "remote peer" : "us" ) );
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }

    public void setActivity(MainActivity activity){
        mActivity = activity;
    }
}
