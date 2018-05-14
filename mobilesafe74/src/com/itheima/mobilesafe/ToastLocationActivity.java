package com.itheima.mobilesafe;

import com.itheima.mobilesafe.utils.SpUtil;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;

public class ToastLocationActivity extends Activity {
       private ImageView iv_drag;
	private Button bt_top;
	private Button bt_bottom;
	private WindowManager mWM;
	private int mScreenHeight;
	private int mScreenWidth;
	private long startTime=0;
	private long[] mHits=new long[2];

	@Override
    protected void onCreate(Bundle savedInstanceState) {
    	setContentView(R.layout.activity_toast_location);
    	super.onCreate(savedInstanceState);
    	initUI();
    }

	private void initUI() {
		  iv_drag = (ImageView) findViewById(R.id.iv_drag);
		  bt_top = (Button) findViewById(R.id.bt_top);
		  bt_bottom = (Button) findViewById(R.id.bt_bottom);
		  //获得窗口的管理者（屏幕的宽度和高度）
		  mWM = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		  mScreenHeight = mWM.getDefaultDisplay().getHeight();
		  mScreenWidth = mWM.getDefaultDisplay().getWidth();
		  
		  //imageview位置的回显
		  int locationX = SpUtil.getInt(getApplicationContext(), ConstantValue.LOCATION_X, 0);
		  int locationY = SpUtil.getInt(getApplicationContext(), ConstantValue.LOCATION_Y, 0);
		  //左上角坐标作用在iv_drag上
		  //iv_drag在相对布局中,所以它的坐标规则要有相对布局来提供
		  
		  //指宽高都为wrap_content
		  LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
				  RelativeLayout.LayoutParams.WRAP_CONTENT);
		  
		  //将左上角的坐标作用在iv_drag对应规则参数上
		  layoutParams.leftMargin=locationX;
		  layoutParams.topMargin=locationY;
		  //将以上规则作用在iv_drag上 
		  iv_drag.setLayoutParams(layoutParams);
		  
		  //button位置的回显
		  if(locationY>(mScreenHeight-22)/2){
				bt_top.setVisibility(View.VISIBLE);
				bt_bottom.setVisibility(View.INVISIBLE);
			}else{
				bt_top.setVisibility(View.INVISIBLE);
				bt_bottom.setVisibility(View.VISIBLE);
			}
		  
		  iv_drag.setOnClickListener(new OnClickListener() {
			
			 //点击两次居中显示 
			@Override
			public void onClick(View v) {
				 //1,原数组(要靠背的的数组)
				  //2,原数组的拷贝起始位置索引值
				  //3,目标数组(原数组的数据———拷贝———目标数组)
				  //4,目标是数组接收值的起始索引位置
			      //5，拷贝的长度
				  System.arraycopy(mHits, 1, mHits, 0, mHits.length-1);
				  mHits[mHits.length-1]=SystemClock.uptimeMillis();
				  if(mHits[mHits.length-1]-mHits[0]<500){
					  int top=mScreenHeight/2 -iv_drag.getHeight()/2;
					  int bottom=mScreenHeight/2 +iv_drag.getHeight()/2;
					  int left=mScreenWidth/2-iv_drag.getWidth()/2;
					  int right=mScreenWidth/2+iv_drag.getWidth()/2;
					  
					  //控件按以上原则显示
					  iv_drag.layout(left, top, right, bottom);
					  
					  //存储当前的状态，
					  SpUtil.putInt(getApplicationContext(), ConstantValue.LOCATION_X, iv_drag.getLeft());
					  SpUtil.putInt(getApplicationContext(), ConstantValue.LOCATION_Y, iv_drag.getTop());
					  
				  }
				
			}
		});
		  
		  //为iamgeView设置触摸监听事件（按下，移动，抬起）
		  iv_drag.setOnTouchListener(new OnTouchListener() {
			  
			private int rawX;
			private int rawY;
			private int disx;
			private int disy;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					//1获取初始的坐标
					rawX = (int) event.getRawX();
					rawY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_MOVE:
					//2获得移动中的坐标
					int moveX = (int)event.getRawX();
					int moveY = (int)event.getRawY();
					//3计算与开始坐标的差值
					
					disx = moveX-rawX;
					disy = moveY-rawY;
					
					//3,获取当前控件所在屏幕的（左，上）角的位置
					int left=iv_drag.getLeft()+disx;
					int top=iv_drag.getTop()+disy;
					int right=iv_drag.getRight()+disx;
					int bottom=iv_drag.getBottom()+disy;
					
					//根据imageview的位置，切换button的显示位置
					if(top>(mScreenHeight-22)/2){
						bt_top.setVisibility(View.VISIBLE);
						bt_bottom.setVisibility(View.INVISIBLE);
					}else{
						bt_top.setVisibility(View.INVISIBLE);
						bt_bottom.setVisibility(View.VISIBLE);
					}
					//容错处理(iv_drag不能拉出手机屏幕)
					if(left<0){
						return true;
					}if(right>mScreenWidth){
						return true;
					}if(top<0){
						return true;
					}if(bottom>mScreenHeight-22){
						return true;
					}
					
					//2告知移动的控件，按计算出来的坐标去做展示
					iv_drag.layout(left,top,right,bottom);
					//4.更新初始坐标
					rawX = (int) event.getRawX();
					rawY = (int) event.getRawY();
					
					break;
				case MotionEvent.ACTION_UP:
					//存储的移动的位置
					SpUtil.putInt(getApplicationContext(), ConstantValue.LOCATION_X, iv_drag.getLeft());
					SpUtil.putInt(getApplicationContext(), ConstantValue.LOCATION_Y, iv_drag.getTop());
					
					break;

				
				}
				//返回true响应事件，返回false不响应事件
				//若是有触摸事件和点击事件，需要改为false
				return false;
			}
		});
		  
		  
	}
}
