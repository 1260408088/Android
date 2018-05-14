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
    	//加载UI
    	initUI();
    }
        
        private void initUI() {
        //找到按钮和Edittext
		et_phone_number = (EditText) findViewById(R.id.et_phone_number);
		
		//********选择号码之后要回显,在oncreat中回显（最先执行）
		String phone = SpUtil.getString(getApplicationContext(), ConstantValue.CONTACT_PHONE, "");
		et_phone_number.setText(phone);
		
		bt_select_number = (Button) findViewById(R.id.bt_select_number);
		bt_select_number.setOnClickListener(new OnClickListener() {
            //点击按钮到联系人选择页面
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),contactlistActivity.class);
				//需要返回数据
				startActivityForResult(intent, 0);
			}
		});
	}
        
        //于此传回数据
        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        	//处理用户特殊操作，不选择直接返回
        	if(data!=null){
        	String phone = data.getStringExtra("phone");
        	//将字符串中的特殊字符用空字符取代
        	phone=phone.replace("-", "").replace(" ", "").trim();
        	et_phone_number.setText(phone);
        	//存储号码到spUtil中
        	SpUtil.putString(this, ConstantValue.CONTACT_PHONE, phone);
        	}
        	
        	
        	super.onActivityResult(requestCode, resultCode, data);
        }
       
        //连个按钮的点击事件
		public void nexBut(View v){
			//得到输入框的内容，再判断是否为空。
			//特殊操作，直接输入的号码********
			String phone = et_phone_number.getText().toString();
			//不为空跳转，
			if(!TextUtils.isEmpty(phone)){
				Intent intent = new Intent(this,Setup4Activity.class);
	        	startActivity(intent);
	        	overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);
	        	finish();
	        	//应对直接输入的情况，再保存一次号码
	        	SpUtil.putString(this, ConstantValue.CONTACT_PHONE, phone);
			}else{
				//为空toast一下
				Toastutils.show(this, "请输入号码");
			}
        	
        }
        
        public void preBut(View v){
        	Intent intent = new Intent(this,Setup2Activity.class);
        	startActivity(intent);
        	overridePendingTransition(R.anim.pre_in_anim, R.anim.pre_out_anim);
        	finish();
        }
}
