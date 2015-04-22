## AutoAndInfiniteViewPager 简介
* AutoAndInfiniteViewPager 缘起是广告轮播。
* AutoAndInfiniteViewPager 最低兼容android 2.2 (api level 8)

## 目前 AutoAndInfiniteViewPager 主要有以下功能：
* item 单击：
* item 左右滑动事件拦截：
* 自动轮播：
* 无限左右滑动：

## 混淆时注意事项：

	-keep class com.bestviewpager.** { *; }

## item 单击使用方法：

	viewpager.setOnPagerItemClickListener(new AutoAndInfiniteViewPager.OnPagerItemClickListener() {
		@Override
		public void pagerItemClicked(int position) {
			// do yourself
		}
	});

----
## item 左右滑动事件拦截使用方法
	无需处理即可直接使用

## 自动轮播及无限左右滚动使用方法：

	//正常直接设置自己的adapter即可,默认就是开启自动轮播和无限滚动
	viewpager.setAdapter(adapter);

	//如果你不需要自动轮播,可以使用重载函数,传false即可,此时任何支持无限左右滚动
	viewpager.setAdapter(adapter,false); 
	
	//也可以单独设置是否自动轮播
	viewpager.setOpenLoop(true);

	//可以单独设置是否支持无限左右滚动,如果已经开启了自动轮播,此项设置无效
	//if loop is open , infinite always true
	viewpager.setOpenInfinite(true);

	//设置轮播的间隔时间
	//viewpager.setIntervalTime(5000);//设置间隔时间,有默认值,默认是3000毫秒
	
	....

	//当你数据设置到adapter里面后,直接调用viewpager的notifyDataSetChanged方法即可
	viewpager.notifyDataSetChanged();

	//注意:无须手动调用viewpager.setCurrentItem();,在notifyDataSetChanged中已经处理

----
# 关于我
* 第一次发,大家多多支持
* Email： <biaoliu_2007@163.com>
* 有任何建议或者使用中遇到问题都可以给我发邮件
