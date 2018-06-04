package com.sammie.pager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.sammie.pagerlib.IPageClickListener;
import com.sammie.pagerlib.SlidingViewPagerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //本地图片
        ArrayList<Integer> localImgList = new ArrayList<>();
        localImgList.add(R.mipmap.pic_01);
        localImgList.add(R.mipmap.pic_02);
        localImgList.add(R.mipmap.pic_03);
        SlidingViewPagerView localViewPagerView = findViewById(R.id.id_local_sliding_view_pager_view);
        localViewPagerView.setLocalImage(localImgList);

        //URL
        ArrayList<String> urlImgList = new ArrayList<>();
        urlImgList.add("https://b-ssl.duitang.com/uploads/item/201604/12/20160412094155_nAmci.thumb.700_0.jpeg");
        urlImgList.add("https://b-ssl.duitang.com/uploads/item/201604/12/20160412094534_4VFKi.jpeg");
        urlImgList.add("https://a-ssl.duitang.com/uploads/item/201604/12/20160412094520_c3fUC.thumb.700_0.jpeg");
        urlImgList.add("https://b-ssl.duitang.com/uploads/item/201607/22/20160722103000_ewA8G.thumb.700_0.jpeg");

        ArrayList<String> toUrlList = new ArrayList<>();
        toUrlList.add("https://www.baidu.com/");
        toUrlList.add("https://cn.bing.com/");
        toUrlList.add("https://www.sogou.com/");
        toUrlList.add("https://www.so.com/");

        SlidingViewPagerView remoteViewPagerView = findViewById(R.id.id_remote_sliding_view_pager_view);
        //普通显示URL图片
//        remoteViewPagerView.setUrlImage(urlImgList, R.mipmap.ic_launcher, R.mipmap.ic_launcher);
        //可点击的URL图片
        remoteViewPagerView.setUrlImage(urlImgList, toUrlList, new IPageClickListener() {
            @Override
            public void onClickPageImage(String url, int position) {
                //拿到 URL 做跳转动作
            }
        });
    }
}
