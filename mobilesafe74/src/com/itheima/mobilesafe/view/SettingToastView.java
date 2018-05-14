package com.itheima.mobilesafe.view;

/*
 * 自定义view
 *  
 */


import com.itheima.mobilesafe.R;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SettingToastView extends RelativeLayout {
  									

	private TextView tv_des;
	private TextView tv_title;


	public SettingToastView(Context context) {
		this(context,null);
		
	}
        
	public SettingToastView(Context context, AttributeSet attrs) {
        this(context,attrs,0);
	}
    
	public SettingToastView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		//xml--->view  将设置页面的一个条目转换成view对象，
		//直接添加到当前的SettingIteamView对应的view中
		View.inflate(context,R.layout.setting_toast_view, this);
		/*View view = View.inflate(context, R.layout.setting_item_view, null);
		this.addView(view);*///与上面的等价
		//自定义组合控件中的标题描述
		
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_des = (TextView) findViewById(R.id.tv_des);
		//获取自定义以及原生属性的操作，写在此处 AttributeSet attrs对象中获取
	}
	
	//自定义控件里 添加设置标题的方法
	public void setTitle(String tiltle){
		tv_title.setText(tiltle);
	}
	
	//在自定义控件里 添加设置描述的方法
	public void setDes(String des){
		tv_des.setText(des);
	}




}
