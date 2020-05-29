package com.example.tool;

import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Build;


import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationManagerCompat;

import com.example.bloodculture.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


public class NotificationUtil {

    Context mContext;
    //判断是否需要打开设置界面
    //@RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public  void openNotificationSetting(Context context) {
        mContext = context;
        if (!isNotificationEnabled()) {
            showSettingDialog();
        }
    }

    //判断该app是否打开了通知
    private boolean isNotificationEnabled() {
        boolean isOpened = false;
        try {
            isOpened = NotificationManagerCompat.from(mContext).areNotificationsEnabled();
        } catch (Exception e) {
            e.printStackTrace();
            isOpened = false;
        }
        return isOpened;

    }
    //打开手机设置页面

    /**
     * 假设没有开启通知权限，点击之后就需要跳转到 APP的通知设置界面，对应的Action是：Settings.ACTION_APP_NOTIFICATION_SETTINGS, 这个Action是 API 26 后增加的
     * 如果在部分手机中无法精确的跳转到 APP对应的通知设置界面，那么我们就考虑直接跳转到 APP信息界面，对应的Action是：Settings.ACTION_APPLICATION_DETAILS_SETTINGS
     */
    private  void gotoSet() {

        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= 26) {
            // android 8.0引导
            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            intent.putExtra("android.provider.extra.APP_PACKAGE", mContext.getPackageName());
        } else if (Build.VERSION.SDK_INT >= 21) {
            // android 5.0-7.0
            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            intent.putExtra("app_package", mContext.getPackageName());
            intent.putExtra("app_uid", mContext.getApplicationInfo().uid);
        } else {
            // 其他
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", mContext.getPackageName(), null));
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }



    public  void showSettingDialog() {
        //提示弹窗

        AlertDialog alertDialog = new AlertDialog.Builder(mContext)
                .setTitle("需要开启通知")//标题
                .setMessage("开启通知后，才能收到通知")//内容
                .setIcon(R.mipmap.app_icon)//图标
                .setPositiveButton(R.string.ok_title,lis)
                .setNegativeButton(R.string.cancel_title,lis)
                .create();
        alertDialog.show();

    }

    DialogInterface.OnClickListener lis = new DialogInterface.OnClickListener(){
        public void onClick(DialogInterface dialog, int which){
            System.out.println("DialogInterface.OnClickListener:" + which);

            if( -1 == which){
                gotoSet();
            }else if( -2 == which){


            }
        }
    };

}