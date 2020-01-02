package com.example.yiqi;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class DeviceFragment extends Fragment implements View.OnClickListener {

    View root = null;

    String str;


    public DeviceFragment() {
        // Required empty public constructor
    }

    public void switchPage(int who){
        int page2 = View.VISIBLE;
        int fragmentDevlist =View.INVISIBLE;
        if( who == 1){
            page2 = View.VISIBLE;
            fragmentDevlist = View.INVISIBLE;
        }else if( who == 2){
            page2 = View.INVISIBLE;
            fragmentDevlist = View.VISIBLE;
        }
        root.findViewById(R.id.page2).setVisibility(page2);
        root.findViewById(R.id.fragmentDevlist).setVisibility(fragmentDevlist);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        root = inflater.inflate(R.layout.fragment_device, container, false);

        //  root.findViewById(R.id.bt24).setOnClickListener(this);

        root.findViewById(R.id.bt24).setOnClickListener(this);
        root.findViewById(R.id.bt48).setOnClickListener(this);
        root.findViewById(R.id.bt60).setOnClickListener(this);
        root.findViewById(R.id.bt120).setOnClickListener(this);
        root.findViewById(R.id.bt168).setOnClickListener(this);
        root.findViewById(R.id.a96).setOnClickListener(this);
        root.findViewById(R.id.d2plus).setOnClickListener(this);
        root.findViewById(R.id.d2mini).setOnClickListener(this);
        root.findViewById(R.id.dry8).setOnClickListener(this);
        root.findViewById(R.id.dry16).setOnClickListener(this);
        return root;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
    }

    @Override
    public void onClick(View v) {
        if (!(v instanceof TextView)) {
            return;
        }
        TextView t = (TextView) v;
        System.out.println("onClick:" + t.getText());
        root.findViewById(R.id.page2).setVisibility(View.INVISIBLE);
        root.findViewById(R.id.fragmentDevlist).setVisibility(View.VISIBLE);
        str = t.getText().toString();
        new MyThread().start();
    }




    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (msg.what == 30) {

                DevListFragment dev = (DevListFragment)getChildFragmentManager().findFragmentById(R.id.fragmentDevlist);
                dev.setDevList(str);
            }
            return true;
        }
    });

    public class  MyThread extends Thread {
        public void run() {

            try {
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            mHandler.sendEmptyMessage(30);

        }
    }



}
