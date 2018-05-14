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
					textView.setText("发现病毒"+scaninfo.Name);
				}else{
					textView.setTextColor(Color.BLACK);
					textView.setText("扫描安全"+scaninfo.Name);
				}
				//从最开始添加Textview
				ll_text_list.addView(textView,0);
				break;
			case SCAN_FINISH:
				//设置顶部的textview为扫描完成
				tv_scanAppName.setText("扫描完成");
				//停止正在执行的动画
				iv_scan.clearAnimation();
				//调用卸载方法，将这些带有病毒的应用 全部删除
				uninstallVirus();
				break;
			}
		}

		private void uninstallVirus() {
			//隐式意图，调用卸载方法
			for (Scaninfo viruslist :virusScanList){
				//源码
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
	 * 开始进行数据扫描
	 */
	private void CheckVirus() {
		new Thread(){

			public void run(){
				//获得病毒数据库的所有的病毒的的MD5码
				
				List<String> virusList = VirusDao.getVirusList();
				
				//获得手机上所有的程序的签名文件的MD5码
				//1获得包的管理者对象
				PackageManager pm = getPackageManager();
				//2.获取所有应用程序签名文件(PackageManager.GET_SIGNATURES 已安装应用的签名文件+)
				//PackageManager.GET_UNINSTALLED_PACKAGES 卸载完了的应用,残余的文件
				List<PackageInfo> packageInfoList = pm.getInstalledPackages(PackageManager.GET_SIGNATURES+
								  PackageManager.GET_UNINSTALLED_PACKAGES);
				
				virusScanList = new ArrayList<Scaninfo>();
			    //存储所有应用的集合
			    List<Scaninfo> AllScanList = new ArrayList<Scaninfo>();
			    pb_scanApp.setMax(packageInfoList.size());
				for (PackageInfo packageInfo : packageInfoList) {
					 
					Scaninfo scaninfo = new Scaninfo();
					//1.获取签名文件的数组
					Signature[] signatures=packageInfo.signatures;
					//2.获取签名文件数组的第一位,然后进行md5,将此md5和数据库中的md5比对
					Signature signature=signatures[0];
					String string = signature.toCharsString();//(和toSting的区别要查一下)
					//3.32位字符串,16进制字符(0-f)
					String encoder = Md5Util.encoder(string);
					if(virusList.contains(encoder)){
						scaninfo.isVirus=true;
						//是病毒添加到VirusScanList但是并不影响之后对scaninfo中的其他内容赋值
						virusScanList.add(scaninfo);
					}else{
						scaninfo.isVirus=false;
					}
					//6,维护对象的包名,以及应用名称
					//将此应用的包名和应用名称添加到scaninfo之中
					scaninfo.packageName=packageInfo.packageName;
					//获取此时正在扫描的应用的应用名称
					scaninfo.Name=packageInfo.applicationInfo.loadLabel(pm).toString();
					AllScanList.add(scaninfo);
					
					//延时随机设置延时时间，给用户真实的感受
					try {
						Thread.sleep(50+new Random().nextInt(100));
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//更新progress进度，
					index++;
					pb_scanApp.setProgress(index);
					//8.在子线程中发送消息,告知主线程更新UI(1:顶部扫描应用的名称2:扫描过程中往线性布局中添加view)
					Message msg = Message.obtain();
					msg.what=SCANING;
					msg.obj=scaninfo;
					mHandler.sendMessage(msg);
				}
				//扫描完后也发送message通知更新UI
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
	 * 初始化旋转动画
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
