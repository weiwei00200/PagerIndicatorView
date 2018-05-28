package com.sammie.pagerlib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class SlidingViewPagerView extends FrameLayout {

    private Context mContext;

    private boolean mIsShowIndicator = false;//是否显示地下的圆点
    private float mIndicatorSpace = 10;//圆点间距 dp
    private float mIndicatorSize = 10;//圆点大小 dp
    private int mCurrentSelectedPosition = 0;

    private ViewPager mViewPager;
    private ArrayList<ImageView> mImgList;
    private LinearLayout mLayoutIndicator;

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
        mIsShowIndicator = typedArray.getBoolean(R.styleable.PagerIndicatorView_PagerIndicatorView_isShowIndicator, false);
        mIndicatorSpace = typedArray.getDimension(R.styleable.PagerIndicatorView_PagerIndicatorView_indicatorSpace, 10);
        mIndicatorSize = typedArray.getDimension(R.styleable.PagerIndicatorView_PagerIndicatorView_indicatorSize, 10);
        mCurrentSelectedPosition = typedArray.getInteger(R.styleable.PagerIndicatorView_PagerIndicatorView_selectedPosition, 0);
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

            }
        });
    }

    private void initIndicator(int pagerAmount) {
        mLayoutIndicator.setVisibility(mIsShowIndicator ? View.VISIBLE : View.GONE);
        View indicatorItem = null;
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(dp2px(mIndicatorSize), dp2px(mIndicatorSize));
        int margin = dp2px(mIndicatorSpace);
        lp.setMargins(margin, 0, margin, 0);
        for (int i = 0; i < pagerAmount; i++) {
            indicatorItem = new View(mContext);
            indicatorItem.setBackgroundResource(R.drawable.indicator_selector);
            indicatorItem.setLayoutParams(lp);
            mLayoutIndicator.addView(indicatorItem);
        }
        setSelectedPosition(mCurrentSelectedPosition);
    }

    public void setSelectedPosition(int position) {
        View childView = null;
        for (int i = 0; i < mLayoutIndicator.getChildCount(); i++) {
            childView = mLayoutIndicator.getChildAt(i);
            childView.setSelected(position == i);
        }
    }

    public SlidingViewPagerView setLocalImageView(List<Integer> pagePicList) {
        try {
            mImgList = new ArrayList<>();
            ImageView img = null;
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            for (Integer picRes : pagePicList) {
                img = new ImageView(mContext);
                img.setScaleType(ImageView.ScaleType.FIT_XY);
                img.setLayoutParams(lp);
                img.setImageResource(picRes);
                mImgList.add(img);
            }
            initIndicator(pagePicList.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public void show() {
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
     * dp转px
     *
     * @param dipValue   Value in dp
     * @return px
     */
    private int dp2px(float dipValue) {
        float scale = getResources().getDisplayMetrics().density;
        return (int)(dipValue * scale + 0.5f);
    }
}
