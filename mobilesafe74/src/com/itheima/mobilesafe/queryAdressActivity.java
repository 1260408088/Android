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
				//Handler接收消息更新UI
				tv_result.setText(address);
			};
		};
		@Override
        protected void onCreate(Bundle savedInstanceState) {
        	super.onCreate(savedInstanceState);
        	setContentView(R.layout.activity_querryadresss);
        	//调用地址查询
        	//Address.Address("079112345678");
        	initUI();
        }

		private void initUI() {
			//1找到相应的控件
			et_phone = (EditText) findViewById(R.id.et_phone);
			bt_query = (Button) findViewById(R.id.bt_query);
			tv_result = (TextView) findViewById(R.id.tv_result);
			bt_query.setOnClickListener(new OnClickListener() {
			//点击按钮查询
			@Override
			public void onClick(View v){
				//2获取输入的号码
				String phone = et_phone.getText().toString();
				if(!TextUtils.isEmpty(phone)){
					query(phone);	
				}else{
					//为空 抖动效果
					Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
					et_phone.startAnimation(shake);
					//获取震动的服务
					Vibrator vibrator=(Vibrator) getSystemService(VIBRATOR_SERVICE);
					//设置震动的方式，停2000，震5000，停2000，震5000, 循环两次。
					vibrator.vibrate(new long[]{2000,50000,2000,50000}, 2);
				}
				
				}
			});
			//实时查询
			et_phone.addTextChangedListener(new TextWatcher(){

				@Override
				public void afterTextChanged(Editable s) {
					//获得输入框的内容
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
			    //3消息机制，告诉主线程可以更新UI
			    mHandler.sendEmptyMessage(0);
				 };
			 }.start();
		}
			
		
}
