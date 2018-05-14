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
    	//1.����ѡ�һ
    	TabSpec tab1 = getTabHost().newTabSpec("clear_cache").setIndicator("��������");
//		ImageView imageView = new ImageView(this); 
//		imageView.setBackgroundResource(R.drawable.ic_launcher);
//		View view = View.inflate(this, R.layout.test, null);
		
//		TabSpec tab1 = getTabHost().newTabSpec("clear_cache").setIndicator(view);
    	//2.����ѡ���
    	TabSpec tab2 = getTabHost().newTabSpec("clear_sd_cache").setIndicator("sd������");
    	//3.��֪����ѡ���ĵĺ�������
    	tab1.setContent(new Intent(this,CacheClearActivity.class));
    	tab2.setContent(new Intent(this,SdClearActivity.class));
    	
    	//4.��������ѡ�ά��host(ѡ�����)��ȥ
    	getTabHost().addTab(tab1);
    	getTabHost().addTab(tab2);
    }
}
