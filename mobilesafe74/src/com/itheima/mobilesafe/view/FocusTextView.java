package com.itheima.mobilesafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
//跑马灯
public class FocusTextView extends TextView {
    //使用在通过java代码创建控件
	public FocusTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
	}
	//由系统调用（带属性+上下文构造方法）
	public FocusTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
	}
	//由系统调用（带属性+上下文环境构造方法+布局文件中定义样式文件构造方法）
	public FocusTextView(Context context) {
		super(context);
		
	}
	//重写获取焦点的方
	@Override
	public boolean isFocused(){
		return true;
	}
	
}
