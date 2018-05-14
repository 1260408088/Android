package com.itheima.mobilesafe.service;

import com.itheima.mobilesafe.ConstantValue;
import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.queryAdressActivity;
import com.itheima.mobilesafe.engine.Address;
import com.itheima.mobilesafe.utils.SpUtil;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class addrssService extends Service {
	
    public static final String tag = "addrssService";
	private TelephonyManager mTm;
	private MyPhoneStateListener mMyPhoneStateListener;
	private final WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();
	private WindowManager mWM;
	private View mToast_view;
	private TextView tv_toast;
	private String address;
	private int mScreenHeight;
	private int mScreenWidth;
	private Handler mHandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			 tv_toast.setText(address);
		};
		   
	};
	private IntentOutCallReceiver intentOutCallReceiver;

	@Override
    public void onCreate() {
    	//������˾
    	//�绰״̬�ļ���
		//��ȡ�绰�Ĺ����߶���
    	mTm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
    	//�����绰��״̬
    	mMyPhoneStateListener = new MyPhoneStateListener();
    	mTm.listen(mMyPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
    	//��ȡ�������
    	mWM = (WindowManager) getSystemService(WINDOW_SERVICE);
    	mScreenHeight = mWM.getDefaultDisplay().getHeight();
		mScreenWidth = mWM.getDefaultDisplay().getWidth();
		
		//�����绰�Ĺ㲥������
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
		//�����㲥������
		intentOutCallReceiver = new IntentOutCallReceiver();
		registerReceiver(intentOutCallReceiver, intentFilter);
    	super.onCreate();
    }
	
	//�����绰�Ĺ㲥������
	class IntentOutCallReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			//��õ绰����
			String number = getResultData();
			//������˾�����ݺ�����ʾ����
			showToast(number);		
		}
		
	}
	
	
    class MyPhoneStateListener extends PhoneStateListener{
    	

		

		//�ֶ���д�绰״̬�����ı�ķ���
    	@Override
    	public void onCallStateChanged(int state, String incomingNumber) {
    		switch(state){
    		
    		case TelephonyManager.CALL_STATE_IDLE:
    			//����״̬��û���κλ
    			Log.i(tag, "����");
//    			if(mWM!=null&&mToast_view!=null){
//    			mWM.removeView(mToast_view);
//    			}
    			break;
    		case TelephonyManager.CALL_STATE_OFFHOOK:
    			//ժ��״̬��������һ��״̬ͨ�������߲���
    			Log.i(tag, "��ͨ״̬");
    			break;
    		case TelephonyManager.CALL_STATE_RINGING:
    			//����״̬
    			Log.i(tag, "����״̬");
    			showToast(incomingNumber);
    			break;
    		
    		}
    		super.onCallStateChanged(state, incomingNumber);
    	}

    }
    
    /**
	 * ��˾�������
	 */
	private void showToast(String number) {
		//Toast.makeText(getApplicationContext(), number, 1).show();
		final WindowManager.LayoutParams params = mParams;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
//                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE	Ĭ���ܹ�������
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        params.format = PixelFormat.TRANSLUCENT;
        //�������ʱ����ʾ��˾,�͵绰����һ��
        params.type = WindowManager.LayoutParams.TYPE_PHONE;
        params.setTitle("Toast");
        
        //ָ����˾������λ��(����˾ָ�������Ͻ�)
        params.gravity = Gravity.LEFT+Gravity.TOP;
        
     
        
        //��˾��ʾЧ��(��˾�����ļ�),xml-->view(��˾),����˾���ڵ�windowManager������
        mToast_view = View.inflate(getApplicationContext(), R.layout.toast_view, null);
        tv_toast = (TextView) mToast_view.findViewById(R.id.tv_toast);
        
        mToast_view.setOnTouchListener(new OnTouchListener() {
        	 private int startx ;
			 private int starty;
			 private int disx;
			 private int disy;
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				 
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					//1��ȡ��ʼ������
					startx = (int) event.getRawX();
					starty = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_MOVE:
					//2����ƶ��е�����
					int moveX = (int)event.getRawX();
					int moveY = (int)event.getRawY();
					//3�����뿪ʼ����Ĳ�ֵ
					
					disx = moveX-startx;
					disy = moveY-starty;
					
					params.x=params.x+disx;
					params.y=params.y+disy;
					//�ݴ���
					if(params.x<0){
						params.x=0;
					}if(params.y<0){
						params.y=0;
					}if(params.x>mScreenWidth-mToast_view.getWidth()){
						params.x=mScreenWidth-mToast_view.getWidth();
					}if(params.y>mScreenHeight-mToast_view.getHeight()-22){
						params.y=mScreenHeight-mToast_view.getHeight()-22;
					}
					mWM.updateViewLayout(mToast_view, params);
				
					//4.���³�ʼ����
					startx = (int) event.getRawX();
					starty = (int) event.getRawY();
					
					break;
				case MotionEvent.ACTION_UP:
					//�洢���ƶ���λ��
					SpUtil.putInt(getApplicationContext(), ConstantValue.LOCATION_X, params.x);
					SpUtil.putInt(getApplicationContext(), ConstantValue.LOCATION_Y, params.y);
					
					break;
				}
				return true;
			}
		});
        
        
        int[] Style=new int[]{
        	  R.drawable.call_locate_white,
        	  R.drawable.call_locate_orange,
        	  R.drawable.call_locate_blue,
        	  R.drawable.call_locate_gray,
        	  R.drawable.call_locate_green
        };
        int intStylet = SpUtil.getInt(getApplicationContext(), ConstantValue.TOAST_STYLE, 0);
        //ͨ��sp�����ʽ
        tv_toast.setBackgroundResource(Style[intStylet]);
        //�ڴ����Ϲ���һ��view��Ȩ�ޣ�
        params.x = SpUtil.getInt(getApplicationContext(), ConstantValue.LOCATION_X, 0);
        params.y = SpUtil.getInt(getApplicationContext(), ConstantValue.LOCATION_Y, 0);
        mWM.addView(mToast_view, mParams);
        
        //��ѯ������
        query(number);
      
        
	}
	
	private void query(final String number) {
		//��ѯ�Ǻ�ʱ����
		new Thread(){
			
			public void run(){
				address = Address.Address(number);
				//��ѯ��Ϸ���һ������Ϣ��mHandler��
				
				mHandler.sendEmptyMessage(0);
			};
			
		}.start();
		
	}

	@Override
	public IBinder onBind(Intent intent) {
		
		return null;
	}
	//������˾
    @Override
    public void onDestroy() {
    	//ȡ���Ե绰�ļ���,��Ҫ��֤���İ�ȫ�ԣ��жϵ绰�������������󶼱����������ˣ�
    	if(mTm!=null&&mMyPhoneStateListener!=null){
    		mTm.listen(mMyPhoneStateListener, PhoneStateListener.LISTEN_NONE);
    	}
    	
    	if(intentOutCallReceiver!=null){
    		unregisterReceiver(intentOutCallReceiver);
    	}
    	
    	super.onDestroy();
    }
}
