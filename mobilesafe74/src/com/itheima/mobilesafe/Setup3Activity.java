package com.itheima.mobilesafe;

import com.itheima.mobilesafe.utils.SpUtil;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Setup3Activity extends Activity {
    private EditText et_phone_number;
	private Button bt_select_number;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_setup3);
    	//����UI
    	initUI();
    }
        
        private void initUI() {
        //�ҵ���ť��Edittext
		et_phone_number = (EditText) findViewById(R.id.et_phone_number);
		
		//********ѡ�����֮��Ҫ����,��oncreat�л��ԣ�����ִ�У�
		String phone = SpUtil.getString(getApplicationContext(), ConstantValue.CONTACT_PHONE, "");
		et_phone_number.setText(phone);
		
		bt_select_number = (Button) findViewById(R.id.bt_select_number);
		bt_select_number.setOnClickListener(new OnClickListener() {
            //�����ť����ϵ��ѡ��ҳ��
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),contactlistActivity.class);
				//��Ҫ��������
				startActivityForResult(intent, 0);
			}
		});
	}
        
        //�ڴ˴�������
        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        	//�����û������������ѡ��ֱ�ӷ���
        	if(data!=null){
        	String phone = data.getStringExtra("phone");
        	//���ַ����е������ַ��ÿ��ַ�ȡ��
        	phone=phone.replace("-", "").replace(" ", "").trim();
        	et_phone_number.setText(phone);
        	//�洢���뵽spUtil��
        	SpUtil.putString(this, ConstantValue.CONTACT_PHONE, phone);
        	}
        	
        	
        	super.onActivityResult(requestCode, resultCode, data);
        }
       
        //������ť�ĵ���¼�
		public void nexBut(View v){
			//�õ����������ݣ����ж��Ƿ�Ϊ�ա�
			//���������ֱ������ĺ���********
			String phone = et_phone_number.getText().toString();
			//��Ϊ����ת��
			if(!TextUtils.isEmpty(phone)){
				Intent intent = new Intent(this,Setup4Activity.class);
	        	startActivity(intent);
	        	overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);
	        	finish();
	        	//Ӧ��ֱ�������������ٱ���һ�κ���
	        	SpUtil.putString(this, ConstantValue.CONTACT_PHONE, phone);
			}else{
				//Ϊ��toastһ��
				Toastutils.show(this, "���������");
			}
        	
        }
        
        public void preBut(View v){
        	Intent intent = new Intent(this,Setup2Activity.class);
        	startActivity(intent);
        	overridePendingTransition(R.anim.pre_in_anim, R.anim.pre_out_anim);
        	finish();
        }
}
