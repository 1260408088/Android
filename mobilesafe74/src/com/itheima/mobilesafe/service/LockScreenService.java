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
		//����action
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
    
	//���յ������㲥�����������������ڴ�ķ���
	class Innerreceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			//����ɱ�����̵ķ���
			ProgressInfoProvider.killProgressAll(context);
		}
	}
	@Override
	public void onDestroy() {
		//�رշ���Ҫȡ�������¼��ļ���
		if(innerreceiver!=null){
			unregisterReceiver(innerreceiver);
		}
		super.onDestroy();
	}
}
