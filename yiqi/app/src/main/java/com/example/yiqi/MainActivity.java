package com.example.yiqi;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import com.example.tool.Http;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ImageButton btn = findViewById(R.id.btnMainLogin);
        btn.setOnClickListener(new View.OnClickListener(){
            public  void  onClick(View v){
               Intent intent = new Intent(MainActivity.this, LoginActivity.class);
               startActivity(intent);
            }
        });

        findViewById(R.id.myhome).setVisibility(View.VISIBLE);
        findViewById(R.id.mydevice).setVisibility(View.INVISIBLE);
        findViewById(R.id.myquery).setVisibility(View.INVISIBLE);
        findViewById(R.id.myme).setVisibility(View.INVISIBLE);

        findViewById(R.id.btnBottonHome).setBackgroundResource(R.drawable.home1);

        httpBaoyang();

        thread.start();

        System.out.println("onCreate");
    }

    @Override
    protected void onStart(){
        super.onStart();
        Intent intent = getIntent();

        String userName = intent.getStringExtra("userName");
        TextView userTxt = findViewById(R.id.textViewUserName);
        userTxt.setText(userName);

        System.out.println("onStart");
    }

    @Override
    public void onRestart(){
        super.onRestart();
        System.out.println("onRestart");
    }

    @Override
    public void onResume(){
        super.onResume();
        System.out.println("onResume");
    }

    @Override
    public void onPause(){
        super.onPause();
        System.out.println("onPause");
    }

    @Override
    public void onStop(){
        super.onStop();
        System.out.println("onStop");
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        System.out.println("onDestroy");
    }

    public void onBtnBottonHomeClick(View v){
        //Fragment home =  getSupportFragmentManager().findFragmentById(R.id.myhome);
        findViewById(R.id.myhome).setVisibility(View.VISIBLE);
        findViewById(R.id.mydevice).setVisibility(View.INVISIBLE);
        findViewById(R.id.myquery).setVisibility(View.INVISIBLE);
        findViewById(R.id.myme).setVisibility(View.INVISIBLE);

        findViewById(R.id.btnBottonHome).setBackgroundResource(R.drawable.home1);
        findViewById(R.id.btnBottonDevice).setBackgroundResource(R.drawable.device2);
        findViewById(R.id.btnBottonQuery).setBackgroundResource(R.drawable.query2);
        findViewById(R.id.btnBottonMe).setBackgroundResource(R.drawable.user2);


    }

    public void onBtnBottonDeviceClick(View v){
        findViewById(R.id.myhome).setVisibility(View.INVISIBLE);
        findViewById(R.id.mydevice).setVisibility(View.VISIBLE);
        findViewById(R.id.myquery).setVisibility(View.INVISIBLE);
        findViewById(R.id.myme).setVisibility(View.INVISIBLE);

        findViewById(R.id.btnBottonHome).setBackgroundResource(R.drawable.home2);
        findViewById(R.id.btnBottonDevice).setBackgroundResource(R.drawable.device1);
        findViewById(R.id.btnBottonQuery).setBackgroundResource(R.drawable.query2);
        findViewById(R.id.btnBottonMe).setBackgroundResource(R.drawable.user2);
    }

    public void onBtnBottonQueryClick(View v){
        findViewById(R.id.myhome).setVisibility(View.INVISIBLE);
        findViewById(R.id.mydevice).setVisibility(View.INVISIBLE);
        findViewById(R.id.myquery).setVisibility(View.VISIBLE);
        findViewById(R.id.myme).setVisibility(View.INVISIBLE);

        findViewById(R.id.btnBottonHome).setBackgroundResource(R.drawable.home2);
        findViewById(R.id.btnBottonDevice).setBackgroundResource(R.drawable.device2);
        findViewById(R.id.btnBottonQuery).setBackgroundResource(R.drawable.query1);
        findViewById(R.id.btnBottonMe).setBackgroundResource(R.drawable.user2);
    }

    public void onBtnBottonMeClick(View v){
        findViewById(R.id.myhome).setVisibility(View.INVISIBLE);
        findViewById(R.id.mydevice).setVisibility(View.INVISIBLE);
        findViewById(R.id.myquery).setVisibility(View.INVISIBLE);
        findViewById(R.id.myme).setVisibility(View.VISIBLE);

        findViewById(R.id.btnBottonHome).setBackgroundResource(R.drawable.home2);
        findViewById(R.id.btnBottonDevice).setBackgroundResource(R.drawable.device2);
        findViewById(R.id.btnBottonQuery).setBackgroundResource(R.drawable.query2);
        findViewById(R.id.btnBottonMe).setBackgroundResource(R.drawable.user1);
    }

    Thread  thread = new Thread()
    {
        public void run() {

            while(true) {

                try {
                    httpOnlinesummary();
                    httpExceptionsummary();
                    httpAlldisksummary();
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }

                try {
                    sleep(5000);
                } catch (Exception e) {
                    System.out.println(e.getMessage()  );
                }
                System.out.println("MainActivity run");

                mHandler.sendEmptyMessage(20);
            }
        }
    };

    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (msg.what == 20) {

            }
            return true;
        }
    });

    private void httpOnlinesummary(){

        List<PieEntry> strings = new ArrayList<>();
        JsonObject json = Http.onlinesummary();
        if (json.get("code").getAsInt() == 0) {
            String online_count = json.get("online_count").getAsString();
            String count = json.get("count").getAsString();
            TextView t = findViewById(R.id.textViewOnline2);
            t.setText(online_count + "/" + count);

            TextView t2 = findViewById(R.id.textViewOnline3);
            t2.setText("总共" +  count + "台设备,在线"  + online_count + "台");

            strings.add(new PieEntry( Integer.parseInt(online_count) ,""));
            strings.add(new PieEntry(  Integer.parseInt(count) - Integer.parseInt(online_count),""));

        } else {
            TextView t = findViewById(R.id.textViewOnline2);
            t.setText("0/0");
            TextView t2 = findViewById(R.id.textViewOnline3);
            t2.setText("总共0台设备,在线0台");

            strings.add(new PieEntry( 0 ,""));
            strings.add(new PieEntry(  0,""));
        }

        setCharData(strings,1);
    }

    private void httpExceptionsummary(){

        List<PieEntry> strings = new ArrayList<>();
        JsonObject json = Http.exceptionsummary();
        if (json.get("code").getAsInt() == 0) {
            String exception_count = json.get("exception_count").getAsString();
            String count = json.get("count").getAsString();


            strings.add(new PieEntry( Integer.parseInt(exception_count) ,""));
            strings.add(new PieEntry(  Integer.parseInt(count) - Integer.parseInt(exception_count),""));

        } else {

            strings.add(new PieEntry(  0,""));
        }

        setCharData(strings,2);
    }

    private void httpAlldisksummary(){

        List<PieEntry> strings = new ArrayList<>();
        JsonObject json = Http.exceptionsummary();
        if (json.get("code").getAsInt() == 0) {
            String available = json.get("available").getAsString();
            String total = json.get("total").getAsString();


            strings.add(new PieEntry( Integer.parseInt(available) ,""));
            strings.add(new PieEntry(  Integer.parseInt(total) - Integer.parseInt(total),""));

        } else {

            strings.add(new PieEntry(  0,""));
        }

        setCharData(strings,3);
    }

    private void httpBaoyang(){
        String[] data = {"Sunny","Cloudy","Few Clouds","Partly Cloudy","Overcast","Windy","Calm","Light Breeze",
                "Moderate","Fresh Breeze","Strong Breeze","High Wind","Gale","Strong Gale","Storm","Violent Storm","Hurricane",
                "Tornado","Tropical Storm","Shower Rain","Heavy Shower Rain","Thundershower","Heavy Thunderstorm",
                "Thundershower with hail" ,"Unknown"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, data);
        ListView listView = (ListView)findViewById(R.id.listViewBaoyang);
        //将构建好的适配器对象传进去
        listView.setAdapter(adapter);

    }


    public void setCharData(List<PieEntry> strings,int type){

        PieChart chart1 ;
        ArrayList<Integer> colors = new ArrayList<>();
        if(type == 1){
            chart1 = findViewById(R.id.chart1);

            colors.add(getResources().getColor(R.color.greencao));
            colors.add(getResources().getColor(R.color.blackb3));


        }else if( type == 2){
            chart1 = findViewById(R.id.chart2);

            colors.add(getResources().getColor(R.color.red));
            colors.add(getResources().getColor(R.color.blackb3));


        }else if( type == 3){
            chart1 = findViewById(R.id.chart3);

            colors.add(getResources().getColor(R.color.redju));
            colors.add(getResources().getColor(R.color.blackb3));


        }else{
            return;
        }
        PieDataSet dataSet = new PieDataSet(strings,"");





        dataSet.setColors(colors);

        PieData pieData = new PieData(dataSet);
        pieData.setDrawValues(false);


        Description description = new Description();
        description.setText("");
        chart1.setDescription(description);

        chart1.setData(pieData);
        chart1.invalidate();
    }

}