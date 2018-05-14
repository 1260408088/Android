package com.itheima.mobilesafe.receiver;

import com.itheima.mobilesafe.service.UpdateWidgetService;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
 
@TargetApi(Build.VERSION_CODES.JELLY_BEAN) public class ProgressWidget extends AppWidgetProvider {
     private String tag="ProgressWidget";

	/* 
     * ���С��������д���������������С�����Ľ�����µĲ���
     */
    @Override
    public void onReceive(Context context, Intent intent) {
    	super.onReceive(context, intent);
    }
    
    @Override
	public void onEnabled(Context context) {
		//������һ������С�����ķ���
		Log.i(tag, "onEnabled ������һ������С�������÷���");
		//��������(onCreate)
		context.startService(new Intent(context, UpdateWidgetService.class));
		super.onEnabled(context);
	}
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		Log.i(tag, "onUpdate ������һ������С�������÷���");
		//��������
		context.startService(new Intent(context, UpdateWidgetService.class));
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}
	@Override
	public void onAppWidgetOptionsChanged(Context context,
			AppWidgetManager appWidgetManager, int appWidgetId,
			Bundle newOptions) {
		//������С������߷����ı��ʱ����÷���,����С������ʱ��,Ҳ���ô˷���
		//��������
		context.startService(new Intent(context, UpdateWidgetService.class));
		Log.i(tag, "onAppWidgetOptionsChanged ������һ������С�������÷���");
		super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId,
				newOptions);
	}
	
	
	public void onDeleted(Context context, int[] appWidgetIds) {
		Log.i(tag, "onDeleted ɾ��һ������С�������÷���");
		super.onDeleted(context, appWidgetIds);
	}
	
	@Override
	public void onDisabled(Context context) {
		Log.i(tag, "onDisabled ɾ�����һ������С�������÷���");
		//�رշ���
		context.stopService(new Intent(context, UpdateWidgetService.class));
		super.onDisabled(context);
	}
}
