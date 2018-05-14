package com.itheima.mobilesafe.service;

import com.itheima.mobilesafe.engine.ProgressInfoProvider;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class LockScreenService extends Service {
	
    private IntentFilter intentFilter;
	private Innerreceiver innerreceiver;
	@Override
    public void onCreate() {
		//锁屏action
    	intentFilter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
    	innerreceiver = new Innerreceiver();
    	registerReceiver(innerreceiver, intentFilter);
    	super.onCreate();
    }
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
    
	//接收到锁屏广播，接下来调用清理内存的方法
	class Innerreceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			//调用杀死进程的方法
			ProgressInfoProvider.killProgressAll(context);
		}
	}
	@Override
	public void onDestroy() {
		//关闭服务要取消锁屏事件的监听
		if(innerreceiver!=null){
			unregisterReceiver(innerreceiver);
		}
		super.onDestroy();
	}
}
