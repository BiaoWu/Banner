package org.biao.widget;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;

/**
 * AutoAndInfiniteViewPager
 * multifunction ViewPager 具有很多功能的ViewPager
 * item click   item点击
 * item nest    作为item时自动处理左右滑动
 * auto scroll  自动滚动
 * infinite     无限的
 *
 * @author wubiao
 */
public class Banner extends ViewPager {
    private static final String TAG = Banner.class.getSimpleName();
    private static final boolean DEBUG = false;
    private InfiniteLoopPageChangeListener infiniteLoopPageChangeListener;

    private float mPagingTouchSlop;

    private LoopTask mLoopTask;

    public Banner(Context context) {
        this(context, null);
    }

    public Banner(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setOnTouchListener(new MyOnTouchListener());//for single click
        infiniteLoopPageChangeListener = new InfiniteLoopPageChangeListener();
        super.setOnPageChangeListener(infiniteLoopPageChangeListener);
        multifunctionPagerAdapter = new InfinitePagerAdapter();
        super.setAdapter(multifunctionPagerAdapter);

        final ViewConfiguration conf = ViewConfiguration.get(getContext());
        mPagingTouchSlop = conf.getScaledTouchSlop() * 2;

        mLoopTask = new LoopTask();
    }

    @Override
    public void setOnPageChangeListener(OnPageChangeListener listener) {
        infiniteLoopPageChangeListener.setOnPagerChangeListener(listener);
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
    private DispatchTouchEventListener mDispatchTouchEventListener;

    public void setDispatchTouchEventListener(DispatchTouchEventListener dispatchTouchEventListener) {
        mDispatchTouchEventListener = dispatchTouchEventListener;
    }

    public interface DispatchTouchEventListener {
        void disallowInterceptTouchEvent(boolean disallow);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    /**
     * 自己处理左右滑动,比如作为list view item时
     * sliding around self , example item for list view
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (DEBUG)
            Log.i(TAG, "dispatchTouchEvent ing");
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (DEBUG)
                    Log.i(TAG, "dispatchTouchEvent ACTION_DOWN");
                isIntercept = false;
                onTouchDownX = ev.getX();
                onTouchDownY = ev.getY();
                getParent().requestDisallowInterceptTouchEvent(true);
                if (mDispatchTouchEventListener != null) {
                    mDispatchTouchEventListener.disallowInterceptTouchEvent(true);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                // when isIntercept, only Intercept
                if (isIntercept) {
                    if (DEBUG)
                        Log.i(TAG, "dispatchTouchEvent ACTION_MOVE isIntercept = true");
                    getParent().requestDisallowInterceptTouchEvent(true);
                } else {
                    // check first down
                    float offsetX = onTouchDownX - ev.getX();
                    float offsetY = onTouchDownY - ev.getY();
                    if ((Math.abs(offsetX) > mPagingTouchSlop && Math.abs(offsetX) * 2 > Math.abs(offsetY))) {
                        stopLoop();
                        isIntercept = true;
                        if (DEBUG)
                            Log.i(TAG, "dispatchTouchEvent ACTION_MOVE isIntercept = false change to true");
                        getParent().requestDisallowInterceptTouchEvent(true);
                        if (mDispatchTouchEventListener != null) {
                            mDispatchTouchEventListener.disallowInterceptTouchEvent(true);
                        }
                    } else {
                        if (DEBUG)
                            Log.i(TAG, "dispatchTouchEvent ACTION_MOVE isIntercept = false too");
                        getParent().requestDisallowInterceptTouchEvent(false);
                        if (mDispatchTouchEventListener != null) {
                            mDispatchTouchEventListener.disallowInterceptTouchEvent(false);
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (DEBUG)
                    Log.i(TAG, "dispatchTouchEvent ACTION_UP");
                getParent().requestDisallowInterceptTouchEvent(false);
                if (mDispatchTouchEventListener != null) {
                    mDispatchTouchEventListener.disallowInterceptTouchEvent(false);
                }
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
        mLoopTask.isOpenLoop = isOpenLoop;
        this.isOpenInfinite = isOpenInfinite;
        multifunctionPagerAdapter.setAdapter(adapter);
    }

    /**
     * 关闭无限,设置false即可
     * close infinite
     *
     * @param isOpenInfinite true is open
     */
    public void setOpenInfinite(boolean isOpenInfinite) {
        this.isOpenInfinite = isOpenInfinite;
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
        mLoopTask.isOpenLoop = isOpenLoop;
    }

    public boolean isOpenLoop() {
        return mLoopTask.isOpenLoop;
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

    /**
     * 默认间隔时间是3000毫秒,如果需要设置即可,单位是毫秒
     * default interval time is 3000 milliseconds
     * if you want custom you can invoke this method
     *
     * @param intervalTime 间隔时间
     */
    public void setIntervalTime(int intervalTime) {
        if (intervalTime > 0) {
            mLoopTask.intervalTime = intervalTime;
        }
    }

    private int getNextPosition() {
        int position = getCurrentItem();
        if (position == getAdapter().getCount() - 1) {
            position = 0;
        } else {
            position++;
        }
        return position;
    }

    /**
     * 启动轮播
     */
    public void startLoop() {
        mLoopTask.startLoop();
    }

    /**
     * 停止轮播
     */
    public void stopLoop() {
        mLoopTask.stopLoop();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mLoopTask.finish();
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
        private boolean isMoved;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    isMoved = false;
                    startTimeMillis = System.currentTimeMillis();
                    break;
                case MotionEvent.ACTION_MOVE:
                    isMoved = true;
                    break;
                case MotionEvent.ACTION_UP:
                    if (!isMoved && System.currentTimeMillis() - startTimeMillis < INTERVAL_TIME) {
                        if (DEBUG)
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

    /**
     * 无限的PagerAdapter
     *
     * @author wubiao
     */
    private class InfinitePagerAdapter extends PagerAdapter {
        private PagerAdapter adapter;
        private SparseArray<View> views = new SparseArray<>();

        public void setAdapter(PagerAdapter adapter) {
            this.adapter = adapter;
        }

        public PagerAdapter getAdapter() {
            return adapter;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            adapter.destroyItem(container, position, object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = views.get(position);
            if (view == null) {
                int actualPosition = getActualPosition(position);
                view = (View) adapter.instantiateItem(container, actualPosition);
                views.put(position, view);
            }
            ViewParent parent = view.getParent();
            if (parent == null) {
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

    /**
     * 无限轮播的核心PageChangeListener
     *
     * @author wubiao
     */
    private class InfiniteLoopPageChangeListener implements OnPageChangeListener {
        private OnPageChangeListener onPagerChangeListener;
        private boolean isChanged;
        private int mCurrentPagePosition; // if open Infinite , it is not the actual position

        public void setOnPagerChangeListener(OnPageChangeListener onPagerChangeListener) {
            this.onPagerChangeListener = onPagerChangeListener;
        }

        @Override
        public void onPageSelected(int position) {
            if (DEBUG)
                Log.i(TAG, "onPageSelected");
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
            if (DEBUG)
                Log.i(TAG, "onPageScrollStateChanged");
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
            if (DEBUG)
                Log.i(TAG, "onPageScrolled");
            if (onPagerChangeListener != null) {
                onPagerChangeListener.onPageScrolled(getActualPosition(position), positionOffset, positionOffsetPixels);
            }
        }
    }

    private class LoopTask implements Runnable {
        //for loop
        private static final int DEFAULT_INTERVAL_TIME = 3000;
        int intervalTime = DEFAULT_INTERVAL_TIME;
        boolean isLooping;
        boolean isOpenLoop;

        void startLoop() {
            if (isOpenLoop && !isLooping) {
                isLooping = true;
                postDelayed(this, intervalTime);
            }
        }

        void stopLoop() {
            isLooping = false;
            removeCallbacks(this);
        }


        void finish() {
            stopLoop();
        }

        @Override
        public void run() {
            if (isOpenLoop) {
                if (DEBUG)
                    Log.i(TAG, "mLoopTask sendEmptyMessage");
                setCurrentItem(getNextPosition());
                postDelayed(this, intervalTime);
            }
        }
    }
}
