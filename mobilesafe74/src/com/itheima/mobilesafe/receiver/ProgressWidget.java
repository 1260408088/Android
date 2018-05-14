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
     * 添加小部件，覆写几个方法，已完成小部件的界面更新的操作
     */
    @Override
    public void onReceive(Context context, Intent intent) {
    	super.onReceive(context, intent);
    }
    
    @Override
	public void onEnabled(Context context) {
		//创建第一个窗体小部件的方法
		Log.i(tag, "onEnabled 创建第一个窗体小部件调用方法");
		//开启服务(onCreate)
		context.startService(new Intent(context, UpdateWidgetService.class));
		super.onEnabled(context);
	}
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		Log.i(tag, "onUpdate 创建多一个窗体小部件调用方法");
		//开启服务
		context.startService(new Intent(context, UpdateWidgetService.class));
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}
	@Override
	public void onAppWidgetOptionsChanged(Context context,
			AppWidgetManager appWidgetManager, int appWidgetId,
			Bundle newOptions) {
		//当窗体小部件宽高发生改变的时候调用方法,创建小部件的时候,也调用此方法
		//开启服务
		context.startService(new Intent(context, UpdateWidgetService.class));
		Log.i(tag, "onAppWidgetOptionsChanged 创建多一个窗体小部件调用方法");
		super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId,
				newOptions);
	}
	
	
	public void onDeleted(Context context, int[] appWidgetIds) {
		Log.i(tag, "onDeleted 删除一个窗体小部件调用方法");
		super.onDeleted(context, appWidgetIds);
	}
	
	@Override
	public void onDisabled(Context context) {
		Log.i(tag, "onDisabled 删除最后一个窗体小部件调用方法");
		//关闭服务
		context.stopService(new Intent(context, UpdateWidgetService.class));
		super.onDisabled(context);
	}
}
