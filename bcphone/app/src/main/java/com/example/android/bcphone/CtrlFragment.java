package com.example.android.bcphone;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.JsonObject;

import entity.ShareData;
import entity.ShowMsg;
import tool.Http;


/**
 * A simple {@link Fragment} subclass.
 */
public class CtrlFragment extends Fragment {


    View root;
    Button btnShutDown;
    Button btnReboot;

    public CtrlFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root =  inflater.inflate(R.layout.fragment_ctrl, container, false);
        btnShutDown = root.findViewById(R.id.btnShutDown);
        btnReboot = root.findViewById(R.id.btnReboot);

        btnShutDown.setOnClickListener(onBtnShutDownClick);
        btnReboot.setOnClickListener(onBtnRebootClick);
        return root;
    }

    View.OnClickListener onBtnShutDownClick = new View.OnClickListener() {
        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        public void onClick(View v) {

            CmdThread cmdThread  = new CmdThread();
            cmdThread.arg = "1";
            cmdThread.start();
        }
    };


    View.OnClickListener onBtnRebootClick = new View.OnClickListener() {
        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        public void onClick(View v) {
            CmdThread cmdThread  = new CmdThread();
            cmdThread.arg = "2";
            cmdThread.start();
        }
    };


    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {

            if(msg.what == HandleId.cmd){
                Toast.makeText(getContext(),((ShowMsg)msg.obj).msg,Toast.LENGTH_LONG).show();
            }

            return true;
        }
    });

    class CmdThread extends  Thread
    {
        public  String arg;
        public void run(){
            JsonObject ret = Http.cmd(arg);
            Message msg = new Message();
            msg.what = HandleId.cmd;
            ShowMsg showMsg = new ShowMsg();
            showMsg.msg = ret.get("msg").getAsString();
            showMsg.state = ret.get("state").getAsInt();
            msg.obj = showMsg;
            mHandler.sendMessage(msg);


        }
    }

}
