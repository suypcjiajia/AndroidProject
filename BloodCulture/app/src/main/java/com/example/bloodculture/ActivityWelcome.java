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

        thread.start();
    }


    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (msg.what == 0x11) {

                Intent intent = new Intent(ActivityWelcome.this, ActivityMain.class);
                startActivity(intent);

            }
            return true;
        }
    });






    Thread  thread = new Thread()
    {
        public void run() {

            try {
                sleep(1000*3);
            } catch (InterruptedException e) {

            }

            mHandler.sendEmptyMessage(0x11);

        }
    };

}
