package com.zz.wanandroid.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.zz.wanandroid.bean.Tree;

import java.util.List;

/**
 * @author zhaozhuo
 * @date 2018/7/16 15:42
 */
public class MyPageAdapter extends FragmentPagerAdapter {

    private List<Fragment> datas;
    private List<Tree> titles;

    public MyPageAdapter(FragmentManager fm, List<Fragment> fragments, List<Tree> trees) {
        super(fm);
        this.datas = fragments;
        this.titles = trees;
    }

//    public void setData(ArrayList<Fragment> datas) {
//        this.datas = datas;
//    }
//
//    public void setTitles(ArrayList<Tree> titles) {
//        this.titles = titles;
//    }

    @Override
    public Fragment getItem(int position) {
        return datas == null ? null : datas.get(position);
    }

    @Override
    public int getCount() {
        return datas == null ? 0 : datas.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles == null ? null : titles.get(position).getName();
    }
}
