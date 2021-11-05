package com.example.android.bcphone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import adapter.MyViewAdapter;
import entity.DeviceEntity;
import entity.ShareData;
import tool.Http;

import static android.view.View.INVISIBLE;

public class MainActivity extends AppCompatActivity {

    BaoYangFragment baoYangFragment;
    StateFragment stateFragment;
    QueryFragment queryFragment;
    CountFragment countFragment;
    CtrlFragment ctrlFragment;

    Button btnBaoYang;
    Button btnState;
    Button btnQuery;
    Button btnCount;
    Button btnCtrl;

    View viewTop;



    androidx.viewpager.widget.ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        baoYangFragment = new BaoYangFragment();
        stateFragment = new StateFragment();
        queryFragment = new QueryFragment();
        countFragment = new CountFragment();
        ctrlFragment = new CtrlFragment();
        List<Fragment> listView = new ArrayList<>();
        listView.add(baoYangFragment);
        listView.add(stateFragment);
        listView.add(queryFragment);
        listView.add(countFragment);
        listView.add(ctrlFragment);

        MyViewAdapter viewAdapter = new MyViewAdapter(getSupportFragmentManager(), listView);
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(viewAdapter);
        viewPager.setOffscreenPageLimit(6);
        viewPager.addOnPageChangeListener(onPageChangeListener);
        viewPager.setCurrentItem(0);




        btnBaoYang = findViewById(R.id.btnBaoYang);
        btnBaoYang.setOnClickListener(onBtnBaoYangClickListener);
        btnState = findViewById(R.id.btnState);
        btnState.setOnClickListener(onBtnStateClickListener);
        btnQuery = findViewById(R.id.btnQuery);
        btnQuery.setOnClickListener(onBtnQueryClickListener);
        btnCount = findViewById(R.id.btnCount);
        btnCount.setOnClickListener(onBtnCountClickListener);
        btnCtrl = findViewById(R.id.btnCtrl);
        btnCtrl.setOnClickListener(onBtnCtrlClickListener);

        setBottonBaoYang();

        viewTop = findViewById(R.id.board_top);
        ImageButton back = viewTop.findViewById(R.id.back);
        back.setOnClickListener(onBtnBackClickListener);

        System.out.println("MainActivity onCreate");


    }

    @Override
    protected void onStart() {
        super.onStart();
        new GetHouseThread().start();
    }

    public void setBottonBaoYang() {

        btnBaoYang.setBackgroundResource(R.drawable.hight);
        btnState.setBackgroundResource(R.drawable.dark);
        btnQuery.setBackgroundResource(R.drawable.dark);
        btnCount.setBackgroundResource(R.drawable.dark);
        btnCtrl.setBackgroundResource(R.drawable.dark);

        new GetBaoYangThread().start();


    }
    public void setBottonState() {

        btnBaoYang.setBackgroundResource(R.drawable.dark);
        btnState.setBackgroundResource(R.drawable.hight);
        btnQuery.setBackgroundResource(R.drawable.dark);
        btnCount.setBackgroundResource(R.drawable.dark);
        btnCtrl.setBackgroundResource(R.drawable.dark);

        new GetBoardThread().start();
        new GetStateThread().start();
    }
    public void setBottonQuery() {
        btnBaoYang.setBackgroundResource(R.drawable.dark);
        btnState.setBackgroundResource(R.drawable.dark);
        btnQuery.setBackgroundResource(R.drawable.hight);
        btnCount.setBackgroundResource(R.drawable.dark);
        btnCtrl.setBackgroundResource(R.drawable.dark);
    }
    public void setBottonCount() {
        btnBaoYang.setBackgroundResource(R.drawable.dark);
        btnState.setBackgroundResource(R.drawable.dark);
        btnQuery.setBackgroundResource(R.drawable.dark);
        btnCount.setBackgroundResource(R.drawable.hight);
        btnCtrl.setBackgroundResource(R.drawable.dark);
    }
    public void setBottonCtrl() {
        btnBaoYang.setBackgroundResource(R.drawable.dark);
        btnState.setBackgroundResource(R.drawable.dark);
        btnQuery.setBackgroundResource(R.drawable.dark);
        btnCount.setBackgroundResource(R.drawable.dark);
        btnCtrl.setBackgroundResource(R.drawable.hight);
    }



    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }


        public void onPageSelected(int position) {
            if (position == 0) {
                setBottonBaoYang();
            } else if (position == 1) {
                setBottonState();
            }else if (position == 2) {
                setBottonQuery();
            }else if (position == 3) {
                setBottonCount();
            }else if (position == 4) {
                setBottonCtrl();
            }
        }


        public void onPageScrollStateChanged(int state) {

        }
    };



    View.OnClickListener onBtnBaoYangClickListener = new View.OnClickListener() {
        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        public void onClick(View v) {
            setBottonBaoYang();
            viewPager.setCurrentItem(0);
        }
    };
    View.OnClickListener onBtnStateClickListener = new View.OnClickListener() {
        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        public void onClick(View v) {
            setBottonState();
            viewPager.setCurrentItem(1);
        }
    };
    View.OnClickListener onBtnQueryClickListener = new View.OnClickListener() {
        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        public void onClick(View v) {
            setBottonQuery();
            viewPager.setCurrentItem(2);
        }
    };
    View.OnClickListener onBtnCountClickListener = new View.OnClickListener() {
        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        public void onClick(View v) {
            setBottonCount();
            viewPager.setCurrentItem(3);
        }
    };
    View.OnClickListener onBtnCtrlClickListener = new View.OnClickListener() {
        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        public void onClick(View v) {
            setBottonCtrl();
            viewPager.setCurrentItem(4);
        }
    };

    View.OnClickListener onBtnBackClickListener = new View.OnClickListener() {
        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, DiscoverActivity.class);
            startActivity(intent);
           // finish();
        }
    };


    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (msg.what == HandleId.getBaoYang) {
                baoYangFragment.reflash();
            }
            if(msg.what == HandleId.getBoard){
                stateFragment.updateHoleState();
            }
            if(msg.what == HandleId.getState){
                stateFragment.updateState();
            }
            if(msg.what == HandleId.getHouse){
                stateFragment.initHouse();
            }
            return true;
        }
    });

    class GetBaoYangThread extends  Thread
    {
        public void run(){
            JsonObject ret = Http.getBaoYang();
            if(ret.get("state").getAsInt() == 0){
                 ShareData.getBaoYang = ret.get("result").getAsJsonArray();
            }

            Message msg = new Message();
            msg.what = HandleId.getBaoYang;
            mHandler.sendMessage(msg);
        }
    }

    class GetBoardThread extends  Thread
    {
        public void run(){
            JsonObject ret = Http.getBoard();
            if(ret.get("state").getAsInt() == 0){
                ShareData.getBoard = ret.get("result").getAsJsonArray();
            }

            Message msg = new Message();
            msg.what = HandleId.getBoard;
            mHandler.sendMessage(msg);
        }
    }

    class GetStateThread extends  Thread
    {
        public void run(){
            JsonObject ret = Http.getState();
            if(ret.get("state").getAsInt() == 0){
                ShareData.getState = ret.get("result").getAsJsonObject();
            }

            Message msg = new Message();
            msg.what = HandleId.getState;
            mHandler.sendMessage(msg);
        }
    }

    class GetHouseThread extends  Thread
    {
        public void run(){
            JsonObject ret = Http.getHouse();
            if(ret.get("state").getAsInt() == 0){
                ShareData.getHouse = ret.get("result").getAsJsonObject();
            }

            Message msg = new Message();
            msg.what = HandleId.getHouse;
            mHandler.sendMessage(msg);
        }
    }


}
