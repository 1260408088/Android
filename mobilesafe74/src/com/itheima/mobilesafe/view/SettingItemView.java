package com.itheima.mobilesafe.view;

/*
 * �Զ���view
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
	 * ͨ���� ��������
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
		//xml--->view  ������ҳ���һ����Ŀת����view����
		//ֱ����ӵ���ǰ��SettingIteamView��Ӧ��view��
		
		View.inflate(context, R.layout.setting_item_view, this);
		/*View view = View.inflate(context, R.layout.setting_item_view, null);
		this.addView(view);*///������ĵȼ�
		
		//�Զ�����Ͽؼ��еı�������
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_des = (TextView) findViewById(R.id.tv_des);
		cb_box = (CheckBox) findViewById(R.id.cb_box);
		//��ȡ�Զ����Լ�ԭ�����ԵĲ�����д�ڴ˴� AttributeSet attrs�����л�ȡ
		initAttrs(attrs);
		tv_title.setText(mDestitle);
		
	}

	
	/**
	 * @param attrs ���췽�� ��ά���õ����Լ���
	 * �������Լ������Զ�����������ֵ
	 */
	private void initAttrs(AttributeSet attrs) {
		
		//ͨ������������ȡ����&����ֵ
		/*for(int i=0;i<attrs.getAttributeCount();i++){
			Log.i(tag,"name ="+attrs.getAttributeName(i));
			Log.i(tag,"value ="+attrs.getAttributeValue(i));
			Log.i(tag,"�ָ���===============================");
		}*/
		
	    //ͨ������������ȡ��������&���ռ�
		//Ϊ�������������ֵ���������ģ���ѡ��ı������
		mDestitle = attrs.getAttributeValue(NAMESPACE,"destitle");
		mDesoff = attrs.getAttributeValue(NAMESPACE,"desoff");
		mDeson = attrs.getAttributeValue(NAMESPACE,"deson");
		
		Log.i(tag,mDestitle);
		Log.i(tag,mDesoff);
		Log.i(tag,mDeson);
	}

	/**�ж��Ƿ����ķ���
	 * @return ���ص�ǰSettingIteamView�Ƿ�ѡ��״̬ true(����)  flase(�ر�)
	 */
	public boolean isCheck() {
		//��checkBOX��ѡ�н����������ǰ��Ŀ�Ƿ���
		return cb_box.isChecked();
		
	}
	
	/**
	 * @param isCheck �����״̬
	 */
	public void setCheck(boolean isCheck){
		//��ǰ��Ŀ��ѡ��Ĺ����У�cb_boxѡ��״̬Ҳ�ڸ��棨ischeck���仯
		cb_box.setChecked(isCheck);
		if(isCheck){
			//����
			tv_des.setText(mDeson);
		}else{
			
			tv_des.setText(mDesoff);
		}
			
		
	}

}
