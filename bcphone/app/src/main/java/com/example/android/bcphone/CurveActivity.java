package com.example.android.bcphone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.text.ParseException;
import java.util.ArrayList;

import entity.ShareData;
import entity.ShowMsg;
import tool.Http;
import tool.TimeUtil;

public class CurveActivity extends AppCompatActivity {

    LineChart lineChart;
    View viewTop;
    CurveActivity me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curve);

        viewTop = findViewById(R.id.board_top);
        ImageButton back = viewTop.findViewById(R.id.back);
        back.setOnClickListener(onBtnBackClickListener);


        lineChart = findViewById(R.id.lineChar);

        me = this;

    }



    @Override
    protected void onPostResume() {
        super.onPostResume();
        GetCurveThread getCurveThread =  new GetCurveThread();
        getCurveThread.start();
    }

    void reflash(Boolean init){
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        //设置数据1  参数1：数据源 参数2：图例名称
        ArrayList<Entry> values1 = new ArrayList<>();

        int yMin = -1;
        int yMax = -1;

        if( ShareData.getCurve.has(ShareData.PutInTime + ShareData.HoleNum)){

            JsonArray array =  ShareData.getCurve.get(ShareData.PutInTime + ShareData.HoleNum).getAsJsonArray();


            if( init || array.size() == 0){//初始化(显示一个点0，0)
                values1.add(new Entry(0, 0));
            }else {
                for (int i = 0; i < array.size(); i++) {
                    JsonObject obj = array.get(i).getAsJsonObject();
                    String x = obj.get("time_x").getAsString();
                    int y = obj.get("y").getAsInt();
                    long lx;
                    try {
                        lx = TimeUtil.stringToLong(x, "yyyy-MM-dd HH:mm:ss");
                    } catch (ParseException e) {
                        System.out.println(e.getMessage());
                        continue;
                    }
                    values1.add(new Entry(lx, y));
                    if(yMax == -1){
                        yMax = y;
                    }
                    if(yMin == -1){
                        yMin = y;
                    }
                    if(y > yMax){
                         yMax = y;
                    }
                    if(y < yMin){
                        yMax = y;
                    }
                }
            }
        }else{
            values1.add(new Entry(0, 0));
        }


        LineDataSet set1 = new LineDataSet(values1, String.format("%s%d瓶生长曲线",ShareData.getMaching( ShareData.ExtensionNum),ShareData.HoleNum + 1));
        set1.setColor(Color.GRAY);
        set1.setCircleColor(Color.GRAY);
        set1.setLineWidth(1f);//设置线宽
        set1.setCircleRadius(1f);//设置焦点圆心的大小
        set1.setDrawCircleHole(false);
        set1.setHighlightEnabled(false);//是否禁用点击高亮线
        set1.setValueTextSize(1f);//设置显示值的文字大小
        set1.setValueTextColor(0x00ffffff);
        set1.setDrawFilled(false);//设置禁用范围背景填充
        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);//设置曲线展示为圆滑曲线（

        //保存LineDataSet集合
        //ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1); // add the datasets



        //创建LineData对象 属于LineChart折线图的数据集合
        LineData data = new LineData(dataSets);
        // 添加到图表中
        lineChart.setData(data);

        //获取此图表的x轴
        XAxis xAxis = lineChart.getXAxis();
        YAxis leftYAxis = lineChart.getAxisLeft();
        YAxis rightYaxis = lineChart.getAxisRight();
        leftYAxis.setAxisMinimum(yMin - yMax);
        leftYAxis.setAxisMaximum(yMax*2);
        rightYaxis.setAxisMinimum(yMin - yMax);
        rightYaxis.setAxisMaximum(yMax*2);
        xAxis.setValueFormatter(iAxisValueFormatter);
        xAxis.setDrawLabels(true);//绘制标签  指x轴上的对应数值
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//设置x轴的显示位置
        //绘制图表
        lineChart.invalidate();
        Description description1 = new Description();
        description1.setText("");
        lineChart.setDescription(description1);//去掉描述
        //绘制图表
        lineChart.invalidate();
    }

    View.OnClickListener onBtnBackClickListener = new View.OnClickListener() {
        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        public void onClick(View v) {
            Intent intent = new Intent(CurveActivity.this, MainActivity.class);
            startActivity(intent);
        }
    };


    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (msg.what == HandleId.getCurve) {
                reflash(false);
            }
            if(msg.what == HandleId.err){
                Toast.makeText(me,((ShowMsg)msg.obj).msg,Toast.LENGTH_LONG).show();
            }
            return true;
        }
    });


    class GetCurveThread extends  Thread
    {

        public void run(){
            JsonObject ret = Http.getCurve(ShareData.PutInTime,ShareData.HoleNum);
            if(ret.get("state").getAsInt() == 0){
                JsonElement element = ret.get("result");
                ShareData.getCurve.add(ShareData.PutInTime + ShareData.HoleNum,element);
                Message msg = new Message();
                msg.what = HandleId.getCurve;
                mHandler.sendMessage(msg);
            }else{
                Message msg = new Message();
                msg.what = HandleId.err;
                ShowMsg showMsg = new ShowMsg();
                showMsg.msg = ret.get("msg").getAsString();
                showMsg.state = ret.get("state").getAsInt();
                msg.obj = showMsg;
                mHandler.sendMessage(msg);
            }


        }
    }


    IAxisValueFormatter iAxisValueFormatter = new IAxisValueFormatter() {
        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            String timestr;

            if(value < 1000000){
                return "";
            }

            try{
                timestr = TimeUtil.longToString((long)value,"yyyy-MM-dd HH:mm:ss");
                System.out.println("xxx:" + value  + " " + timestr);
                if( timestr != null && timestr.length() ==19){
                    return timestr.substring(11,16);
                }
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
            return "";

        }
    };
}
