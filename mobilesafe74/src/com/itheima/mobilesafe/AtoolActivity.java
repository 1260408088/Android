package com.itheima.mobilesafe;

import java.io.File;

import javax.security.auth.callback.Callback;

import com.itheima.mobilesafe.utils.SmsUtils;
import com.itheima.mobilesafe.utils.SmsUtils.CallBack;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class AtoolActivity extends Activity {
       @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_atool);
    	//来电归属地查询
    	initPhoneadress();
    	//短信备份
    	initSmsBackup();
    	//开启号码查询
    	initSmsquerry();
    	//开启程序锁
    	initLockApp();
    }

	private void initLockApp() {
		  TextView tv_clock_app = (TextView) findViewById(R.id.tv_clock_app);
		  tv_clock_app.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),LockAppActivity.class);
				startActivity(intent);
			}
		});
	}

	private void initSmsquerry() {
		 TextView tv_common_number = (TextView)findViewById(R.id.tv_common_number);
		 tv_common_number.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//开启查询页面
				Intent intent = new Intent(getApplicationContext(),CommonNumberQueryActivity.class);
				startActivity(intent);
				
			}
		});
	}

	private void initSmsBackup() {
		TextView tv_sms_backup = (TextView) findViewById(R.id.tv_sms_backup);
		tv_sms_backup.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				 //开启一个进度条对话框
				 showBackupDialog();
			}

		});
		
	}
	
	/**
	 * 创建一个带有进度条的对话框
	 */
	private void showBackupDialog() {
		  //1创建一个带进度条的对话框
		  final ProgressDialog progressDialog = new ProgressDialog(this);
		  progressDialog.setIcon(R.drawable.ic_launcher);
		  progressDialog.setTitle("短信备份");
		  //2设置水平样式
		  progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		  //3展示进度条
		  progressDialog.show();
		  //4直接调用短信备份方法即可(短信备份为耗时操作，需要放置在子线程中尽心)
		  new Thread(){
			  @Override
			  public void run(){
				     String path = Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"sms74.xml";
				     SmsUtils.backup(getApplicationContext(),path,new CallBack() {
						@Override
						public void setMax(int max) {
							progressDialog.setMax(max);
						}
						//使用了回调，可以不用更改工具类就可以直接改变需求（进度条对话框或者进度条）
						@Override
						public void setProgress(int index) {
							progressDialog.setProgress(index);
						}
					});
				      //隐藏对话框
					  progressDialog.dismiss();
			  };
		  }.start();
		  
		
	}

	private void initPhoneadress(){
		TextView tv_query_phone_adress = (TextView) findViewById(R.id.tv_query_phone_adress);
		tv_query_phone_adress.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//跳转到地址查询界面
				 Intent intent = new Intent(getApplicationContext(),queryAdressActivity.class);
				 startActivity(intent);
				 
				 
			}
		});
	}
}
