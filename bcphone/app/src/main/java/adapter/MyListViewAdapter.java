package adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;

import com.example.android.bcphone.R;


import java.util.Date;
import java.util.List;
import java.util.Map;

import tool.TimeUtil;

public class MyListViewAdapter extends SimpleAdapter {

    public MyListViewAdapter(Context context, List<? extends Map<String, ?>> data,
                             @LayoutRes int resource, String[] from, @IdRes int[] to) {
       super(context,data,resource,from, to);
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        View  view = super.getView(position,convertView,parent);
        TextView itemTime  = view.findViewById(R.id.itemTime);
        TextView itemKong  = view.findViewById(R.id.itemKong);
        TextView itemMaching  = view.findViewById(R.id.itemMaching);
        TextView itemType  = view.findViewById(R.id.itemType);
        String txt = itemType.getText().toString();


        String addTime = itemTime.getText().toString();
        try{
            long seconds =   new Date().getTime()/1000 - TimeUtil.stringToLong(addTime,"yyyy-MM-dd HH:mm:ss");
            System.out.println("seconds:" + seconds + " " + addTime  + " " + itemKong.getText() + " " + position);
            if(seconds < 60*60){//报阳在一个小时内的
                itemType.setTextColor(Color.RED);
                itemMaching.setTextColor(Color.RED);
                itemKong.setTextColor(Color.RED);
                itemTime.setTextColor(Color.RED);
            }else if(seconds < 60*60*3){//报阳在3个小时内的
                itemType.setTextColor(0xFFfed406);
                itemMaching.setTextColor(0xFFfed406);
                itemKong.setTextColor(0xFFfed406);
                itemTime.setTextColor(0xFFfed406);
            }else {//报阳在12天内的
                itemType.setTextColor(Color.BLUE);
                itemMaching.setTextColor(Color.BLUE);
                itemKong.setTextColor(Color.BLUE);
                itemTime.setTextColor(Color.BLUE);
            }


        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return view;
    }
}
