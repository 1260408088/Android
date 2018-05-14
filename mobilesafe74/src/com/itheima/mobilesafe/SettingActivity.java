package com.itheima.mobilesafe;

import com.itheima.mobilesafe.service.BlackNumberService;
import com.itheima.mobilesafe.service.WatchDogService;
import com.itheima.mobilesafe.service.addrssService;
import com.itheima.mobilesafe.utils.ServiceUtils;
import com.itheima.mobilesafe.utils.SpUtil;
import com.itheima.mobilesafe.view.SettingItemView;
import com.itheima.mobilesafe.view.SettingToastView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.style.SuperscriptSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class SettingActivity extends Activity{
     private String[] styles;
	private int toast_style;
	private SettingToastView scv_style;
	private SettingItemView siv_app_lock;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_setting);

    	initUpdate();
    	initAdress();
    	initStyle();
    	initLocation();
    	initBlackNumber();
    	initAppLock();
    	
    }
     
	/**
	 * ��������������
	 */
	private void initAppLock() {
		  siv_app_lock = (SettingItemView) findViewById(R.id.siv_app_lock);
		  
		  boolean isrunning = ServiceUtils.isRunning(getApplicationContext(), "com.itheima.mobilesafe.service.WatchDogService");
		  siv_app_lock.setCheck(isrunning);
		  
		  siv_app_lock.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//��ÿؼ��ϵĵ��checkbox�ĵ��״̬,��������ѡ��״̬
				boolean ischeck = siv_app_lock.isCheck();
				siv_app_lock.setCheck(!ischeck);
				
				if(!ischeck){
					 startService(new Intent(getApplicationContext(), WatchDogService.class));
				}else{
					 stopService(new Intent(getApplicationContext(), WatchDogService.class));
				}
			}
		});
	}

	/**
	 * ���غ��������ź� �绰
	 * 
	 */
	private void initBlackNumber() {
		final SettingItemView siv_blacknumber = (SettingItemView)findViewById(R.id.siv_blacknumber);
		final boolean isruning = ServiceUtils.isRunning(this, "com.itheima.mobilesafe.service.BlackNumberService");
		siv_blacknumber.setCheck(isruning);
		siv_blacknumber.setOnClickListener(new  OnClickListener() {
			@Override
			public void onClick(View v) {
				  //��ȡ��ǰ��ѡ��״̬
				  boolean check = siv_blacknumber.isCheck();
				  //�����״̬ȡ�����Ըı�ѡ��״̬
				  siv_blacknumber.setCheck(!check);
				  if(!check){
					  startService(new Intent(getApplicationContext(),BlackNumberService.class));
				  }else{
					  stopService(new Intent(getApplicationContext(),BlackNumberService.class));
				  }
			}
		});
	}

	/**
	 * ���ù�������ʾ��λ��
	 */
	private void initLocation() {
		SettingToastView scv_location = (SettingToastView) findViewById(R.id.scv_location);
		
		scv_location.setTitle("��������ʾ���λ��");
		scv_location.setDes("���ù�������ʾ���λ��");
		
		scv_location.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//����������˾λ�õ�activity
				Intent intent = new Intent(getApplicationContext(),ToastLocationActivity.class);
				startActivity(intent);
			}
		});
	}

	private void initStyle() {
		   scv_style = (SettingToastView) findViewById(R.id.scv_style);
		   
		   //1���������������ڵ�String��������
		   styles = new String[]{"͸��","��ɫ","��ɫ","��ɫ","��ɫ"};
		   //2��ȡtoast����ʽ����ֵ
		   toast_style = SpUtil.getInt(this, ConstantValue.TOAST_STYLE, 0);
		   scv_style.setTitle("���ù�������ʾ���");
		   //3������������
		   scv_style.setDes(styles[toast_style]);
		   scv_style.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//����󵯳���˾��ʽѡ��ĵ�ѡ��
				showToastStyleDialog();
			}
		});
		   
	}
	
	/**
	 * ������ʾ��ѡ��
	 */
	private void showToastStyleDialog(){
			Builder builder = new AlertDialog.Builder(this);
			builder.setIcon(R.drawable.ic_launcher);
			builder.setTitle("��ѡ���������ʽ");
			//������Ŀ�ĵ���¼�
			/*
			 * 1.string���͵����֣�����������ɫ
			 * 2.�����Ի���ʱѡ�������ֵ
			 * 3.�����Ŀ�󴥷��ĵ���¼�(1��¼ѡ������ֵ��2��ʾѡ�е���ɫ��3�رնԻ���)
			 */
			builder.setSingleChoiceItems(styles, toast_style, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//1.����ѡ�е�����ֵ
					SpUtil.putInt(getApplicationContext(), ConstantValue.TOAST_STYLE, which);
					//2.������ɫ
					scv_style.setDes(styles[which]);
					//3.�رնԻ���
					dialog.dismiss();
				}
			});
			builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//�رնԻ���
					dialog.dismiss();
				}
			});
			
			builder.show();
			
	}

	/**
	 * ���������toast��ʾ
	 */
	private void initAdress() {
     		final SettingItemView siv_adress = (SettingItemView) findViewById(R.id.siv_adress);
     		//��ȡ״̬��������
     		boolean running = ServiceUtils.isRunning(this, "com.itheima.mobilesafe.service.addrssService");
     		siv_adress.setCheck(running);
     		//�������������������Ƿ����໥�л�
     		siv_adress.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					boolean ischeck = siv_adress.isCheck();
					siv_adress.setCheck(!ischeck);
					if(!ischeck){
						//�������񣬹�����˾
						startService(new Intent(getApplicationContext(),addrssService.class)); 
					}else{
						stopService(new Intent(getApplicationContext(),addrssService.class));
					}
					
				}
			});
	}

	/**
	 * �汾���¿���
	 */
	private void initUpdate() {
		
		final SettingItemView siv_update = (SettingItemView) findViewById(R.id.siv_update);
		//��ȡ���еĿ���״̬��������ʾ
		boolean open_update = SpUtil.getBoolean(getApplicationContext(), ConstantValue.OPEN_UPDATE, false);
		//����CheckBoxĬ��״̬���Ƿ�ѡ�У�������һ�δ洢�Ľ����������
	    siv_update.setCheck(open_update);
	    siv_update.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//���֮ǰ��ѡ�еģ�������󣬱��δѡ��
			    //���֮ǰ��δѡ�У�������󣬱��ѡ��
				
				//��ȡ֮ǰ��ѡ��״̬
				boolean isCheck = siv_update.isCheck();
				//��ԭ��״̬ȡ������ͬ���ߵ���������
				siv_update.setCheck(!isCheck);
			    //��ȡ�����״̬�洢����Ӧsp
				SpUtil.putBoolean(getApplicationContext(), ConstantValue.OPEN_UPDATE, !isCheck);
				
			}
		});
		
	}
}
