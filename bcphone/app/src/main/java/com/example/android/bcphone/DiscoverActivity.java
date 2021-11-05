package com.example.android.bcphone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import custom.MyDevice;
import db.DeviceDao;
import entity.DeviceEntity;
import tool.Http;

import static android.view.View.INVISIBLE;

public class DiscoverActivity extends AppCompatActivity {

    Button btnDiscover;
    ScrollView srcoll;
    MyDevice btnImag0;
    MyDevice btnImag1;
    MyDevice btnImag2;
    MyDevice btnImag3;
    LinearLayout layoutProgress;
    TextView seconds;
    TextView progressMsg;
    Button btnGo;
    EditText edtInputIp;

    List<String> tmp;

    DeviceDao deviceDao;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover);

        deviceDao = new DeviceDao(this);

        btnDiscover = findViewById(R.id.btnDisCover);
        srcoll = findViewById(R.id.scroll);
        btnImag0 = findViewById(R.id.btnImag0);
        btnImag1 = findViewById(R.id.btnImag1);
        btnImag2 = findViewById(R.id.btnImag2);
        btnImag3 = findViewById(R.id.btnImag3);
        layoutProgress = findViewById(R.id.layoutProgress);
        seconds = findViewById(R.id.seconds);
        progressMsg = findViewById(R.id.progressMsg);
        btnGo = findViewById(R.id.btnGo);
        edtInputIp = findViewById(R.id.edtInputIp);

        btnDiscover.setOnClickListener(onBtnDiscoverClick);
        btnImag0.setOnMyClickListener(onBtnImageClick);
        btnImag1.setOnMyClickListener(onBtnImageClick);
        btnImag2.setOnMyClickListener(onBtnImageClick);
        btnImag3.setOnMyClickListener(onBtnImageClick);
        btnGo.setOnClickListener(onBtnGoClick);

        tmp = new ArrayList<>();
        clearImage();
        layoutProgress.setVisibility(View.GONE);


        new ListenThread().start();



        System.out.println("Discover onCreate");

    }

    protected void onResume(){
        super.onResume();
        System.out.println("Discover onResume");
    }

    void clearImage(){
        btnImag0.clear();
        btnImag1.clear();
        btnImag2.clear();
        btnImag3.clear();
        tmp.clear();
        //deviceDao.clear();
    }

    /**
     *
     * @param v
     */
    View.OnClickListener onBtnDiscoverClick = new View.OnClickListener() {
        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        public void onClick(View v) {
            layoutProgress.setVisibility(View.VISIBLE);
            btnDiscover.setEnabled(false);
            clearImage();
            new MyThread().start();
        }
    };


    /**
     *
     * @param v
     */
    View.OnClickListener onBtnGoClick = new View.OnClickListener() {
        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        public void onClick(View v) {
            Http.setIp( edtInputIp.getText().toString() );
            Intent intent = new Intent(DiscoverActivity.this, MainActivity.class);
            startActivity(intent);
            //finish();
        }
    };


    /**
     *
     * @param v
     */
    View.OnClickListener onBtnImageClick = new View.OnClickListener() {
        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        public void onClick(View v) {
            MyDevice myDevice = (MyDevice)v;
            Http.setIp( myDevice.getIpAddr() );
            Intent intent = new Intent(DiscoverActivity.this, MainActivity.class);
            startActivity(intent);
            //finish();
        }
    };

    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (msg.what == HandleId.progress) {//进度消息

                seconds.setText( String.valueOf(msg.arg1));
                if(msg.arg1 == 0) {
                    layoutProgress.setVisibility(View.GONE);
                    btnDiscover.setEnabled(true);
                }

            }else if(msg.what == HandleId.revDevice){//发现设备
                DeviceEntity deviceEntity = (DeviceEntity)msg.obj;
                //先判断存在

                if(tmp.contains(deviceEntity.Id)){
                    return true;
                }
//                if(deviceDao.getOne(deviceEntity.Id) != null ){
//                    return true;
//                }
                //显示设备
                if(btnImag0.getVisibility() == INVISIBLE){
                    btnImag0.setIpAddr(deviceEntity.ip);
                    btnImag0.setMsg(deviceEntity.msg);
                    btnImag0.setMyId(deviceEntity.Id);
                }else if (btnImag1.getVisibility() == INVISIBLE){
                    btnImag1.setIpAddr(deviceEntity.ip);
                    btnImag1.setMsg(deviceEntity.msg);
                    btnImag1.setMyId(deviceEntity.Id);
                }else if (btnImag2.getVisibility() == INVISIBLE){
                    btnImag2.setIpAddr(deviceEntity.ip);
                    btnImag2.setMsg(deviceEntity.msg);
                    btnImag2.setMyId(deviceEntity.Id);
                }else if (btnImag3.getVisibility() == INVISIBLE){
                    btnImag3.setIpAddr(deviceEntity.ip);
                    btnImag3.setMsg(deviceEntity.msg);
                    btnImag3.setMyId(deviceEntity.Id);
                }
                tmp.add(deviceEntity.Id);
                //deviceDao.insert(deviceEntity);

            }
            return true;
        }
    });

    class MyThread  extends Thread
    {



        @Override
        public void run() {

            try {
                int count = 2;
                while(count != 0){
                    sleep(1000);
                    count--;
                    Message msg = new Message();
                    msg.what = HandleId.progress;
                    msg.arg1 = count;
                    mHandler.sendMessage(msg);


                    DatagramSocket socket =  new DatagramSocket();

                    byte[] buf =  new byte[7];
                    buf[0] = (byte)0xff;
                    buf[1] = (byte)0xff;
                    String msg1 = "query";
                    System.arraycopy(msg1.getBytes(),0,buf,2,msg1.length());


                    DatagramPacket packet = new DatagramPacket(buf,
                            buf.length,
                            InetAddress.getByName("255.255.255.255"),
                            6666);
                    //广播
                    socket.send(packet);
                    socket.close();

                }
            }
            catch (IOException e) {
                System.out.println( "MyThread:" + e);
            }
            catch (InterruptedException e){
                System.out.println( "MyThread:" +  e);
            }

        }
    }

    class ListenThread  extends  Thread
    {
        DatagramSocket socket;
        public void run(){
            try {
                System.out.println("ListenThread");
                socket = new DatagramSocket(6666);
                while(true) {
                    byte[] buf = new byte[512];
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    //收到广播
                    socket.receive(packet);
                    if(buf[0] != (byte)0xff || buf[1] != (byte)0xfe){
                        continue;
                    }
                    String ip = packet.getAddress().getHostAddress();
                    int port = packet.getPort();
                    int idLen = buf[2];
                    String id = new String(buf,3,idLen);
                    int msgLen = buf[3 + idLen];
                    String msg = new String(buf, 4 + idLen , msgLen);
                    System.out.println("监听到: " + ip + "\tport: " + port + "\t信息: " + id + " " + msg);

                    Message msg1 = new Message();
                    msg1.what = HandleId.revDevice;
                    msg1.arg1 = 0;
                    msg1.obj = new DeviceEntity(id,msg,ip);
                    mHandler.sendMessage(msg1);


                }

            }catch (Exception e){

                System.out.println( "ListenThread err:" + e);
            }
        }
    }
}
