package com.dl.d2ia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ActivityMain extends AppCompatActivity {


    FragmentSingleTest  fragmentSingleTest;
    FragmentCurve fragmentCurve;


    androidx.viewpager.widget.ViewPager viewPager;
    ImageView singleTestBtn;
    ImageView curveBtn;

    private BluetoothAdapter mBluetoothAdapter;
    public static final int REQUEST_ENABLE_BT = 2;
    private IntentFilter filter = new IntentFilter();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentSingleTest = new FragmentSingleTest();
        fragmentCurve = new FragmentCurve();
        List<Fragment> listView = new ArrayList<>();
        listView.add(fragmentSingleTest);
        listView.add(fragmentCurve);
        MyViewAdapter viewAdapter = new MyViewAdapter(getSupportFragmentManager(), listView);
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(viewAdapter);
        viewPager.setOffscreenPageLimit(3);
        viewPager.addOnPageChangeListener(onPageChangeListener);
        viewPager.setCurrentItem(0);

        setBottonSingleTest();

        singleTestBtn = findViewById(R.id.imageViewSingleTest);
        singleTestBtn.setOnClickListener(onBtnSingelTestClickListener);

        curveBtn = findViewById(R.id.imageViewCurve);
        curveBtn.setOnClickListener(onBtnCurveClickListener);



        // 得到本地蓝牙适配器
        mBluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        // 若当前设备不支持蓝牙功能
        if(mBluetoothAdapter == null){
            Toast.makeText(this,"蓝牙不可用",Toast.LENGTH_LONG).show();
            finish();
            return;
        }


        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver,filter);

    }


    @Override
    public void onStart(){
        super.onStart();
        if(!mBluetoothAdapter.isEnabled()){
            // 若当前设备蓝牙功能未开启，则开启蓝牙
            Intent enableIntent = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent,REQUEST_ENABLE_BT);
        } else{
            countDownTimer.start();
        }
    }

    @Override
    public void onActivityResult(int requesstCode, int resultCode, Intent data){
        switch (requesstCode) {
            case REQUEST_ENABLE_BT:
                if (resultCode == Activity.RESULT_OK) {

                    countDownTimer.start();
                }else {
                    Toast.makeText(this, "没有打开蓝牙",
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
    }


    public void setBottonSingleTest() {
        findViewById(R.id.imageViewSingleTest).setBackgroundResource(R.drawable.single_test_bottom_select);
        findViewById(R.id.imageViewCurve).setBackgroundResource(R.drawable.curve_bottom);
    }

    public void setBottonCurver() {
        findViewById(R.id.imageViewSingleTest).setBackgroundResource(R.drawable.single_test_bottom);
        findViewById(R.id.imageViewCurve).setBackgroundResource(R.drawable.curve_bottom_select);
        fragmentCurve.showFirstRecord();
    }



    private void doDiscovery(){
        if(mBluetoothAdapter.isDiscovering())
            mBluetoothAdapter.cancelDiscovery();
        mBluetoothAdapter.startDiscovery();
    }




    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }


        public void onPageSelected(int position) {
            if (position == 0) {
                setBottonSingleTest();
            } else if (position == 1) {
                setBottonCurver();
            }
        }


        public void onPageScrollStateChanged(int state) {

        }
    };


    View.OnClickListener onBtnSingelTestClickListener = new View.OnClickListener() {
        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        public void onClick(View v) {
            setBottonSingleTest();
            viewPager.setCurrentItem(0);
        }
    };

    View.OnClickListener onBtnCurveClickListener = new View.OnClickListener() {
        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        public void onClick(View v) {
            setBottonCurver();
            viewPager.setCurrentItem(1);
        }
    };

    CountDownTimer countDownTimer = new CountDownTimer(200,100) {
        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            doDiscovery();//倒计时结束启动扫描
        }
    };


    /**
     * 扫描结果通知回调
     */
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            if(BluetoothDevice.ACTION_FOUND
                    .equals(action)){
                BluetoothDevice  device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if( device == null){
                    return;
                }
                if(device.getBondState() != BluetoothDevice.BOND_BONDED){
                    if( device.getName() == null){
                        return;
                    }
                    if( device.getName().contains("DL") ) {//目标设备
                        fragmentSingleTest.addTargetDeviceInfo(device);
                    }
                }
            }else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED
                    .equals(action)){
                countDownTimer.cancel();
                countDownTimer.start();

            }
        }
    };




}
