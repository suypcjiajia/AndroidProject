package com.example.android.bcphone;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import entity.ShareData;
import entity.ShowMsg;
import tool.Http;


/**
 * A simple {@link Fragment} subclass.
 */
public class CountFragment extends Fragment {

    View root;
    Spinner spinner;
    BarChart mybarchart;
    PieChart mypiechart;
    RadioButton radioMonth;
    RadioButton radioJudu;
    Button btnCount;

    int[] pieColor = {0xff66dddd,0xff66cccc,0xff66bbbb,0xff66aaaa,0xff669999,0xff668888,
            0xff667777,0xff666666,0xff665555,0xff664444,0xff663333,0xff662222};

    public CountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root =  inflater.inflate(R.layout.fragment_count, container, false);

        spinner = root.findViewById(R.id.spinner);
        mybarchart = root.findViewById(R.id.barChar);
        mypiechart = root.findViewById(R.id.pieChar);
        radioJudu = root.findViewById(R.id.radioJudu);
        radioMonth = root.findViewById(R.id.radioMonth);
        btnCount = root.findViewById(R.id.btnCount);


        radioMonth.setOnCheckedChangeListener(onMonthCheckedChanage);
        radioJudu.setOnCheckedChangeListener(onJuduCheckedChanage);
        btnCount.setOnClickListener(onBtnCountClickListener);

        String[] items = {"2021","2020","2019","2018"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item,items);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        radioJudu.setChecked(true);


        return root;
    }

    public String getYear(){
        return spinner.getSelectedItem().toString();
    }
    public String getModel(){
        if(radioMonth.isChecked()){
            return "1";
        }else{
            return "2";
        }
    }

    void initJuduData(){
        if(ShareData.getTongji == null){
            return;
        }
        List<BarEntry> list = new ArrayList<>();

        int index = 0;
        int ju1 = 0,ju2 = 0,ju3=0,ju4 =0;
        for(int i = 0; i < 12; i++ ){
            String month = String.format("%02d",i+1);
            if(ShareData.getTongji.has(month)){
                int count = ShareData.getTongji.get(month).getAsInt();
                if(i >= 0 && i <= 2){
                    ju1 += count;
                }
                if(i >= 3 && i <= 5){
                    ju2 += count;
                }
                if(i >= 6 && i <= 8){
                    ju3 += count;
                }
                if(i >= 9 && i <= 11){
                    ju4 += count;
                }
            }
        }
        float total = ju1 + ju2 + ju3 + ju4;

        if(ju1 > 0){
            BarEntry da1 = new BarEntry(1,ju1);
            list.add(da1);
        }
        if(ju2 > 0){
            BarEntry da1 = new BarEntry(2,ju2);
            list.add(da1);
        }
        if(ju3 > 0){
            BarEntry da1 = new BarEntry(3,ju3);
            list.add(da1);
        }
        if(ju4 > 0){
            BarEntry da1 = new BarEntry(4,ju4);
            list.add(da1);
        }

        BarData data= new BarData();
        BarDataSet set = new BarDataSet(list,"季度");
        data.addDataSet(set);
        mybarchart.setData(data);
        Description description = new Description();
        description.setText("");
        mybarchart.setDescription(description);//去掉描述
        mybarchart.invalidate();//刷新

        List<PieEntry> strings = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();
        if(ju1 > 0){
            strings.add(new PieEntry(ju1/total*100,"1季"));
            colors.add(pieColor[index]);
            index++;
        }
        if(ju2 > 0){
            strings.add(new PieEntry(ju2/total*100,"2季"));
            colors.add(pieColor[index]);
            index++;
        }
        if(ju3 > 0){
            strings.add(new PieEntry(ju3/total*100,"3季"));
            colors.add(pieColor[index]);
            index++;
        }
        if(ju4 > 0){
            strings.add(new PieEntry(ju4/total*100,"4季"));
            colors.add(pieColor[index]);
            index++;
        }

        PieDataSet dataSet = new PieDataSet(strings,"季度");

        dataSet.setColors(colors);
        PieData pieData = new PieData(dataSet);
        pieData.setDrawValues(true);
        pieData.setValueTextSize(12f);
        pieData.setValueFormatter(new PercentFormatter());
        mypiechart.setData(pieData);
        Description description1 = new Description();
        description1.setText("");
        mypiechart.setDescription(description1);//去掉描述
        mypiechart.setHoleRadius(0f);//实心
        mypiechart.setTransparentCircleRadius(0f);//实心
        mypiechart.invalidate();//刷新
    }

    void initMonthData(){
        if(ShareData.getTongji == null){
            return;
        }
        List<BarEntry> list = new ArrayList<>();
        List<PieEntry> strings = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();
        int index = 0;
        float total = 0;
        for(int i = 0; i < 12; i++ ){
            String month = String.format("%02d",i+1);
            if(ShareData.getTongji.has(month)){
                total += ShareData.getTongji.get(month).getAsInt();
            }
        }
        for(int i = 0; i < 12; i++ ){
            String month = String.format("%02d",i+1);
            if(ShareData.getTongji.has(month)){
                BarEntry da1 = new BarEntry(Integer.parseInt(month),ShareData.getTongji.get(month).getAsInt());
                list.add(da1);

                strings.add(new PieEntry(ShareData.getTongji.get(month).getAsInt()/total*100,month + "月"));
                colors.add(pieColor[index]);
                index++;
            }
        }

        BarData data= new BarData();
        BarDataSet set = new BarDataSet(list,"月度");
        data.addDataSet(set);
        mybarchart.setData(data);
        Description description = new Description();
        description.setText("");
        mybarchart.setDescription(description);//去掉描述
        mybarchart.invalidate();//刷新


        PieDataSet dataSet = new PieDataSet(strings,"月度");

        dataSet.setColors(colors);
        PieData pieData = new PieData(dataSet);
        pieData.setDrawValues(true);
        pieData.setValueTextSize(12f);
        pieData.setValueFormatter(new PercentFormatter());
        mypiechart.setData(pieData);
        Description description1 = new Description();
        description1.setText("");
        mypiechart.setDescription(description1);//去掉描述
        mypiechart.setHoleRadius(0f);//实心
        mypiechart.setTransparentCircleRadius(0f);//实心
        mypiechart.invalidate();//刷新
    }


    CompoundButton.OnCheckedChangeListener onMonthCheckedChanage = new CompoundButton.OnCheckedChangeListener(){
        /**
         * Called when the checked state of a compound button has changed.
         *
         * @param buttonView The compound button view whose state has changed.
         * @param isChecked  The new checked state of buttonView.
         */
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
            if(isChecked){
                initMonthData();
            }
        }
    };
    CompoundButton.OnCheckedChangeListener onJuduCheckedChanage = new CompoundButton.OnCheckedChangeListener(){
        /**
         * Called when the checked state of a compound button has changed.
         *
         * @param buttonView The compound button view whose state has changed.
         * @param isChecked  The new checked state of buttonView.
         */
         public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
             if(isChecked){
                 initJuduData();
             }
        }
    };

    View.OnClickListener onBtnCountClickListener = new View.OnClickListener() {
        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        public void onClick(View v) {
            btnCount.setEnabled(false);
            new GetTongjiThread().start();
        }
    };

    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {

            if(msg.what == HandleId.getTongji){

                if(radioJudu.isChecked()){
                    initJuduData();
                }else{
                    initMonthData();
                }

                btnCount.setEnabled(true);
            }
            if(msg.what == HandleId.err){
                Toast.makeText(getContext(),((ShowMsg)msg.obj).msg,Toast.LENGTH_LONG).show();
                btnCount.setEnabled(true);
            }
            return true;
        }
    });

    class GetTongjiThread extends  Thread
    {
        public void run(){
            JsonObject ret = Http.getTongji(getYear());
            if(ret.get("state").getAsInt() == 0){
                ShareData.getTongji = ret.get("result").getAsJsonObject();
                Message msg = new Message();
                msg.what = HandleId.getTongji;
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
}
