package com.zz.wanandroid.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;


import java.util.List;

/**
 * @author zhaozhuo
 * @date 2018/7/10 16:37
 */
public class BannerPageAdapter extends PagerAdapter {

    private List<View> banners;

    public BannerPageAdapter (List<View> banners ){
        this.banners = banners;
    }

    @Override
    public int getCount() {
        return banners==null?0:banners.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(banners.get(position));
        return banners.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(banners.get(position));
    }

}
