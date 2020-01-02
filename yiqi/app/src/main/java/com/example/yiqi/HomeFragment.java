package com.example.yiqi;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.gson.JsonArray;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {


    TextView  textViewOnLine;
    TextView textViewExpection;
    TextView  textViewDisk;
    TextView textViewBaoyang;

    View fragment_home = null;

    public HomeFragment() {
        // Required empty public constructor
    }

    public void setCharData(List<PieEntry> strings, int type){

        PieChart chart1 ;
        ArrayList<Integer> colors = new ArrayList<>();
        if(type == 1){
            chart1 = fragment_home.findViewById(R.id.chart1);

            colors.add(getResources().getColor(R.color.greencao));
            colors.add(getResources().getColor(R.color.blackb3));


        }else if( type == 2){
            chart1 = fragment_home.findViewById(R.id.chart2);

            colors.add(getResources().getColor(R.color.red));
            colors.add(getResources().getColor(R.color.blackb3));


        }else if( type == 3){
            chart1 = fragment_home.findViewById(R.id.chart3);

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

    public  void switch2Baoyang(){
        fragment_home.findViewById(R.id.onlinestate).setVisibility(View.INVISIBLE);
        fragment_home.findViewById(R.id.expection).setVisibility(View.INVISIBLE);
        fragment_home.findViewById(R.id.disk).setVisibility(View.INVISIBLE);
        fragment_home.findViewById(R.id.baoyang).setVisibility(View.VISIBLE);

        textViewOnLine.setTextColor(Color.parseColor("#000000"));
        textViewExpection.setTextColor(Color.parseColor("#000000"));
        textViewDisk.setTextColor(Color.parseColor("#000000"));
        textViewBaoyang.setTextColor(Color.parseColor("#ff6600"));
    }


    public void setBaoYang(JsonArray array){
        if(!isAdded()){
            return;
        }
        BaoyangFragment baoyangFragment  = (BaoyangFragment)getChildFragmentManager().findFragmentById(R.id.baoyang);
        baoyangFragment.setBaoYangList(array);
    }

    public  void setOnLineTxt(String e1,String e2){
        if( e1 != null) {
            TextView t = fragment_home.findViewById(R.id.textViewOnline2);
            t.setText(e1);
        }
        if( e2 != null) {
            TextView t2 = fragment_home.findViewById(R.id.textViewOnline3);
            t2.setText(e2);
        }
    }

    public void setExceptionTxt(String e1,String e2){
        if( e1 != null) {
            TextView t = fragment_home.findViewById(R.id.textViewExpection2);
            t.setText(e1);
        }
        if( e2 != null) {
            TextView t2 = fragment_home.findViewById(R.id.textViewExpection3);
            t2.setText(e2);
        }
    }

    public void setDiskTxt(String e1,String e2){
        if( e1 != null) {
            TextView t = fragment_home.findViewById(R.id.textViewDisk2);
            t.setText(e1);

        }
        if( e2 != null) {

            TextView t2 = fragment_home.findViewById(R.id.textViewDisk3);
            t2.setText(e2);
        }
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        fragment_home = inflater.inflate(R.layout.fragment_home, container, false);
        textViewOnLine = fragment_home.findViewById(R.id.textViewOnLine);
        textViewExpection = fragment_home.findViewById(R.id.textViewExpection);
        textViewDisk = fragment_home.findViewById(R.id.textViewDisk);
        textViewBaoyang = fragment_home.findViewById(R.id.textViewBaoYang);

        fragment_home.findViewById(R.id.onlinestate).setVisibility(View.VISIBLE);
        fragment_home.findViewById(R.id.expection).setVisibility(View.INVISIBLE);
        fragment_home.findViewById(R.id.disk).setVisibility(View.INVISIBLE);
        fragment_home.findViewById(R.id.baoyang).setVisibility(View.INVISIBLE);

        textViewOnLine.setTextColor(Color.parseColor("#ff6600"));


        textViewOnLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment_home.findViewById(R.id.onlinestate).setVisibility(View.VISIBLE);
                fragment_home.findViewById(R.id.expection).setVisibility(View.INVISIBLE);
                fragment_home.findViewById(R.id.disk).setVisibility(View.INVISIBLE);
                fragment_home.findViewById(R.id.baoyang).setVisibility(View.INVISIBLE);

                textViewOnLine.setTextColor(Color.parseColor("#ff6600"));
                textViewExpection.setTextColor(Color.parseColor("#000000"));
                textViewDisk.setTextColor(Color.parseColor("#000000"));
                textViewBaoyang.setTextColor(Color.parseColor("#000000"));

            }
        });
        textViewExpection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment_home.findViewById(R.id.onlinestate).setVisibility(View.INVISIBLE);
                fragment_home.findViewById(R.id.expection).setVisibility(View.VISIBLE);
                fragment_home.findViewById(R.id.disk).setVisibility(View.INVISIBLE);
                fragment_home.findViewById(R.id.baoyang).setVisibility(View.INVISIBLE);

                textViewOnLine.setTextColor(Color.parseColor("#000000"));
                textViewExpection.setTextColor(Color.parseColor("#ff6600"));
                textViewDisk.setTextColor(Color.parseColor("#000000"));
                textViewBaoyang.setTextColor(Color.parseColor("#000000"));

            }
        });
        textViewDisk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment_home.findViewById(R.id.onlinestate).setVisibility(View.INVISIBLE);
                fragment_home.findViewById(R.id.expection).setVisibility(View.INVISIBLE);
                fragment_home.findViewById(R.id.disk).setVisibility(View.VISIBLE);
                fragment_home.findViewById(R.id.baoyang).setVisibility(View.INVISIBLE);

                textViewOnLine.setTextColor(Color.parseColor("#000000"));
                textViewExpection.setTextColor(Color.parseColor("#000000"));
                textViewDisk.setTextColor(Color.parseColor("#ff6600"));
                textViewBaoyang.setTextColor(Color.parseColor("#000000"));

            }
        });
        textViewBaoyang.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                switch2Baoyang();

            }
        });

        ImageButton btn = fragment_home.findViewById(R.id.btnMainLogin);

        btn.setOnClickListener(new View.OnClickListener(){
            public  void  onClick(View v){
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);

            }
        });
        return fragment_home;
    }


}
