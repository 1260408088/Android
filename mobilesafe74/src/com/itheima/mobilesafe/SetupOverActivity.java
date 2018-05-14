package com.itheima.mobilesafe;

import com.itheima.mobilesafe.utils.SpUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
/*
 * 设置完成跳转到此页面，如果不能跳到设置页面
 * 
 */
public class SetupOverActivity extends Activity {
       private TextView tv_number;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	//判断是否设置完成
    	boolean set_up_over = SpUtil.getBoolean(this, ConstantValue.STUP_OVER, false);
    	if(set_up_over){
    		setContentView(R.layout.activity_setup_over);
    		initdate();
    	}else{
    		//未完成就跳转到设置界面
    		Intent intent = new Intent(this,Setup1Activity.class);
    		startActivity(intent);
    		finish();
    	}
    }

	private void initdate() {
	       //显示设置的安全号码
           tv_number = (TextView) findViewById(R.id.tv_number);
           String number = SpUtil.getString(getApplicationContext(), ConstantValue.CONTACT_PHONE, "");
           tv_number.setText(number);
           //为重新设置的安全号码的TextView设置点击事件，（ImageView和TextView都不具备默认点击事件）
           TextView tv_reset = (TextView) findViewById(R.id.tv_reset);
           tv_reset.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				  //进入重新设置的界面
				  Intent intent = new Intent(getApplicationContext(),Setup1Activity.class);
				  startActivity(intent);
				  finish();
			}
		});
           
	}
}
