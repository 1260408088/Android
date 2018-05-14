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
    	//��������ز�ѯ
    	initPhoneadress();
    	//���ű���
    	initSmsBackup();
    	//���������ѯ
    	initSmsquerry();
    	//����������
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
				//������ѯҳ��
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
				 //����һ���������Ի���
				 showBackupDialog();
			}

		});
		
	}
	
	/**
	 * ����һ�����н������ĶԻ���
	 */
	private void showBackupDialog() {
		  //1����һ�����������ĶԻ���
		  final ProgressDialog progressDialog = new ProgressDialog(this);
		  progressDialog.setIcon(R.drawable.ic_launcher);
		  progressDialog.setTitle("���ű���");
		  //2����ˮƽ��ʽ
		  progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		  //3չʾ������
		  progressDialog.show();
		  //4ֱ�ӵ��ö��ű��ݷ�������(���ű���Ϊ��ʱ��������Ҫ���������߳��о���)
		  new Thread(){
			  @Override
			  public void run(){
				     String path = Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"sms74.xml";
				     SmsUtils.backup(getApplicationContext(),path,new CallBack() {
						@Override
						public void setMax(int max) {
							progressDialog.setMax(max);
						}
						//ʹ���˻ص������Բ��ø��Ĺ�����Ϳ���ֱ�Ӹı����󣨽������Ի�����߽�������
						@Override
						public void setProgress(int index) {
							progressDialog.setProgress(index);
						}
					});
				      //���ضԻ���
					  progressDialog.dismiss();
			  };
		  }.start();
		  
		
	}

	private void initPhoneadress(){
		TextView tv_query_phone_adress = (TextView) findViewById(R.id.tv_query_phone_adress);
		tv_query_phone_adress.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//��ת����ַ��ѯ����
				 Intent intent = new Intent(getApplicationContext(),queryAdressActivity.class);
				 startActivity(intent);
				 
				 
			}
		});
	}
}
