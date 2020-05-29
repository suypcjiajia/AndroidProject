package com.example.bloodculture;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ProgressBar;

public class ActivityWelcome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        new MyThread().start();
    }

    @Override
    protected void onStart(){
        super.onStart();
    }


    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (msg.what == HandleWhat.welcomeEnd) {

                Intent intent = new Intent(ActivityWelcome.this, ActivityMain.class);
                startActivity(intent);

                finish();

            }
            return true;
        }
    });






    class MyThread  extends Thread
    {
        @Override
        public void run() {

            try {
                sleep(1000*2);
            } catch (InterruptedException e) {

            }

            mHandler.sendEmptyMessage(HandleWhat.welcomeEnd);

        }
    }

}
