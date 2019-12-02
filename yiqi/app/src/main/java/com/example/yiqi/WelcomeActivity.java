package com.example.yiqi;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tool.Http;

public class WelcomeActivity extends AppCompatActivity {

    private Boolean threadExit = false;

    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (msg.what == 0x11) {
                if (threadExit) {
                    return true;
                }
                ProgressBar bar = findViewById(R.id.progressBar);
                int pro = bar.getProgress();
                if (pro >= 100) {
                    threadExit = true;
                    Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                bar.setProgress(bar.getProgress() + 20);

            }
            return true;
        }
    });






    Thread  thread = new Thread()
    {
        public void run() {

            while(!threadExit) {
                try {
                    sleep(500);
                } catch (InterruptedException e) {

                }
                System.out.println("run");

                mHandler.sendEmptyMessage(0x11);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        thread.start();
    }


}
