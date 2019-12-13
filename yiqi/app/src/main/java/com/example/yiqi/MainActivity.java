package com.example.yiqi;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import com.example.tool.Http;
import com.example.tool.WsClient;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    String[] baoYangs;
    List<PieEntry> onlinePieEntry;
    List<PieEntry> exceptionPieEntry;
    List<PieEntry> diskPieEntry;
    Boolean toBaoyang = false;

    private WsClient mWsClient;
    private NotificationManager notificationMg ;

    private  int notifiIndex = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ImageButton btn = findViewById(R.id.btnMainLogin);
        btn.setOnClickListener(new View.OnClickListener(){
            public  void  onClick(View v){
               Intent intent = new Intent(MainActivity.this, LoginActivity.class);
               startActivity(intent);

            }
        });

        findViewById(R.id.myhome).setVisibility(View.VISIBLE);
        findViewById(R.id.mydevice).setVisibility(View.INVISIBLE);
        findViewById(R.id.myquery).setVisibility(View.INVISIBLE);
        findViewById(R.id.myme).setVisibility(View.INVISIBLE);

        findViewById(R.id.btnBottonHome).setBackgroundResource(R.drawable.home1);


        notificationMg  =(NotificationManager) getSystemService(NOTIFICATION_SERVICE);


        try {
            mWsClient = new WsClient();
            mWsClient.setActivity(MainActivity.this);
            mWsClient.start();

        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        thread.start();

        System.out.println("MainActivity onCreate");

    }

    @Override
    protected void onStart(){
        super.onStart();
        Intent intent = getIntent();


        String userName = intent.getStringExtra("userName");
        String userLevel = intent.getStringExtra("userLevel");
        TextView userTxt = findViewById(R.id.textViewUserName);
        userTxt.setText(userName);
        TextView textViewLevel = findViewById(R.id.textViewLevel);
        textViewLevel.setText(userLevel);


        if( toBaoyang){
            toBaoyang = false;
            findViewById(R.id.myhome).setVisibility(View.VISIBLE);
            findViewById(R.id.mydevice).setVisibility(View.INVISIBLE);
            findViewById(R.id.myquery).setVisibility(View.INVISIBLE);
            findViewById(R.id.myme).setVisibility(View.INVISIBLE);

            ((HomeFragment)getSupportFragmentManager().findFragmentById(R.id.myhome)).switch2Baoyang();
        }

        System.out.println("MainActivity onStart");
    }

    @Override
    public void onRestart(){
        super.onRestart();
        System.out.println("MainActivity onRestart");
    }

    @Override
    public void onResume(){
        super.onResume();
        System.out.println("MainActivity onResume");
    }

    @Override
    public void onPause(){
        super.onPause();
        System.out.println("MainActivity onPause");
    }

    @Override
    public void onStop(){
        super.onStop();
        System.out.println("MainActivity onStop");
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        System.out.println("MainActivity onDestroy");
    }


    public void onBtnBottonHomeClick(View v){
        //Fragment home =  getSupportFragmentManager().findFragmentById(R.id.myhome);
        findViewById(R.id.myhome).setVisibility(View.VISIBLE);
        findViewById(R.id.mydevice).setVisibility(View.INVISIBLE);
        findViewById(R.id.myquery).setVisibility(View.INVISIBLE);
        findViewById(R.id.myme).setVisibility(View.INVISIBLE);

        findViewById(R.id.btnBottonHome).setBackgroundResource(R.drawable.home1);
        findViewById(R.id.btnBottonDevice).setBackgroundResource(R.drawable.device2);
        findViewById(R.id.btnBottonQuery).setBackgroundResource(R.drawable.query2);
        findViewById(R.id.btnBottonMe).setBackgroundResource(R.drawable.user2);


    }

    public void onBtnBottonDeviceClick(View v){
        findViewById(R.id.myhome).setVisibility(View.INVISIBLE);
        findViewById(R.id.mydevice).setVisibility(View.VISIBLE);
        findViewById(R.id.myquery).setVisibility(View.INVISIBLE);
        findViewById(R.id.myme).setVisibility(View.INVISIBLE);

        findViewById(R.id.btnBottonHome).setBackgroundResource(R.drawable.home2);
        findViewById(R.id.btnBottonDevice).setBackgroundResource(R.drawable.device1);
        findViewById(R.id.btnBottonQuery).setBackgroundResource(R.drawable.query2);
        findViewById(R.id.btnBottonMe).setBackgroundResource(R.drawable.user2);


        findViewById(R.id.page2).setVisibility(View.VISIBLE);
        findViewById(R.id.fragmentDevlist).setVisibility(View.INVISIBLE);
    }

    public void onBtnBottonQueryClick(View v){
        findViewById(R.id.myhome).setVisibility(View.INVISIBLE);
        findViewById(R.id.mydevice).setVisibility(View.INVISIBLE);
        findViewById(R.id.myquery).setVisibility(View.VISIBLE);
        findViewById(R.id.myme).setVisibility(View.INVISIBLE);

        findViewById(R.id.btnBottonHome).setBackgroundResource(R.drawable.home2);
        findViewById(R.id.btnBottonDevice).setBackgroundResource(R.drawable.device2);
        findViewById(R.id.btnBottonQuery).setBackgroundResource(R.drawable.query1);
        findViewById(R.id.btnBottonMe).setBackgroundResource(R.drawable.user2);
    }

    public void onBtnBottonMeClick(View v){
        findViewById(R.id.myhome).setVisibility(View.INVISIBLE);
        findViewById(R.id.mydevice).setVisibility(View.INVISIBLE);
        findViewById(R.id.myquery).setVisibility(View.INVISIBLE);
        findViewById(R.id.myme).setVisibility(View.VISIBLE);

        findViewById(R.id.btnBottonHome).setBackgroundResource(R.drawable.home2);
        findViewById(R.id.btnBottonDevice).setBackgroundResource(R.drawable.device2);
        findViewById(R.id.btnBottonQuery).setBackgroundResource(R.drawable.query2);
        findViewById(R.id.btnBottonMe).setBackgroundResource(R.drawable.user1);
    }


    Thread  thread = new Thread()
    {
        public void run() {


            while(true) {

                try {
                    httpOnlinesummary();
                    httpExceptionsummary();
                    httpAlldisksummary();
                    httpBaoyang();
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }

                try {
                    sleep(5000);
                } catch (Exception e) {
                    System.out.println(e.getMessage()  );
                }
                System.out.println("MainActivity run");

                mHandler.sendEmptyMessage(20);

            }
        }
    };

    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {

            if (msg.what == 20) {

            }else if(msg.what == 21){
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, baoYangs);
                ListView listView = (ListView)findViewById(R.id.listViewBaoyang);
                //将构建好的适配器对象传进去
                listView.setAdapter(adapter);
            }else if( msg.what == 22){
                setCharData(onlinePieEntry,1);
            }else if( msg.what == 23){
                setCharData(exceptionPieEntry,2);
            }else if( msg.what == 24){
                setCharData(diskPieEntry,3);
            }
            return true;
        }
    });

    private void httpOnlinesummary(){

        List<PieEntry> strings = new ArrayList<>();
        JsonObject json = Http.onlinesummary();
        if (json.get("code").getAsInt() == 0) {
            String online_count = json.get("online_count").getAsString();
            String count = json.get("count").getAsString();
            TextView t = findViewById(R.id.textViewOnline2);
            t.setText(online_count + "/" + count);

            TextView t2 = findViewById(R.id.textViewOnline3);
            t2.setText("总共" +  count + "台设备,在线"  + online_count + "台");

            strings.add(new PieEntry( Integer.parseInt(online_count) ,""));
            strings.add(new PieEntry(  Integer.parseInt(count) - Integer.parseInt(online_count),""));

        } else {
            TextView t = findViewById(R.id.textViewOnline2);
            t.setText("0/0");
            TextView t2 = findViewById(R.id.textViewOnline3);
            t2.setText("总共0台设备,在线0台");

            strings.add(new PieEntry( 0 ,""));
            strings.add(new PieEntry(  0,""));
        }
        onlinePieEntry = strings;
        mHandler.sendEmptyMessage(22);


    }

    private void httpExceptionsummary(){

        List<PieEntry> strings = new ArrayList<>();
        JsonObject json = Http.exceptionsummary();
        if (json.get("code").getAsInt() == 0) {
            String exception_count = json.get("exception_count").getAsString();
            String count = json.get("count").getAsString();


            strings.add(new PieEntry( Integer.parseInt(exception_count) ,""));
            strings.add(new PieEntry(  Integer.parseInt(count) - Integer.parseInt(exception_count),""));

        } else {

            strings.add(new PieEntry(  0,""));
        }
        exceptionPieEntry = strings;

        mHandler.sendEmptyMessage(23);

    }

    private void httpAlldisksummary(){

        List<PieEntry> strings = new ArrayList<>();
        JsonObject json = Http.alldisksummary();
        if (json.get("code").getAsInt() == 0) {
            String available = json.get("available").getAsString() ;
            String total =  json.get("total").getAsString();


            strings.add(new PieEntry( Float.parseFloat(available) ,""));
            strings.add(new PieEntry(  Float.parseFloat(total) - Float.parseFloat(available),""));

        } else {

            strings.add(new PieEntry(  0,""));
        }
        diskPieEntry = strings;
        mHandler.sendEmptyMessage(24);


    }

    private void httpBaoyang(){

        JsonObject json = Http.bchistory("2019-11-01");

        if (json.get("code").getAsInt() == 0){
            JsonArray lists = json.get("lists").getAsJsonArray();

            String[] data  = new String[lists.size()];

            for( int i = 0; i < lists.size(); i++){

                String MachineID = lists.get(i).getAsJsonObject().get("MachineID").toString();
                String ExtensionNum = lists.get(i).getAsJsonObject().get("ExtensionNum").toString();
                String HoleNum = lists.get(i).getAsJsonObject().get("HoleNum").toString();
                String PositiveTime = lists.get(i).getAsJsonObject().get("PositiveTime").toString();

                data[i] = MachineID + " " +  ExtensionNum  + " " + HoleNum + " " +  PositiveTime;

                System.out.println( "item:" + data[i]);


            }
            baoYangs = data;

            mHandler.sendEmptyMessage(21);


        }


    }





    public void setCharData(List<PieEntry> strings,int type){

        PieChart chart1 ;
        ArrayList<Integer> colors = new ArrayList<>();
        if(type == 1){
            chart1 = findViewById(R.id.chart1);

            colors.add(getResources().getColor(R.color.greencao));
            colors.add(getResources().getColor(R.color.blackb3));


        }else if( type == 2){
            chart1 = findViewById(R.id.chart2);

            colors.add(getResources().getColor(R.color.red));
            colors.add(getResources().getColor(R.color.blackb3));


        }else if( type == 3){
            chart1 = findViewById(R.id.chart3);

            colors.add(getResources().getColor(R.color.redju));
            colors.add(getResources().getColor(R.color.blackb3));


        }else{
            return;
        }
        PieDataSet dataSet = new PieDataSet(strings,"");





        dataSet.setColors(colors);

        PieData pieData = new PieData(dataSet);
        pieData.setDrawValues(false);


        Description description = new Description();
        description.setText("");
        chart1.setDescription(description);

        chart1.setData(pieData);
        chart1.invalidate();
    }

    public void mynotify(String msg){
        //定义一个PendingIntent点击Notification后启动一个Activity
        Intent intent = new Intent(MainActivity.this,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0, intent, 0);


        Notification noti = new Notification.Builder(MainActivity.this)
                .setSmallIcon(R.drawable.home1)
                .setContentTitle("报阳通知")
                .setContentText(msg)
                .setSmallIcon(R.drawable.home1)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.home1))
                .setAutoCancel(true)
                .setVibrate(new long[]{0, 1000, 1000, 1000}) //通知栏消息震动
                .setLights(Color.GREEN, 1000, 2000) //通知栏消息闪灯(亮一秒间隔两秒再亮)
                .setContentIntent(pendingIntent)
                .build();//链接结束


        noti.defaults = Notification.DEFAULT_ALL; //震动,加提示音
        notificationMg.notify(notifiIndex++,noti);//管理者发送通知,数字代表标识


        //获取电源管理器对象
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        //获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, ":bright");
        //点亮屏幕
        wl.acquire();
        //释放
        wl.release();


        toBaoyang = true;
    }

}