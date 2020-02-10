package com.example.bloodculture;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class MyViewAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;//fragment管理员
    private FragmentManager fm;//fragment数组

    /**
     * 构造函数
     * @param fm fragment管理员对象
     * @param fragmentList fragment数组
     */
    public MyViewAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.fragments=fragmentList;
        this.fm=fm;
    }


    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }


}
