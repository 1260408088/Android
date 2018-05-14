package com.itheima.mobilesafe;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost.TabSpec;

public class BaseCacheClearActivity extends TabActivity {
      @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_bser_clear_cache);
    	//1.生成选项卡一
    	TabSpec tab1 = getTabHost().newTabSpec("clear_cache").setIndicator("缓存清理");
//		ImageView imageView = new ImageView(this); 
//		imageView.setBackgroundResource(R.drawable.ic_launcher);
//		View view = View.inflate(this, R.layout.test, null);
		
//		TabSpec tab1 = getTabHost().newTabSpec("clear_cache").setIndicator(view);
    	//2.生成选项卡二
    	TabSpec tab2 = getTabHost().newTabSpec("clear_sd_cache").setIndicator("sd卡清理");
    	//3.告知点中选项卡后的的后续操作
    	tab1.setContent(new Intent(this,CacheClearActivity.class));
    	tab2.setContent(new Intent(this,SdClearActivity.class));
    	
    	//4.将此两个选项卡维护host(选项卡宿主)中去
    	getTabHost().addTab(tab1);
    	getTabHost().addTab(tab2);
    }
}
