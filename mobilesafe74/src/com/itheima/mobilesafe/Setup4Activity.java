package com.itheima.mobilesafe;


import com.itheima.mobilesafe.utils.SpUtil;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class Setup4Activity extends Activity {
           private CheckBox cb_box;
           private ComponentName mDeviceAdminSample;
           private DevicePolicyManager mDmp;
		@Override
        protected void onCreate(Bundle savedInstanceState) {
        	super.onCreate(savedInstanceState);
        	setContentView(R.layout.activity_setup4);
        	initData();
        }
           
         /**
         *加载界面 
         */
        private void initData() {
        	//开启设备管理器的变量
        	mDeviceAdminSample = new ComponentName(this,DevericeAdmin.class);
        	 //获取设备管理者对象
            mDmp = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
			cb_box = (CheckBox) findViewById(R.id.cb_box);
			//1保存状态，让其回显
			boolean open_security = SpUtil.getBoolean(this, ConstantValue.OPEN_SECURITY, false);
			cb_box.setChecked(open_security);
			//2根据状态改变文字
			if(open_security){
				cb_box.setText("您开启了防盗保护");
				if(!mDmp.isAdminActive(mDeviceAdminSample)){
					//开启设备管理器
					Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
					intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceAdminSample);
					intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,"设备管理器");
					startActivity(intent);
				}
				
			}else{
				cb_box.setText("您未开启防盗保护");
			}
			//监听状态中，其状态会直接发生改变
			cb_box.setOnCheckedChangeListener(new OnCheckedChangeListener(){

				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
				        //4点击之后存储，点击之后的状态
					SpUtil.putBoolean(getApplicationContext(), ConstantValue.OPEN_SECURITY, isChecked);
					
					if(isChecked){
						cb_box.setText("您开启了防盗保护");
						if(!mDmp.isAdminActive(mDeviceAdminSample)){
							//开启设备管理器
							Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
							intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceAdminSample);
							intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,"设备管理器");
							startActivity(intent);
						}
						
					}else{
						cb_box.setText("您未开启防盗保护");
					}

				}
				
			});
		}

		//下一页
           public void nexBut(View v){
        	boolean open_security1 = SpUtil.getBoolean(getApplicationContext(), ConstantValue.OPEN_SECURITY, false);
        	if(open_security1){
        		Intent intent = new Intent(this,SetupOverActivity.class);
               	startActivity(intent);
               	overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);
               	finish();
               	//存储设置完成的状态
               	SpUtil.putBoolean(this, ConstantValue.STUP_OVER, true);
        	}else{
        		Toastutils.show(this, "请开启防盗保护");
        	}
           	
           }
           //上一页
           public void preBut(View v){
           	Intent intent = new Intent(this,Setup3Activity.class);
           	startActivity(intent);
           	overridePendingTransition(R.anim.pre_in_anim, R.anim.pre_out_anim);
           	finish();
           }
}
