package com.example.bloodculture;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.tool.Http;
import com.example.tool.ShapeUtil;
import com.example.tool.ShowTips;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class ActivityBoard extends AppCompatActivity {

    JsonArray mHoleSummary;//瓶孔预览(从服务器拿的数据，有可能是空的，也有可能不全)
    MyThread thread ;//线程任务：从服务器获取瓶孔预览
    BottolView currentHoleView = null; //用户选了哪一个瓶子(view)
    int mHoleCount;//面板共有多少瓶子
    String machineID;
    int mBaoyangHoleNum;
    int callHttp = 0; //==1时表示从曲线返回，这时不用去刷新（不用http请求）



    View viewTop;
    TextView textViewCulture;
    TextView textViewPositive;
    TextView textViewNegative;
    TextView textViewAnoPositive;
    TextView textViewAnoNegative;
    Spinner mSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("ActivityBoard onCreate");
        setContentView(R.layout.activity_board);

        viewTop = findViewById(R.id.board_top);
        ImageButton back = viewTop.findViewById(R.id.back);
        back.setOnClickListener(onBtnBackClickListener);

        View viewHole = findViewById(R.id.board_holenums);
        textViewCulture = viewHole.findViewById(R.id.textViewCulture);
        textViewPositive = viewHole.findViewById(R.id.textViewPositive);
        textViewNegative = viewHole.findViewById(R.id.textViewNegative);
        textViewAnoPositive = viewHole.findViewById(R.id.textViewAnoPositive);
        textViewAnoNegative = viewHole.findViewById(R.id.textViewAnoNegative);

        mSpinner = findViewById(R.id.spinner);
        mSpinner.setOnItemSelectedListener(onItemSelectedListener);



    }

    @Override
    public void onStart() {
        super.onStart();
        System.out.println("ActivityBoard onStart");

        thread = new MyThread();
        thread.who = 1;

        Intent intent = getIntent();
        machineID = intent.getStringExtra("MachineID");
        thread.machineID = machineID;
        thread.extensionNum = intent.getStringExtra("ExtensionNum");
        mBaoyangHoleNum = intent.getIntExtra("HoleNum",-1);
        System.out.println("ActivityBoard machineID:" + thread.machineID + " ExtensionNum:" + thread.extensionNum);



        String type = intent.getStringExtra("type");
        if (type.isEmpty()) {
            type = "Bt";
            mHoleCount = 60;
        } else {
            if (type.length() < 4) {
                mHoleCount = 60;
            } else
                mHoleCount = Integer.parseInt(type.substring(2));
        }
        ((TextView) viewTop.findViewById(R.id.txtDeviceType)).setText(machineID);

        System.out.println("ActivityBoard mHoleCount:" + mHoleCount);

        MyThread thread2 = new MyThread();
        thread2.who = 2;
        thread2.machineID = thread.machineID;
        thread2.extensionNum = thread.extensionNum;


        if( callHttp != 1) {
            thread.start();
        }
        if( !ShareData.mExtensioninfo.containsKey(thread2.machineID)) {
            if (callHttp != 1) {
                thread2.start();
            }
        }
        else{
            if(callHttp != 1) {
                JsonArray array = ShareData.mExtensioninfo.get(thread2.machineID);
                setmSpinner(array);

                selectionSpinner(thread.extensionNum, thread2.machineID);
            }

        }

    }

    @Override
    public void onNewIntent(Intent intent){
        super.onNewIntent(intent);
        callHttp = intent.getIntExtra("callHttp",-1);
    }

    private void selectionSpinner(String extensionNum,String machineID){
        JsonArray array = ShareData.mExtensioninfo.get(machineID);
        for(int i = 0; i < array.size(); i++){
            if(array.get(i).getAsString().equals(extensionNum) ){
                mSpinner.setSelection(i);
            }
        }
    }

    private void setmSpinner(JsonArray array){
        String[] arry = new String[array.size() ];
        for( int i = 0; i < array.size(); i++){

            if( array.get(i).getAsString().equals("0")){
                arry[i] = "主机" ;
            }else{
                arry[i] = "分机" +  array.get(i).getAsString();
            }

        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (this, R.layout.simple_item_fenji, arry);
        mSpinner.setAdapter(arrayAdapter);
    }


    @Override
    public void onRestart() {
        super.onRestart();
        System.out.println("ActivityBoard onRestart");
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("ActivityBoard onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        System.out.println("ActivityBoard onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        System.out.println("ActivityBoard onStop");
    }

    public void onBtnGrowCurveClick(View v) {
        if( currentHoleView == null ){
            ShowTips.showMsg("先点击选择瓶子", this);
            return;
        }
        Intent intent = new Intent(this, ActivityGrowCurve.class);
        intent.putExtra("MachineID", thread.machineID);
        intent.putExtra("ExtensionNum", thread.extensionNum);
        intent.putExtra("HoleNum", currentHoleView.getText());

        startActivity(intent);
    }

    public void onBtnPepoleClick(View v) {
        if( currentHoleView == null  ){
            ShowTips.showMsg("先点击选择瓶子", this);
            return;
        }
        Intent intent = new Intent(this, ActivityPepole.class);
        intent.putExtra("MachineID", thread.machineID);
        intent.putExtra("ExtensionNum", thread.extensionNum);
        intent.putExtra("HoleNum", currentHoleView.getText());
        startActivity(intent);
    }

    private void initBoard() {
        GridLayout gridLayout = findViewById(R.id.gridLayoutBottle);
        gridLayout.removeAllViews();
        int gridWidth = gridLayout.getWidth();
        System.out.println("GridLayout width:" + gridWidth);
        int columnCount = 6;
        int rowCount = mHoleCount % columnCount == 0 ? mHoleCount / columnCount : mHoleCount / columnCount + 1;
        gridLayout.setColumnCount(columnCount);
        gridLayout.setRowCount(rowCount);
        int holeWidth = gridWidth / columnCount;
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                if (i * columnCount + j + 1 > mHoleCount) {
                    break;
                }
                GridLayout.Spec rowSpec = GridLayout.spec(i);
                GridLayout.Spec cloumnSpec = GridLayout.spec(j);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams(rowSpec, cloumnSpec);

                params.width = holeWidth/2;
                params.height = params.width;
                int margin = (holeWidth - params.width) / 2;
                params.setMargins(margin, margin/3, margin, margin/3);
                BottolView view = new BottolView(this);
                view.setId(10000 + i * columnCount + j );
                view.setText(String.valueOf(i * columnCount + j + 1));
                view.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                view.setGravity(Gravity.CENTER_VERTICAL);

                view.setMyColor(0xff999999);
                view.setMyState(HoleState.hole_empty);
                view.back();

                gridLayout.addView(view, params);
                view.setOnClickListener(onHoleClick);
            }
        }
        textViewCulture.setText(String.valueOf(0));
        textViewPositive.setText(String.valueOf(0));
        textViewNegative.setText(String.valueOf(0));
        textViewAnoPositive.setText(String.valueOf(0));
        textViewAnoNegative.setText(String.valueOf(0));
    }

    private void updateHoleState() {

        int nums = mHoleSummary.size();
        GridLayout gridLayout = findViewById(R.id.gridLayoutBottle);

        int cultureNum = 0;
        int positiveNum = 0;
        int negativeNum = 0;
        int anopositiveNum = 0;
        int anonegativeNum = 0;


        for (int i = 0; i < nums; i++) {

            JsonObject hole = mHoleSummary.get(i).getAsJsonObject();

            int HoleNum = hole.get("HoleNum").getAsInt();
            if (HoleNum + 1 > mHoleCount) {
                continue;
            }

            BottolView view = gridLayout.findViewById(10000 + HoleNum);
            if (view == null) {
                continue;
            }

            int state = hole.get("CurrentState").getAsInt();
            view.setMyState(state);
            if (state == HoleState.hole_invalid) {
                view.setMyColor(0xffB5B510);

            } else if (state == HoleState.hole_empty) {
                view.setMyColor(0xff999999);

            } else if (state == HoleState.hole_culture) {
                view.setMyColor(0xff98B2E4);
                cultureNum++;
            } else if (state == HoleState.hole_positive) {
                view.setMyColor(0xffC90606);
                positiveNum++;
            } else if (state == HoleState.hole_negative) {
                view.setMyColor(0xffA2B792);
                negativeNum++;
            } else if (state == HoleState.hole_anoposivive) {
                view.setMyColor(0xffF2B76E);
                anopositiveNum++;
            } else if (state == HoleState.hole_anonegative) {
                view.setMyColor(0xffBDD391);
                anonegativeNum++;
            }
            if( HoleNum == mBaoyangHoleNum){
                view.selected();
                currentHoleView = view;
            }else {
                view.back();
            }
        }


        textViewCulture.setText(String.valueOf(cultureNum));
        textViewPositive.setText(String.valueOf(positiveNum));
        textViewNegative.setText(String.valueOf(negativeNum));
        textViewAnoPositive.setText(String.valueOf(anopositiveNum));
        textViewAnoNegative.setText(String.valueOf(anonegativeNum));

    }

    private void httpHoleSummary(String machineID, String extensionNum) {

        //JsonObject json = Http.getHoleSummary("bt-64-No.1","0");
        JsonObject json = Http.getHoleSummary(machineID, extensionNum);

        if (json.get("code").getAsInt() == 0) {
            mHoleSummary = json.get("lists").getAsJsonArray();
            mHandler.sendEmptyMessage(HandleWhat.getHoleSummary);
        } else {
            mHandler.sendEmptyMessage(HandleWhat.getGetHoleSummaryError);
        }
    }


    View.OnClickListener onBtnBackClickListener = new View.OnClickListener() {
        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        public void onClick(View v) {
            Intent intent = new Intent(ActivityBoard.this, ActivityMain.class);
            startActivity(intent);
        }
    };


    class MyThread extends Thread {


        public String machineID;
        public String extensionNum;
        public int who;


        public void run() {

            System.out.println("ActivityBoard tread run");


            try {
                if( 1 == who) {
                    httpHoleSummary(machineID, extensionNum);
                }else if(2 == who){
                    JsonObject json = Http.getExtensioninfo(machineID);

                    if (json.get("code").getAsInt() == 0) {
                        ShareData.mExtensioninfo.put(machineID,json.get("list").getAsJsonArray());
                        mHandler.sendEmptyMessage(HandleWhat.getExtensioninfo);
                    }
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            try {

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }


        }
    }

    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {

            if (msg.what == HandleWhat.getHoleSummary) {
                initBoard();

                updateHoleState();
            } else if (msg.what == HandleWhat.getGetHoleSummaryError) {
                initBoard();
            }else if(msg.what == HandleWhat.getExtensioninfo){
                JsonArray array = ShareData.mExtensioninfo.get(thread.machineID);
                setmSpinner(array);
                selectionSpinner(thread.extensionNum,thread.machineID);
            }
            return true;
        }
    });

    AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {

        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//            String txt = mSpinner.getSelectedItem().toString();
//            if( txt.isEmpty() || !txt.contains("分机")){
//                return;
//            }

            thread = new MyThread();
            thread.extensionNum = String.valueOf(position);
            thread.machineID = machineID;
            thread.who = 1;
            thread.start();

        }

        public void onNothingSelected(AdapterView<?> parent) {

        }
    };


    View.OnClickListener onHoleClick = new View.OnClickListener() {
        public void onClick(View v) {
            BottolView clickView = (BottolView)v;
            if(clickView.equals(currentHoleView)){
                return;
            }
            if( currentHoleView != null){
                currentHoleView.back();
            }

            clickView.selected();
            currentHoleView = clickView;
        }
    };

}
