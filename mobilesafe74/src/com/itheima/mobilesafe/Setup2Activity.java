package com.itheima.mobilesafe;

import com.itheima.mobilesafe.utils.SpUtil;
import com.itheima.mobilesafe.view.SettingItemView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

public class Setup2Activity extends Activity {
       private SettingItemView siv_sim_bound;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_setip2);
    	initUI();
    	
    }

  
    /**
     * 加载布局
     */
    private void initUI() {
    		//匿名内部类，使用外部变量要么加上final 要么把变量变成类变量
    		siv_sim_bound = (SettingItemView) findViewById(R.id.siv_sim_bound);
    		String sim_number=SpUtil.getString(this, ConstantValue.SIM_NUMBER, "");
    		if(TextUtils.isEmpty(sim_number)){
    			siv_sim_bound.setCheck(false);
    		}else{
    			siv_sim_bound.setCheck(true);
    		}
    		//设置监听事件
    		siv_sim_bound.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					boolean ischeck = siv_sim_bound.isCheck();
					//点击状态取反
					siv_sim_bound.setCheck(!ischeck);
					if(!ischeck){
						//找到电话监听
						//获取sim卡序列号 TelephonyManager
						TelephonyManager manger = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
						//获取sim卡序列号
						String simSerialNumber = manger.getSimSerialNumber();
						//存储sim卡的序列号
						SpUtil.putString(getApplicationContext(), ConstantValue.SIM_NUMBER, simSerialNumber);
					}else{
						//7,将存储序列的节点，从sp点中删除
						 SpUtil.remove(getApplicationContext(),ConstantValue.SIM_NUMBER);
					}
					
				}
			});
    		
	}


	//下一页按钮
	public void nexBut(View v){
    	
    	String simSerialNumber = SpUtil.getString(getApplicationContext(), ConstantValue.SIM_NUMBER, "");
    	//判断sim卡序号是否为空
    	if(!TextUtils.isEmpty(simSerialNumber)){
    		//不为空，跳转
    		Intent intent = new Intent(this,Setup3Activity.class);
        	startActivity(intent);
        	overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);
        	finish();
    	}else{
    		//为空提示用户一下
    		Toastutils.show(getApplicationContext(), "清选择绑定sim卡");
    	}
    	
    }
	
	//上一页按钮
	public void preBut(View v){
		Intent intent = new Intent(this,Setup1Activity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.pre_in_anim, R.anim.pre_out_anim);
		finish();
		
	}
}
