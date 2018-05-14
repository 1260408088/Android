package com.itheima.mobilesafe.service;

import java.util.Timer;
import java.util.TimerTask;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.engine.ProgressInfoProvider;
import com.itheima.mobilesafe.receiver.ProgressWidget;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.RemoteViews;

public class UpdateWidgetService extends Service {

	private Timer mTimer;
	private InnerReceiver mInnerReceiver;
	private String tag="UpdateWidgetService";
	@Override
	public IBinder onBind(Intent intent) {
		
		return null;
	}
	@Override
	public void onCreate() {
		//管理进程总数和可用内存数更新(定时器)
		startTimer();
		
		//注册开锁，解锁广播接受者
		IntentFilter intentFilter = new IntentFilter();
		//开锁action
		intentFilter.addAction(Intent.ACTION_SCREEN_ON);
		//锁屏action
		intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
		mInnerReceiver = new InnerReceiver();
		//注册广播
		registerReceiver(mInnerReceiver, intentFilter);
		super.onCreate();
	}
	
	class InnerReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction().equals(Intent.ACTION_SCREEN_ON)){
				//开启定时更新任务
				startTimer();
			}else{
				//关闭定时更新 任务
				cancelTumerTask();
			}
		}
		
	}
	
	private void cancelTumerTask() {
		if(mTimer!=null){
		    mTimer.cancel();
			//将引用对象抹掉（垃圾回收机制）
			mTimer=null;
		}
		
	}
	
	private void startTimer() {
		mTimer = new Timer();
		mTimer.scheduleAtFixedRate(new TimerTask(){

			@Override
			public void run() {
				//UI定时刷新
				updateAppWidget();
				Log.i(tag, "更新进行中.....");
			}

		}, 0, 5000);
	}
	
	private void updateAppWidget() {
		//1.获取Appwidget对象
		AppWidgetManager aWM = AppWidgetManager.getInstance(this);
		//2.获取窗体小部件布局转化成的view对象（定位应用的包名，当前应用中放入那块布局文件）
		RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.process_widget);
		//3.给窗体小部件布view对象，内部控件赋值
		remoteViews.setTextViewText(R.id.tv_process_count, "进程总数"+ProgressInfoProvider.getProgressCount(getApplicationContext()));
		//4.显示可用内存大小
		String formatFileSize = Formatter.formatFileSize(getBaseContext(), ProgressInfoProvider.getAvailSpace(getApplicationContext()));
		remoteViews.setTextViewText(R.id.tv_prigress_memor, "可用内存"+formatFileSize);
		
		//点击窗体小部件，进入应用
		//1:在那个控件上响应点击事件2,：延期意图
		Intent intent = new Intent("android.intent.action.Home");
		intent.addCategory("android.intent.category.DEFAULT");
		PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		remoteViews.setOnClickPendingIntent(R.id.ll_root, pendingIntent);
		
		//点击窗体上的按钮发送广播，清理内存
		Intent broadCastIntent = new Intent("android.intent.action.kILL_BACKGROUND_PROGRESS");
		PendingIntent broadcast = pendingIntent.getBroadcast(getApplicationContext(), 0, broadCastIntent, PendingIntent.FLAG_CANCEL_CURRENT);
		remoteViews.setOnClickPendingIntent(R.id.btn_clean, broadcast);
		//上下文环境，窗体小部件对应广播接受者的字节码文件
		ComponentName componentName = new ComponentName(this,ProgressWidget.class); 
		aWM.updateAppWidget(componentName, remoteViews);
		
	}
	@Override
	public void onDestroy(){
		if(mInnerReceiver!=null){
			unregisterReceiver(mInnerReceiver);
		}
		//调用ondestroy即关闭服务，关闭服务的方法在移除最后一个窗体小部件的时候调用
		//定时也 没必要维护 
		cancelTumerTask();
		super.onDestroy();
	}

}
