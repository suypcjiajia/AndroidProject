package com.example.bloodculture;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;

import com.example.tool.TimeUtil;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class MyAttentionApt extends SimpleAdapter {

    public MyAttentionApt(Context context, List<? extends Map<String, ?>> data,
                          @LayoutRes int resource, String[] from, @IdRes int[] to) {
       super(context,data,resource,from, to);
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        View  view = super.getView(position,convertView,parent);
        //TextView itemTime  = view.findViewById(R.id.itemId);
        //TextView itemKong  = view.findViewById(R.id.itemAddTime);

        return view;
    }
}
