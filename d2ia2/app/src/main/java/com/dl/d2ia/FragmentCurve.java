package com.dl.d2ia;


import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dl.com.dl.bo.TestRecord;
import com.dl.db.TestResultDao;
import com.dl.tool.TimeUtil;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

import static java.lang.Integer.parseInt;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentCurve extends Fragment {

    View root;

    TextView viewCount;

    int index = 0;
    int count = 0;

    public FragmentCurve() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root =  inflater.inflate(R.layout.fragment_curve, container, false);
        setText(R.id.number,"样本编号");
        setText(R.id.project,"检测项目");
        setText(R.id.piChi,"批次编码");
        setText(R.id.cheLiangZhi,"测量值");
        setText(R.id.nonDu,"检测浓度");
        setText(R.id.result,"检测结论");
        setText(R.id.testTime,"检测时间");

        setText(R.id.unit,"单位");
        setText(R.id.canKaoZhi,"参考值");
        setText(R.id.name,"姓名");
        setText(R.id.testMan,"检验员");

        root.findViewById(R.id.imagePre).setOnClickListener(clickBtnPre);
        root.findViewById(R.id.imageNext).setOnClickListener(clickBtnNext);
        viewCount = root.findViewById(R.id.viewCount);

        return root;
    }

    View.OnClickListener  clickBtnPre = new View.OnClickListener(){

        public void onClick(View v){
            if( count != 0) {
                if (index == 0) {
                    Toast.makeText(getContext(), "已是最前一条",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                index--;
                showRecord(FragmentSingleTest.biaoBenDao.getOne(index));
            }

            showCount();
        }
    };


    View.OnClickListener  clickBtnNext = new View.OnClickListener(){

        public void onClick(View v){
            if( count != 0) {
                if (index == count - 1) {
                    Toast.makeText(getContext(), "已是最后一条",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                index++;
                showRecord(FragmentSingleTest.biaoBenDao.getOne(index));
            }
            showCount();
        }
    };


    public void showRecord(TestRecord testRecord){
        if( testRecord == null){
            return;
        }
        setInfo(R.id.number, testRecord.id);
        setInfo(R.id.project,"无");
        setInfo(R.id.piChi,"无");
        setInfo(R.id.cheLiangZhi,"无");
        setInfo(R.id.nonDu,"20%");
        setInfo(R.id.result,testRecord.result);
        setInfo(R.id.testTime,testRecord.testTime);

        setInfo(R.id.unit,"无");
        setInfo(R.id.canKaoZhi,"无");
        setInfo(R.id.name, testRecord.name);
        setInfo(R.id.testMan,testRecord.testMan);
        makeLineChartData(testRecord);

    }

    public void showFirstRecord(){
        index = 0;
        count = FragmentSingleTest.biaoBenDao.count();
        if( count != 0) {
            showRecord(FragmentSingleTest.biaoBenDao.getOne(index));
        }
        showCount();

    }



    private  void setText(int id,String txt ){
        View view1  = root.findViewById(id);
        TextView textView = view1.findViewById(R.id.textView);
        textView.setText(txt);
    }


    private  void setInfo(int id,String txt){
        View view1  = root.findViewById(id);
        TextView textView = view1.findViewById(R.id.editText);
        textView.setText(txt);
    }

    private void showCount(){
        int tmp = index + 1;
        if( count == 0){
            viewCount.setText("没有检测数据");
            return;
        }
        viewCount.setText("总共" + count + "条,当前第" + tmp + "条");
    }


    private void makeLineChartData(TestRecord testRecord) {

        ArrayList<Entry> values1 = new ArrayList<>();
        LineChart lineChart = root.findViewById(R.id.lineChar);

        float max = 10;
        float min = 2;
        if( testRecord.curver.size() == 0){//初始化(显示一个点0，0)
            values1.add(new Entry(0, 0));
        }else{

            for (int i = 0; i < testRecord.curver.size(); i++) {
                String elem = testRecord.curver.get(i);//"2020-02-17 16:30:01,92"
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
                values1.add(new Entry(x_time%100000, parseInt(dataarry[1])));
                if( parseInt(dataarry[1]) > max){
                    max = parseInt(dataarry[1]);
                }
                if( parseInt(dataarry[1]) < min){
                    min = parseInt(dataarry[1]);
                }

            }

        }

        //设置数据1  参数1：数据源 参数2：图例名称
        LineDataSet set1 = new LineDataSet(values1, "曲线");
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
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1); // add the datasets
        //创建LineData对象 属于LineChart折线图的数据集合
        LineData data = new LineData(dataSets);
        // 添加到图表中

        lineChart.setData(data);

        //获取此图表的x轴
        XAxis xAxis = lineChart.getXAxis();
//        YAxis leftYAxis = lineChart.getAxisLeft();
//        YAxis rightYaxis = lineChart.getAxisRight();
//        leftYAxis.setAxisMinimum(0f);
//        leftYAxis.setAxisMaximum(max);
//        rightYaxis.setAxisMinimum(0f);
//        rightYaxis.setAxisMaximum(max);

       // xAxis.setValueFormatter(iAxisValueFormatter);
        xAxis.setDrawLabels(false);//绘制标签  指x轴上的对应数值
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//设置x轴的显示位置
        //绘制图表
        lineChart.invalidate();
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
                if( timestr != null && timestr.length() ==19){
                    return timestr.substring(14,19);
                }
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
            return "";

        }
    };


    private void testLineChart() {
        ArrayList<Entry> values1 = new ArrayList<>();
        values1.add(new Entry(16750, 10));
        values1.add(new Entry(16751, 15));
        values1.add(new Entry(16752, 20));
        values1.add(new Entry(16753, 5));
        values1.add(new Entry(16754, 30));

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
        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);//设置曲线展示为圆滑曲线

        //保存LineDataSet集合
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1); // add the datasets
        //创建LineData对象 属于LineChart折线图的数据集合
        LineData data = new LineData(dataSets);
        // 添加到图表中
        LineChart lineChart = root.findViewById(R.id.lineChar);


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



}
