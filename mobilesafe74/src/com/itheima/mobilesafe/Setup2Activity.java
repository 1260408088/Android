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
     * ���ز���
     */
    private void initUI() {
    		//�����ڲ��࣬ʹ���ⲿ����Ҫô����final Ҫô�ѱ�����������
    		siv_sim_bound = (SettingItemView) findViewById(R.id.siv_sim_bound);
    		String sim_number=SpUtil.getString(this, ConstantValue.SIM_NUMBER, "");
    		if(TextUtils.isEmpty(sim_number)){
    			siv_sim_bound.setCheck(false);
    		}else{
    			siv_sim_bound.setCheck(true);
    		}
    		//���ü����¼�
    		siv_sim_bound.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					boolean ischeck = siv_sim_bound.isCheck();
					//���״̬ȡ��
					siv_sim_bound.setCheck(!ischeck);
					if(!ischeck){
						//�ҵ��绰����
						//��ȡsim�����к� TelephonyManager
						TelephonyManager manger = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
						//��ȡsim�����к�
						String simSerialNumber = manger.getSimSerialNumber();
						//�洢sim�������к�
						SpUtil.putString(getApplicationContext(), ConstantValue.SIM_NUMBER, simSerialNumber);
					}else{
						//7,���洢���еĽڵ㣬��sp����ɾ��
						 SpUtil.remove(getApplicationContext(),ConstantValue.SIM_NUMBER);
					}
					
				}
			});
    		
	}


	//��һҳ��ť
	public void nexBut(View v){
    	
    	String simSerialNumber = SpUtil.getString(getApplicationContext(), ConstantValue.SIM_NUMBER, "");
    	//�ж�sim������Ƿ�Ϊ��
    	if(!TextUtils.isEmpty(simSerialNumber)){
    		//��Ϊ�գ���ת
    		Intent intent = new Intent(this,Setup3Activity.class);
        	startActivity(intent);
        	overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);
        	finish();
    	}else{
    		//Ϊ����ʾ�û�һ��
    		Toastutils.show(getApplicationContext(), "��ѡ���sim��");
    	}
    	
    }
	
	//��һҳ��ť
	public void preBut(View v){
		Intent intent = new Intent(this,Setup1Activity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.pre_in_anim, R.anim.pre_out_anim);
		finish();
		
	}
}
