package com.itheima.mobilesafe;

import com.itheima.mobilesafe.service.LockScreenService;
import com.itheima.mobilesafe.utils.ServiceUtils;
import com.itheima.mobilesafe.utils.SpUtil;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class ProgressSetingActivity extends Activity {
    
	private CheckBox cb_show_system;
	private CheckBox cb_open_clean;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_progress_setting);
		initSystemshow();
		initLookScreen();
		super.onCreate(savedInstanceState);
	}

	private void initLookScreen() {
		
		        cb_open_clean = (CheckBox) findViewById(R.id.cb_open_clean);
		        //״̬����
		        boolean running = ServiceUtils.isRunning(getApplicationContext(), "com.itheima.mobilesafe.service.LockScreenService");
		        cb_open_clean.setChecked(running);
				if(running){
					cb_open_clean.setText("���������Կ���");
				}else{
					cb_open_clean.setText("���������Թر�");
				}
				//����¼�
				cb_open_clean.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						  if(isChecked){
							  cb_open_clean.setText("���������Կ���");
							  startService(new Intent(getApplicationContext(),LockScreenService.class));
						  }else{
							  cb_open_clean.setText("���������Թر�");
							  stopService(new Intent(getApplicationContext(),LockScreenService.class));
						  }
					}
				});
	}

	private void initSystemshow() {
	    //�ҵ��ؼ�
		cb_show_system = (CheckBox) findViewById(R.id.cb_show_system);
		//���Թ�ѡ״̬
		
		boolean ischeck = SpUtil.getBoolean(getApplicationContext(), ConstantValue.SHOW_SYSTEM, false);
		cb_show_system.setChecked(ischeck);
		if(ischeck){
			cb_show_system.setText("ϵͳӦ������ʾ");
		}else{
			cb_show_system.setText("ϵͳӦ��������");
		}
		//����¼�
		cb_show_system.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				  if(isChecked){
					  cb_show_system.setText("ϵͳӦ������ʾ");
				  }else{
					  cb_show_system.setText("ϵͳӦ��������");
				  }
				  SpUtil.putBoolean(getApplicationContext(), ConstantValue.SHOW_SYSTEM, isChecked);
			}
		});
		
	}

}
