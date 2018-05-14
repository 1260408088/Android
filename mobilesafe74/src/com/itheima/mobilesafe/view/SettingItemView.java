package com.itheima.mobilesafe.view;

/*
 * 自定义view
 * 
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

public class SettingItemView extends RelativeLayout {
    
	/**
	 * 通过次 调用属性
	 */										
	private static final String NAMESPACE = "http://schemas.android.com/apk/res/com.itheima.mobilesafe";
	private static final String tag = "SettingItemView";
	private CheckBox cb_box;
	private TextView tv_des;
	private TextView tv_title;
	private String mDestitle;
	private String mDesoff;
	private String mDeson;

	public SettingItemView(Context context) {
		this(context,null);
		
	}

	public SettingItemView(Context context, AttributeSet attrs) {
        this(context,attrs,0);
	}

	public SettingItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		//xml--->view  将设置页面的一个条目转换成view对象，
		//直接添加到当前的SettingIteamView对应的view中
		
		View.inflate(context, R.layout.setting_item_view, this);
		/*View view = View.inflate(context, R.layout.setting_item_view, null);
		this.addView(view);*///与上面的等价
		
		//自定义组合控件中的标题描述
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_des = (TextView) findViewById(R.id.tv_des);
		cb_box = (CheckBox) findViewById(R.id.cb_box);
		//获取自定义以及原生属性的操作，写在此处 AttributeSet attrs对象中获取
		initAttrs(attrs);
		tv_title.setText(mDestitle);
		
	}

	
	/**
	 * @param attrs 构造方法 中维护好的属性集合
	 * 返回属性集合中自定义属性属性值
	 */
	private void initAttrs(AttributeSet attrs) {
		
		//通过属性索引获取名称&属性值
		/*for(int i=0;i<attrs.getAttributeCount();i++){
			Log.i(tag,"name ="+attrs.getAttributeName(i));
			Log.i(tag,"value ="+attrs.getAttributeValue(i));
			Log.i(tag,"分割线===============================");
		}*/
		
	    //通过属性索引获取属性名称&名空间
		//为布局添加了属性值，用来更改，单选框的变更内容
		mDestitle = attrs.getAttributeValue(NAMESPACE,"destitle");
		mDesoff = attrs.getAttributeValue(NAMESPACE,"desoff");
		mDeson = attrs.getAttributeValue(NAMESPACE,"deson");
		
		Log.i(tag,mDestitle);
		Log.i(tag,mDesoff);
		Log.i(tag,mDeson);
	}

	/**判断是否开启的方法
	 * @return 返回当前SettingIteamView是否选中状态 true(开启)  flase(关闭)
	 */
	public boolean isCheck() {
		//由checkBOX的选中结果，决定当前条目是否开启
		return cb_box.isChecked();
		
	}
	
	/**
	 * @param isCheck 传入的状态
	 */
	public void setCheck(boolean isCheck){
		//当前条目在选择的过程中，cb_box选中状态也在跟随（ischeck）变化
		cb_box.setChecked(isCheck);
		if(isCheck){
			//开启
			tv_des.setText(mDeson);
		}else{
			
			tv_des.setText(mDesoff);
		}
			
		
	}

}
