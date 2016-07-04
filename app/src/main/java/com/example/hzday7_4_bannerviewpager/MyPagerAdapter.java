package com.example.hzday7_4_bannerviewpager;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by Wakesy on 2016/7/4.
 */
public class MyPagerAdapter extends PagerAdapter {
    private List<ImageView>mlist;


    public MyPagerAdapter(List<ImageView> mlist) {
        this.mlist=mlist;
    }

    @Override
    public int getCount() {
        //实现无限循环
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView=mlist.get(position%mlist.size());//循环播放
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
       container.removeView(mlist.get(position%mlist.size()));
    }
}
