package com.example.shark;
import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.Toast;
import com.example.fragment.LeftFragment;
import com.itcast.switchs.ui.ToggleView;
import com.itcast.switchs.ui.ToggleView.OnSwitchStateUpdateListener;
import com.itcast.switchs.ui.ToggleView.OnSwitchStateUpdateListener.*;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.king.three.FanBtn;
public class MainActivity extends SlidingFragmentActivity{
		
	
		private int key=1;
		private ToggleView tv_start;
		//private ComponentName mDeviceAdminSample;
		//private DevicePolicyManager mDmp;
		private boolean running;
		private CheckBox cb_check;
		private ImageButton ib_toggle;
		private FragmentManager fm;
		
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initUI();
		
		//��Ӳ������
		setBehindContentView(R.layout.letf_menu_activity);
		SlidingMenu slidingMenu = getSlidingMenu();
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);// ��Ե����
		//�ô���������Ļ
		WindowManager windowManager = getWindowManager();
		int width = windowManager.getDefaultDisplay().getWidth();  //�����Ļ�ľ�ȷ��С
		slidingMenu.setBehindOffset(width*160/300);
		initFragment();
		
	}
	/**
	 * ��ʼ��fragment
	 */
	private void initFragment(){
		fm = getSupportFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();// ��ʼ����
		// ��fragment�滻֡����;��1:֡����������id;��2:��Ҫ�滻��fragment;��3:���
		/*transaction.replace(R.id.fl_left, new LeftFragment(), "TAG_CONTENT");*/
		transaction.replace(R.id.fl_left, new LeftFragment(),"TAG_LEFT_MENU");
		transaction.commit();// �ύ����
	}
	
	/*//��ȡ�����fragment����
		public LeftFragment getLeftMenuFragment() {
			FragmentManager fm = getSupportFragmentManager();
			LeftFragment fragment = (LeftFragment) fm
					.findFragmentByTag("TAG_LEFT_MENU");// ���ݱ���ҵ���Ӧ��fragment
			return fragment;
		}*/
	
	private void initUI(){
		tv_start =(ToggleView) findViewById(R.id.toggleView);
		cb_check = (CheckBox) findViewById(R.id.cb_check);
		ib_toggle = (ImageButton) findViewById(R.id.ib_toggle);
		
		tv_start.setSwitchBackgroundResource(R.drawable.switch_background);
		tv_start.setSlideButtonRessource(R.drawable.slide_button);
		tv_start.setSwitchState(false);
		
		boolean check = SpUtiles.getBoolean(getApplicationContext(),"checked", false);
		if(check){
			cb_check.setText("�ر���");
		}else{
			cb_check.setText("������");
		}
		cb_check.setChecked(check);
		//��ô�ʱ���������״̬
		running = ServiceUtils.isRunning(getApplicationContext(), "com.example.shark.TestSensorServer");
	    tv_start.setSwitchState(running);
		
		tv_start.setOnSwitchStateUpdateListener(new OnSwitchStateUpdateListener() {
			private int value;
			@Override
			public void onStateUpdate(boolean state){
				if(!running&&state){//�ر�״̬,Ȼ����                 
					/*mDeviceAdminSample = new ComponentName(getApplicationContext(),DevericeAdmin.class);
			        //��ȡ�豸�����߶���
			        mDmp = (DevicePolicyManager)getSystemService(DEVICE_POLICY_SERVICE);
					//�����豸������
					openDevice();
					//����˦����ط���
*/					
					 value = SpUtiles.getInt(getApplicationContext(), "smart", 0);
					 Toast.makeText(getApplicationContext(), "11111 "+value+"---"+running, 0).show();
					 Intent intent = new Intent(getApplicationContext(),TestSensorServer.class);
					 //��ֵ��̫��ʵ�ˣ�����ʹ��ȫ�ֱ�����application	
					 startService(intent);
				}else /*if(!running)*/{//����״̬��Ȼ��ر� &&!state
					 Toast.makeText(getApplicationContext(), "22222 "+value+"---"+running, 0).show();
					 Intent intent = new Intent(getApplicationContext(),TestSensorServer.class);
					 stopService(intent);
				}
				//�ٴλ��״̬,
				running = ServiceUtils.isRunning(getApplicationContext(), "com.example.shark.TestSensorActivity");
			}
		});
				
		/*tv_start.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View v) {
				
				if(!running){                     
					tv_start.setBackgroundResource(R.drawable.stop);
					mDeviceAdminSample = new ComponentName(getApplicationContext(),DevericeAdmin.class);
			        //��ȡ�豸�����߶���
			        mDmp = (DevicePolicyManager)getSystemService(DEVICE_POLICY_SERVICE);
					//�����豸������
					openDevice();
					//����˦����ط���
					 Toast.makeText(getApplicationContext(), "11111", 0).show();
					 Intent intent = new Intent(getApplicationContext(),TestSensorActivity.class);
					 startService(intent);
					 
					 
				}else{
					 Toast.makeText(getApplicationContext(), "22222", 0).show();
					 tv_start.setBackgroundResource(R.drawable.start);              
					 Intent intent = new Intent(getApplicationContext(),TestSensorActivity.class);
					 stopService(intent);
				}
				//�ٴλ��״̬,
				running = ServiceUtils.isRunning(getApplicationContext(), "com.example.shark.TestSensorActivity");
			}
		});
		*/
		
		cb_check.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				
				   SpUtiles.putBoolean(getApplicationContext(), "checked", isChecked);
				   if(isChecked){
					   cb_check.setText("�ر���");
				   }else{
					   cb_check.setText("������");
				   }
				   
			}
		});
		
			
		final SlidingMenu slidingMenu = getSlidingMenu();
		//Ϊ��ť���õ���¼�
		ib_toggle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				slidingMenu.toggle();// �����ǰ״̬�ǿ�, ���ú�͹�; ��֮��Ȼ
			}
		});
		
	}
	
	/*private void openDevice(){
		Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
		intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceAdminSample);
		intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,"�豸������");
		startActivity(intent);				
	}
	*/
   
}   

