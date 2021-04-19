package com.example.bloodculture;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.bo.AttentionRecord;
import com.example.db.AttentionDao;
import com.google.gson.JsonObject;
import com.uuzuche.lib_zxing.activity.CaptureActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentAttention extends Fragment  implements Button.OnClickListener{

    View root;
    ListView listView ;
    Button btnAdd;
    static AttentionDao attentionDao ;

    List<Map<String,Object>> items;

    public FragmentAttention() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root =  inflater.inflate(R.layout.fragment_attention, container, false);
        listView = root.findViewById(R.id.viewAttention);
        btnAdd = root.findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

        attentionDao = new AttentionDao(getContext());


        items = new ArrayList<>();
        List<AttentionRecord>  tmp =  attentionDao.getAll();
        for(int i  = 0; i < tmp.size(); i ++){
            AttentionRecord e = tmp.get(i);
            Map<String,Object> item = new HashMap<>();
            item.put("id",e.id);
            item.put("addTime",e.addTime);
            items.add(item);

        }

        MyAttentionApt simpleAdapter = new MyAttentionApt(getActivity(),items,R.layout.simple_item_attention,
                new String[]{"id","addTime"},
                new int[]{R.id.itemId,R.id.itemAddTime});
        listView.setAdapter(simpleAdapter);

        listView.setOnItemLongClickListener(onItemLongClickListener);

        return root;
    }


    public void onClick(View v) {
        ActivityMain.self.startScan();
    }

    public void append(AttentionRecord d){

        Map<String,Object> item = new HashMap<>();
        item.put("id",d.id);
        item.put("addTime",d.addTime);
        items.add(item);


        MyAttentionApt simpleAdapter = new MyAttentionApt(getActivity(),items,R.layout.simple_item_attention,
                new String[]{"id","addTime"},
                new int[]{R.id.itemId,R.id.itemAddTime});
        listView.setAdapter(simpleAdapter);
    }

    public void delete(int i){
        if(!attentionDao.delete(items.get(i).get("id").toString())){
            return;
        }
        items.remove(i);
        MyAttentionApt simpleAdapter = new MyAttentionApt(getActivity(),items,R.layout.simple_item_attention,
                new String[]{"id","addTime"},
                new int[]{R.id.itemId,R.id.itemAddTime});
        listView.setAdapter(simpleAdapter);
    }


    public List<Map<String,Object>> get(){
        return items;
    }


    AdapterView.OnItemLongClickListener onItemLongClickListener = new AdapterView.OnItemLongClickListener(){
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id){
            delete(position);
            return true;
        }
    };




}
