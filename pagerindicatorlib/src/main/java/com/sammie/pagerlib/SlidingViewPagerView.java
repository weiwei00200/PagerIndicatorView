package com.sammie.pagerlib;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SlidingViewPagerView extends FrameLayout {

    private Context mContext;
    private final String LEFT = "left", RIGHT = "right", MIDDLE = "middle";

    private boolean mIsAutoSliding = false;//是否自动滑动
    private int mSlidingInterval = 5;//自动滑动的时间间隔，单位秒
    private int mSlidingSpeed = 1000;//滑动的时间，单位毫秒
    private boolean mIsShowIndicator = false;//是否显示指示器
    private float mIndicatorSpace = 10;//指示器间距 dp
    private float mIndicatorSize = 10;//指示器大小 dp
    private float mIndicatorMarginBottom = 20;//指示器marginBottom
    private int mCurrentSelectedPosition = 0;//当前显示的Position下标
    private String mIndicatorGravity = MIDDLE; // 指示器对齐方式
    private boolean mIsLoop = false;//是否要循环滚动

    private ViewPager mViewPager;
    private ArrayList<ImageView> mImgList;
    private LinearLayout mLayoutIndicator;
    private Timer mCountDownTimer;
    private List<String> mToUrlList = new ArrayList<>();
    private boolean mIsClickImageListenerEffective = false;

    private IPageClickListener mPageClickListener;

    public SlidingViewPagerView(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public SlidingViewPagerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttribute(attrs);
        initView(context);
    }

    public SlidingViewPagerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttribute(attrs);
        initView(context);
    }

    private void initAttribute(AttributeSet attrs) {
        TypedArray typedArray = getResources().obtainAttributes(attrs, R.styleable.PagerIndicatorView);
        mIsAutoSliding = typedArray.getBoolean(R.styleable.PagerIndicatorView_isAutoSliding, false);
        mSlidingInterval = typedArray.getInteger(R.styleable.PagerIndicatorView_slidingInterval, 5);
        mSlidingSpeed = typedArray.getInteger(R.styleable.PagerIndicatorView_slidingSpeed, 0);
        mIsShowIndicator = typedArray.getBoolean(R.styleable.PagerIndicatorView_isShowIndicator, false);
        mIndicatorSpace = typedArray.getDimension(R.styleable.PagerIndicatorView_indicatorSpace, 10);
        mIndicatorSize = typedArray.getDimension(R.styleable.PagerIndicatorView_indicatorSize, 10);
        mIsLoop = typedArray.getBoolean(R.styleable.PagerIndicatorView_isLoop, false);
        mCurrentSelectedPosition = typedArray.getInteger(R.styleable.PagerIndicatorView_selectedPosition, mIsLoop ? 1 : 0) + (mIsLoop ? 1 : 0);
        mIndicatorMarginBottom = typedArray.getDimension(R.styleable.PagerIndicatorView_indicatorMarginBottom, 20);
        String gravityStr = typedArray.getString(R.styleable.PagerIndicatorView_indicatorGravity);
        if (!TextUtils.isEmpty(gravityStr)) {
            mIndicatorGravity = gravityStr.toLowerCase();
        }
    }

    private void initView(Context context) {
        mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.layout_sliding_view_pager_view, null, false);
        addView(view);
        mLayoutIndicator = view.findViewById(R.id.id_indicator_layout);
        mViewPager = view.findViewById(R.id.id_view_pager);
        initListener();
    }

    private void initListener() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setSelectedPosition(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (mIsLoop) {
                    final int lastPosition = mImgList.size() - 1;
                    if (state == ViewPager.SCROLL_STATE_IDLE) {//页面加载完成
                        if (mViewPager.getCurrentItem() == lastPosition) {
                            mViewPager.setCurrentItem(mCurrentSelectedPosition, false);
                        } else if (mViewPager.getCurrentItem() == 0) {
                            mViewPager.setCurrentItem(lastPosition - 1, false);
                        }
                    }
                }
            }
        });
        mViewPager.setOnTouchListener(new OnTouchListener() {
            boolean isClick = false;//是否是点击事件
            float downX, downY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        downX = event.getX();
                        downY = event.getY();
                        if (mIsAutoSliding) {
                            stopAutoSliding();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (mIsAutoSliding) {
                            startAutoSliding();
                        }
                        isClick = Math.abs(event.getX() - downX) < 5 && Math.abs(event.getY() - downY) < 5;
                        if (isClick && mIsClickImageListenerEffective) {
                            mPageClickListener.onClickPageImage(mToUrlList.get(mViewPager.getCurrentItem()), mImgList.size() > 1 ? mViewPager.getCurrentItem() - 1 : mViewPager.getCurrentItem());
                        }
                        downX = 0;
                        downY = 0;
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        mViewPager.post(new Runnable() {
            @Override
            public void run() {
//                if (mSlidingSpeed != 0) {
//                    setScrollSpeed(mSlidingSpeed);
//                }
            }
        });
    }

    private void initIndicator(int pagerAmount) {
        mLayoutIndicator.setVisibility(mIsShowIndicator ? View.VISIBLE : View.GONE);
        if (mIsShowIndicator) {
            if (mIsLoop) {
                pagerAmount -= 2;
            }
            RelativeLayout.LayoutParams containerLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            containerLayoutParams.addRule(TextUtils.equals(mIndicatorGravity, LEFT) ? RelativeLayout.ALIGN_PARENT_LEFT : (TextUtils.equals(mIndicatorGravity, RIGHT) ? RelativeLayout.ALIGN_PARENT_RIGHT : RelativeLayout.CENTER_HORIZONTAL));
            containerLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            containerLayoutParams.bottomMargin = Math.round(mIndicatorMarginBottom);
            mLayoutIndicator.setLayoutParams(containerLayoutParams);
            //显示指示器
            View indicatorItem = null;
            LinearLayout.LayoutParams indicatorLayoutParams = new LinearLayout.LayoutParams(Math.round(mIndicatorSize), Math.round(mIndicatorSize));
            int margin = Math.round(mIndicatorSpace);
            indicatorLayoutParams.setMargins(margin, 0, margin, 0);
            for (int i = 0; i < pagerAmount; i++) {
                indicatorItem = new View(mContext);
                indicatorItem.setBackgroundResource(R.drawable.indicator_selector);
                indicatorItem.setLayoutParams(indicatorLayoutParams);
                mLayoutIndicator.addView(indicatorItem);
            }
            setSelectedPosition(mCurrentSelectedPosition);
        }
    }

    public void clearData() {
        try {
            mLayoutIndicator.removeAllViews();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setSelectedPosition(int position) {
        View childView = null;
        position = position - (mIsLoop ? 1 : 0);
        for (int i = 0; i < mLayoutIndicator.getChildCount(); i++) {
            childView = mLayoutIndicator.getChildAt(i);
            childView.setSelected(position == i);
        }
    }

    /**
     * 设置本地图片
     *
     * @param pagePicList
     * @return
     */
    public SlidingViewPagerView setLocalImage(List<Integer> pagePicList) {
        try {
            if (mIsLoop && pagePicList.size() > 1) {//大于1张图片才需要轮播,才开启定时轮播
                //需要循环滚动，加入首尾项
                pagePicList.add(0, pagePicList.get(pagePicList.size() - 1));
                pagePicList.add(pagePicList.get(1));
                if (mIsAutoSliding) {
                    startAutoSliding();
                }
            }
            mImgList = new ArrayList<>();
            ImageView img = null;
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            for (Integer picRes : pagePicList) {
                img = new ImageView(mContext);
                img.setScaleType(ImageView.ScaleType.FIT_XY);
                img.setLayoutParams(lp);
                mImgList.add(img);
                Picasso.get().load(picRes).into(img);
//                ImageLoader.loadImageByDefault(img, img, picRes);
            }
            if (pagePicList.size() > 1) {
                //大于1张才需要初始化Indicator
                initIndicator(pagePicList.size() + (mIsLoop ? -2 : 0));
            }
            show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * 设置网络图片
     *
     * @param urlList
     * @return
     */
    public SlidingViewPagerView setUrlImage(List<String> urlList) {
        setUrlImage(urlList, null, null);
        return this;
    }

    public void setUrlImage(List<String> urlList, final List<String> toUrlList, final IPageClickListener listener) {
        try {
            if (mIsLoop && toUrlList.size() > 1) {//大于1张图片才需要轮播,才开启定时轮播
                //需要循环滚动，加入首尾项
                urlList.add(0, urlList.get(urlList.size() - 1));
                urlList.add(urlList.get(1));
                toUrlList.add(0, toUrlList.get(toUrlList.size() - 1));
                toUrlList.add(toUrlList.get(1));
                if (mIsAutoSliding) {
                    startAutoSliding();
                }
            }
            mToUrlList = toUrlList;
            mIsClickImageListenerEffective = listener != null && null != toUrlList && toUrlList.size() == urlList.size();
            mPageClickListener = listener;
            mImgList = new ArrayList<>();
            ImageView img = null;
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            //toUrlList和listener都设置了,并且toUrlList个数和图片个数相同，才有点击返回事件并返回url
            for (int i = 0; i < urlList.size(); i++) {
                String picUrl = urlList.get(i);
                img = new ImageView(mContext);
                img.setScaleType(ImageView.ScaleType.FIT_XY);
                img.setLayoutParams(lp);
                mImgList.add(img);
                Picasso.get().load(picUrl).into(img);
            }
            if (urlList.size() > 1) {
                //大于1张才需要初始化Indicator
                initIndicator(urlList.size());
            }
            show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置滑动的速度
     *
     * @param millisSeconds 毫秒
     */
    private void setScrollSpeed(int millisSeconds) {
        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(mViewPager.getContext(), new AccelerateInterpolator());
            field.set(mViewPager, scroller);
            scroller.setDuration(millisSeconds);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void show() {
        mViewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return mImgList.size();
            }

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public void destroyItem(ViewGroup view, int position, Object object) {
                view.removeView(mImgList.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup view, int position) {
                view.addView(mImgList.get(position));
                return mImgList.get(position);
            }
        });
        mViewPager.setCurrentItem(mCurrentSelectedPosition);
    }

    /**
     * 开始自动滚动
     */
    private void startAutoSliding() {
        stopAutoSliding();
        mCountDownTimer = new Timer();
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                try {
                    if (null == mContext || (mContext instanceof Activity && ((Activity) mContext).isFinishing())) {
                        stopAutoSliding();
                        return;
                    }
                    if (mViewPager.getCurrentItem() == mImgList.size() - 1) {
                        mViewPager.setCurrentItem(0);
                    } else {
                        mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    stopAutoSliding();
                }
            }
        };
        mCountDownTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
            }
        }, mSlidingInterval * 1000, mSlidingInterval * 1000);
    }

    /**
     * 停止自动滚动， 关闭界面的时候需要调用一下
     */
    private void stopAutoSliding() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;//虽然违反了GC原则，但是还是加上吧
        }
    }
}
