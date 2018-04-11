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
		// ��ȡ�豸�����߶���
		mDmp = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
		// this.getIntent().getParcelableExtra("DOM");
		openDevice();
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		if (sensorManager != null) {// ע�������
			sensorManager.registerListener(sensorEventListener,
					sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
					SensorManager.SENSOR_DELAY_NORMAL);
			// ��һ��������Listener���ڶ������������ô��������ͣ�����������ֵ��ȡ��������Ϣ��Ƶ��
			sensorManager.registerListener(sensorEventListener,
	                sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY),
	                SensorManager.SENSOR_DELAY_UI);
		}

	}

	@Override
	public void onDestroy() {
		if (sensorManager != null) {// ȡ��������
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
					//�жϴ�ʱ��Ļ�Ƿ�������״̬
					pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
					isScreenOn = pm.isScreenOn();// ���Ϊtrue�����ʾ��Ļ�������ˣ�������Ļ�������ˡ�
					Log.i(TAG, "scrennn" + isScreenOn);
					
					  switch(event.sensor.getType()) {//���������
		                case Sensor.TYPE_ACCELEROMETER:
							 new Thread(){
								  public void run(){
									  float[] accValues = event.values.clone();
						                 // ��һ�����̣߳�������ʱ������˯�ߣ�
						                 // ��������Ϣ�ı�ʱִ�и÷���
											float[] values = event.values;
											float x = values[0]; // x�᷽����������ٶȣ�����Ϊ��
											float y = values[1]; // y�᷽����������ٶȣ���ǰΪ��
											float z = values[2]; // z�᷽����������ٶȣ�����Ϊ��
											Log.i(TAG, "x�᷽����������ٶ�" + x + "��y�᷽����������ٶ�" + y
													+ "��z�᷽����������ٶ�" + z);
											// һ����������������������ٶȴﵽ40�ʹﵽ��ҡ���ֻ���״̬��

											int medumValue = SpUtiles.getInt(
													getApplicationContext(), "smart", 0);
											// ��������������е��͸���ֵ,����10�Ļ��Ͳ�����,��Ϊz���ϵļ��ٶȱ�����Ѿ��ﵽ10��
									  if (Math.abs(x) > medumValue
												|| Math.abs(y) > medumValue
												|| Math.abs(z) > medumValue) {
											Log.i(TAG, "��⵽ҡ�Σ�ִ�в�����");
											ACCELEROMETER=true;
											//handler.sendMessage(msg);
											//Toast.makeText(TestSensorServer.this, "������Ҫֵ", 0).show();
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
		                case Sensor.TYPE_PROXIMITY:    //���봫����
		                	 float[] value = event.values.clone();
		                    if (value[0] ==0.0) {// �����ֻ�
		        				Toast.makeText(TestSensorServer.this, "ѹס", 0).show();
		        				PROXIMITY=false;
		        				return;
		        			} else {// Զ���ֻ�
		        				Toast.makeText(TestSensorServer.this, "δѹס", 0).show();
		        				PROXIMITY=true;
		        			}
		                    break;
				}
			  if(ACCELEROMETER&&PROXIMITY){
				  //Toast.makeText(TestSensorServer.this, "��������", 0).show();
				  //handler.sendMessage(msg);
				  Toast.makeText(TestSensorServer.this, "ִ��", 0).show();
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
	 * ����ִ��
	 */
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case SENSOR_SHAKE:
				if (mDmp.isAdminActive(mDeviceAdminSample) && isScreenOn) {
					// ����
					mDmp.lockNow();
					// ͬʱ���ý�������
					// mDmp.resetPassword("123456", 0);
					// ����
				} else {
					/*
					 * if(check){ vibrator.vibrate(200); }
					 */
					// ��ȡ��Դ����������
					pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
					// ��ȡPowerManager.WakeLock���󣬺���Ĳ���|��ʾͬʱ��������ֵ�������ǵ����õ�Tag
					wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP
							| PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "bright");
					// ������Ļ
					wl.acquire();
					// �õ�����������������
					km = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
					kl = km.newKeyguardLock("unLock");
					// ����
					kl.disableKeyguard();

				}
				// Toast.makeText(getApplicationContext(), "��⵽ҡ�Σ�ִ�в�����",
				// Toast.LENGTH_SHORT).show();
				Log.i(TAG, "��⵽ҡ�Σ�ִ�в�����");
				break;
			}
		}

	};

	private void openDevice() {
		Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
				mDeviceAdminSample);
		intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "�豸������");
		startActivity(intent);
	}
}
