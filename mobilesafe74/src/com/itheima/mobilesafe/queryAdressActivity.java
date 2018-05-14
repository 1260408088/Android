package com.itheima.mobilesafe;


import com.itheima.mobilesafe.engine.Address;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class queryAdressActivity extends Activity {
	
        private EditText et_phone;
		private Button bt_query;
		private TextView tv_result;
		private String address;
		private Handler mHandler=new Handler(){
			public void handleMessage(android.os.Message msg) {
				//Handler������Ϣ����UI
				tv_result.setText(address);
			};
		};
		@Override
        protected void onCreate(Bundle savedInstanceState) {
        	super.onCreate(savedInstanceState);
        	setContentView(R.layout.activity_querryadresss);
        	//���õ�ַ��ѯ
        	//Address.Address("079112345678");
        	initUI();
        }

		private void initUI() {
			//1�ҵ���Ӧ�Ŀؼ�
			et_phone = (EditText) findViewById(R.id.et_phone);
			bt_query = (Button) findViewById(R.id.bt_query);
			tv_result = (TextView) findViewById(R.id.tv_result);
			bt_query.setOnClickListener(new OnClickListener() {
			//�����ť��ѯ
			@Override
			public void onClick(View v){
				//2��ȡ����ĺ���
				String phone = et_phone.getText().toString();
				if(!TextUtils.isEmpty(phone)){
					query(phone);	
				}else{
					//Ϊ�� ����Ч��
					Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
					et_phone.startAnimation(shake);
					//��ȡ�𶯵ķ���
					Vibrator vibrator=(Vibrator) getSystemService(VIBRATOR_SERVICE);
					//�����𶯵ķ�ʽ��ͣ2000����5000��ͣ2000����5000, ѭ�����Ρ�
					vibrator.vibrate(new long[]{2000,50000,2000,50000}, 2);
				}
				
				}
			});
			//ʵʱ��ѯ
			et_phone.addTextChangedListener(new TextWatcher(){

				@Override
				public void afterTextChanged(Editable s) {
					//�������������
					String phone = et_phone.getText().toString();
					query(phone);
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
					
				}

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					
				}
				
			});
			
		}
		
		private void query(final String phone) {
			 new Thread(){
				public void run(){
			    address = Address.Address(phone);
			    //3��Ϣ���ƣ��������߳̿��Ը���UI
			    mHandler.sendEmptyMessage(0);
				 };
			 }.start();
		}
			
		
}
