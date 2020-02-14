package com.example.bloodculture;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.tool.Http;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class ActivityBoard extends AppCompatActivity {

    JsonArray mHoleSummary;//瓶孔预览
    MyThread thread = new MyThread();

    TextView textViewCulture;
    TextView textViewPositive;
    TextView textViewNegative;
    TextView textViewAnoPositive;
    TextView textViewAnoNegative;
    Spinner mSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        View viewTop = findViewById(R.id.board_top);
        ImageButton back = viewTop.findViewById(R.id.back);
        back.setOnClickListener(onBtnBackClickListener);

        View viewHole = findViewById(R.id.board_holenums);
        textViewCulture = viewHole.findViewById(R.id.textViewCulture);
        textViewPositive = viewHole.findViewById(R.id.textViewPositive);
        textViewNegative = viewHole.findViewById(R.id.textViewNegative);
        textViewAnoPositive = viewHole.findViewById(R.id.textViewAnoPositive);
        textViewAnoNegative = viewHole.findViewById(R.id.textViewAnoNegative);

        mSpinner = findViewById(R.id.spinner);
        String[] arry = {"0","1","2"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (this,android.R.layout.simple_spinner_item,arry);
        mSpinner.setAdapter(arrayAdapter);
        mSpinner.setOnItemSelectedListener(onItemSelectedListener);

        thread.open();
        thread.start();

    }

    @Override
    public void onStart(){
        super.onStart();
        System.out.println("ActivityBoard onStart");
        Intent intent = getIntent();
        thread.machineID = intent.getStringExtra("MachineID");
        thread.extensionNum = intent.getStringExtra("ExtensionNum");
        System.out.println("ActivityBoard machineID:" + thread.machineID + " ExtensionNum:" + thread.extensionNum);
        ((TextView)findViewById(R.id.textViewFenji)).setText( "分机" + thread.extensionNum);

    }


    @Override
    public void onRestart(){
        super.onRestart();
        System.out.println("ActivityBoard onRestart");
        thread.open();
    }

    @Override
    public void onResume(){
        super.onResume();
        System.out.println("ActivityBoard onResume");
    }

    @Override
    public void onPause(){
        super.onPause();
        System.out.println("ActivityBoard onPause");
    }

    @Override
    public void onStop(){
        super.onStop();
        System.out.println("ActivityBoard onStop");
        thread.close();
    }

    public void onBtnGrowCurveClick(View v){
        Intent intent = new Intent(this, ActivityGrowCurve.class);
        intent.putExtra("machineID",thread.machineID);
        intent.putExtra("extensionNum",thread.extensionNum);
        intent.putExtra("machineID","0");

        startActivity(intent);
    }

    public void onBtnPepoleClick(View v){
        Intent intent = new Intent(this, ActivityPepole.class);
        startActivity(intent);
    }

    private void makeBoard(){

        int nums = mHoleSummary.size();
        GridLayout gridLayout = findViewById(R.id.gridLayoutBottle);
        gridLayout.removeAllViews();
        int gridWidth = gridLayout.getWidth();

        int cultureNum = 0;
        int positiveNum = 0;
        int negativeNum = 0;
        int anopositiveNum = 0;
        int anonegativeNum = 0;

        int columnCount = 10;
        int rowCount = nums%columnCount==0 ? nums/10:nums/10+1;
        gridLayout.setColumnCount(columnCount);
        gridLayout.setRowCount(rowCount);
        int holeWidth = gridWidth/columnCount;
        for( int i = 0 ; i < rowCount ; i++){
            for( int j = 0; j < columnCount ; j++){
                if( i*columnCount + j + 1 > nums){
                    break;
                }
                GridLayout.Spec rowSpec = GridLayout.spec(i);
                GridLayout.Spec cloumnSpec = GridLayout.spec(j);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams(rowSpec,cloumnSpec);

                params.width = 50;
                params.height = 50;
                int margin = (holeWidth - 50)/2;
                params.setMargins(margin,margin,margin,margin);
                TextView view = new TextView(this);
                JsonObject hole = mHoleSummary.get(i*columnCount + j).getAsJsonObject();

                view.setText(hole.get("HoleNum").getAsString());
                view.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                int state = hole.get("CurrentState").getAsInt();
                if( state == HoleState.hole_invalid){
                    view.setBackgroundResource(R.drawable.button_circle_shape_invalid);

                }else if( state == HoleState.hole_empty){
                    view.setBackgroundResource(R.drawable.button_circle_shape_empty);
                }else if( state == HoleState.hole_culture){
                    view.setBackgroundResource(R.drawable.button_circle_shape_culture);
                    cultureNum++;
                }else if( state == HoleState.hole_positive){
                    view.setBackgroundResource(R.drawable.button_circle_shape_positive);
                    positiveNum++;
                }else if( state == HoleState.hole_negative){
                    view.setBackgroundResource(R.drawable.button_circle_shape_negative);
                    negativeNum++;
                }else if( state == HoleState.hole_anoposivive){
                    view.setBackgroundResource(R.drawable.button_circle_shape_anopositive);
                    anopositiveNum++;
                }else if( state == HoleState.hole_anonegative){
                    view.setBackgroundResource(R.drawable.button_circle_shape_anonegative);
                    anonegativeNum++;
                }

                gridLayout.addView(view,params);

                view.setOnClickListener(onHoleClick);
            }
        }
        textViewCulture.setText(String.valueOf(cultureNum));
        textViewPositive.setText(String.valueOf(positiveNum));
        textViewNegative.setText(String.valueOf(negativeNum));
        textViewAnoPositive.setText(String.valueOf(anopositiveNum));
        textViewAnoNegative.setText(String.valueOf(anonegativeNum));

    }

    private void httpHoleSummary(String machineID,String extensionNum){

        //JsonObject json = Http.getHoleSummary("bt-64-No.1","0");
        JsonObject json = Http.getHoleSummary(machineID,extensionNum);

        if (json.get("code").getAsInt() == 0){
            mHoleSummary = json.get("lists").getAsJsonArray();
            System.out.println("httpHoleSummary:" + mHoleSummary.toString());
            mHandler.sendEmptyMessage(30);
        }
    }


    View.OnClickListener onBtnBackClickListener =  new  View.OnClickListener() {
        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        public void onClick(View v){
            Intent intent = new Intent(ActivityBoard.this, ActivityMain.class);
            startActivity(intent);
        }
    };



    class MyThread extends Thread
    {
        Boolean closed = false;
        public  String machineID;
        public  String extensionNum;

        public  void close(){
            closed = true;
        }
        public  void open(){
            closed = false;
        }
        public void run() {
            while (true) {

                if( closed){
                    continue;
                }


                try {
                    httpHoleSummary(machineID,extensionNum);
                    sleep(2000);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                try {

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }


            }
        }
    }

    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {

            if (msg.what == 30) {
                makeBoard();
            }
            return true;
        }
    });

     AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener(){

        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        }

        public void onNothingSelected(AdapterView<?> parent){

        }
    };


    View.OnClickListener onHoleClick = new View.OnClickListener(){
        public void onClick(View v){
            String holeNum = ((TextView) v).getText().toString();
        }
    };

}
