package com.itheima.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class EnterpsdAvtivity extends Activity{
		private TextView tv_name;
		private ImageView im_icon;
		private EditText ed_password;
		private Button submit;
		private String packageName;

		@Override
		protected void onCreate(Bundle savedInstanceState) {
            setContentView(R.layout.activity_renter_psd);
            initUI();
            initDate();
			super.onCreate(savedInstanceState);
		}

		private void initDate() {
			Intent intent = getIntent();
			//获得传递过来的包名
			packageName = intent.getStringExtra("packagename");
			//通过包名获取包名的应用名称和图标
			PackageManager pm = getPackageManager();
			try {
				ApplicationInfo Info = pm.getApplicationInfo(packageName,0);
				Drawable icon = Info.loadIcon(pm);
				im_icon.setBackgroundDrawable(icon);
				tv_name.setText(Info.loadLabel(pm).toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			submit.setOnClickListener(new OnClickListener() {
				/* 
				 * 结束界面后直接跳到安全卫士页面，需要改变EnterpsdAvtivity的启动方式
				 * 
				 */
				@Override
				public void onClick(View v) {
					String password = ed_password.getText().toString();
					if(TextUtils.isEmpty(password)){
						Toastutils.show(getApplicationContext(), "密码不能为空");
					}else{
						if(!password.equals("123")){
							Toastutils.show(getApplicationContext(), "密码错误");
						}else{
						      //发送一个广播告知看门狗当前的解锁应用已经跳过，不用在弹出拦截应用界面
							  Intent intent = new Intent();
							  intent.setAction("android.intent.action.SKIP");
							  intent.putExtra("packageName", packageName);
							  sendBroadcast(intent);
							  finish();
						}
					}
				}
			});
			
		}

		private void initUI() {
			tv_name = (TextView) findViewById(R.id.tv_name);
			im_icon = (ImageView) findViewById(R.id.im_icon);
			ed_password = (EditText) findViewById(R.id.ed_password);
			submit = (Button) findViewById(R.id.submit);
		}
		@Override
		public void onBackPressed() {
			//通过隐式意图,跳转到桌面
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			startActivity(intent);
			super.onBackPressed();
		}
}
