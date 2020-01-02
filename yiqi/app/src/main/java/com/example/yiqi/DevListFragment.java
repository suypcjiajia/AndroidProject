package com.example.yiqi;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
        ListView listView = (ListView)root.findViewById(R.id.listViewDevlist);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public  void onItemClick(AdapterView<?> parent, View view, int position, long id){
                System.out.println("onItemClick:" + position + "," + id);
                if( position == 0)
                    ((MainActivity)getActivity()).toHospitaoPos("珠海市香洲区拱北粤华路208号","珠海第二人民医院");
                else
                    ((MainActivity)getActivity()).toHospitaoPos("珠海市金湾区金湖路与虹阳路交叉口","金湾中心医院");
            }
        });

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
        item.put("city","珠海");
        items.add(item);
        Map<String,Object> item2 = new HashMap<>();
        item2.put("name","d35678");
        item2.put("online","离线");
        item2.put("disk","30%");
        item2.put("yichang","正常");
        item2.put("city","珠海");
        items.add(item2);
        SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(),items,R.layout.simple_item,
                new String[]{"name","online","disk","yichang","city"},
                new int[]{R.id.itemName,R.id.itemOnline,R.id.itemDiskUse,R.id.itemException,R.id.itemCity});

        ListView list = root.findViewById(R.id.listViewDevlist);
        list.setAdapter(simpleAdapter);
    }

}
