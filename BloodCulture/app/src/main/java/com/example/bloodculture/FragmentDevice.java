package com.example.bloodculture;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    View root;
    public FragmentDevice() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root =  inflater.inflate(R.layout.fragment_device, container, false);
        return root;
    }

    public void setBaoYangList(JsonArray array){

        List<Map<String,Object>> items = new ArrayList<>();

        for( int i = 0; i < array.size(); i ++){
            JsonObject an = array.get(i).getAsJsonObject();

            Map<String,Object> item = new HashMap<>();
            item.put("item1",an.get("item1").getAsString());
            item.put("item2",an.get("item2").getAsString());
            items.add(item);

        }


        SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(),items,R.layout.simple_item_device,
                new String[]{"item1","item2"},
                new int[]{R.id.item1,R.id.item2});

        ListView list = root.findViewById(R.id.listViewDevice);
        list.setAdapter(simpleAdapter);
    }


}
