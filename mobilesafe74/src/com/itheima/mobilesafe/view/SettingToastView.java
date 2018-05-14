package com.itheima.mobilesafe.view;

/*
 * �Զ���view
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
		//xml--->view  ������ҳ���һ����Ŀת����view����
		//ֱ����ӵ���ǰ��SettingIteamView��Ӧ��view��
		View.inflate(context,R.layout.setting_toast_view, this);
		/*View view = View.inflate(context, R.layout.setting_item_view, null);
		this.addView(view);*///������ĵȼ�
		//�Զ�����Ͽؼ��еı�������
		
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_des = (TextView) findViewById(R.id.tv_des);
		//��ȡ�Զ����Լ�ԭ�����ԵĲ�����д�ڴ˴� AttributeSet attrs�����л�ȡ
	}
	
	//�Զ���ؼ��� ������ñ���ķ���
	public void setTitle(String tiltle){
		tv_title.setText(tiltle);
	}
	
	//���Զ���ؼ��� ������������ķ���
	public void setDes(String des){
		tv_des.setText(des);
	}




}
