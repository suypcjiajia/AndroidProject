package com.example.bloodculture;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.example.tool.Http;
import com.example.tool.NotificationUtil;
import com.example.tool.PhoneMaderSetting;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ActivityMain extends AppCompatActivity {

    FragmentBaoyang fragmentBaoyang;
    FragmentDevice fragmentDevice;
    androidx.viewpager.widget.ViewPager viewPager;

    JsonArray mBaoYangs;
    JsonArray mDevices;
    Boolean  started = false;
    Boolean mThreadWork = false;

    long backKeyLastTime = 0;//记录返回键上一次按下的时间


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentBaoyang = new FragmentBaoyang();
        fragmentDevice = new FragmentDevice();
        List<Fragment> listView = new ArrayList<>();
        listView.add(fragmentBaoyang);
        listView.add(fragmentDevice);
        MyViewAdapter viewAdapter = new MyViewAdapter(getSupportFragmentManager(), listView);
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(viewAdapter);
        viewPager.setOffscreenPageLimit(3);
        viewPager.addOnPageChangeListener(onPageChangeListener);
        viewPager.setCurrentItem(0);

        setBottonBaoyang();

        //启动服务
        Intent serintent = new Intent(this,MyService.class);
        startService(serintent);


        //判断该app是否打开了通知，如果没有的话就打开手机设置页面
        NotificationUtil notiUtil = new NotificationUtil();
        notiUtil.openNotificationSetting(this);

        PhoneMaderSetting.showBatteryOptimizations(this);

        if( !started) {
            started = true;
            thread.start();
        }
    }





    @Override
    protected void onStart(){
        super.onStart();
        System.out.println("ActivityMain onStart");
        mThreadWork = true;
        Intent intent = getIntent();
        handleNotify(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mThreadWork = true;
        System.out.println("ActivityMain onNewIntent");
        handleNotify(intent);
    }

    @Override
    protected void onStop(){
        super.onStop();
        System.out.println("ActivityMain onStop");
        mThreadWork = false;
    }
    @Override
    protected void onPause(){
        super.onPause();
        System.out.println("ActivityMain onPause");
        mThreadWork = false;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_BACK){
            long backKeyCurrentTime = new Date().getTime()/1000;
            if( backKeyCurrentTime - 3 <= backKeyLastTime){
                return super.onKeyDown(keyCode, event);
            }else{
                backKeyLastTime = backKeyCurrentTime;
                Toast.makeText(getApplicationContext(), "再按一次退出", Toast.LENGTH_SHORT).show();
            }

            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }

    }



    public void onBtnBottonBaoyangClick(View v) {

        toBaoyangPage();

    }



    public void onBtnBottonDeviceClick(View v) {
        toDevicePage();
    }

    //切换Fragment到报阳页
    public void toBaoyangPage() {

        viewPager.setCurrentItem(0);
        setBottonBaoyang();

    }

    //切换Fragment到报阳页
    public void toDevicePage() {

        viewPager.setCurrentItem(1);
        setBottonDevice();

    }

    public void setBottonBaoyang() {
        findViewById(R.id.imageViewBaoyang).setBackgroundResource(R.drawable.baoyang_button2);
        findViewById(R.id.imageViewDevice).setBackgroundResource(R.drawable.device_button1);
    }

    public void setBottonDevice() {
        findViewById(R.id.imageViewBaoyang).setBackgroundResource(R.drawable.baoyang_button1);
        findViewById(R.id.imageViewDevice).setBackgroundResource(R.drawable.device_button2);
    }

    private void httpBaoyang(){

        JsonObject json = Http.bchistory(0,200);

        if (json.get("code").getAsInt() == 0){
            mBaoYangs = json.get("lists").getAsJsonArray();
            mHandler.sendEmptyMessage(HandleWhat.bchistory);
        }
    }

    private void httpBloodDevices(){

        JsonObject json = Http.getDevicesByType("bt");

        if (json.get("code").getAsInt() == 0){
            mDevices = json.get("lists").getAsJsonArray();
            mHandler.sendEmptyMessage(HandleWhat.getDevicesByType);
        }
    }

    private void handleNotify(@NonNull  Intent intent){
        Bundle bdl = intent.getExtras();
        if(bdl != null){
            String cmd = intent.getExtras().getString("cmd");
            if(cmd != null && cmd.equals("to")){
                toBaoyangPage();
            }
        }
    }



    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }


        public void onPageSelected(int position) {
            if (position == 0) {
                setBottonBaoyang();
            } else if (position == 1) {
                setBottonDevice();
            }
        }


        public void onPageScrollStateChanged(int state) {

        }
    };

    Thread thread = new Thread() {
        public void run() {
            while (true) {

                if(!mThreadWork){
                    sleepNoThrow(100);
                    continue;
                }

                try {
                    httpBaoyang();
                    httpBloodDevices();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

                sleepNoThrow(5000);


            }
        }

        private void sleepNoThrow(long millis){
            try {
                sleep(millis);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    };

    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {

            if(msg.what == HandleWhat.bchistory){
                fragmentBaoyang.setBaoYangList(mBaoYangs);
            }else if( msg.what == HandleWhat.getDevicesByType){
                fragmentDevice.setDeviceList(mDevices);
            }
            return true;
        }
    });
}
