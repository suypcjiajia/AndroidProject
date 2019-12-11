package com.example.yiqi;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class DevListFragment extends Fragment {

    View root;

    public DevListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root =  inflater.inflate(R.layout.fragment_dev_list, container, false);
        return root;
    }

    public void setDevList(String type){

        ((TextView)root.findViewById(R.id.typeName)).setText(type + "设备列表");
        List<Map<String,Object>> items = new ArrayList<>();

        Map<String,Object> item = new HashMap<>();
        item.put("name","d123456");
        item.put("online","在线");
        item.put("disk","20%");
        item.put("yichang","正常");
        items.add(item);
        Map<String,Object> item2 = new HashMap<>();
        item2.put("name","d35678");
        item2.put("online","离线");
        item2.put("disk","30%");
        item2.put("yichang","正常");
        items.add(item2);
        SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(),items,R.layout.simple_item,
                new String[]{"name","online","disk","yichang"},
                new int[]{R.id.itemName,R.id.itemOnline,R.id.itemDiskUse,R.id.itemException});

        ListView list = root.findViewById(R.id.listViewDevlist);
        list.setAdapter(simpleAdapter);
    }

}
