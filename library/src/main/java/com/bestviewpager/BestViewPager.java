package com.bestviewpager;

import android.content.Context;
import android.nfc.Tag;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Map;

/**
 * BestViewPager
 * multifunction ViewPager 具有很多功能的ViewPager
 * item click   item点击
 * item nest    作为item时自动处理左右滑动
 * auto scroll  自动滚动
 * infinite     无限的
 *
 * @author wubiao
 */
public class BestViewPager extends ViewPager {
    private static final String TAG = "BestViewPager";
    public BestViewPager(Context context) {
        this(context, null);
    }

    public BestViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private InfiniteLoopPageChangeListener infiniteLoopPageChangeListener;

    private void init() {
        setOnTouchListener(new MyOnTouchListener());//for single click
        infiniteLoopPageChangeListener = new InfiniteLoopPageChangeListener();
        super.setOnPageChangeListener(infiniteLoopPageChangeListener); //for infinite
    }

    @Override
    public void setOnPageChangeListener(OnPageChangeListener listener) {
        infiniteLoopPageChangeListener.setOnPagerChangeListener(listener);
    }

    /**
     * 处理单击事件
     * for single click
     *
     * @author wubiao
     */
    private class MyOnTouchListener implements OnTouchListener {
        private static final int INTERVAL_TIME = 500;
        private long startTimeMillis;
        private float onTouchDownX;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    onTouchDownX = event.getX();
                    startTimeMillis = System.currentTimeMillis();
                    break;
                case MotionEvent.ACTION_MOVE:
                    break;
                case MotionEvent.ACTION_UP:
                    if (System.currentTimeMillis() - startTimeMillis < INTERVAL_TIME
                            && event.getX() == onTouchDownX) {
                        Log.i(this.getClass().getSimpleName(), "item click----"
                                + getCurrentItem());
                        if (onPagerItemClickListener != null) {//single click
                            onPagerItemClickListener.pagerItemClicked(getActualPosition());
                        }
                        return true;
                    }
                    break;
            }
            return false;
        }
    }

    private OnPagerItemClickListener onPagerItemClickListener;

    /**
     * @param onPagerItemClickListener ItemClickListener
     */
    public void setOnPagerItemClickListener(
            OnPagerItemClickListener onPagerItemClickListener) {
        this.onPagerItemClickListener = onPagerItemClickListener;
    }

    //for single click
    public interface OnPagerItemClickListener {
        void pagerItemClicked(int position);
    }

    // for Nesting
    private float onTouchDownX;
    private float onTouchDownY;
    private boolean isIntercept = false;

    /**
     * 自己处理左右滑动,比如作为list view item时
     * sliding around self , example item for list view
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                stopLoop();
                isIntercept = false;
                onTouchDownX = ev.getX();
                onTouchDownY = ev.getY();
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                // when isIntercept, only Intercept
                if (isIntercept) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                } else {
                    // check first down
                    if (Math.abs(onTouchDownX - ev.getX()) > Math.abs(onTouchDownY
                            - ev.getY())) {
                        isIntercept = true;
                        getParent().requestDisallowInterceptTouchEvent(true);
                    } else {
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                getParent().requestDisallowInterceptTouchEvent(false);
                startLoop();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean isOpenInfinite = true;
    private InfinitePagerAdapter multifunctionPagerAdapter;

    /**
     * 设置adapter 默认开启轮播与无限的,所以建议直接设置adapter即可
     * default is open loop and infinite , so suggest to use this method
     *
     * @param adapter yourself
     */
    @Override
    public void setAdapter(PagerAdapter adapter) {
        setAdapter(adapter, true);
    }

    /**
     * 带参数的设置adapter是否开启轮播
     * if you do not want open loop , set false
     *
     * @param adapter    yourself
     * @param isOpenLoop true is open Loop 开启轮播
     */
    public void setAdapter(PagerAdapter adapter, boolean isOpenLoop) {
        setAdapter(adapter, isOpenLoop, true);
    }

    /**
     * 带参数设置adapter是否开启轮播,及无限左右滑动
     * if you do not want open loop and infinite , set false and false
     *
     * @param adapter        yourself
     * @param isOpenLoop     true is open Loop 开启轮播
     * @param isOpenInfinite true is open infinite 开启无限左右滑动
     */
    public void setAdapter(PagerAdapter adapter, boolean isOpenLoop, boolean isOpenInfinite) {
        this.isOpenLoop = isOpenLoop;
        this.isOpenInfinite = isOpenLoop || isOpenInfinite;
        if (multifunctionPagerAdapter == null) {
            multifunctionPagerAdapter = new InfinitePagerAdapter();
            super.setAdapter(multifunctionPagerAdapter);
        }
        if (adapter != null) {
            multifunctionPagerAdapter.setAdapter(adapter);
        }
    }

    /**
     * 关闭无限,设置false即可
     * close infinite
     *
     * @param isOpenInfinite true is open
     */
    public void setOpenInfinite(boolean isOpenInfinite) {
        this.isOpenInfinite = isOpenLoop || isOpenInfinite;
    }

    public boolean isOpenInfinite() {
        return isOpenInfinite;
    }

    /**
     * 关闭轮播,设置false即可
     * close loop
     *
     * @param isOpenLoop true is open
     */
    public void setOpenLoop(boolean isOpenLoop) {
        this.isOpenLoop = isOpenLoop;
    }

    public boolean isOpenLoop() {
        return isOpenLoop;
    }

    /**
     * 无限的PagerAdapter
     *
     * @author wubiao
     */
    private class InfinitePagerAdapter extends PagerAdapter {
        private PagerAdapter adapter;
        private final Map<String, View> views = new HashMap<>();

        public void setAdapter(PagerAdapter adapter) {
            this.adapter = adapter;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            adapter.destroyItem(container, position, object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = views.get(String.valueOf(position));
            if (view == null) {
                int actualPosition = getActualPosition(position);
                view = (View) adapter.instantiateItem(container, actualPosition);
                views.put(String.valueOf(position), view);
                container.addView(view);
            }
            return view;
        }

        @Override
        public int getCount() {
            return adapter == null || adapter.getCount() == 0
                    ? 0
                    : adapter.getCount() + (isOpenInfinite ? 2 : 0);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return adapter.isViewFromObject(view, object);
        }

        @Override
        public int getItemPosition(Object object) {
            return adapter.getItemPosition(object);
        }
    }

    private int getActualPosition() {
        return isOpenInfinite ? getActualPosition(getCurrentItem()) : getCurrentItem();
    }

    //获取到实际的位置
    private int getActualPosition(int position) {
        if (isOpenInfinite) {
            if (position == 0) {
                position = getAdapter().getCount() - 3;
            } else if (position == getAdapter().getCount() - 1) {
                position = 0;
            } else {
                position -= 1;
            }
        }
        return position;
    }

    private int mCurrentPagePosition; // if open Infinite , it is not the actual position

    /**
     * 无限轮播的核心PageChangeListener
     *
     * @author wubiao
     */
    private class InfiniteLoopPageChangeListener implements OnPageChangeListener {
        private OnPageChangeListener onPagerChangeListener;
        private boolean isChanged;

        public void setOnPagerChangeListener(OnPageChangeListener onPagerChangeListener) {
            this.onPagerChangeListener = onPagerChangeListener;
        }

        @Override
        public void onPageSelected(int position) {
            if (isOpenInfinite) {
                isChanged = true;

                // C A B C A , 0->3, 4->1
                if (position == 0) {
                    position = getAdapter().getCount() - 2;
                } else if (position == getAdapter().getCount() - 1) {
                    position = 1;
                }

                mCurrentPagePosition = position;
            }

            if (onPagerChangeListener != null) {
                onPagerChangeListener.onPageSelected(isOpenInfinite ? position - 1 : position);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (isOpenInfinite && ViewPager.SCROLL_STATE_IDLE == state) {
                if (isChanged) {
                    isChanged = false;
                    setCurrentItem(mCurrentPagePosition, false);
                }
            }

            if (onPagerChangeListener != null) {
                onPagerChangeListener.onPageScrollStateChanged(state);
            }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (onPagerChangeListener != null) {
                onPagerChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
        }
    }

    /**
     * 数据改变时用来刷新数据
     * if data is change , invoke this method
     */
    public void notifyDataSetChanged() {
        stopLoop();
        getAdapter().notifyDataSetChanged();
        setCurrentItem(isOpenInfinite ? 1 : 0);
        startLoop();
    }

    //for loop
    private static final int DEFAULT_INTERVAL_TIME = 3000;
    private static final int MESSAGE_FLAG = 0;
    private int intervalTime = DEFAULT_INTERVAL_TIME;
    private boolean isLooping;
    private boolean isOpenLoop;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (isOpenLoop) {
                setCurrentItem(getNextPosition(mCurrentPagePosition));
                handler.postDelayed(LoopTask, intervalTime);
            }
        }
    };

    /**
     * 默认间隔时间是3000毫秒,如果需要设置即可,单位是毫秒
     * default interval time is 3000 milliseconds
     * if you want custom you can invoke this method
     *
     * @param intervalTime 间隔时间
     */
    public void setIntervalTime(int intervalTime) {
        if (intervalTime > 0) {
            this.intervalTime = intervalTime;
        }
    }

    private int getNextPosition(int mCurrentPagePosition) {
        if (mCurrentPagePosition == getAdapter().getCount() - 1) {
            return 0;
        }
        return ++mCurrentPagePosition;
    }

    /**
     * 启动轮播
     */
    public void startLoop() {
        if (isOpenLoop && !isLooping) {
            isLooping = true;
            handler.postDelayed(LoopTask, intervalTime);
        }
    }

    /**
     * 停止轮播
     */
    public void stopLoop() {
        if (isOpenLoop && isLooping) {
            isLooping = false;
            handler.removeMessages(MESSAGE_FLAG);
        }
    }

    private Runnable LoopTask = new Runnable() {
        @Override
        public void run() {
            if (isOpenLoop) {
                Log.i(TAG,"LoopTask sendEmptyMessage");
                handler.sendEmptyMessage(MESSAGE_FLAG);
            }
        }

    };
}
