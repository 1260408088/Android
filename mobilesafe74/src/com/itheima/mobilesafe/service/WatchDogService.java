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
		
		//����һ���㲥������
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.SKIP");
		recever = new InnerRecever();
		registerReceiver(recever, filter);
		//ע��һ�����ݹ۲���,�۲����ݿ�ı仯,һ��������ɾ���������,����Ҫ��mPacknameList���»�ȡһ������
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
		//1.�����߳��п���һ���ɿص����߳� 
		new Thread(){
			public void run(){
				packageNameList = Dao.findall();
				while(WatchDog){
					 //2������ڿ�����Ӧ��,����ջ
					//3��ȡactivity�����߶���
					ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
					//4��ȡ���ڿ���Ӧ�õ�����ջ
					List<RunningTaskInfo> runningTasks = am.getRunningTasks(1);
					RunningTaskInfo runningTaskInfo = runningTasks.get(0);
					//5.��ȡջ����activity��Ȼ���ڻ�ȡ��activity����Ӧ�İ���
					String packageName = runningTaskInfo.topActivity.getPackageName();
					if(packageNameList.contains(packageName)){
						//����㲥��������mPackageName�͵�ǰ��packagename��һ�������ٵ�������ҳ�棬���ߵ�������ҳ��
						if(!packageName.equals(mPackageName)){
							//mPackageName="";
							//����һ��intent���ڸ�ס��ǰactivity
							Intent intent = new Intent(getApplicationContext(),EnterpsdAvtivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//�ڷ����￪��һ��activity��Ҫ����flag
							intent.putExtra("packagename", packageName);
							startActivity(intent);
						}
					}else if(!packageName.equals(getPackageName())){
						    //getPackageName()��ô�ʱָ��ҳ��İ�������
						   //��������Լ���Ӧ�ã���mPackageName����Ϊ��
						   mPackageName="";//��activityΪlockScreenActivity����ʱ��tempPackName�����ı�
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
		//�����������ˣ���Ҫ���㲥��ע����
		if(recever!=null){
		   unregisterReceiver(recever);
		}
		//�����ݹ����߷�ע�ᣨȡ������
		if(mContentObserver!=null){
			getContentResolver().unregisterContentObserver(mContentObserver);
		}
		super.onDestroy();
	}

}
