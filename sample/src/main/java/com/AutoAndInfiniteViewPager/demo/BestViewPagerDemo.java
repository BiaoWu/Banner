package com.autoandinfiniteviewpager.demo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.autoandinfiniteviewpager.AutoAndInfiniteViewPager;
import com.autoandinfiniteviewpager.demo.adapter.ThinkBasePagerAdapter;
import com.autoandinfiniteviewpager.demo.util.ThinkData;
import com.nostra13.universalimageloader.core.ImageLoader;


public class BestViewPagerDemo extends Activity {

    private AutoAndInfiniteViewPager viewpager;
    private DemoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_best_view_pager_demo_item);

        viewpager = (AutoAndInfiniteViewPager) findViewById(R.id.wi_viewpager);

        adapter = new DemoAdapter(this);
        viewpager.setAdapter(adapter);//直接设置自己的adapter即可
        //viewpager.setAdapter(adapter,false); //默认是true
        //viewpager.setOpenLoop(true);
        //viewpager.setOpenInfinite(true);//if loop is open , infinite always true
        //viewpager.setIntervalTime(5000);//设置间隔时间,有默认值,默认是3000毫秒
        //无须手动调用viewpager.setCurrentItem();,在notifyDataSetChanged中已经处理

        adapter.setData(ThinkData.getData());
        viewpager.notifyDataSetChanged();

        viewpager.setOnPagerItemClickListener(new AutoAndInfiniteViewPager.OnPagerItemClickListener() {
            @Override
            public void pagerItemClicked(int position) {
                // do yourself
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewpager.stopLoop();
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
