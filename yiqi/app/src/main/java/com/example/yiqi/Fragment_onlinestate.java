package com.example.yiqi;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;




/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_onlinestate extends Fragment {

    public  View me;

    public Fragment_onlinestate() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        me = inflater.inflate(R.layout.fragment_onlinestate, container, false);
        return me;
    }





}
