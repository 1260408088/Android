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
    	//开启土司
    	//电话状态的监听
		//获取电话的管理者对象
    	mTm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
    	//监听电话的状态
    	mMyPhoneStateListener = new MyPhoneStateListener();
    	mTm.listen(mMyPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
    	//获取窗体对象
    	mWM = (WindowManager) getSystemService(WINDOW_SERVICE);
    	mScreenHeight = mWM.getDefaultDisplay().getHeight();
		mScreenWidth = mWM.getDefaultDisplay().getWidth();
		
		//拨出电话的广播过滤者
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
		//创建广播接受者
		intentOutCallReceiver = new IntentOutCallReceiver();
		registerReceiver(intentOutCallReceiver, intentFilter);
    	super.onCreate();
    }
	
	//播出电话的广播接收类
	class IntentOutCallReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			//获得电话号码
			String number = getResultData();
			//设置土司，传递号码显示归属
			showToast(number);		
		}
		
	}
	
	
    class MyPhoneStateListener extends PhoneStateListener{
    	

		

		//手动重写电话状态发生改变的方法
    	@Override
    	public void onCallStateChanged(int state, String incomingNumber) {
    		switch(state){
    		
    		case TelephonyManager.CALL_STATE_IDLE:
    			//空闲状态，没有任何活动
    			Log.i(tag, "空闲");
//    			if(mWM!=null&&mToast_view!=null){
//    			mWM.removeView(mToast_view);
//    			}
    			break;
    		case TelephonyManager.CALL_STATE_OFFHOOK:
    			//摘机状态，至少有一个状态通话，或者拨打
    			Log.i(tag, "接通状态");
    			break;
    		case TelephonyManager.CALL_STATE_RINGING:
    			//响铃状态
    			Log.i(tag, "响铃状态");
    			showToast(incomingNumber);
    			break;
    		
    		}
    		super.onCallStateChanged(state, incomingNumber);
    	}

    }
    
    /**
	 * 土司来电号码
	 */
	private void showToast(String number) {
		//Toast.makeText(getApplicationContext(), number, 1).show();
		final WindowManager.LayoutParams params = mParams;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
//                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE	默认能够被触摸
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        params.format = PixelFormat.TRANSLUCENT;
        //在响铃的时候显示吐司,和电话类型一致
        params.type = WindowManager.LayoutParams.TYPE_PHONE;
        params.setTitle("Toast");
        
        //指定吐司的所在位置(将吐司指定在左上角)
        params.gravity = Gravity.LEFT+Gravity.TOP;
        
     
        
        //吐司显示效果(吐司布局文件),xml-->view(吐司),将吐司挂在到windowManager窗体上
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
					//1获取初始的坐标
					startx = (int) event.getRawX();
					starty = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_MOVE:
					//2获得移动中的坐标
					int moveX = (int)event.getRawX();
					int moveY = (int)event.getRawY();
					//3计算与开始坐标的差值
					
					disx = moveX-startx;
					disy = moveY-starty;
					
					params.x=params.x+disx;
					params.y=params.y+disy;
					//容错处理
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
				
					//4.更新初始坐标
					startx = (int) event.getRawX();
					starty = (int) event.getRawY();
					
					break;
				case MotionEvent.ACTION_UP:
					//存储的移动的位置
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
        //通过sp获得样式
        tv_toast.setBackgroundResource(Style[intStylet]);
        //在窗体上挂载一个view（权限）
        params.x = SpUtil.getInt(getApplicationContext(), ConstantValue.LOCATION_X, 0);
        params.y = SpUtil.getInt(getApplicationContext(), ConstantValue.LOCATION_Y, 0);
        mWM.addView(mToast_view, mParams);
        
        //查询归属地
        query(number);
      
        
	}
	
	private void query(final String number) {
		//查询是耗时操作
		new Thread(){
			
			public void run(){
				address = Address.Address(number);
				//查询完毕发送一个空消息给mHandler，
				
				mHandler.sendEmptyMessage(0);
			};
			
		}.start();
		
	}

	@Override
	public IBinder onBind(Intent intent) {
		
		return null;
	}
	//销毁土司
    @Override
    public void onDestroy() {
    	//取消对电话的监听,但要保证他的安全性（判断电话监听的两个对象都被创建出来了）
    	if(mTm!=null&&mMyPhoneStateListener!=null){
    		mTm.listen(mMyPhoneStateListener, PhoneStateListener.LISTEN_NONE);
    	}
    	
    	if(intentOutCallReceiver!=null){
    		unregisterReceiver(intentOutCallReceiver);
    	}
    	
    	super.onDestroy();
    }
}
