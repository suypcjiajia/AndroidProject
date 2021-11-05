package com.example.android.bcphone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        new MyThread().start();
    }


    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (msg.what == HandleId.welcomeEnd) {

                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
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

            mHandler.sendEmptyMessage(HandleId.welcomeEnd);

        }
    }
}
