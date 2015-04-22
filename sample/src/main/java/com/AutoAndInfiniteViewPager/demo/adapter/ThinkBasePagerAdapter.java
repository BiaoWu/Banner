package com.autoandinfiniteviewpager.demo.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 *
 * @author wubiao
 *
 * @param <T>
 *            data 集合的泛型
 */
public abstract class ThinkBasePagerAdapter<T> extends PagerAdapter {
    protected Context mContext;
    protected List<T> data;

    public ThinkBasePagerAdapter(Context context) {
        super();
        this.mContext = context;
    }

    public ThinkBasePagerAdapter(Context context, List<T> data) {
        super();
        this.mContext = context;
        this.data = data;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return instantiateItemCall(position);
    }

    protected abstract View instantiateItemCall(int position);

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}