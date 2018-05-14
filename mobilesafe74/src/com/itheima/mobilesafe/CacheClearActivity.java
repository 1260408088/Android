package com.itheima.mobilesafe;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.Format;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PackageStats;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class CacheClearActivity extends Activity {
	private ProgressBar pb_bar;
	private TextView tv_name;
	private LinearLayout ll_casch;
	private Button bt_clear;
	private PackageManager mPm;
	public static final int UPDATE_CACHE_APP = 100;
	protected static final int NEW_CACHE_APP = 101;
	protected static final int FINASH_CACHE_APP = 102;
	protected static final int CLEAN_FINISH = 103;
	protected static final int SINGLE_CLEAN = 104;
	private String tag="CacheClearActivity";
	private int mIndex=0;
	private Handler mHandler = new Handler(){
    	   public void handleMessage(Message msg) {
    		   switch (msg.what) {
    		   
			case UPDATE_CACHE_APP:
				final CacheInfo info=(CacheInfo) msg.obj;
				//�����Բ�������ӻ����������Ŀ
				View view = View.inflate(getApplicationContext(), R.layout.linearlayout_cache_item, null);
				
				ImageView iv_licon = (ImageView) view.findViewById(R.id.iv_licon);
				TextView tv_lname = (TextView) view.findViewById(R.id.tv_lname);
				ImageButton ib_clearcashe = (ImageButton) view.findViewById(R.id.ib_clearcashe);
				TextView tv_cacheview = (TextView) view.findViewById(R.id.tv_cacheview);
				
				iv_licon.setBackgroundDrawable(info.icon);
				tv_lname.setText(info.name);
				tv_cacheview.setText(Formatter.formatFileSize(getApplicationContext(), info.cacheSize));
				ll_casch.addView(view, 0);
				
				ib_clearcashe.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						
						/*��Ҫ�õ�ϵͳȨ�� ���Դ˷������ܹ������ĵ��÷��������
						 * android.permission.DELETE_CACHE_FILES
						 * try {
							Class<?> clazz = Class.forName("android.content.pm.PackageManager");
							//2.��ȡ���÷�������
							Method method = clazz.getMethod("deleteApplicationCacheFiles", String.class,IPackageDataObserver.class);
							//3.��ȡ������÷���
							method.invoke(mPm, info.packageName,new IPackageDataObserver.Stub() {
								@Override
								public void onRemoveCompleted(String packageName, boolean succeeded)
										throws RemoteException {
									    Log.i(tag, "_________");
								}
							});
						} catch (Exception e) {
							e.printStackTrace();
						}
					}*/
						//�����鿴Ӧ��������棬���û��ֶ������cache
						Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
						intent.setData(Uri.parse("package:"+info.packageName));
						startActivity(intent);
						
						 Log.i(tag, "_________");
					}
				});
				break;
			case NEW_CACHE_APP:
				pb_bar.setProgress(mIndex);
				String name=(String) msg.obj;
				tv_name.setText(name);
				break;
			case FINASH_CACHE_APP:
				tv_name.setText("ɨ�����");
				break;
			case CLEAN_FINISH:
				ll_casch.removeAllViews();
				break;
			}
    	   };
    };
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cache_clear);
			
		initUI();
		// ���ݳ�ʼ��
		initData();

	}

	private void initData(){
		  new Thread(){
			  
			public void run(){
				  //1.��ȡ���Ĺ����߶���
				  mPm = getPackageManager();
				  //2.��ȡ�ֻ������е�Ӧ��
				  List<PackageInfo> packages = mPm.getInstalledPackages(0);
				  //3.���������������ֵ
				  pb_bar.setMax(packages.size());
				  //4.��������Ӧ�û�ȡ,��ȡ���е����õĻ�����Ϣ
				  for (PackageInfo info : packages){
					  String packagename = info.packageName;
					  //�鿴Ӧ�û����cache
					  getPackageCach(packagename);
					  
					  try {
						 //���������ʱʱ��
						Thread.sleep(100+new Random().nextInt(50));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					mIndex++;
					Message msg = Message.obtain();
					msg.what=NEW_CACHE_APP;
					String name=null;
					try {
						name=mPm.getApplicationInfo(packagename, 0).loadLabel(mPm).toString();
					} catch (NameNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					msg.obj=name;
					mHandler.sendMessage(msg);
				}
				  //ɨ��������͵�handler
				  Message msg = Message.obtain();
				  msg.what=FINASH_CACHE_APP;
				  mHandler.sendMessage(msg);
			  };
		  }.start();
	}

	public void getPackageCach(String packageName){
	 
	 IPackageStatsObserver.Stub mStatsObserver = new IPackageStatsObserver.Stub(){
		@Override
		public void onGetStatsCompleted(PackageStats pStats, boolean succeeded)
				throws RemoteException {
			//���߳��з���,�õ���Ϣ����
			//4.��ȡָ�������Ļ����С
			long cacheSize = pStats.cacheSize;
			if(cacheSize>0){
			   //6.��֪���̸߳���UI
				Message msg = Message.obtain();
				msg.what=UPDATE_CACHE_APP;
				msg.obj=null;
				CacheInfo cacheInfo = new CacheInfo();
				cacheInfo.cacheSize=cacheSize;
				cacheInfo.packageName=pStats.packageName;
				try {
					cacheInfo.icon=mPm.getApplicationInfo(pStats.packageName, 0).loadIcon(mPm);
					cacheInfo.name=mPm.getApplicationInfo(pStats.packageName,0).loadLabel(mPm).toString();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				msg.obj=cacheInfo;
				mHandler.sendMessage(msg);	
						
			}
		}
	 };
	 
	try {
		//1.��ȡָ������ֽ����ļ�
		Class<?> clazz = Class.forName("android.content.pm.PackageManager");
		 //2.��ȡ���÷����Ķ���
		 Method method = clazz.getMethod("getPackageSizeInfo", String.class,IPackageStatsObserver.class);
		 //3.��ȡ������÷���
		 method.invoke(mPm, packageName,mStatsObserver);
	} catch (Exception e){
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
 }

	class CacheInfo {
		public String name;
		public String packageName;
		public Drawable icon;
		public long cacheSize;
	}

	private void initUI(){
		pb_bar = (ProgressBar) findViewById(R.id.pb_bar);
		tv_name = (TextView) findViewById(R.id.tv_name);
		ll_casch = (LinearLayout) findViewById(R.id.ll_casch);
		bt_clear = (Button) findViewById(R.id.bt_clear);
		
		bt_clear.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				try {
					Class<?> clazz = Class.forName("android.content.pm.PackageManager");
					//2.��ȡ���÷�������
					Method method = clazz.getMethod("freeStorageAndNotify", long.class,IPackageDataObserver.class);
					//3.��ȡ������÷���
					method.invoke(mPm, Long.MAX_VALUE,new IPackageDataObserver.Stub() {
						@Override
						public void onRemoveCompleted(String packageName, boolean succeeded)
								throws RemoteException {
							//���������ɺ���õķ���(����Ȩ��)
							Message msg = Message.obtain();
							msg.what = CLEAN_FINISH;
							mHandler.sendMessage(msg);
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}
}
