package com.example.bloodculture;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentBaoyang extends Fragment {

    View root;
    ListView listView ;

    JsonArray mBaoyangData;

    public FragmentBaoyang() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root=  inflater.inflate(R.layout.fragment_baoyang, container, false);
        listView = root.findViewById(R.id.listViewBaoyang);
        listView.setOnItemClickListener(onItemClickListener);
        return root;
    }

    public void setBaoYangList(JsonArray array){

        List<Map<String,Object>> items = new ArrayList<>();
        mBaoyangData = array;

        for( int i = 0; i < array.size(); i ++){
            JsonObject an = array.get(i).getAsJsonObject();

            Map<String,Object> item = new HashMap<>();
            item.put("name",an.get("MachineID").getAsString());
            item.put("maching",an.get("ExtensionNum").getAsString());
            item.put("kong",an.get("HoleNum").getAsString());
            item.put("time",an.get("AddedTime").getAsString());
            items.add(item);

        }


        SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(),items,R.layout.simple_item_baoyang,
                new String[]{"name","maching","kong","time"},
                new int[]{R.id.itemName,R.id.itemMaching,R.id.itemKong,R.id.itemTime});

        listView.setAdapter(simpleAdapter);
    }

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener(){
        public void onItemClick(AdapterView<?> parent, View view, int position, long id){
            JsonObject elem = mBaoyangData.get(position).getAsJsonObject();

            Intent intent = new Intent(getContext(), ActivityBoard.class);
            intent.putExtra("MachineID",elem.get("MachineID").getAsString());
            intent.putExtra("ExtensionNum",elem.get("ExtensionNum").getAsString());


            startActivity(intent);
        }
    };

}
