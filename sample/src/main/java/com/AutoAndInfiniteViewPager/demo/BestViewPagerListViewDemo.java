package com.autoandinfiniteviewpager.demo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.autoandinfiniteviewpager.AutoAndInfiniteViewPager;
import com.autoandinfiniteviewpager.demo.adapter.ThinkBaseAdapter;
import com.autoandinfiniteviewpager.demo.adapter.ThinkBasePagerAdapter;
import com.autoandinfiniteviewpager.demo.util.ThinkData;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class BestViewPagerListViewDemo extends Activity {

    private ListView mListview;
    private DemoListAdapter mListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_best_view_pager_demo);
        mListview = (ListView) findViewById(R.id.wi_listview);
        mListAdapter = new DemoListAdapter(this);
        mListview.setAdapter(mListAdapter);

        List<List<String>> data = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            data.add(ThinkData.getData());
        }

        mListAdapter.setData(data);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mListAdapter.stopLoop();
    }

    private static class DemoListAdapter extends ThinkBaseAdapter<List<String>> {
        private List<AutoAndInfiniteViewPager> viewPagers = new ArrayList<>();

        //remember stopLoop
        public void stopLoop() {
            for (AutoAndInfiniteViewPager bestViewPager : viewPagers) {
                bestViewPager.stopLoop();
            }
        }

        public DemoListAdapter(Context context) {
            super(context);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.activity_best_view_pager_demo_item, null);
                holder = new ViewHolder();
                holder.bestViewPager = (AutoAndInfiniteViewPager) convertView.findViewById(R.id.wi_viewpager);

                viewPagers.add(holder.bestViewPager);

                holder.adapter = new DemoAdapter(context);
                holder.bestViewPager.setAdapter(holder.adapter);//直接设置自己的adapter即可

                //default is open autoscroll and infinite,默认是开启自动轮播和无限的
                //holder.bestViewPager.setAdapter(adapter,false);
                holder.bestViewPager.setOpenLoop(position % 2 == 0 ? true : false);
                holder.bestViewPager.setOpenInfinite(position % 2 == 0 ? true : false);
                holder.bestViewPager.setIntervalTime((new Random().nextInt(2) + 3) * 1000);//设置间隔时间,默认值是3000毫秒
                //无须手动调用holder.bestViewPager.setCurrentItem(),在notifyDataSetChanged中已经处理

                holder.bestViewPager.setOnPagerItemClickListener(new AutoAndInfiniteViewPager.OnPagerItemClickListener() {
                    @Override
                    public void pagerItemClicked(int itemPosition) {
                        Toast.makeText(context, "listview的第:" + position + "个item的bestviewpager的第" + itemPosition + "个tiem", Toast.LENGTH_LONG).show();
                    }
                });

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.adapter.setData(data.get(position));
            holder.bestViewPager.notifyDataSetChanged();

            return convertView;
        }

        private class ViewHolder {
            public AutoAndInfiniteViewPager bestViewPager;
            public DemoAdapter adapter;
        }
    }


    private static class DemoAdapter extends ThinkBasePagerAdapter<String> {

        public DemoAdapter(Context context) {
            super(context);
        }

        @Override
        protected View instantiateItemCall(int position) {
            ImageView imageView = new ImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ImageLoader.getInstance().displayImage(data.get(position), imageView);
            return imageView;
        }
    }
}
