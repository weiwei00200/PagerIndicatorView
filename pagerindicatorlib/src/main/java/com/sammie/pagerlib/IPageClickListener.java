package com.sammie.pagerlib;

public interface IPageClickListener {

    /**
     * 点击viewpager中的事件回调
     * @param url 如果一开始没设置，则会返回""空字符串
     * @param position 点击Page下标
     */
    void onClickPageImage(String url,int position);
}
