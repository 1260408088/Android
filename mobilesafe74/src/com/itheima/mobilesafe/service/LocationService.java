package com.itheima.mobilesafe.service;

import com.itheima.mobilesafe.ConstantValue;
import com.itheima.mobilesafe.utils.SpUtil;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.gsm.SmsManager;

public class LocationService extends Service {
    @Override
    public void onCreate() {
    	super.onCreate();
    	//获取手机的经纬度
    	//1获取位置管理者对象
    	LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    	//2以最优的方式获取经纬度坐标
    	Criteria criteria = new Criteria();
    	//允许花费
    	criteria.setCostAllowed(true);
    	criteria.setAccuracy(Criteria.ACCURACY_FINE);//指定获取经纬度的精确度
    	String bestProvider = lm.getBestProvider(criteria, true);
    	//3,在一定事件间隔，移动一定距离后获取经纬度坐标
    	lm.requestLocationUpdates(bestProvider, 0, 0, new myLocationListener());
    }
    class myLocationListener implements LocationListener{
    	@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			//gps状态发生切换的事件监听
		}
		
		@Override
		public void onProviderEnabled(String provider) {
			//gps开启的时候的事件监听
			
		}
		
		@Override
		public void onProviderDisabled(String provider) {
			//gps关闭的时候的事件监听
		}
		//位置变换的时候，调用的方法
		@Override
		public void onLocationChanged(Location location) {
			//经度
			double longitude = location.getLongitude();
			//纬度
			double latitude = location.getLatitude();
			String loc="longitude:"+longitude+",location:"+latitude;
			//给安全号码发送坐标短信
			SmsManager sm = SmsManager.getDefault();
			String safenumber = SpUtil.getString(getApplicationContext(), ConstantValue.CONTACT_PHONE, "");
			sm.sendTextMessage(safenumber, null, loc, null, null);
		}
		
    }
    
	@Override
	public IBinder onBind(Intent intent) {
		
		return null;
	}
	
	@Override
	public void onDestroy() {
		
		super.onDestroy();
	}

}
