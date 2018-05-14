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
        	//数据初始化
        	initData();
        	
        	    
        }

		/**
		 * 
		 */
		private void initData() {
			//准备数据（文字（9组），图片（9张））
			mTitleStrs = new String[]{"手机防盗","通信卫士","软件管理","进程管理","流量统计","手机杀毒","缓存清理","高级工具","设置中心"};
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
							    // 点中列表条目的索引
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
									//点击高级工具跳转
									startActivity(new Intent(getApplicationContext(),AtoolActivity.class));
									break;
								case 8:
									//点击应用设置跳转
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
		 *弹出对话框 
		 */
		private void showDialog() {
			//判断本地是否有存储密码（sp 字符串）
			String psd = SpUtil.getString(this, ConstantValue.MOBILE_SAFE_PSD, "");
			if(TextUtils.isEmpty(psd)){
				//1,初始化设置密码对话框
				showSetPsdDialog();
			}else{
				
				//2,确认密码对话框
				showConfirmPsdDialog();
			}
			
		
			
		}
		
		/**
		 * 确认密码对话
		 */
		private void showConfirmPsdDialog() {
			//因为需要去自己定义对话框的展示样式，所以需要调用dialog.setView(view);
			//view是由自己编写的xml转换成的view对象xml——>view
			  Builder builder = new AlertDialog.Builder(this);
			  final AlertDialog dialog = builder.create();
			  
			  final View view = View.inflate(this,R.layout.dialog_confirm_psd,null);
			  //让对话框显示一个自己定义的对话框界面效果
			  dialog.setView(view,0,0,0,0);
			  dialog.show();
			  Button bt_submit = (Button) view.findViewById(R.id.bt_submit);
			  Button bt_cancel = (Button) view.findViewById(R.id.bt_cancel);
			  
			  //设置监听事件
			  bt_submit.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					  
					  EditText et_confirm_psd = (EditText) view.findViewById(R.id.et_confirm_psd);
					
					 
					  String confirmPsd = et_confirm_psd.getText().toString();
					  String psd = SpUtil.getString(getApplicationContext(), ConstantValue.MOBILE_SAFE_PSD, confirmPsd);
					  if(!TextUtils.isEmpty(confirmPsd)){
						  //加入加密算法
						   if(psd.equals( Md5Util.encoder(confirmPsd))){
							   //进去手机防盗模块，开启一个新的activity
							    Intent intent = new Intent(getApplicationContext(),SetupOverActivity.class);
							    startActivity(intent);
							    //跳转界面之后要隐藏对话框
							    dialog.dismiss();
						   }else{
							   Toastutils.show(getApplicationContext(), "密码不正确");
						   }
					  }else{
						  Toastutils.show(getApplicationContext(),"密码不能为空");
					  }
				}
			});
			  
			  //取消点击事件
			  bt_cancel.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//隐藏对话框
					dialog.dismiss();
				}
			});
			
		}

		/**
		 * 初次设置密码对话框
		 */
		private void showSetPsdDialog() {
			//因为需要去自己定义对话框的展示样式，所以需要调用dialog.setView(view);
			//view是由自己编写的xml转换成的view对象xml——>view
			  Builder builder = new AlertDialog.Builder(this);
			  final AlertDialog dialog = builder.create();
			  
			  final View view = View.inflate(this,R.layout.dialog_set_psd,null);
			  //让对话框显示一个自己定义的对话框界面效果
			  //dialog.setView(view);
			  //为了兼容低版本，给对话框设置布局的时候，让其没有内边距（android系统默认提供出来的）
			  dialog.setView(view, 0, 0, 0, 0);
			  dialog.show();
			  Button bt_submit = (Button) view.findViewById(R.id.bt_submit);
			  Button bt_cancel = (Button) view.findViewById(R.id.bt_cancel);
			  
			  //设置监听事件 （确认）
			  bt_submit.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					  EditText et_set_psd = (EditText) view.findViewById(R.id.et_set_psd);
					  EditText et_confirm_psd = (EditText) view.findViewById(R.id.et_confirm_psd);
					  String psd = et_set_psd.getText().toString();
					  String confirmPsd = et_confirm_psd.getText().toString();
					  if(!TextUtils.isEmpty(psd)&&!TextUtils.isEmpty(confirmPsd)){
						   if(psd.equals(confirmPsd)){
							   //进去手机防盗模块，开启一个新的activity
							    Intent intent = new Intent(getApplicationContext(),SetupOverActivity.class);
							    startActivity(intent);
							    //保存设置密码
							  
							    SpUtil.putString(getApplicationContext(), ConstantValue.MOBILE_SAFE_PSD, Md5Util.encoder(psd));
							  //跳转界面之后要隐藏对话框
							    dialog.dismiss();
						   }else{
							   Toastutils.show(getApplicationContext(), "密码不一致");
						   }
					  }else{
						  Toastutils.show(getApplicationContext(),"密码不能为空");
					  }
				}
			});
			  //取消点击事件
			  bt_cancel.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//隐藏对话框
					dialog.dismiss();
				}
			});
		}

		/**
		 * 找到所需控件
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
