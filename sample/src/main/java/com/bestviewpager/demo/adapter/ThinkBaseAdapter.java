package com.bestviewpager.demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author wubiao
 * 
 * @param <T>
 *            data 集合的泛型
 */
public abstract class ThinkBaseAdapter<T> extends BaseAdapter {

	protected Context context;
	protected List<T> data;
	protected LayoutInflater inflater;
	protected int size;

	public List<T> getData() {
		return data;
	}

    public void setData(List<T> data) {
        this.data = data;
    }

    public void clear() {
		data.clear();
	}

	public ThinkBaseAdapter(Context context, List<T> data, int size) {
		this.context = context;
		this.data = data;
		this.size = size;
		initOptions();
	}

	public ThinkBaseAdapter(Context context, List<T> data) {
		this.context = context;
		this.data = data;
		initOptions();
	}

	public ThinkBaseAdapter(Context context, int size) {
		this.context = context;
		this.data = new ArrayList<T>();
		this.size = size;
		initOptions();
	}

	public ThinkBaseAdapter(Context context) {
		this.context = context;
		this.data = new ArrayList<T>();
		initOptions();
	}

	private void initOptions() {
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return data == null ? 0 : data.size() + size;
	}

	@Override
	public Object getItem(int position) {
		return data == null ? null : data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
}
