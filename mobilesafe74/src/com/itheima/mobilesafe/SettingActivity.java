package com.itheima.mobilesafe;

import com.itheima.mobilesafe.service.BlackNumberService;
import com.itheima.mobilesafe.service.WatchDogService;
import com.itheima.mobilesafe.service.addrssService;
import com.itheima.mobilesafe.utils.ServiceUtils;
import com.itheima.mobilesafe.utils.SpUtil;
import com.itheima.mobilesafe.view.SettingItemView;
import com.itheima.mobilesafe.view.SettingToastView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.style.SuperscriptSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class SettingActivity extends Activity{
     private String[] styles;
	private int toast_style;
	private SettingToastView scv_style;
	private SettingItemView siv_app_lock;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_setting);

    	initUpdate();
    	initAdress();
    	initStyle();
    	initLocation();
    	initBlackNumber();
    	initAppLock();
    	
    }
     
	/**
	 * 开启程锁所服务
	 */
	private void initAppLock() {
		  siv_app_lock = (SettingItemView) findViewById(R.id.siv_app_lock);
		  
		  boolean isrunning = ServiceUtils.isRunning(getApplicationContext(), "com.itheima.mobilesafe.service.WatchDogService");
		  siv_app_lock.setCheck(isrunning);
		  
		  siv_app_lock.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//获得控件上的点击checkbox的点击状态,并且设置选中状态
				boolean ischeck = siv_app_lock.isCheck();
				siv_app_lock.setCheck(!ischeck);
				
				if(!ischeck){
					 startService(new Intent(getApplicationContext(), WatchDogService.class));
				}else{
					 stopService(new Intent(getApplicationContext(), WatchDogService.class));
				}
			}
		});
	}

	/**
	 * 拦截黑名单短信和 电话
	 * 
	 */
	private void initBlackNumber() {
		final SettingItemView siv_blacknumber = (SettingItemView)findViewById(R.id.siv_blacknumber);
		final boolean isruning = ServiceUtils.isRunning(this, "com.itheima.mobilesafe.service.BlackNumberService");
		siv_blacknumber.setCheck(isruning);
		siv_blacknumber.setOnClickListener(new  OnClickListener() {
			@Override
			public void onClick(View v) {
				  //获取当前的选中状态
				  boolean check = siv_blacknumber.isCheck();
				  //点击后将状态取反，以改变选中状态
				  siv_blacknumber.setCheck(!check);
				  if(!check){
					  startService(new Intent(getApplicationContext(),BlackNumberService.class));
				  }else{
					  stopService(new Intent(getApplicationContext(),BlackNumberService.class));
				  }
			}
		});
	}

	/**
	 * 设置归属地显示的位置
	 */
	private void initLocation() {
		SettingToastView scv_location = (SettingToastView) findViewById(R.id.scv_location);
		
		scv_location.setTitle("归属地提示框的位置");
		scv_location.setDes("设置归属地提示框的位置");
		
		scv_location.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//开启设置土司位置的activity
				Intent intent = new Intent(getApplicationContext(),ToastLocationActivity.class);
				startActivity(intent);
			}
		});
	}

	private void initStyle() {
		   scv_style = (SettingToastView) findViewById(R.id.scv_style);
		   
		   //1创建文字描述所在的String类型数组
		   styles = new String[]{"透明","橙色","蓝色","灰色","绿色"};
		   //2获取toast的样式索引值
		   toast_style = SpUtil.getInt(this, ConstantValue.TOAST_STYLE, 0);
		   scv_style.setTitle("设置归属地显示风格");
		   //3设置文字描述
		   scv_style.setDes(styles[toast_style]);
		   scv_style.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//点击后弹出土司样式选择的单选框
				showToastStyleDialog();
			}
		});
		   
	}
	
	/**
	 * 创建显示单选框
	 */
	private void showToastStyleDialog(){
			Builder builder = new AlertDialog.Builder(this);
			builder.setIcon(R.drawable.ic_launcher);
			builder.setTitle("请选择归属地样式");
			//单个条目的点击事件
			/*
			 * 1.string类型的数字，用来描述颜色
			 * 2.弹出对话框时选择的属性值
			 * 3.点击条目后触发的点击事件(1记录选中索引值，2显示选中的颜色，3关闭对话框)
			 */
			builder.setSingleChoiceItems(styles, toast_style, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//1.保存选中的索引值
					SpUtil.putInt(getApplicationContext(), ConstantValue.TOAST_STYLE, which);
					//2.回显颜色
					scv_style.setDes(styles[which]);
					//3.关闭对话框
					dialog.dismiss();
				}
			});
			builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//关闭对话框
					dialog.dismiss();
				}
			});
			
			builder.show();
			
	}

	/**
	 * 来电归属地toast显示
	 */
	private void initAdress() {
     		final SettingItemView siv_adress = (SettingItemView) findViewById(R.id.siv_adress);
     		//获取状态用来回显
     		boolean running = ServiceUtils.isRunning(this, "com.itheima.mobilesafe.service.addrssService");
     		siv_adress.setCheck(running);
     		//点击过程中来电归属地是否开启相互切换
     		siv_adress.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					boolean ischeck = siv_adress.isCheck();
					siv_adress.setCheck(!ischeck);
					if(!ischeck){
						//开启服务，管理土司
						startService(new Intent(getApplicationContext(),addrssService.class)); 
					}else{
						stopService(new Intent(getApplicationContext(),addrssService.class));
					}
					
				}
			});
	}

	/**
	 * 版本更新开关
	 */
	private void initUpdate() {
		
		final SettingItemView siv_update = (SettingItemView) findViewById(R.id.siv_update);
		//获取已有的开关状态，用作显示
		boolean open_update = SpUtil.getBoolean(getApplicationContext(), ConstantValue.OPEN_UPDATE, false);
		//设置CheckBox默认状态（是否选中，根据上一次存储的结果做决定）
	    siv_update.setCheck(open_update);
	    siv_update.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//如果之前是选中的，点击过后，便成未选中
			    //如果之前是未选中，点击过后，变成选中
				
				//获取之前的选中状态
				boolean isCheck = siv_update.isCheck();
				//将原有状态取反，等同上诉的两部操作
				siv_update.setCheck(!isCheck);
			    //将取反后的状态存储到相应sp
				SpUtil.putBoolean(getApplicationContext(), ConstantValue.OPEN_UPDATE, !isCheck);
				
			}
		});
		
	}
}
