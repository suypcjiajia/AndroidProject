package com.example.yiqi;

import android.app.Notification;
import android.app.NotificationChannel;
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
import android.os.Build;
import android.os.Bundle;

import com.example.tool.Http;
import com.example.tool.NotificationUtil;
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
import androidx.annotation.Px;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

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


    JsonArray mBaoYangs;
    List<PieEntry> onlinePieEntry;
    List<PieEntry> exceptionPieEntry;
    List<PieEntry> diskPieEntry;

    String onlineTxt1;
    String onlineTxt2;
    String exceptionTxt1;
    String exceptionTxt2;
    String diskTxt1;
    String diskTxt2;

    HomeFragment homePage;
    DeviceFragment devicePage;
    QueryFragment queryPage;
    MeFragment mePage;

    androidx.viewpager.widget.ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);





//        homePage = (HomeFragment) getSupportFragmentManager().findFragmentById(R.id.myhome);
//        devicePage = (DeviceFragment) getSupportFragmentManager().findFragmentById(R.id.mydevice);
//        queryPage = (QueryFragment) getSupportFragmentManager().findFragmentById(R.id.myquery);
//        mePage = (MeFragment) getSupportFragmentManager().findFragmentById(R.id.myme);

        homePage = new HomeFragment();
        devicePage = new DeviceFragment();
        queryPage = new QueryFragment();
        mePage = new MeFragment();
        List<Fragment> listView = new ArrayList<>();
        listView.add(homePage);
        listView.add(devicePage);
        listView.add(queryPage);
        listView.add(mePage);
        MyViewAdapter viewAdapter = new MyViewAdapter(getSupportFragmentManager(), listView);
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(viewAdapter);
        viewPager.setOffscreenPageLimit(3);
        viewPager.addOnPageChangeListener(onPageChangeListener);

        setBottonHomeButton();


        thread.start();

        //启动服务
        Intent serintent = new Intent(this,MyService.class);
        startService(serintent);

        //判断该app是否打开了通知，如果没有的话就打开手机设置页面
        NotificationUtil notiUtil = new NotificationUtil();
        notiUtil.openNotificationSetting(this);


        System.out.println("MainActivity onCreate");

    }

    @Override
    protected void onStart(){
        super.onStart();
        Intent intent = getIntent();


        handleNotify(intent);

        System.out.println("MainActivity onStart");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        System.out.println("MainActivity onNewIntent");
        String userName = intent.getStringExtra("userName");
        String userLevel = intent.getStringExtra("userLevel");
        mePage.setUserInfo(userName,userLevel);
        handleNotify(intent);
    }

    private void handleNotify(Intent intent){
        Bundle bdl = intent.getExtras();
        if(bdl != null){
            String cmd = intent.getExtras().getString("cmd");
            if(cmd != null && cmd.equals("to")){
                toHomePage();
                homePage.switch2Baoyang();
            }
        }
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

        toHomePage();

    }

    public void onBtnBottonDeviceClick(View v){

        toDevicePage();


    }

    public void onBtnBottonQueryClick(View v){

        toQueryPage();

    }

    public void onBtnBottonMeClick(View v){

        toMePage();

    }

    //切换Fragment到home页
    public void toHomePage(){

        viewPager.setCurrentItem(0);
        setBottonHomeButton();

    }

    public void setBottonHomeButton(){
        findViewById(R.id.btnBottonHome).setBackgroundResource(R.drawable.home1);
        findViewById(R.id.btnBottonDevice).setBackgroundResource(R.drawable.device2);
        findViewById(R.id.btnBottonQuery).setBackgroundResource(R.drawable.ditu2);
        findViewById(R.id.btnBottonMe).setBackgroundResource(R.drawable.user2);
    }
    //切换Fragment到Device页
    public void toDevicePage(){


//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .hide(homePage).show(devicePage).hide(queryPage).hide(mePage).commit();
        viewPager.setCurrentItem(1);
        devicePage.switchPage(1);
        setBottonDeviceButton();

    }

    public void setBottonDeviceButton(){
        findViewById(R.id.btnBottonHome).setBackgroundResource(R.drawable.home2);
        findViewById(R.id.btnBottonDevice).setBackgroundResource(R.drawable.device1);
        findViewById(R.id.btnBottonQuery).setBackgroundResource(R.drawable.ditu2);
        findViewById(R.id.btnBottonMe).setBackgroundResource(R.drawable.user2);
    }
    //切换Fragment到query页
    public void toQueryPage(){


//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .hide(homePage).hide(devicePage).show(queryPage).hide(mePage).commit();
        viewPager.setCurrentItem(2);

        setBottonQueryButton();
    }

    public void setBottonQueryButton(){
        findViewById(R.id.btnBottonHome).setBackgroundResource(R.drawable.home2);
        findViewById(R.id.btnBottonDevice).setBackgroundResource(R.drawable.device2);
        findViewById(R.id.btnBottonQuery).setBackgroundResource(R.drawable.ditu1);
        findViewById(R.id.btnBottonMe).setBackgroundResource(R.drawable.user2);
    }
    //切换Fragment到me页
    public void toMePage(){


//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .hide(homePage).hide(devicePage).hide(queryPage).show(mePage).commit();
        viewPager.setCurrentItem(3);
        setBottonMeButton();
    }

    public void setBottonMeButton(){
        findViewById(R.id.btnBottonHome).setBackgroundResource(R.drawable.home2);
        findViewById(R.id.btnBottonDevice).setBackgroundResource(R.drawable.device2);
        findViewById(R.id.btnBottonQuery).setBackgroundResource(R.drawable.ditu2);
        findViewById(R.id.btnBottonMe).setBackgroundResource(R.drawable.user1);
    }

    public void toHospitaoPos(String address,String name){
        toQueryPage();
        queryPage.toHospitalPos(address,name);
    }



    Thread  thread = new Thread()
    {
        public void run() {

            int loops = 0;

            while(true) {

                try {
                    if( loops == 0) {
                        httpOnlinesummary();
                        httpExceptionsummary();
                        httpAlldisksummary();
                    }
                    httpBaoyang();
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }

                try {
                    sleep(5000);
                } catch (Exception e) {
                    System.out.println(e.getMessage()  );
                }

                loops++;
                if( loops >= 2){
                    loops = 0;
                }

            }
        }
    };

    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {

            if (msg.what == 20) {

            }else if(msg.what == 21){

                homePage.setBaoYang(mBaoYangs);


            }else if( msg.what == 22){
                homePage.setOnLineTxt(onlineTxt1,onlineTxt2);
                homePage.setCharData(onlinePieEntry,1);
            }else if( msg.what == 23){
                homePage.setExceptionTxt(exceptionTxt1,exceptionTxt2);
                homePage.setCharData(exceptionPieEntry,2);
            }else if( msg.what == 24){
                homePage.setDiskTxt(diskTxt1,diskTxt2);
                homePage.setCharData(diskPieEntry,3);
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
            onlineTxt1 = online_count + "/" + count;
            onlineTxt2 = "总共" +  count + "台设备,在线"  + online_count + "台";

            strings.add(new PieEntry( Integer.parseInt(online_count) ,""));
            strings.add(new PieEntry(  Integer.parseInt(count) - Integer.parseInt(online_count),""));

        } else {

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
            exceptionTxt1 = exception_count + "/" + count;
            exceptionTxt2 = "总共" +  count + "台设备,异常"  + exception_count + "台";


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
            diskTxt1 = available + "/" + total;
            diskTxt2 = "总共" +  total + "Mb,已使用"  + available + "Mb";
            strings.add(new PieEntry( Float.parseFloat(available) ,""));
            strings.add(new PieEntry(  Float.parseFloat(total) - Float.parseFloat(available),""));

        } else {

            strings.add(new PieEntry(  0,""));
        }
        diskPieEntry = strings;
        mHandler.sendEmptyMessage(24);


    }

    private void httpBaoyang(){

        JsonObject json = Http.bchistory("2020-01-01");

        if (json.get("code").getAsInt() == 0){
            mBaoYangs = json.get("lists").getAsJsonArray();

            mHandler.sendEmptyMessage(21);


        }


    }

    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener(){
        public  void onPageScrolled(int position, float positionOffset, int positionOffsetPixels){

        }


        public  void onPageSelected(int position){
            if( position == 0){
                setBottonHomeButton();
            }else if( position == 1){
                toDevicePage();
            }else if( position == 2){
                setBottonQueryButton();
            }else if( position == 3){
                setBottonMeButton();
            }
        }



        public void onPageScrollStateChanged(int state){

        }
    };


}