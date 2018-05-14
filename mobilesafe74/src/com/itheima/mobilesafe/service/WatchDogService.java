package com.itheima.mobilesafe.service;

import java.util.List;

import com.itheima.mobilesafe.EnterpsdAvtivity;
import com.itheima.mobilesafe.dao.AppLockDao;
import com.itheima.mobilesafe.service.BlackNumberService.MyContentObserver;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;

public class WatchDogService extends Service {
	
	private AppLockDao Dao;
	private boolean WatchDog;
	private List<String> packageNameList;
	private String mPackageName;
	private InnerRecever recever;
	private MyContentObserver mContentObserver;
	@Override
	public void onCreate() {
		Dao = AppLockDao.getinstance(getApplicationContext());
		WatchDog=true;
		watch();
		
		//创建一个广播接收者
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.SKIP");
		recever = new InnerRecever();
		registerReceiver(recever, filter);
		//注册一个内容观察者,观察数据库的变化,一旦数据有删除或者添加,则需要让mPacknameList重新获取一次数据
		mContentObserver = new MyContentObserver(new Handler());
		getContentResolver().registerContentObserver(
				Uri.parse("content://applock/change"), true, mContentObserver);
		super.onCreate();
		
		super.onCreate();
	}
	
	class MyContentObserver extends ContentObserver{

		public MyContentObserver(Handler handler) {
			super(handler);
		}
		@Override
		public void onChange(boolean selfChange) {
			new Thread(){
				public void run() {
					packageNameList = Dao.findall();
				};
			}.start();
			super.onChange(selfChange);
		}
		
	}
	
	class InnerRecever extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			  mPackageName = intent.getStringExtra("packageName");
		}
		
	}
	
	private void watch(){
		//1.在子线程中开启一个可控的子线程 
		new Thread(){
			public void run(){
				packageNameList = Dao.findall();
				while(WatchDog){
					 //2检测现在开启的应用,任务栈
					//3获取activity管理者对象
					ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
					//4获取正在开启应用的任务栈
					List<RunningTaskInfo> runningTasks = am.getRunningTasks(1);
					RunningTaskInfo runningTaskInfo = runningTasks.get(0);
					//5.获取栈顶的activity，然后在获取此activity所对应的包名
					String packageName = runningTaskInfo.topActivity.getPackageName();
					if(packageNameList.contains(packageName)){
						//如果广播传回来的mPackageName和当前的packagename是一样的则不再弹出拦截页面，否者弹出拦截页面
						if(!packageName.equals(mPackageName)){
							//mPackageName="";
							//开启一个intent来遮盖住当前activity
							Intent intent = new Intent(getApplicationContext(),EnterpsdAvtivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//在服务里开启一个activity需要设置flag
							intent.putExtra("packagename", packageName);
							startActivity(intent);
						}
					}else if(!packageName.equals(getPackageName())){
						    //getPackageName()获得此时指定页面的包名，和
						   //如果不是以加锁应用，给mPackageName设置为空
						   mPackageName="";//当activity为lockScreenActivity界面时，tempPackName不做改变
					}
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						
						e.printStackTrace();
					}
					
				}
			};
		}.start();
	}

	@Override
	public IBinder onBind(Intent intent){
		return null;
	}
	
	@Override
	public void onDestroy() {
		WatchDog=false;
		//最后服务销毁了，需要将广播反注册了
		if(recever!=null){
		   unregisterReceiver(recever);
		}
		//将内容管理者反注册（取消掉）
		if(mContentObserver!=null){
			getContentResolver().unregisterContentObserver(mContentObserver);
		}
		super.onDestroy();
	}

}
