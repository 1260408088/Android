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
		
		//添加侧边栏了
		setBehindContentView(R.layout.letf_menu_activity);
		SlidingMenu slidingMenu = getSlidingMenu();
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);// 边缘触摸
		//用代码适配屏幕
		WindowManager windowManager = getWindowManager();
		int width = windowManager.getDefaultDisplay().getWidth();  //获得屏幕的精确大小
		slidingMenu.setBehindOffset(width*160/300);
		initFragment();
		
	}
	/**
	 * 初始化fragment
	 */
	private void initFragment(){
		fm = getSupportFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();// 开始事务
		// 用fragment替换帧布局;参1:帧布局容器的id;参2:是要替换的fragment;参3:标记
		/*transaction.replace(R.id.fl_left, new LeftFragment(), "TAG_CONTENT");*/
		transaction.replace(R.id.fl_left, new LeftFragment(),"TAG_LEFT_MENU");
		transaction.commit();// 提交事务
	}
	
	/*//获取侧边栏fragment对象
		public LeftFragment getLeftMenuFragment() {
			FragmentManager fm = getSupportFragmentManager();
			LeftFragment fragment = (LeftFragment) fm
					.findFragmentByTag("TAG_LEFT_MENU");// 根据标记找到对应的fragment
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
			cb_check.setText("关闭震动");
		}else{
			cb_check.setText("开启震动");
		}
		cb_check.setChecked(check);
		//获得此时服务的运行状态
		running = ServiceUtils.isRunning(getApplicationContext(), "com.example.shark.TestSensorServer");
	    tv_start.setSwitchState(running);
		
		tv_start.setOnSwitchStateUpdateListener(new OnSwitchStateUpdateListener() {
			private int value;
			@Override
			public void onStateUpdate(boolean state){
				if(!running&&state){//关闭状态,然后开启                 
					/*mDeviceAdminSample = new ComponentName(getApplicationContext(),DevericeAdmin.class);
			        //获取设备管理者对象
			        mDmp = (DevicePolicyManager)getSystemService(DEVICE_POLICY_SERVICE);
					//开启设备管理器
					openDevice();
					//开启甩动监控服务
*/					
					 value = SpUtiles.getInt(getApplicationContext(), "smart", 0);
					 Toast.makeText(getApplicationContext(), "11111 "+value+"---"+running, 0).show();
					 Intent intent = new Intent(getApplicationContext(),TestSensorServer.class);
					 //传值不太现实了，考虑使用全局变量，application	
					 startService(intent);
				}else /*if(!running)*/{//开启状态，然后关闭 &&!state
					 Toast.makeText(getApplicationContext(), "22222 "+value+"---"+running, 0).show();
					 Intent intent = new Intent(getApplicationContext(),TestSensorServer.class);
					 stopService(intent);
				}
				//再次获得状态,
				running = ServiceUtils.isRunning(getApplicationContext(), "com.example.shark.TestSensorActivity");
			}
		});
				
		/*tv_start.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View v) {
				
				if(!running){                     
					tv_start.setBackgroundResource(R.drawable.stop);
					mDeviceAdminSample = new ComponentName(getApplicationContext(),DevericeAdmin.class);
			        //获取设备管理者对象
			        mDmp = (DevicePolicyManager)getSystemService(DEVICE_POLICY_SERVICE);
					//开启设备管理器
					openDevice();
					//开启甩动监控服务
					 Toast.makeText(getApplicationContext(), "11111", 0).show();
					 Intent intent = new Intent(getApplicationContext(),TestSensorActivity.class);
					 startService(intent);
					 
					 
				}else{
					 Toast.makeText(getApplicationContext(), "22222", 0).show();
					 tv_start.setBackgroundResource(R.drawable.start);              
					 Intent intent = new Intent(getApplicationContext(),TestSensorActivity.class);
					 stopService(intent);
				}
				//再次获得状态,
				running = ServiceUtils.isRunning(getApplicationContext(), "com.example.shark.TestSensorActivity");
			}
		});
		*/
		
		cb_check.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				
				   SpUtiles.putBoolean(getApplicationContext(), "checked", isChecked);
				   if(isChecked){
					   cb_check.setText("关闭震动");
				   }else{
					   cb_check.setText("开启震动");
				   }
				   
			}
		});
		
			
		final SlidingMenu slidingMenu = getSlidingMenu();
		//为按钮设置点击事件
		ib_toggle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				slidingMenu.toggle();// 如果当前状态是开, 调用后就关; 反之亦然
			}
		});
		
	}
	
	/*private void openDevice(){
		Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
		intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceAdminSample);
		intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,"设备管理器");
		startActivity(intent);				
	}
	*/
   
}   

