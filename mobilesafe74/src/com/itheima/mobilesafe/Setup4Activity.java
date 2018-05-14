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
         *���ؽ��� 
         */
        private void initData() {
        	//�����豸�������ı���
        	mDeviceAdminSample = new ComponentName(this,DevericeAdmin.class);
        	 //��ȡ�豸�����߶���
            mDmp = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
			cb_box = (CheckBox) findViewById(R.id.cb_box);
			//1����״̬���������
			boolean open_security = SpUtil.getBoolean(this, ConstantValue.OPEN_SECURITY, false);
			cb_box.setChecked(open_security);
			//2����״̬�ı�����
			if(open_security){
				cb_box.setText("�������˷�������");
				if(!mDmp.isAdminActive(mDeviceAdminSample)){
					//�����豸������
					Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
					intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceAdminSample);
					intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,"�豸������");
					startActivity(intent);
				}
				
			}else{
				cb_box.setText("��δ������������");
			}
			//����״̬�У���״̬��ֱ�ӷ����ı�
			cb_box.setOnCheckedChangeListener(new OnCheckedChangeListener(){

				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
				        //4���֮��洢�����֮���״̬
					SpUtil.putBoolean(getApplicationContext(), ConstantValue.OPEN_SECURITY, isChecked);
					
					if(isChecked){
						cb_box.setText("�������˷�������");
						if(!mDmp.isAdminActive(mDeviceAdminSample)){
							//�����豸������
							Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
							intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceAdminSample);
							intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,"�豸������");
							startActivity(intent);
						}
						
					}else{
						cb_box.setText("��δ������������");
					}

				}
				
			});
		}

		//��һҳ
           public void nexBut(View v){
        	boolean open_security1 = SpUtil.getBoolean(getApplicationContext(), ConstantValue.OPEN_SECURITY, false);
        	if(open_security1){
        		Intent intent = new Intent(this,SetupOverActivity.class);
               	startActivity(intent);
               	overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);
               	finish();
               	//�洢������ɵ�״̬
               	SpUtil.putBoolean(this, ConstantValue.STUP_OVER, true);
        	}else{
        		Toastutils.show(this, "�뿪����������");
        	}
           	
           }
           //��һҳ
           public void preBut(View v){
           	Intent intent = new Intent(this,Setup3Activity.class);
           	startActivity(intent);
           	overridePendingTransition(R.anim.pre_in_anim, R.anim.pre_out_anim);
           	finish();
           }
}
