package com.dl.d2ia;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import com.dl.db.TestResultDao;

public class InfoDialog {


    //初始化并弹出对话框方法
    public static void showDialog(Context context){

        View dialogXml = LayoutInflater.from(context).inflate(R.layout.enter_info,null,false);

        final EditText edtName = dialogXml.findViewById(R.id.edtName);
        final EditText edtNumber = dialogXml.findViewById(R.id.edtNumber);
        final EditText edtAge = dialogXml.findViewById(R.id.edtAge);
        final EditText edtTestMan = dialogXml.findViewById(R.id.edtTestMan);
        final RadioButton rdoBoy =  dialogXml.findViewById(R.id.rdoBoy);
        final RadioButton rdoGirl = dialogXml.findViewById(R.id.rdoGirl);
        final AlertDialog dialog = new AlertDialog.Builder(context).setView(dialogXml).create();

        edtAge.setText(TestResultDao.testRecord.age);
        edtName.setText(TestResultDao.testRecord.name);
        edtNumber.setText(TestResultDao.testRecord.id);
        edtTestMan.setText(TestResultDao.testRecord.testMan);

        if( TestResultDao.testRecord.sex.equals("男")){
            rdoBoy.setChecked(true);
        }else if( TestResultDao.testRecord.sex.equals("女")){
            rdoGirl.setChecked(true);
        }else{
            rdoBoy.setChecked(false);
            rdoGirl.setChecked(false);
        }

        dialogXml.findViewById(R.id.imageClear).setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                edtAge.setText("");
                edtName.setText("");
                edtTestMan.setText("");
                edtNumber.setText("");
                rdoBoy.setChecked(false);
                rdoGirl.setChecked(false);
            }
        });

        dialogXml.findViewById(R.id.imageSave).setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                TestResultDao.testRecord.age = edtAge.getText().toString();
                TestResultDao.testRecord.name =  edtName.getText().toString();
                TestResultDao.testRecord.testMan = edtTestMan.getText().toString();
                TestResultDao.testRecord.id = edtNumber.getText().toString();
                if( rdoBoy.isChecked()){
                    TestResultDao.testRecord.sex = "男";
                }else if( rdoGirl.isChecked()){
                    TestResultDao.testRecord.sex = "女";
                }else{
                    TestResultDao.testRecord.sex = "";
                }
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
