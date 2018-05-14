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
		//������������Ϳ����ڴ�������(��ʱ��)
		startTimer();
		
		//ע�Ὺ���������㲥������
		IntentFilter intentFilter = new IntentFilter();
		//����action
		intentFilter.addAction(Intent.ACTION_SCREEN_ON);
		//����action
		intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
		mInnerReceiver = new InnerReceiver();
		//ע��㲥
		registerReceiver(mInnerReceiver, intentFilter);
		super.onCreate();
	}
	
	class InnerReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction().equals(Intent.ACTION_SCREEN_ON)){
				//������ʱ��������
				startTimer();
			}else{
				//�رն�ʱ���� ����
				cancelTumerTask();
			}
		}
		
	}
	
	private void cancelTumerTask() {
		if(mTimer!=null){
		    mTimer.cancel();
			//�����ö���Ĩ�����������ջ��ƣ�
			mTimer=null;
		}
		
	}
	
	private void startTimer() {
		mTimer = new Timer();
		mTimer.scheduleAtFixedRate(new TimerTask(){

			@Override
			public void run() {
				//UI��ʱˢ��
				updateAppWidget();
				Log.i(tag, "���½�����.....");
			}

		}, 0, 5000);
	}
	
	private void updateAppWidget() {
		//1.��ȡAppwidget����
		AppWidgetManager aWM = AppWidgetManager.getInstance(this);
		//2.��ȡ����С��������ת���ɵ�view���󣨶�λӦ�õİ�������ǰӦ���з����ǿ鲼���ļ���
		RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.process_widget);
		//3.������С������view�����ڲ��ؼ���ֵ
		remoteViews.setTextViewText(R.id.tv_process_count, "��������"+ProgressInfoProvider.getProgressCount(getApplicationContext()));
		//4.��ʾ�����ڴ��С
		String formatFileSize = Formatter.formatFileSize(getBaseContext(), ProgressInfoProvider.getAvailSpace(getApplicationContext()));
		remoteViews.setTextViewText(R.id.tv_prigress_memor, "�����ڴ�"+formatFileSize);
		
		//�������С����������Ӧ��
		//1:���Ǹ��ؼ�����Ӧ����¼�2,��������ͼ
		Intent intent = new Intent("android.intent.action.Home");
		intent.addCategory("android.intent.category.DEFAULT");
		PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		remoteViews.setOnClickPendingIntent(R.id.ll_root, pendingIntent);
		
		//��������ϵİ�ť���͹㲥�������ڴ�
		Intent broadCastIntent = new Intent("android.intent.action.kILL_BACKGROUND_PROGRESS");
		PendingIntent broadcast = pendingIntent.getBroadcast(getApplicationContext(), 0, broadCastIntent, PendingIntent.FLAG_CANCEL_CURRENT);
		remoteViews.setOnClickPendingIntent(R.id.btn_clean, broadcast);
		//�����Ļ���������С������Ӧ�㲥�����ߵ��ֽ����ļ�
		ComponentName componentName = new ComponentName(this,ProgressWidget.class); 
		aWM.updateAppWidget(componentName, remoteViews);
		
	}
	@Override
	public void onDestroy(){
		if(mInnerReceiver!=null){
			unregisterReceiver(mInnerReceiver);
		}
		//����ondestroy���رշ��񣬹رշ���ķ������Ƴ����һ������С������ʱ�����
		//��ʱҲ û��Ҫά�� 
		cancelTumerTask();
		super.onDestroy();
	}

}
