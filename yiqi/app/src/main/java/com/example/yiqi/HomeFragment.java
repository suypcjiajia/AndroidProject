package com.example.yiqi;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class HomeFragment extends Fragment {


    TextView  textViewOnLine;
    TextView textViewExpection;
    TextView  textViewDisk;
    TextView textViewBaoyang;

    View fragment_home;

    public HomeFragment() {
        // Required empty public constructor
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
                fragment_home.findViewById(R.id.onlinestate).setVisibility(View.INVISIBLE);
                fragment_home.findViewById(R.id.expection).setVisibility(View.INVISIBLE);
                fragment_home.findViewById(R.id.disk).setVisibility(View.INVISIBLE);
                fragment_home.findViewById(R.id.baoyang).setVisibility(View.VISIBLE);

                textViewOnLine.setTextColor(Color.parseColor("#000000"));
                textViewExpection.setTextColor(Color.parseColor("#000000"));
                textViewDisk.setTextColor(Color.parseColor("#000000"));
                textViewBaoyang.setTextColor(Color.parseColor("#ff6600"));

            }
        });
        return fragment_home;
    }




}
