package com.itheima.mobilesafe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.itheima.mobilesafe.engine.VirusDao;
import com.itheima.mobilesafe.utils.Md5Util;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
public class KillVirusActivity extends Activity {
	
	protected static final int SCANING = 100;

	protected static final int SCAN_FINISH = 101;
	private TextView tv_scanAppName;
	private ProgressBar pb_scanApp;
	private LinearLayout ll_text_list;
	private ImageView iv_scan;
	private int index=0;
	private List<Scaninfo> virusScanList;
	private Handler mHandler=new Handler(){
		public void handleMessage(android.os.Message msg){
			switch (msg.what) {
			case SCANING:
				Scaninfo scaninfo=(Scaninfo) msg.obj;
				tv_scanAppName.setText(scaninfo.Name);
				TextView textView = new TextView(getApplicationContext());
				if(scaninfo.isVirus){
					textView.setTextColor(Color.RED);
					textView.setText("���ֲ���"+scaninfo.Name);
				}else{
					textView.setTextColor(Color.BLACK);
					textView.setText("ɨ�谲ȫ"+scaninfo.Name);
				}
				//���ʼ���Textview
				ll_text_list.addView(textView,0);
				break;
			case SCAN_FINISH:
				//���ö�����textviewΪɨ�����
				tv_scanAppName.setText("ɨ�����");
				//ֹͣ����ִ�еĶ���
				iv_scan.clearAnimation();
				//����ж�ط���������Щ���в�����Ӧ�� ȫ��ɾ��
				uninstallVirus();
				break;
			}
		}

		private void uninstallVirus() {
			//��ʽ��ͼ������ж�ط���
			for (Scaninfo viruslist :virusScanList){
				//Դ��
				Intent intent = new Intent("android.intent.action.DELETE");
				intent.addCategory("android.intent.category.DEFAULT");
				intent.setData(Uri.parse("package:"+viruslist.packageName));
				startActivity(intent);
			}
		};
	};

	@Override
    protected void onCreate(Bundle savedInstanceState) {
    	setContentView(R.layout.activity_kill_virtus);
    	initUI();
    	initAnmiation();
    	CheckVirus();
    	super.onCreate(savedInstanceState);
    }
	
	
	/**
	 * ��ʼ��������ɨ��
	 */
	private void CheckVirus() {
		new Thread(){

			public void run(){
				//��ò������ݿ�����еĲ����ĵ�MD5��
				
				List<String> virusList = VirusDao.getVirusList();
				
				//����ֻ������еĳ����ǩ���ļ���MD5��
				//1��ð��Ĺ����߶���
				PackageManager pm = getPackageManager();
				//2.��ȡ����Ӧ�ó���ǩ���ļ�(PackageManager.GET_SIGNATURES �Ѱ�װӦ�õ�ǩ���ļ�+)
				//PackageManager.GET_UNINSTALLED_PACKAGES ж�����˵�Ӧ��,������ļ�
				List<PackageInfo> packageInfoList = pm.getInstalledPackages(PackageManager.GET_SIGNATURES+
								  PackageManager.GET_UNINSTALLED_PACKAGES);
				
				virusScanList = new ArrayList<Scaninfo>();
			    //�洢����Ӧ�õļ���
			    List<Scaninfo> AllScanList = new ArrayList<Scaninfo>();
			    pb_scanApp.setMax(packageInfoList.size());
				for (PackageInfo packageInfo : packageInfoList) {
					 
					Scaninfo scaninfo = new Scaninfo();
					//1.��ȡǩ���ļ�������
					Signature[] signatures=packageInfo.signatures;
					//2.��ȡǩ���ļ�����ĵ�һλ,Ȼ�����md5,����md5�����ݿ��е�md5�ȶ�
					Signature signature=signatures[0];
					String string = signature.toCharsString();//(��toSting������Ҫ��һ��)
					//3.32λ�ַ���,16�����ַ�(0-f)
					String encoder = Md5Util.encoder(string);
					if(virusList.contains(encoder)){
						scaninfo.isVirus=true;
						//�ǲ�����ӵ�VirusScanList���ǲ���Ӱ��֮���scaninfo�е��������ݸ�ֵ
						virusScanList.add(scaninfo);
					}else{
						scaninfo.isVirus=false;
					}
					//6,ά������İ���,�Լ�Ӧ������
					//����Ӧ�õİ�����Ӧ��������ӵ�scaninfo֮��
					scaninfo.packageName=packageInfo.packageName;
					//��ȡ��ʱ����ɨ���Ӧ�õ�Ӧ������
					scaninfo.Name=packageInfo.applicationInfo.loadLabel(pm).toString();
					AllScanList.add(scaninfo);
					
					//��ʱ���������ʱʱ�䣬���û���ʵ�ĸ���
					try {
						Thread.sleep(50+new Random().nextInt(100));
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//����progress���ȣ�
					index++;
					pb_scanApp.setProgress(index);
					//8.�����߳��з�����Ϣ,��֪���̸߳���UI(1:����ɨ��Ӧ�õ�����2:ɨ������������Բ��������view)
					Message msg = Message.obtain();
					msg.what=SCANING;
					msg.obj=scaninfo;
					mHandler.sendMessage(msg);
				}
				//ɨ�����Ҳ����message֪ͨ����UI
				Message msg = Message.obtain();
				msg.what = SCAN_FINISH;
				mHandler.sendMessage(msg);
			};
		}.start();
		}
	
	class Scaninfo{
		 public boolean isVirus;
		 public String packageName;
		 public String Name;
	}
	
	/**
	 * ��ʼ����ת����
	 */
	private void initAnmiation(){
		RotateAnimation rotateAnimation = new RotateAnimation(0, 360, 
				Animation.RELATIVE_TO_SELF, 0.5f, 
				Animation.RELATIVE_TO_SELF, 0.5f);
		rotateAnimation.setRepeatCount(RotateAnimation.INFINITE);
		rotateAnimation.setDuration(1000);
		rotateAnimation.setFillAfter(true);
		iv_scan.startAnimation(rotateAnimation);
	}

	private void initUI() {
		tv_scanAppName = (TextView) findViewById(R.id.tv_scanAppName);
		pb_scanApp = (ProgressBar) findViewById(R.id.pb_scanApp);
		ll_text_list = (LinearLayout) findViewById(R.id.ll_text_list);
		iv_scan = (ImageView) findViewById(R.id.iv_scan);
	}
}
