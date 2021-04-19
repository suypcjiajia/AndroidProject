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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.tool.TimeUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import static com.example.bloodculture.ActivityMain.fragmentAttention;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentBaoyang extends Fragment {

    View root;
    ListView listView ;

    JsonArray mBaoyangData = new JsonArray();

    String oldFristItmeTime = new String();

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

        if( array.size() == 0 ){
            return;
        }


        if( !oldFristItmeTime.isEmpty()){
            String newFirstItemTime = array.get(0).getAsJsonObject().get("AddedTime").getAsString();
            if(newFirstItemTime.compareTo(oldFristItmeTime) <= 0){//没有最新的
                return;
            }
        }
        oldFristItmeTime = array.get(0).getAsJsonObject().get("AddedTime").getAsString();

        List<Map<String,Object>> attens = ActivityMain.fragmentAttention.get();//关注的条码

        List<Map<String,Object>> items = new ArrayList<>();

        for( int i = 0; i < array.size(); i ++){
            JsonObject an = array.get(i).getAsJsonObject();

            for(int j = 0; j < attens.size(); j++){
                Map<String,Object> atten = attens.get(j);
                if(atten.get("id").toString().equals(an.get("MachineID").getAsString())){
                    //报阳瓶上的条码是要关注的条码
                    appendBaoYang(items,an);
                    mBaoyangData.add(array.get(i));

                    break;
                }
            }


        }

        for( int i = 0; i < array.size(); i ++){
            JsonObject an = array.get(i).getAsJsonObject();

            boolean match = false;
            for(int j = 0; j < attens.size(); j++){
                Map<String,Object> atten = attens.get(j);
                if(atten.get("id").toString().equals(an.get("MachineID").getAsString())){
                    match = true;
                    break;
                }
            }
            if(!match){
                appendBaoYang(items,an);
                mBaoyangData.add(array.get(i));
            }
        }


        MyListViewAdapter simpleAdapter = new MyListViewAdapter(getActivity(),items,R.layout.simple_item_baoyang,
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
            intent.putExtra("HoleNum",elem.get("HoleNum").getAsInt());
            intent.putExtra("type",elem.get("type").getAsString());


            startActivity(intent);
        }
    };

    private void  appendBaoYang(List<Map<String,Object>> items,JsonObject an){
        Map<String,Object> item = new HashMap<>();
        item.put("name",an.get("MachineID").getAsString());
        if( an.get("ExtensionNum").getAsString().equals("0")){//0是主机代码
            item.put("maching","主机");
        }else {
            item.put("maching",  "分机"  + an.get("ExtensionNum").getAsString());
        }
        item.put("kong",an.get("HoleNum").getAsInt() + 1);
        item.put("time",an.get("AddedTime").getAsString());
        items.add(item);
    }

}
