package com.example.android.bcphone;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapter.MyListViewAdapter;
import entity.BottleType;
import entity.ShareData;
import tool.Http;


/**
 * A simple {@link Fragment} subclass.
 */
public class BaoYangFragment extends Fragment {


    View root;
    ListView listView;
    public BaoYangFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root =  inflater.inflate(R.layout.fragment_bao_yang, container, false);
        listView = root.findViewById(R.id.listViewBaoyang);
        listView.setOnItemClickListener(onItemClickListener);


        return root;
    }

    public void reflash(){
        if(ShareData.getBaoYang == null){
            return;
        }
        List<Map<String,Object>> items = new ArrayList<>();
        for(int i = 0 ; i < ShareData.getBaoYang.size(); i++){
            JsonObject elem = ShareData.getBaoYang.get(i).getAsJsonObject();

            Map<String,Object> item = new HashMap<>();
            item.put("maching",  ShareData.getMaching(elem.get("ExtensionNum").getAsInt()) );
            item.put("kong",elem.get("HoleNum").getAsInt() + 1 + "瓶");
            item.put("type", BottleType.BottleType[elem.get("BottleType").getAsInt()]);
            item.put("time",elem.get("PositiveTime").getAsString());
            items.add(item);
        }


        MyListViewAdapter simpleAdapter = new MyListViewAdapter(getActivity(),items,R.layout.simple_item_baoyang,
                new String[]{"maching","kong","type","time"},
                new int[]{R.id.itemMaching,R.id.itemKong,R.id.itemType,R.id.itemTime});

        listView.setAdapter(simpleAdapter);
    }

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener(){
        public void onItemClick(AdapterView<?> parent, View view, int position, long id){

            Intent intent = new Intent(getContext(), CurveActivity.class);
            if(ShareData.getBaoYang.size() < position){
                return;
            }
            JsonObject element = ShareData.getBaoYang.get(position).getAsJsonObject();
            ShareData.PutInTime = element.get("PutInTime").getAsString();
            ShareData.HoleNum = element.get("HoleNum").getAsInt();
            ShareData.ExtensionNum = element.get("ExtensionNum").getAsInt();
            startActivity(intent);
        }
    };



}
