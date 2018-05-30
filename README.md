# PagerIndicatorView
**介绍：该控件可用于制作Banner或App首次进入的引导页。**
 
### 效果图
![img](https://s19.aconvert.com/convert/p3r68-cdx67/myd4p-b7o41.gif)  

### 使用方法

### 配置步骤1：
    implementation 'com.github.weiwei00200:PagerIndicatorView:1.0.0'
    
### 配置步骤2：
    allprojects {
       repositories {
          maven { url "https://jitpack.io" }
       }
    }
### 开始使用：
    <com.sammie.pagerlib.SlidingViewPagerView
        android:id="@+id/id_remote_sliding_view_pager_view"
        android:layout_width="match_parent"
        app:isShowIndicator="true"
        app:indicatorSpace="4dp"
        app:slidingInterval="5"
        app:slidingSpeed="200"
        app:indicatorMarginBottom = "10dp"
        app:indicatorGravity = "left"
        app:indicatorSize="6dp"
        app:isAutoSliding = "true"
        app:selectedPosition = "0"
        android:layout_height="150dp"/>
        
######  填充数据：
	//--------------网络图片------------------
	ArrayList<String> imgList = new ArrayList<>();
	urlImgList.add("https://b-ssl.duitang.com/uploads/item/201604/12/20160412094155_nAmci.thumb.700_0.jpeg");
	urlImgList.add("https://b-ssl.duitang.com/uploads/item/201604/12/20160412094534_4VFKi.jpeg");
	urlImgList.add("https://a-ssl.duitang.com/uploads/item/201604/12/20160412094520_c3fUC.thumb.700_0.jpeg");
	urlImgList.add("https://b-ssl.duitang.com/uploads/item/201607/22/20160722103000_ewA8G.thumb.700_0.jpeg");
	
	//--------------本地图片------------------
	//ArrayList<Integer> imgList = new ArrayList<>();
	//imgList .add(R.mipmap.pic_01);
	//imgList .add(R.mipmap.pic_02);
	//imgList .add(R.mipmap.pic_03);	    

	SlidingViewPagerView remoteViewPagerView = findViewById(R.id.id_remote_sliding_view_pager_view);
	remoteViewPagerView.setUrlImage(urlImgList,R.mipmap.ic_launcher,R.mipmap.ic_launcher);
    
### 参数说明：（全部参数可选设置）
参数     | 说明
-------- | ---
isAutoSliding | true,false 是否自动滚动。
slidingInterval | 滚动的间隔时间，单位：毫秒。
slidingSpeed | 滚动到下一页这个动作所需时间，单位：毫秒。
isShowIndicator | true,false 是否显示指示器。
indicatorSpace | 每个指示器圆点之间的间隔 单位：dp
indicatorSize|指示器圆点的大小 单位：dp
indicatorMarginBottom | 指示器margin下方的距离 单位：dp
selectedPosition | 显示那一页图片
indicatorGravity|指示器在底部的显示位置 left，right，middle 

### 版本：
版本号     | 备注
-------- | ---
v1.0 | 1）支持自动滚动<br>2）支持本地图片，URL图片
 

