package com.xiaogang.ChildOne.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;

import java.util.List;

public class MyPagerAdapter extends PagerAdapter {
	private List<LinearLayout> list;
	
	public MyPagerAdapter(List<LinearLayout> list) {
		this.list = list;
	}
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public void destroyItem(View arg0, int arg1, Object arg2){
		((ViewPager) arg0).removeView(list.get(arg1));
	}

	@Override
	public Object instantiateItem(View container, int position) {
		((ViewPager) container).addView(list.get(position), 0);
		return list.get(position);
	}
	
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0==arg1;
	}
}