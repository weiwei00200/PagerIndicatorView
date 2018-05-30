# PagerIndicatorView
**介绍：该控件可用于制作Banner或App首次进入的引导页。**
 
### 效果图
![img](https://github.com/weiwei00200/PagerIndicatorView/blob/master/gif/img.gif)  

### 使用方法

### 配置步骤1：
    implementation 'com.github.weiwei00200:PagerIndicatorView:1.1.0'
    
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
        
###  填充数据：
	//初始化控件
	SlidingViewPagerView remoteViewPagerView = findViewById(R.id.id_remote_sliding_view_pager_view);

	//--------------准备数据-网络图片------------------
	ArrayList<String> imgList = new ArrayList<>();
	urlImgList.add("https://b-ssl.duitang.com/uploads/item/201604/12/20160412094155_nAmci.thumb.700_0.jpeg");
	urlImgList.add("https://b-ssl.duitang.com/uploads/item/201604/12/20160412094534_4VFKi.jpeg");
	urlImgList.add("https://a-ssl.duitang.com/uploads/item/201604/12/20160412094520_c3fUC.thumb.700_0.jpeg");
	urlImgList.add("https://b-ssl.duitang.com/uploads/item/201607/22/20160722103000_ewA8G.thumb.700_0.jpeg");

	//--------------准备数据-本地图片------------------
	ArrayList<Integer> imgList = new ArrayList<>();
	imgList .add(R.mipmap.pic_01);
	imgList .add(R.mipmap.pic_02);
	imgList .add(R.mipmap.pic_03);	

	//普通显示URL图片
	remoteViewPagerView.setUrlImage(urlImgList,R.mipmap.ic_launcher,R.mipmap.ic_launcher);

	//可点击的URL图片
	remoteViewPagerView.setUrlImage(urlImgList, toUrlList, R.mipmap.ic_launcher, R.mipmap.ic_launcher, new IPageClickListener() {
	    @Override
	    public void onClickPageImage(String url) {
		//拿到 URL 做跳转动作
	    }
	});
### 控件提供方法说明：
##### 显示网络图片方法
	setUrlImage(List<String> urlList, int loadingImgRes, int errorImgRes)
	setUrlImage(List<String> urlList, List<String> toUrlList, int loadingImgRes, int errorImgRes, IPageClickListener listener)
	
	urlList：图片URL数组
	toUrlList：跳转URL
	loadingImgRes：加载中的显示的图片
	errorImgRes：加载失败的显示的图片
	listener：点击当前显示Page的URL
##### 显示网络图片方法
	setLocalImage(List<Integer> pagePicList, int loadingImgRes, int errorImgRes)
	
	pagePicList：本地图片资源数组（R.mipmap.pic...）
	loadingImgRes：加载中的显示的图片
	errorImgRes：加载失败的显示的图片
	
### 控件参数说明：（全部参数可选设置）
参数     | 说明
-------- | ---
isAutoSliding | true,false 是否自动滚动，默认false
slidingInterval | 滚动的间隔时间，单位：秒，默认5s
isLoop | true,false 是否循环滚动，默认false
isShowIndicator | true,false 是否显示指示器，默认false
indicatorSpace | 每个指示器圆点之间的间隔 单位：dp，默认10dp
indicatorSize|指示器圆点的大小 单位：dp，默认10dp
indicatorMarginBottom | 指示器margin下方的距离 单位：dp，默认20dp
selectedPosition | 显示那一页图片，默认0
indicatorGravity|指示器在底部的显示位置 left，right，middle ，默认middle

### 版本：
版本号     | 备注
-------- | ---
v1.0.0 | 1）支持自动滚动<br>2）支持本地图片，URL图片
v1.1.0 | 1）增加点击显示网络图片的Page，返回预设的URL，方便跳转


###
欢迎使用，欢迎打赏。您的打赏，是我前进的动力。您的使用，是我的荣幸。
主要是后面的版本会不断完善。
更多的公用库会持续加上。希望大家持续关注。
