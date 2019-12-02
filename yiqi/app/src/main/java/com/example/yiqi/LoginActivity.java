package com.example.yiqi;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.tool.Http;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class LoginActivity extends AppCompatActivity {

    JsonObject mResponse;
    String userName;
    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage( Message msg) {
            if (msg.what == 0x1) {
                if( mResponse.get("code").getAsInt() == 0){
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("userName",userName);
                    startActivity(intent);
                }else{
                    System.out.println("showMsg:" + mResponse.get("status").getAsString());
                    showMsg(mResponse.get("status").getAsString());
                }

            }
            return true;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        Button btn = findViewById(R.id.btnLoginLogin);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText edtName = findViewById(R.id.edtName);
                EditText edtPwd = findViewById(R.id.edtPwd);
                final String name = edtName.getText().toString();
                final String pwd = edtPwd.getText().toString();
                System.out.println( "user:" + name + " pwd:" + pwd);

                userName = name;



                new Thread(){
                    public void run(){
                        mResponse = Http.login(name,pwd);

                        mHandler.sendEmptyMessage(1);
                    }
                }.start();


            }
        });

    }

    private void showMsg(String msg){
        AlertDialog alertDialog1 = new AlertDialog.Builder(this)
                .setTitle(msg)//标题
                .setMessage(msg)//内容
                .setIcon(R.mipmap.ic_launcher)//图标
                .create();
        alertDialog1.show();
    }
}
