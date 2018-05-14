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
		//���ý��չ㲥�����ȼ�
		filter.setPriority(1000);
		
		innerSmsReceiver = new InnerSmsReceiver();
		//ע��㲥
		registerReceiver(innerSmsReceiver, filter);
				//�绰���������ص绰
				//�绰״̬�ļ���
				//��ȡ�绰�Ĺ����߶���
		    	mTm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		    	//�����绰��״̬
		    	mMyPhoneStateListener = new MyPhoneStateListener();
		    	mTm.listen(mMyPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
		
	}
	
	class MyPhoneStateListener extends PhoneStateListener{
		  @Override
		public void onCallStateChanged(int state, String incomingNumber) {
			  switch(state){
	    		
	    		case TelephonyManager.CALL_STATE_IDLE:
	    			//����״̬��û���κλ
	    			break;
	    		case TelephonyManager.CALL_STATE_OFFHOOK:
	    			//ժ��״̬��������һ��״̬ͨ�������߲���
	    			break;
	    		case TelephonyManager.CALL_STATE_RINGING:
	    			//����״̬
	    		    //ֱ�ӹҶϵ绰
	    			//ʹ��adil�ͷ������Ҷϵ绰
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
			//���ص绰��ʹ�÷���
			//ServiceManager����Կ��������أ�����ֱ�ӵ��ã���Ҫ�õ�����
			 try {
				 //1��ȡServiceManager���ֽ����ļ�
				Class<?> clazz = Class.forName("android.os.ServiceManager");
				//2��ȡ����
				Method method = clazz.getMethod("getService", String.class);
				//3������ô˷���
			    IBinder ibinder =(IBinder) method.invoke(null,Context.TELEPHONY_SERVICE );
			    //4.���û�ȡaidl�ļ��Ķ��󷽷� 
			    ITelephony asInterface = ITelephony.Stub.asInterface(ibinder);
			    //5.����aidl�����ص�endcall����
			    asInterface.endCall();
			} catch (Exception e) {
				
				e.printStackTrace();
				
			} 
			 
			 //�����ݽ������ϣ�ȥע�����ݹ۲��ߣ�ͨ�����ݹ۲���ȥ�۲����ݿ⣨uri�������Ÿ����Ǹ������ݵı仯
			 myContentObserver = new MyContentObserver(new Handler(),number);
			 getContentResolver().registerContentObserver(Uri.parse("content://call_log/calls"), true, myContentObserver);
			 //�ڶ�������ģ����ѯ������ģ����ѯ����Ϊtrue
		}
	}
	
	class MyContentObserver extends ContentObserver{
		//����һ��number,����֮������ݿ�ɾ�� 
        private String number;
		public MyContentObserver(Handler handler,String number) {
			super(handler);
			this.number=number;
			
		}
		//�����ݿⷢ���任��ʱ����ô˷���
		@Override
		public void onChange(boolean selfChange) {
			 //ɾ����ϵ�����ݿ����number��ͨ����¼
			 getContentResolver().delete(Uri.parse("content://call_log/calls"), "number=?", new String[]{number});
			super.onChange(selfChange);
		}
		
	}
	
	class InnerSmsReceiver extends BroadcastReceiver{
        
		@Override
		public void onReceive(Context context, Intent intent) {
			//1��ȡ��������
			Object[] objects = (Object[]) intent.getExtras().get("pdus");
			//2ѭ���������Ź���
			for (Object object : objects) {
				//3��ȡ���Ŷ���
				SmsMessage sms = SmsMessage.createFromPdu((byte[])object);
				//4��ȡ���Ŷ���Ļ�����Ϣ
				//4.1���Ͷ��ŵĵ�ַ
				String originatingAddress = sms.getOriginatingAddress();
				//4.2���Ͷ��ŵ�����
				String messageBody = sms.getMessageBody();
				int mode = mDao.getmode(originatingAddress);
				if(mode==2||mode==3){
					//���ض���
					//�жϹ㲥�����õ�ǰ�Ĺ㲥�����ߵ����ȼ���ߣ��ù㲥�������жϣ���ϵͳ���Ž��ղ��������㲥
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
		//�ж��Ƿ�Ϊ�գ�Ȼ��رչ㲥������
		if(innerSmsReceiver!=null){
			unregisterReceiver(innerSmsReceiver);
		}
		//ע�����ݹ۲��ߣ���ֹ����bugû��ͨ����¼
		if(myContentObserver!=null){
			getContentResolver().unregisterContentObserver(myContentObserver);
		}
		//ȡ���Ե绰�ļ���
		if(myContentObserver!=null){
			mTm.listen(mMyPhoneStateListener, PhoneStateListener.LISTEN_NONE);
		}
		super.onDestroy();
	}
}
