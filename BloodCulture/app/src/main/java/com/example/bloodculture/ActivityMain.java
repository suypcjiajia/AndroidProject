package com.example.bloodculture;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.example.tool.Http;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class ActivityMain extends AppCompatActivity {

    FragmentBaoyang fragmentBaoyang;
    FragmentDevice fragmentDevice;
    androidx.viewpager.widget.ViewPager viewPager;

    JsonArray mBaoYangs;
    JsonArray mDevices;

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

        thread.start();
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
        findViewById(R.id.imageViewBaoyang).setBackgroundResource(R.drawable.baoyang_botton2);
        findViewById(R.id.imageViewDevice).setBackgroundResource(R.drawable.device_button1);
    }

    public void setBottonDevice() {
        findViewById(R.id.imageViewBaoyang).setBackgroundResource(R.drawable.baoyang_button1);
        findViewById(R.id.imageViewDevice).setBackgroundResource(R.drawable.device_button2);
    }

    private void httpBaoyang(){

        JsonObject json = Http.bchistory("2020-01-01");

        if (json.get("code").getAsInt() == 0){
            mBaoYangs = json.get("lists").getAsJsonArray();
            System.out.println("httpBaoyang:" + mBaoYangs.toString());
            mHandler.sendEmptyMessage(21);
        }
    }

    private void httpBloodDevices(){

        JsonObject json = Http.getDevicesByType("bt");

        if (json.get("code").getAsInt() == 0){
            mDevices = json.get("lists").getAsJsonArray();
            System.out.println("httpBloodDevices:" + mDevices.toString());
            mHandler.sendEmptyMessage(22);
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


                try {
                    sleep(2000);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                try {
                    httpBaoyang();
                    httpBloodDevices();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }


            }
        }
    };

    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {

            if (msg.what == 20) {

            }else if(msg.what == 21){
                fragmentBaoyang.setBaoYangList(mBaoYangs);
            }else if( msg.what == 22){
                fragmentDevice.setDeviceList(mDevices);
            }
            return true;
        }
    });
}
