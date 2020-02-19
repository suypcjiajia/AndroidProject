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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentDevice extends Fragment {

    JsonArray mDeviceData;
    View root;
    ListView listView ;
    public FragmentDevice() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root =  inflater.inflate(R.layout.fragment_device, container, false);
        listView = root.findViewById(R.id.listViewDevice);
        listView.setOnItemClickListener(onItemClickListener);
        return root;
    }

    public void setDeviceList(JsonArray array){

        mDeviceData = array;
        List<Map<String,Object>> items = new ArrayList<>();

        for( int i = 0; i < array.size(); i ++){
            JsonObject an = array.get(i).getAsJsonObject();

            Map<String,Object> item = new HashMap<>();
            item.put("item1",an.get("type").getAsString());
            item.put("item2",an.get("name").getAsString());
            items.add(item);

        }


        SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(),items,R.layout.simple_item_device,
                new String[]{"item1","item2"},
                new int[]{R.id.item1,R.id.item2});


        listView.setAdapter(simpleAdapter);
    }

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener(){
        public void onItemClick(AdapterView<?> parent, View view, int position, long id){
            JsonObject elem = mDeviceData.get(position).getAsJsonObject();
            Intent intent = new Intent(getContext(), ActivityBoard.class);
            intent.putExtra("MachineID",elem.get("name").getAsString());
            intent.putExtra("ExtensionNum","0");
            intent.putExtra("type",elem.get("type").getAsString());
            startActivity(intent);
        }
    };


}
