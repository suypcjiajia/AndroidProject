package com.example.android.bcphone;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapter.MyListViewAdapter;
import entity.BottleType;
import entity.HoleState;
import entity.ShareData;
import entity.ShowMsg;
import tool.Http;
import tool.TimeUtil;


/**
 * A simple {@link Fragment} subclass.
 */
public class QueryFragment extends Fragment {

    View root;
    Button btnQuery;
    ListView listQuery;
    TextView edtDateStart;
    TextView edtDateEnd;

    CheckBox chkPositive;
    CheckBox chkNegitive;
    CheckBox chkCulture;
    CheckBox chkInvalid;
    CheckBox chkChild;
    CheckBox chkNeedO2;
    CheckBox chkAgaistO2;
    CheckBox chkUndined;



    public QueryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root =  inflater.inflate(R.layout.fragment_query, container, false);
        btnQuery = root.findViewById(R.id.btnQuery);
        listQuery = root.findViewById(R.id.listQuery);
        edtDateStart = root.findViewById(R.id.edtDateStart);
        edtDateEnd = root.findViewById(R.id.edtDateEnd);
        chkPositive = root.findViewById(R.id.chkPositive);
        chkNegitive = root.findViewById(R.id.chkNegitive);
        chkCulture = root.findViewById(R.id.chkCulture);
        chkInvalid = root.findViewById(R.id.chkInvalid);
        chkChild = root.findViewById(R.id.chkChild);
        chkNeedO2 = root.findViewById(R.id.chkNeedO2);
        chkAgaistO2 = root.findViewById(R.id.chkAgaistO2);
        chkUndined = root.findViewById(R.id.chkUndined);

        btnQuery.setOnClickListener(onBtnQueryClick);
        listQuery.setOnItemClickListener(onItemClickListener);
        edtDateStart.setOnClickListener(onEdtDateStartClick);
        edtDateEnd.setOnClickListener(onEdtDateEndClick);

        edtDateStart.setText(TimeUtil.today());
        edtDateEnd.setText(TimeUtil.today());
        chkAgaistO2.setChecked(true);
        chkChild.setChecked(true);
        chkCulture.setChecked(true);
        chkInvalid.setChecked(true);
        chkNeedO2.setChecked(true);
        chkNegitive.setChecked(true);
        chkPositive.setChecked(true);
        chkUndined.setChecked(true);
        return root;
    }

    public String getStartTime(){
        return edtDateStart.getText().toString();
    }
    public String getEndTime(){
        return edtDateEnd.getText().toString();
    }

    /**
     * 返回用户选择的瓶子类型
     * @return
     */
    public List<Integer> selectBottleType(){
        List<Integer> tmp =  new ArrayList<>();
        if(chkPositive.isChecked()){
            tmp.add(HoleState.hole_anoposivive);
            tmp.add(HoleState.hole_positive);
        }
        if(chkNegitive.isChecked()){
            tmp.add(HoleState.hole_anonegative);
            tmp.add(HoleState.hole_negative);
        }
        if(chkCulture.isChecked()){
            tmp.add(HoleState.hole_culture);
        }
        if(chkInvalid.isChecked()){
            tmp.add(HoleState.hole_invalid);
        }
        return tmp;
    }

    /**
     * 返回用户选择的瓶子状态
     * @return
     */
    public List<Integer> selectBottleState(){
        List<Integer> tmp =  new ArrayList<>();
        if(chkChild.isChecked()){
            tmp.add(BottleType.Child);
        }
        if(chkAgaistO2.isChecked()){
            tmp.add(BottleType.AgaistO2);
        }
        if(chkNeedO2.isChecked()){
            tmp.add(BottleType.NeedO2);
        }
        if(chkUndined.isChecked()){
            tmp.add(BottleType.Undined);
        }
        return tmp;
    }

    public void updateHistory(){
        if(ShareData.getHistory == null){
            return;
        }
        List<Map<String,Object>> items = new ArrayList<>();
        for(int i = 0; i < ShareData.getHistory.size(); i++){
            JsonObject elem = ShareData.getHistory.get(i).getAsJsonObject();
            Map<String,Object> item = new HashMap<>();
            item.put("maching",  ShareData.getMaching(elem.get("ExtensionNum").getAsInt()) );
            item.put("kong",elem.get("HoleNum").getAsInt() + 1 + "瓶");
            item.put("type", BottleType.BottleType[elem.get("BottleType").getAsInt()]);
            item.put("time",HoleState.FinalState[elem.get("FinalState").getAsInt()]);
            items.add(item);
        }



        MyListViewAdapter simpleAdapter = new MyListViewAdapter(getActivity(),items,R.layout.simple_item_baoyang,
                new String[]{"maching","kong","type","time"},
                new int[]{R.id.itemMaching,R.id.itemKong,R.id.itemType,R.id.itemTime});

        listQuery.setAdapter(simpleAdapter);
    }

    /**
     *
     * @param v
     */
    View.OnClickListener onBtnQueryClick = new View.OnClickListener() {
        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        public void onClick(View v) {

            new GetHistoryThread().start();
            btnQuery.setEnabled(false);

        }
    };

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener(){
        public void onItemClick(AdapterView<?> parent, View view, int position, long id){
            if(ShareData.getHistory.size() < position){
                return;
            }
            Intent intent = new Intent(getContext(), CurveActivity.class);
            JsonObject element = ShareData.getHistory.get(position).getAsJsonObject();
            ShareData.PutInTime = element.get("PutInTime").getAsString();
            ShareData.HoleNum = element.get("HoleNum").getAsInt();
            ShareData.ExtensionNum = element.get("ExtensionNum").getAsInt();
            startActivity(intent);
        }
    };


    View.OnClickListener onEdtDateStartClick = new View.OnClickListener() {
        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        public void onClick(View v) {

            Calendar c = Calendar.getInstance();
            int day = c.get(Calendar.DAY_OF_MONTH);
            int month = c.get(Calendar.MONTH);
            int year = c.get(Calendar.YEAR);

            DatePickerDialog datePicker=new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    int realMonth = monthOfYear + 1;
                    edtDateStart.setText(year + "-" + String.format("%02d",realMonth) + "-" + String.format("%02d",dayOfMonth));
                }
            }, year, month, day);

            datePicker.show();
        }
    };

    View.OnClickListener onEdtDateEndClick = new View.OnClickListener() {
        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        public void onClick(View v) {

            Calendar c = Calendar.getInstance();
            int day = c.get(Calendar.DAY_OF_MONTH);
            int month = c.get(Calendar.MONTH);
            int year = c.get(Calendar.YEAR);

            DatePickerDialog datePicker=new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    int realMonth = monthOfYear + 1;
                    edtDateEnd.setText(year + "-" + String.format("%02d",realMonth) + "-" + String.format("%02d",dayOfMonth));
                }
            }, year, month, day);

            datePicker.show();
        }
    };

    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {

            if(msg.what == HandleId.getHostory){
                updateHistory();
                btnQuery.setEnabled(true);
            }
            if(msg.what == HandleId.err){
                Toast.makeText(getContext(),((ShowMsg)msg.obj).msg,Toast.LENGTH_LONG).show();
                btnQuery.setEnabled(true);
            }
            return true;
        }
    });


    class GetHistoryThread extends  Thread
    {
        public void run(){
            JsonObject ret = Http.getHistory(getStartTime(),
                    getEndTime(),
                    selectBottleType(),
                    selectBottleState());
            if(ret.get("state").getAsInt() == 0){
                ShareData.getHistory = ret.get("result").getAsJsonArray();
                Message msg = new Message();
                msg.what = HandleId.getHostory;
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
