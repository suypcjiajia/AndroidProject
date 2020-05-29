package com.example.tool;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.bloodculture.R;

/**
 * 手机厂商判断和打开权限设置
 */
public class PhoneMaderSetting {


    /**
     * 是否oppo
     * @return
     */
    public static boolean isOPPO() {
        return Build.BRAND != null && Build.BRAND.toLowerCase().equals("oppo");
    }

    /**
     * 跳到Oppo安全管家
     * @param context
     */
    public static void goOPPOSetting( Context context) {
        try {
            showActivity("com.coloros.phonemanager",context);
        } catch (Exception e1) {
            try {
                showActivity("com.oppo.safe",context);
            } catch (Exception e2) {
                try {
                    showActivity("com.coloros.oppoguardelf",context);
                } catch (Exception e3) {
                    showActivity("com.coloros.safecenter",context);
                }
            }
        }
    }

    /**
     * 是否华为
     * @return
     */
    public static boolean isHuawei() {
        if (Build.BRAND == null) {
            return false;
        } else {
            return Build.BRAND.toLowerCase().equals("huawei") || Build.BRAND.toLowerCase().equals("honor");
        }
    }

    /**
     * 跳转华为手机管家的启动管理页
     */
    public static void goHuaweiSetting(Context context) {
        try {
            showActivity("com.huawei.systemmanager",
                    "com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity",context);
        } catch (Exception e) {
            showActivity("com.huawei.systemmanager",
                    "com.huawei.systemmanager.optimize.bootstart.BootStartActivity",context);
        }
    }

    /**
     * 是否小米
     * @return
     */
    public static boolean isXiaomi() {
        return Build.BRAND != null && Build.BRAND.toLowerCase().equals("xiaomi");
    }

    /**
     * 跳转小米安全中心的自启动管理页面
     */
    public static void goXiaomiSetting(Context context) {
        showActivity("com.miui.securitycenter",
                "com.miui.permcenter.autostart.AutoStartManagementActivity",context);
    }

    /**
     * 是否vivo
     * @return
     */
    public static boolean isVIVO() {
        return Build.BRAND != null && Build.BRAND.toLowerCase().equals("vivo");
    }

    /**
     * 跳转 VIVO 手机管家
     */
    public static void goVIVOSetting(Context context) {
        showActivity("com.iqoo.secure",context);
    }

    /**
     * 是否魅族
     * @return
     */
    public static boolean isMeizu() {
        return Build.BRAND != null && Build.BRAND.toLowerCase().equals("meizu");
    }


    /**
     * 跳转魅族手机管家
     */
    public static void goMeizuSetting(Context context) {
        showActivity("com.meizu.safe",context);
    }

    /**
     * 是否三星
     * @return
     */
    public static boolean isSamsung() {
        return Build.BRAND != null && Build.BRAND.toLowerCase().equals("samsung");
    }

    /**
     * 跳转三星智能管理器
     */
    public static void goSamsungSetting(Context context) {
        try {
            showActivity("com.samsung.android.sm_cn",context);
        } catch (Exception e) {
            showActivity("com.samsung.android.sm",context);
        }
    }


    /**
     * 是否乐视
     * @return
     */
    public static boolean isLeTV() {
        return Build.BRAND != null && Build.BRAND.toLowerCase().equals("letv");
    }

    /**
     * 跳转乐视手机管家
     */
    public static void goLetvSetting(Context context) {
        showActivity("com.letv.android.letvsafe",
                "com.letv.android.letvsafe.AutobootManageActivity",context);
    }

    /**
     * 是否锤子
     * @return
     */
    public static boolean isSmartisan() {
        return Build.BRAND != null && Build.BRAND.toLowerCase().equals("smartisan");
    }

    /**
     * 跳转到锤子手机管理
     */
    public static void goSmartisanSetting(Context context) {
        showActivity("com.smartisanos.security",context);
    }

    /**
     * 跳转到指定应用的首页
     */
    private static void showActivity(@NonNull String packageName, Context context) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        context.startActivity(intent);
    }

    /**
     * 跳转到指定应用的指定页面
     */
    private static void showActivity(@NonNull String packageName, @NonNull String activityDir,Context context) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(packageName, activityDir));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    /**
     * 申请加入电源白名单
     * @param context
     */
    public  static  void showBatteryOptimizations(Context context){
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if( !isIgnoringBatteryOptimizations(context)){
                requestIgnoreBatteryOptimizations(context);
            }
        }else{
            showTipsDialog(context);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean isIgnoringBatteryOptimizations(Context context) {
        boolean isIgnoring = false;
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        if (powerManager != null) {
            isIgnoring = powerManager.isIgnoringBatteryOptimizations(context.getPackageName());
        }
        return isIgnoring;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void requestIgnoreBatteryOptimizations(Context context) {
        try {
            Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + context.getPackageName()));
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private  static void showTipsDialog(Context context) {
        //提示弹窗

        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setTitle("优化电源以保活app")//标题
                .setMessage("安卓5.0版本以下需要手动优化电源\n" +
                        "位置:设置->电池")//内容
                .setIcon(R.mipmap.app_icon)//图标
                .setPositiveButton(R.string.ok_title,lis)
                .create();
        alertDialog.show();

    }

    static DialogInterface.OnClickListener lis = new DialogInterface.OnClickListener(){
        public void onClick(DialogInterface dialog, int which){
        }
    };
}
