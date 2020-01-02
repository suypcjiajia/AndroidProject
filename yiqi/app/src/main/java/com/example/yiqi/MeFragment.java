package com.example.yiqi;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class MeFragment extends Fragment {

    View root = null;

    public MeFragment() {
        // Required empty public constructor
    }
    public void setUserInfo(String userName,String userLevel){
        if( root != null){
            TextView userTxt = root.findViewById(R.id.textViewUserName);
            userTxt.setText(userName);
            TextView textViewLevel = root.findViewById(R.id.textViewLevel);
            textViewLevel.setText(userLevel);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root =  inflater.inflate(R.layout.fragment_me, container, false);
        return root;
    }

}
