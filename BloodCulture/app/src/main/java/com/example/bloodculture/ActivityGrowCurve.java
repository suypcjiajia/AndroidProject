package com.example.bloodculture;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.tool.Http;
import com.example.tool.TimeUtil;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

public class ActivityGrowCurve extends AppCompatActivity {

    MyThread thread = new MyThread();
    JsonArray mHoleCurve;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("ActivityGrowCurve onCreate");
        setContentView(R.layout.activity_grow_curve);
        View viewTop = findViewById(R.id.top_in_grow_curve);
        ImageButton back = viewTop.findViewById(R.id.back);
        back.setOnClickListener(onBtnBackClickListener);
    }

    @Override
    public void onStart() {
        super.onStart();
        System.out.println("ActivityGrowCurve onStart");
        Intent intent = getIntent();
        thread.machineID = intent.getStringExtra("MachineID");
        thread.extensionNum = intent.getStringExtra("ExtensionNum");
        thread.holeNum = intent.getStringExtra("HoleNum");
        makeLineChartData(true);

        ((TextView) findViewById(R.id.textViewKongTips)).setText(thread.holeNum + "号孔生长曲线");

        thread.start();

    }

    @Override
    public void onStop() {
        super.onStop();
        System.out.println("ActivityGrowCurve onStop");

    }

    private void makeLineChartData(Boolean init) {

        ArrayList<Entry> values1 = new ArrayList<>();
        LineChart lineChart = findViewById(R.id.lineChar);

        if( init || mHoleCurve.size() == 0){//初始化(显示一个点0，0)
            values1.add(new Entry(0, 0));
        }else{
            for (int i = 0; i < mHoleCurve.size(); i++) {
                String elem = mHoleCurve.get(mHoleCurve.size() - i - 1).getAsString();//"2020-02-17 16:30:01,92"
                String[] dataarry = elem.split(",");
                if (dataarry.length != 2) {
                    continue;
                }
                long x_time;
                try {
                    x_time = TimeUtil.stringToLong(dataarry[0], "yyyy-MM-dd HH:mm:ss");
                } catch (Exception e) {
                    continue;
                }
                values1.add(new Entry(x_time, Integer.parseInt(dataarry[1])));
            }

        }





        //设置数据1  参数1：数据源 参数2：图例名称
        LineDataSet set1 = new LineDataSet(values1, "生长曲线");
        set1.setColor(Color.GRAY);
        set1.setCircleColor(Color.GRAY);
        set1.setLineWidth(1f);//设置线宽
        set1.setCircleRadius(2f);//设置焦点圆心的大小
        set1.enableDashedHighlightLine(10f, 5f, 0f);//点击后的高亮线的显示样式
        set1.setHighlightLineWidth(2f);//设置点击交点后显示高亮线宽
        set1.setHighlightEnabled(true);//是否禁用点击高亮线
        set1.setHighLightColor(Color.RED);//设置点击交点后显示交高亮线的颜色
        set1.setValueTextSize(9f);//设置显示值的文字大小
        set1.setDrawFilled(false);//设置禁用范围背景填充

        //保存LineDataSet集合
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1); // add the datasets
        //创建LineData对象 属于LineChart折线图的数据集合
        LineData data = new LineData(dataSets);
        // 添加到图表中

        lineChart.setData(data);

        //获取此图表的x轴
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setDrawLabels(false);//绘制标签  指x轴上的对应数值
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//设置x轴的显示位置
        //绘制图表
        lineChart.invalidate();
    }

    private void testLineChart() {
        ArrayList<Entry> values1 = new ArrayList<>();
        values1.add(new Entry(4, 10));
        values1.add(new Entry(6, 15));
        values1.add(new Entry(9, 20));
        values1.add(new Entry(12, 5));
        values1.add(new Entry(15, 30));

        //设置数据1  参数1：数据源 参数2：图例名称
        LineDataSet set1 = new LineDataSet(values1, "测试数据1");
        set1.setColor(Color.GRAY);
        set1.setCircleColor(Color.GRAY);
        set1.setLineWidth(1f);//设置线宽
        set1.setCircleRadius(2f);//设置焦点圆心的大小
        set1.enableDashedHighlightLine(10f, 5f, 0f);//点击后的高亮线的显示样式
        set1.setHighlightLineWidth(2f);//设置点击交点后显示高亮线宽
        set1.setHighlightEnabled(true);//是否禁用点击高亮线
        set1.setHighLightColor(Color.RED);//设置点击交点后显示交高亮线的颜色
        set1.setValueTextSize(9f);//设置显示值的文字大小
        set1.setDrawFilled(false);//设置禁用范围背景填充

        //保存LineDataSet集合
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1); // add the datasets
        //创建LineData对象 属于LineChart折线图的数据集合
        LineData data = new LineData(dataSets);
        // 添加到图表中
        LineChart lineChart = findViewById(R.id.lineChar);


        lineChart.setData(data);
        //获取此图表的x轴
        XAxis xAxis = lineChart.getXAxis();
        //xAxis.setEnabled(true);//设置轴启用或禁用 如果禁用以下的设置全部不生效
        //xAxis.setDrawAxisLine(true);//是否绘制轴线
        // xAxis.setDrawGridLines(true);//设置x轴上每个点对应的线
        xAxis.setDrawLabels(false);//绘制标签  指x轴上的对应数值
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//设置x轴的显示位置
        //xAxis.setTextSize(20f);//设置字体
        //xAxis.setTextColor(Color.BLACK);//设置字体颜色
        //设置竖线的显示样式为虚线
        //lineLength控制虚线段的长度
        //spaceLength控制线之间的空间
        //xAxis.enableGridDashedLine(10f, 10f, 0f);
//        xAxis.setAxisMinimum(0f);//设置x轴的最小值
//        xAxis.setAxisMaximum(10f);//设置最大值
        // xAxis.setAvoidFirstLastClipping(true);//图表将避免第一个和最后一个标签条目被减掉在图表或屏幕的边缘
        //  xAxis.setLabelRotationAngle(10f);//设置x轴标签的旋转角度
//        设置x轴显示标签数量  还有一个重载方法第二个参数为布尔值强制设置数量 如果启用会导致绘制点出现偏差
//        xAxis.setLabelCount(10);
//        xAxis.setTextColor(Color.BLUE);//设置轴标签的颜色
//        xAxis.setTextSize(24f);//设置轴标签的大小
//        xAxis.setGridLineWidth(10f);//设置竖线大小
//        xAxis.setGridColor(Color.RED);//设置竖线颜色
//        xAxis.setAxisLineColor(Color.GREEN);//设置x轴线颜色
//        xAxis.setAxisLineWidth(5f);//设置x轴线宽度
//        xAxis.setValueFormatter();//格式化x轴标签显示字符
        //绘制图表
        lineChart.invalidate();
    }

    private void httpHoleCurve(String machineID, String extensionNum,
                               String holeNum) {

        JsonObject json = Http.getHoleCurve(machineID, extensionNum, holeNum,100);

        if (json.get("code").getAsInt() == 0) {
            mHoleCurve = json.get("lists").getAsJsonArray();
            mHandler.sendEmptyMessage(HandleWhat.getHoleCurve);
        }
    }

    View.OnClickListener onBtnBackClickListener = new View.OnClickListener() {
        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        public void onClick(View v) {
            Intent intent = new Intent(ActivityGrowCurve.this, ActivityBoard.class);
            startActivity(intent);
        }
    };

    class MyThread extends Thread {

        public String machineID;
        public String extensionNum;
        public String holeNum;


        public void run() {
            System.out.println("GrowCurve thread run");


            try {
                httpHoleCurve(machineID, extensionNum, holeNum);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }
    }

    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {

            if (msg.what == HandleWhat.getHoleCurve) {
                makeLineChartData(false);
            }
            return true;
        }
    });


}
