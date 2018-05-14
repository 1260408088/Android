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
    	//��ȡ�ֻ��ľ�γ��
    	//1��ȡλ�ù����߶���
    	LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    	//2�����ŵķ�ʽ��ȡ��γ������
    	Criteria criteria = new Criteria();
    	//������
    	criteria.setCostAllowed(true);
    	criteria.setAccuracy(Criteria.ACCURACY_FINE);//ָ����ȡ��γ�ȵľ�ȷ��
    	String bestProvider = lm.getBestProvider(criteria, true);
    	//3,��һ���¼�������ƶ�һ��������ȡ��γ������
    	lm.requestLocationUpdates(bestProvider, 0, 0, new myLocationListener());
    }
    class myLocationListener implements LocationListener{
    	@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			//gps״̬�����л����¼�����
		}
		
		@Override
		public void onProviderEnabled(String provider) {
			//gps������ʱ����¼�����
			
		}
		
		@Override
		public void onProviderDisabled(String provider) {
			//gps�رյ�ʱ����¼�����
		}
		//λ�ñ任��ʱ�򣬵��õķ���
		@Override
		public void onLocationChanged(Location location) {
			//����
			double longitude = location.getLongitude();
			//γ��
			double latitude = location.getLatitude();
			String loc="longitude:"+longitude+",location:"+latitude;
			//����ȫ���뷢���������
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
