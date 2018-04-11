package com.example.shark;

import android.app.Activity;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Parcelable;
import android.os.PowerManager;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

public class TestSensorServer extends Service{

	private SensorManager sensorManager;
	private Vibrator vibrator;
	private static final String TAG = "TestSensorActivity";
	private static final int SENSOR_SHAKE = 10;
	private ComponentName mDeviceAdminSample;
	private DevicePolicyManager mDmp;
	private Activity context;
	private PowerManager pm;
	private boolean isScreenOn;
	private KeyguardManager km;
	private KeyguardLock kl;
	private PowerManager.WakeLock wl;
	private boolean ACCELEROMETER=false;
	private boolean PROXIMITY=false;
	private boolean check;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mDeviceAdminSample = new ComponentName(getApplicationContext(),
				DevericeAdmin.class);
		// 获取设备管理者对象
		mDmp = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
		// this.getIntent().getParcelableExtra("DOM");
		openDevice();
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		if (sensorManager != null) {// 注册监听器
			sensorManager.registerListener(sensorEventListener,
					sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
					SensorManager.SENSOR_DELAY_NORMAL);
			// 第一个参数是Listener，第二个参数是所得传感器类型，第三个参数值获取传感器信息的频率
			sensorManager.registerListener(sensorEventListener,
	                sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY),
	                SensorManager.SENSOR_DELAY_UI);
		}

	}

	@Override
	public void onDestroy() {
		if (sensorManager != null) {// 取消监听器
			sensorManager.unregisterListener(sensorEventListener);
		}
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		return super.onStartCommand(intent, flags, startId);
	}
	
	private SensorEventListener sensorEventListener = new SensorEventListener() {
		@Override
		public void onSensorChanged(final SensorEvent event){
					//判断此时屏幕是否处于锁屏状态
					pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
					isScreenOn = pm.isScreenOn();// 如果为true，则表示屏幕“亮”了，否则屏幕“暗”了。
					Log.i(TAG, "scrennn" + isScreenOn);
					
					  switch(event.sensor.getType()) {//三轴加速器
		                case Sensor.TYPE_ACCELEROMETER:
							 new Thread(){
								  public void run(){
									  float[] accValues = event.values.clone();
						                 // 开一个子线程，方便延时操作（睡眠）
						                 // 传感器信息改变时执行该方法
											float[] values = event.values;
											float x = values[0]; // x轴方向的重力加速度，向右为正
											float y = values[1]; // y轴方向的重力加速度，向前为正
											float z = values[2]; // z轴方向的重力加速度，向上为正
											Log.i(TAG, "x轴方向的重力加速度" + x + "；y轴方向的重力加速度" + y
													+ "；z轴方向的重力加速度" + z);
											// 一般在这三个方向的重力加速度达到40就达到了摇晃手机的状态。

											int medumValue = SpUtiles.getInt(
													getApplicationContext(), "smart", 0);
											// 如果不敏感请自行调低该数值,低于10的话就不行了,因为z轴上的加速度本身就已经达到10了
									  if (Math.abs(x) > medumValue
												|| Math.abs(y) > medumValue
												|| Math.abs(z) > medumValue) {
											Log.i(TAG, "检测到摇晃，执行操作！");
											ACCELEROMETER=true;
											//handler.sendMessage(msg);
											//Toast.makeText(TestSensorServer.this, "大于所要值", 0).show();
											check = SpUtiles.getBoolean(
													getApplicationContext(), "checked",
													false);
											/*
											 * if(check && isScreenOn){
											 * vibrator.vibrate(200); }
											 */
											try {
												Thread.sleep(600);
											} catch (InterruptedException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
										}else{
											ACCELEROMETER=false;
										}
								  }
							  }.start();
							
		                    break;
		                case Sensor.TYPE_PROXIMITY:    //距离传感器
		                	 float[] value = event.values.clone();
		                    if (value[0] ==0.0) {// 贴近手机
		        				Toast.makeText(TestSensorServer.this, "压住", 0).show();
		        				PROXIMITY=false;
		        				return;
		        			} else {// 远离手机
		        				Toast.makeText(TestSensorServer.this, "未压住", 0).show();
		        				PROXIMITY=true;
		        			}
		                    break;
				}
			  if(ACCELEROMETER&&PROXIMITY){
				  //Toast.makeText(TestSensorServer.this, "调用锁屏", 0).show();
				  //handler.sendMessage(msg);
				  Toast.makeText(TestSensorServer.this, "执行", 0).show();
				  new Thread() {
						public void run() {
							Message msg= new Message();
							msg.what = SENSOR_SHAKE;
							handler.sendMessage(msg);
								if (check) {
									vibrator.vibrate(200);
								}
								try {
									Thread.sleep(600);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						
					}.start();
				  
			  }
			  //ACCELEROMETER=false;
			  //PROXIMITY=false;
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub
			
		}
	};
		
	/**
	 * 动作执行
	 */
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case SENSOR_SHAKE:
				if (mDmp.isAdminActive(mDeviceAdminSample) && isScreenOn) {
					// 锁屏
					mDmp.lockNow();
					// 同时设置解锁密码
					// mDmp.resetPassword("123456", 0);
					// 让其
				} else {
					/*
					 * if(check){ vibrator.vibrate(200); }
					 */
					// 获取电源管理器对象
					pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
					// 获取PowerManager.WakeLock对象，后面的参数|表示同时传入两个值，最后的是调试用的Tag
					wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP
							| PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "bright");
					// 点亮屏幕
					wl.acquire();
					// 得到键盘锁管理器对象
					km = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
					kl = km.newKeyguardLock("unLock");
					// 解锁
					kl.disableKeyguard();

				}
				// Toast.makeText(getApplicationContext(), "检测到摇晃，执行操作！",
				// Toast.LENGTH_SHORT).show();
				Log.i(TAG, "检测到摇晃，执行操作！");
				break;
			}
		}

	};

	private void openDevice() {
		Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
				mDeviceAdminSample);
		intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "设备管理器");
		startActivity(intent);
	}
}
