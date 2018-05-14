package com.itheima.mobilesafe;

import java.util.zip.Inflater;

import org.w3c.dom.Text;

import com.itheima.mobilesafe.utils.Md5Util;
import com.itheima.mobilesafe.utils.SpUtil;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeActivity extends Activity {
          private GridView gv_home;
		  private String[] mTitleStrs;
		  private int[] mDrawableIds;
          
          
          
		@Override
        protected void onCreate(Bundle savedInstanceState) {
        	super.onCreate(savedInstanceState);
        	setContentView(R.layout.activity_home);
        	//int i=10/0;
        	initUI();
        	//���ݳ�ʼ��
        	initData();
        	
        	    
        }

		/**
		 * 
		 */
		private void initData() {
			//׼�����ݣ����֣�9�飩��ͼƬ��9�ţ���
			mTitleStrs = new String[]{"�ֻ�����","ͨ����ʿ","�������","���̹���","����ͳ��","�ֻ�ɱ��","��������","�߼�����","��������"};
			mDrawableIds = new int[]{
					R.drawable.home_safe,R.drawable.home_callmsgsafe,
					R.drawable.home_apps,R.drawable.home_taskmanager,
					R.drawable.home_netmanager,R.drawable.home_trojan,
					R.drawable.home_sysoptimize,R.drawable.home_tools,R.drawable.home_settings
			};
			        gv_home.setAdapter(new Myadapter());
			        gv_home.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							    // �����б���Ŀ������
							    switch (position) {
								case 0:
									showDialog();
									break;
								case 1:
									startActivity(new Intent(getApplicationContext(),BlackNumberActivity.class));
									break;
								case 2:
									startActivity(new Intent(getApplicationContext(),AppManagerActivity.class));
									break;
								case 3:
									startActivity(new Intent(getApplicationContext(),ProgressManagerActivity.class));
									break;
								case 4:
									startActivity(new Intent(getApplicationContext(),TrafficActivity.class));
									break;
								case 5:
									startActivity(new Intent(getApplicationContext(),KillVirusActivity.class));
									break;
								case 6:
									//startActivity(new Intent(getApplicationContext(),CacheClearActivity.class));
									startActivity(new Intent(getApplicationContext(),BaseCacheClearActivity.class));
									break;
								case 7:
									//����߼�������ת
									startActivity(new Intent(getApplicationContext(),AtoolActivity.class));
									break;
								case 8:
									//���Ӧ��������ת
									Intent intent = new Intent(getApplicationContext(),SettingActivity.class);
									startActivity(intent);
									break;

								default:
									break;
								}
							
						}

						
					});
		}
         
		
		/**
		 *�����Ի��� 
		 */
		private void showDialog() {
			//�жϱ����Ƿ��д洢���루sp �ַ�����
			String psd = SpUtil.getString(this, ConstantValue.MOBILE_SAFE_PSD, "");
			if(TextUtils.isEmpty(psd)){
				//1,��ʼ����������Ի���
				showSetPsdDialog();
			}else{
				
				//2,ȷ������Ի���
				showConfirmPsdDialog();
			}
			
		
			
		}
		
		/**
		 * ȷ������Ի�
		 */
		private void showConfirmPsdDialog() {
			//��Ϊ��Ҫȥ�Լ�����Ի����չʾ��ʽ��������Ҫ����dialog.setView(view);
			//view�����Լ���д��xmlת���ɵ�view����xml����>view
			  Builder builder = new AlertDialog.Builder(this);
			  final AlertDialog dialog = builder.create();
			  
			  final View view = View.inflate(this,R.layout.dialog_confirm_psd,null);
			  //�öԻ�����ʾһ���Լ�����ĶԻ������Ч��
			  dialog.setView(view,0,0,0,0);
			  dialog.show();
			  Button bt_submit = (Button) view.findViewById(R.id.bt_submit);
			  Button bt_cancel = (Button) view.findViewById(R.id.bt_cancel);
			  
			  //���ü����¼�
			  bt_submit.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					  
					  EditText et_confirm_psd = (EditText) view.findViewById(R.id.et_confirm_psd);
					
					 
					  String confirmPsd = et_confirm_psd.getText().toString();
					  String psd = SpUtil.getString(getApplicationContext(), ConstantValue.MOBILE_SAFE_PSD, confirmPsd);
					  if(!TextUtils.isEmpty(confirmPsd)){
						  //��������㷨
						   if(psd.equals( Md5Util.encoder(confirmPsd))){
							   //��ȥ�ֻ�����ģ�飬����һ���µ�activity
							    Intent intent = new Intent(getApplicationContext(),SetupOverActivity.class);
							    startActivity(intent);
							    //��ת����֮��Ҫ���ضԻ���
							    dialog.dismiss();
						   }else{
							   Toastutils.show(getApplicationContext(), "���벻��ȷ");
						   }
					  }else{
						  Toastutils.show(getApplicationContext(),"���벻��Ϊ��");
					  }
				}
			});
			  
			  //ȡ������¼�
			  bt_cancel.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//���ضԻ���
					dialog.dismiss();
				}
			});
			
		}

		/**
		 * ������������Ի���
		 */
		private void showSetPsdDialog() {
			//��Ϊ��Ҫȥ�Լ�����Ի����չʾ��ʽ��������Ҫ����dialog.setView(view);
			//view�����Լ���д��xmlת���ɵ�view����xml����>view
			  Builder builder = new AlertDialog.Builder(this);
			  final AlertDialog dialog = builder.create();
			  
			  final View view = View.inflate(this,R.layout.dialog_set_psd,null);
			  //�öԻ�����ʾһ���Լ�����ĶԻ������Ч��
			  //dialog.setView(view);
			  //Ϊ�˼��ݵͰ汾�����Ի������ò��ֵ�ʱ������û���ڱ߾ࣨandroidϵͳĬ���ṩ�����ģ�
			  dialog.setView(view, 0, 0, 0, 0);
			  dialog.show();
			  Button bt_submit = (Button) view.findViewById(R.id.bt_submit);
			  Button bt_cancel = (Button) view.findViewById(R.id.bt_cancel);
			  
			  //���ü����¼� ��ȷ�ϣ�
			  bt_submit.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					  EditText et_set_psd = (EditText) view.findViewById(R.id.et_set_psd);
					  EditText et_confirm_psd = (EditText) view.findViewById(R.id.et_confirm_psd);
					  String psd = et_set_psd.getText().toString();
					  String confirmPsd = et_confirm_psd.getText().toString();
					  if(!TextUtils.isEmpty(psd)&&!TextUtils.isEmpty(confirmPsd)){
						   if(psd.equals(confirmPsd)){
							   //��ȥ�ֻ�����ģ�飬����һ���µ�activity
							    Intent intent = new Intent(getApplicationContext(),SetupOverActivity.class);
							    startActivity(intent);
							    //������������
							  
							    SpUtil.putString(getApplicationContext(), ConstantValue.MOBILE_SAFE_PSD, Md5Util.encoder(psd));
							  //��ת����֮��Ҫ���ضԻ���
							    dialog.dismiss();
						   }else{
							   Toastutils.show(getApplicationContext(), "���벻һ��");
						   }
					  }else{
						  Toastutils.show(getApplicationContext(),"���벻��Ϊ��");
					  }
				}
			});
			  //ȡ������¼�
			  bt_cancel.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//���ضԻ���
					dialog.dismiss();
				}
			});
		}

		/**
		 * �ҵ�����ؼ�
		 */
		private void initUI() {
			gv_home = (GridView) findViewById(R.id.gv_home);
			
			
		}
		class Myadapter extends BaseAdapter{

			@Override
			public int getCount() {
				
				return mTitleStrs.length;
			}

			@Override
			public Object getItem(int position) {
				
				return mTitleStrs[position];
			}

			@Override
			public long getItemId(int position) {
				
				return position;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view=View.inflate(getApplicationContext(), R.layout.gridview_item, null);
				TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
				ImageView im_icon = (ImageView) view.findViewById(R.id.im_icon);
				
				tv_title.setText(mTitleStrs[position]);
				im_icon.setBackgroundResource(mDrawableIds[position]);
				return view;
			}
		}
}
