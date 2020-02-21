package com.example.tool;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;

import com.example.bloodculture.R;

public class ShowTips {

    public static  void showMsg(String msg, Context context){
        AlertDialog alertDialog1 = new AlertDialog.Builder(context)
                .setTitle("温馨提示")//标题
                .setMessage(msg)//内容
                .setIcon(R.mipmap.app_icon)//图标
                .create();
        alertDialog1.show();
    }
}
