package com.dl.d2ia;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import com.dl.com.dl.bo.SettingBo;
import com.dl.db.SettingDao;
import com.dl.db.TestResultDao;

public class SettingDialog {

    private  static SettingDao settingDao;
    private  static EditText edtNumber;
    private  final static String fazhi = "fazhi";

    public SettingDialog(){

    }

    //初始化并弹出对话框方法
    public static void showDialog(Context context){

        settingDao = new SettingDao(context);
        SettingBo bo= settingDao.getOne(fazhi);

        View dialogXml = LayoutInflater.from(context).inflate(R.layout.setting,null,false);

        edtNumber = dialogXml.findViewById(R.id.edtNumber);
        final AlertDialog dialog = new AlertDialog.Builder(context).setView(dialogXml).create();

        if(bo == null){
            edtNumber.setText("8");
        }else {
            edtNumber.setText(bo.value);
            System.out.println( "bo.value"  + bo.value);
        }



        dialogXml.findViewById(R.id.imageSave).setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){


                SettingBo bo = new SettingBo();
                bo.id = fazhi;
                bo.value =  edtNumber.getText().toString();
                settingDao.insert(bo);

                dialog.cancel();
            }
        });


        dialogXml.findViewById(R.id.imageCancel).setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                dialog.cancel();
            }
        });

        dialog.show();
        //此处设置位置窗体大小，我这里设置为了手机屏幕宽度的3/4  注意一定要在show方法调用后再写设置窗口大小的代码，否则不起效果会
        //dialog.getWindow().setLayout((ScreenUtils.getScreenWidth(getContext())/4*3), LinearLayout.LayoutParams.WRAP_CONTENT);
    }
}
