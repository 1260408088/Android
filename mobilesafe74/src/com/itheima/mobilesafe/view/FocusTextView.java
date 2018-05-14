package com.itheima.mobilesafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
//�����
public class FocusTextView extends TextView {
    //ʹ����ͨ��java���봴���ؼ�
	public FocusTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
	}
	//��ϵͳ���ã�������+�����Ĺ��췽����
	public FocusTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
	}
	//��ϵͳ���ã�������+�����Ļ������췽��+�����ļ��ж�����ʽ�ļ����췽����
	public FocusTextView(Context context) {
		super(context);
		
	}
	//��д��ȡ����ķ�
	@Override
	public boolean isFocused(){
		return true;
	}
	
}
