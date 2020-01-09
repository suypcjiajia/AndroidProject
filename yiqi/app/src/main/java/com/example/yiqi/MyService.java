package com.example.yiqi;

import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;

import androidx.core.app.NotificationCompat;

import com.example.tool.WsClient;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import static android.os.PowerManager.SCREEN_BRIGHT_WAKE_LOCK;

public class MyService extends Service {

    private final static int NOTIFICATION_ID = android.os.Process.myPid();

    private WsClient mWsClient;
    private NotificationManager notificationMg ;
    public MyService() {
    }

    @Override
    public void onCreate(){
        super.onCreate();
        System.out.println("MyService onCreate");

        notificationMg  =(NotificationManager) getSystemService(NOTIFICATION_SERVICE);


        try {
            mWsClient = new WsClient();
            mWsClient.setService(this);
            mWsClient.start();

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public int onStartCommand(Intent intent,int flags,int startId){
        System.out.println("MyService onStartCommand");
        //startForeground(NOTIFICATION_ID,getForegroudNotification());
        return super.onStartCommand(intent,flags,startId);


    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    /**
     * 消息的通知
     * @param msg
     */
    public void mynotify(String msg){
        //定义一个PendingIntent点击Notification后启动一个Activity
        System.out.println("MyService::mynotify:" + msg);

        String strShow = new String();
        try {
            JsonElement element = JsonParser.parseString(msg);
            JsonObject jmsg = element.getAsJsonObject();

            String   type = jmsg.get("type").getAsString();
            if( type.equals("BloodCultureAlert")){
                String   machineID = jmsg.get("machineID").getAsString();
                String   extensionNum = jmsg.get("extensionNum").getAsString();
                String   holeNum = String.valueOf(jmsg.get("holeNum").getAsInt() +1);
                strShow = "设备号:"  + machineID + "分机号:" +  extensionNum +  "孔号:" + holeNum;
            }

        }catch (Exception e){
            e.printStackTrace();
            return;
        }

        Intent intent = new Intent(MyService.this,MainActivity.class);
        Bundle bdl = new Bundle();
        bdl.putString("cmd","to");
        intent.putExtras(bdl);
        PendingIntent pendingIntent = PendingIntent.getActivity(MyService.this, 0, intent, 0);


        Notification noti;
        String id = "suyp";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){//android 8以上版本
            NotificationChannel mChannel = new NotificationChannel(id, "suyp123", NotificationManager.IMPORTANCE_LOW);
            notificationMg.createNotificationChannel(mChannel);
            noti = new Notification.Builder(this,id)
                    .setContentTitle("报阳通知")
                    .setContentText(strShow)
                    .setSmallIcon(R.drawable.app_icon)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.app_icon))
                    .setAutoCancel(true)
                    .setVibrate(new long[]{0, 1000, 1000, 1000}) //通知栏消息震动
                    .setLights(Color.GREEN, 1000, 2000) //通知栏消息闪灯(亮一秒间隔两秒再亮)
                    .setContentIntent(pendingIntent)
                    .build();//链接结束

        }else{
            noti = new Notification.Builder(this)
                    .setContentTitle("报阳通知")
                    .setContentText(strShow)
                    .setSmallIcon(R.drawable.app_icon)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.app_icon))
                    .setAutoCancel(true)
                    .setVibrate(new long[]{0, 1000, 1000, 1000}) //通知栏消息震动
                    .setLights(Color.GREEN, 1000, 2000) //通知栏消息闪灯(亮一秒间隔两秒再亮)
                    .setContentIntent(pendingIntent)
                    .build();//链接结束
        }



        noti.defaults = Notification.DEFAULT_ALL; //震动,加提示音
        notificationMg.notify(888,noti);//管理者发送通知,数字代表标识

        wakeUpAndUnlock();

    }


    /**
     * 唤醒手机屏幕并解锁
     */
    public  void wakeUpAndUnlock() {
        // 获取电源管理器对象


        PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        boolean screenOn = pm.isScreenOn();
        if (!screenOn) {
            // 获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag


            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK    , ":bright");
            wl.acquire(10000); // 点亮屏幕
            wl.release(); // 释放
        }
    }

    private Notification getForegroudNotification()
    {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "")
                .setContentTitle("服务运行于前台")
                .setContentText("service被设为前台进程")
                .setTicker("service正在后台运行...")
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setWhen(System.currentTimeMillis())
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setContentIntent(pendingIntent);
        Notification notification = builder.build();
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        System.out.println("getForegroudNotification");
        return notification;
    }


}
