package org.biao.widget.demo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.biao.widget.AutoAndInfiniteViewPager;

import java.util.ArrayList;
import java.util.List;


public class AutoAndInfiniteViewPagerDemo extends Activity {

    private AutoAndInfiniteViewPager viewpager;
    private DemoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_best_view_pager_demo_item);

        viewpager = (AutoAndInfiniteViewPager) findViewById(R.id.wi_viewpager);

        adapter = new DemoAdapter();
        viewpager.setAdapter(adapter);//直接设置自己的adapter即可
        //viewpager.setAdapter(adapter,false); //默认是true
        //viewpager.setOpenLoop(true);
        //viewpager.setOpenInfinite(true);//if loop is open , infinite always true
        //viewpager.setIntervalTime(5000);//设置间隔时间,有默认值,默认是3000毫秒
        //无须手动调用viewpager.setCurrentItem();,在notifyDataSetChanged中已经处理

        adapter.updateDate(ThinkData.getData());
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


    private static class DemoAdapter extends PagerAdapter {
        List<String> data;

        @Override
        public int getCount() {
            return data == null ? 0 : data.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(container.getContext());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ImageLoader.getInstance().displayImage(data.get(position), imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        public void updateDate(List<String> data) {
            if (this.data == null) {
                this.data = new ArrayList<>();
            }
            this.data.addAll(data);
        }
    }
}
