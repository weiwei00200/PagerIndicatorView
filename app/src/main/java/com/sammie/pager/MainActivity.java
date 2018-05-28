package com.sammie.pager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.sammie.pagerlib.SlidingViewPagerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SlidingViewPagerView viewPagerView = findViewById(R.id.id_sliding_view_pager_view);

        ArrayList<Integer> localImgList = new ArrayList<>();
        localImgList.add(R.mipmap.guide_01);
        localImgList.add(R.mipmap.guide_02);
        localImgList.add(R.mipmap.guide_03);

        viewPagerView.setLocalImageView(localImgList).show();
    }
}
