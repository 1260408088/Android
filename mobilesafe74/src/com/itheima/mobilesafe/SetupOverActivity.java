package com.itheima.mobilesafe;

import com.itheima.mobilesafe.utils.SpUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
/*
 * ���������ת����ҳ�棬���������������ҳ��
 * 
 */
public class SetupOverActivity extends Activity {
       private TextView tv_number;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	//�ж��Ƿ��������
    	boolean set_up_over = SpUtil.getBoolean(this, ConstantValue.STUP_OVER, false);
    	if(set_up_over){
    		setContentView(R.layout.activity_setup_over);
    		initdate();
    	}else{
    		//δ��ɾ���ת�����ý���
    		Intent intent = new Intent(this,Setup1Activity.class);
    		startActivity(intent);
    		finish();
    	}
    }

	private void initdate() {
	       //��ʾ���õİ�ȫ����
           tv_number = (TextView) findViewById(R.id.tv_number);
           String number = SpUtil.getString(getApplicationContext(), ConstantValue.CONTACT_PHONE, "");
           tv_number.setText(number);
           //Ϊ�������õİ�ȫ�����TextView���õ���¼�����ImageView��TextView�����߱�Ĭ�ϵ���¼���
           TextView tv_reset = (TextView) findViewById(R.id.tv_reset);
           tv_reset.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				  //�����������õĽ���
				  Intent intent = new Intent(getApplicationContext(),Setup1Activity.class);
				  startActivity(intent);
				  finish();
			}
		});
           
	}
}
