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
			//��ô��ݹ����İ���
			packageName = intent.getStringExtra("packagename");
			//ͨ��������ȡ������Ӧ�����ƺ�ͼ��
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
				 * ���������ֱ��������ȫ��ʿҳ�棬��Ҫ�ı�EnterpsdAvtivity��������ʽ
				 * 
				 */
				@Override
				public void onClick(View v) {
					String password = ed_password.getText().toString();
					if(TextUtils.isEmpty(password)){
						Toastutils.show(getApplicationContext(), "���벻��Ϊ��");
					}else{
						if(!password.equals("123")){
							Toastutils.show(getApplicationContext(), "�������");
						}else{
						      //����һ���㲥��֪���Ź���ǰ�Ľ���Ӧ���Ѿ������������ڵ�������Ӧ�ý���
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
			//ͨ����ʽ��ͼ,��ת������
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			startActivity(intent);
			super.onBackPressed();
		}
}
