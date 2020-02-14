package com.example.bloodculture;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.tool.Http;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class ActivityGrowCurve extends AppCompatActivity {

    MyThread thread;
    JsonArray mHoleCurve;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grow_curve);
        View viewTop = findViewById(R.id.top_in_grow_curve);
        ImageButton back = viewTop.findViewById(R.id.back);
        back.setOnClickListener(onBtnBackClickListener);

        thread.open();
        thread.start();
    }

    @Override
    public void onStart() {
        super.onStart();
        System.out.println("ActivityGrowCurve onStart");
        Intent intent = getIntent();
        thread.machineID = intent.getStringExtra("MachineID");
        thread.extensionNum = intent.getStringExtra("ExtensionNum");
        ((TextView)findViewById(R.id.textViewKongTips)).setText( thread.holeNum + "号孔生长曲线" );
    }

    @Override
    public void onStop(){
        super.onStop();
        System.out.println("ActivityGrowCurve onStop");
        thread.close();
    }

    private void httpHoleCurve(String machineID,String extensionNum,
                               String holeNum,
                               String start,String end){

        JsonObject json = Http.getHoleCurve(machineID,extensionNum,holeNum,start,end);

        if (json.get("code").getAsInt() == 0){
            mHoleCurve = json.get("lists").getAsJsonArray();
            mHandler.sendEmptyMessage(40);
        }
    }

    View.OnClickListener onBtnBackClickListener =  new  View.OnClickListener() {
        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        public void onClick(View v){
            Intent intent = new Intent(ActivityGrowCurve.this, ActivityBoard.class);
            startActivity(intent);
        }
    };

    class MyThread extends Thread
    {
        Boolean closed = false;
        public  String machineID;
        public  String extensionNum;
        public String holeNum;
        public String start;
        public String end;

        public  void close(){
            closed = true;
        }
        public  void open(){
            closed = false;
        }
        public void run() {
            while (true) {

                if( closed){
                    continue;
                }


                try {
                    httpHoleCurve(machineID,extensionNum,holeNum,start,end);
                    sleep(1000*60*10);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                try {

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }


            }
        }
    }

    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {

            if (msg.what == 40) {

            }
            return true;
        }
    });


}
