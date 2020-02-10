package com.example.bloodculture;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class ActivityMain extends AppCompatActivity {

    FragmentBaoyang fragmentBaoyang;
    FragmentDevice fragmentDevice;
    androidx.viewpager.widget.ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentBaoyang = new FragmentBaoyang();
        fragmentDevice = new FragmentDevice();
        List<Fragment> listView = new ArrayList<>();
        listView.add(fragmentBaoyang);
        listView.add(fragmentDevice);
        MyViewAdapter viewAdapter = new MyViewAdapter(getSupportFragmentManager(), listView);
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(viewAdapter);
        viewPager.setOffscreenPageLimit(3);
        viewPager.addOnPageChangeListener(onPageChangeListener);
        viewPager.setCurrentItem(0);

        setBottonBaoyang();
    }

    public void setBottonBaoyang(){
        findViewById(R.id.imageViewBaoyang).setBackgroundResource(R.drawable.baoyang_botton2);
        findViewById(R.id.imageViewDevice).setBackgroundResource(R.drawable.device_button1);
    }

    public void setBottonDevice(){
        findViewById(R.id.imageViewBaoyang).setBackgroundResource(R.drawable.baoyang_button1);
        findViewById(R.id.imageViewDevice).setBackgroundResource(R.drawable.device_button2);
    }

    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener(){
        public  void onPageScrolled(int position, float positionOffset, int positionOffsetPixels){

        }


        public  void onPageSelected(int position){
            if( position == 0){
                setBottonBaoyang();
            }else if( position == 1){
                setBottonDevice();
            }
        }



        public void onPageScrollStateChanged(int state){

        }
    };
}
