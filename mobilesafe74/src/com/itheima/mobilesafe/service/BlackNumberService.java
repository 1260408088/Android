package com.itheima.mobilesafe.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.android.internal.telephony.ITelephony;
import com.itheima.mobilesafe.dao.BlackNumberDao;
import com.itheima.mobilesafe.service.addrssService.MyPhoneStateListener;
import com.lidroid.xutils.DbUtils.DaoConfig;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;

public class BlackNumberService extends Service {
	private InnerSmsReceiver innerSmsReceiver;
	private IntentFilter filter;
	private TelephonyManager mTm;
	private MyPhoneStateListener mMyPhoneStateListener;
	private BlackNumberDao mDao;
	private MyContentObserver myContentObserver;
	@Override
	public void onCreate() {
		super.onCreate();
		mDao = BlackNumberDao.getInstance(getApplicationContext());
		filter = new IntentFilter();
		filter.addAction("android.provider.Telephony.SMS_RECEIVED");
		//设置接收广播的优先级
		filter.setPriority(1000);
		
		innerSmsReceiver = new InnerSmsReceiver();
		//注册广播
		registerReceiver(innerSmsReceiver, filter);
				//电话监听，拦截电话
				//电话状态的监听
				//获取电话的管理者对象
		    	mTm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		    	//监听电话的状态
		    	mMyPhoneStateListener = new MyPhoneStateListener();
		    	mTm.listen(mMyPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
		
	}
	
	class MyPhoneStateListener extends PhoneStateListener{
		  @Override
		public void onCallStateChanged(int state, String incomingNumber) {
			  switch(state){
	    		
	    		case TelephonyManager.CALL_STATE_IDLE:
	    			//空闲状态，没有任何活动
	    			break;
	    		case TelephonyManager.CALL_STATE_OFFHOOK:
	    			//摘机状态，至少有一个状态通话，或者拨打
	    			break;
	    		case TelephonyManager.CALL_STATE_RINGING:
	    			//响铃状态
	    		    //直接挂断电话
	    			//使用adil和反射来挂断电话
	    			endcall(incomingNumber);
	    			
	    			
	    			break;
	    		}
		
			super.onCallStateChanged(state, incomingNumber);
		}

		
	}
	
	private void endcall(String number) {
		//return ITelephony.Stub.asInterface(ServiceManager.getService(Context.TELEPHONY_SERVICE));
		int mode = mDao.getmode(number);
		if(mode==1||mode==3){
			//拦截电话，使用反射
			//ServiceManager此类对开发者隐藏，不能直接调用，需要用到反射
			 try {
				 //1获取ServiceManager的字节码文件
				Class<?> clazz = Class.forName("android.os.ServiceManager");
				//2获取方法
				Method method = clazz.getMethod("getService", String.class);
				//3反射调用此方法
			    IBinder ibinder =(IBinder) method.invoke(null,Context.TELEPHONY_SERVICE );
			    //4.调用获取aidl文件的对象方法 
			    ITelephony asInterface = ITelephony.Stub.asInterface(ibinder);
			    //5.调用aidl中隐藏的endcall方法
			    asInterface.endCall();
			} catch (Exception e) {
				
				e.printStackTrace();
				
			} 
			 
			 //再内容解析器上，去注册内容观察者，通过内容观察者去观察数据库（uri决定那张个库那个表）内容的变化
			 myContentObserver = new MyContentObserver(new Handler(),number);
			 getContentResolver().registerContentObserver(Uri.parse("content://call_log/calls"), true, myContentObserver);
			 //第二个参数模糊查询，不用模糊查询设置为true
		}
	}
	
	class MyContentObserver extends ContentObserver{
		//传入一个number,用于之后的数据库删除 
        private String number;
		public MyContentObserver(Handler handler,String number) {
			super(handler);
			this.number=number;
			
		}
		//当数据库发生变换的时候调用此方法
		@Override
		public void onChange(boolean selfChange) {
			 //删除联系人数据库里的number的通话记录
			 getContentResolver().delete(Uri.parse("content://call_log/calls"), "number=?", new String[]{number});
			super.onChange(selfChange);
		}
		
	}
	
	class InnerSmsReceiver extends BroadcastReceiver{
        
		@Override
		public void onReceive(Context context, Intent intent) {
			//1获取短信内容
			Object[] objects = (Object[]) intent.getExtras().get("pdus");
			//2循环遍历短信过程
			for (Object object : objects) {
				//3获取短信对象
				SmsMessage sms = SmsMessage.createFromPdu((byte[])object);
				//4获取短信对象的基本信息
				//4.1发送短信的地址
				String originatingAddress = sms.getOriginatingAddress();
				//4.2发送短信的内容
				String messageBody = sms.getMessageBody();
				int mode = mDao.getmode(originatingAddress);
				if(mode==2||mode==3){
					//拦截短信
					//中断广播，设置当前的广播接收者的优先级最高，让广播从这里中断，让系统短信接收不到此条广播
					abortBroadcast();
				}
			}
			
		}
		
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		
		return null;
	}
    
	@Override
	public void onDestroy() {
		//判断是否为空，然后关闭广播接收者
		if(innerSmsReceiver!=null){
			unregisterReceiver(innerSmsReceiver);
		}
		//注销内容观察者，防止出现bug没有通话记录
		if(myContentObserver!=null){
			getContentResolver().unregisterContentObserver(myContentObserver);
		}
		//取消对电话的监听
		if(myContentObserver!=null){
			mTm.listen(mMyPhoneStateListener, PhoneStateListener.LISTEN_NONE);
		}
		super.onDestroy();
	}
}
