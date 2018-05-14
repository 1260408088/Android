package com.itheima.mobilesafe;

import java.util.ArrayList;
import java.util.List;

import com.itheima.mobilesafe.bean.AppInfo;
import com.itheima.mobilesafe.dao.AppLockDao;
import com.itheima.mobilesafe.dao.BlackNumberDao;
import com.itheima.mobilesafe.db.AppLockOpenHelper;
import com.itheima.mobilesafe.engine.AppInfoProvider;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.InflateException;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class LockAppActivity extends Activity {
	
    private Button bt_unclock,bt_clock;
	private TextView tv_unlock,tv_lock;
	private LinearLayout ll_uncolk_app,ll_colk_app;
	private ListView lv_unlock,lv_lock;
	private List<AppInfo> appInfoList;
	private List<AppInfo> mLockAppList;
	private List<AppInfo> mULockAppList;
	private AppLockDao dao;
	private Myadapter unadapter;
	private Myadapter lockadapter;
    private Handler mHandler=new Handler(){
    	

		@Override
    	public void handleMessage(Message msg) {
    		//Ϊδ��������adapter
    		unadapter = new Myadapter(false);
    		lv_unlock.setAdapter(unadapter);
    		//Ϊ����Ӧ������adapter
    		lockadapter = new Myadapter(true);
    		lv_lock.setAdapter(lockadapter);
    		super.handleMessage(msg);
    	}
    };
	private TranslateAnimation mTranslateAnimation;
    
    class Myadapter extends BaseAdapter{
    	private boolean islock;
		private Hodler hodler;
    	/**
		 * @param isLock	���������Ѽ�����δ����Ӧ�õı�ʾ	true�Ѽ�������������	falseδ��������������
		 */
        public Myadapter(boolean islock){
        	this.islock=islock;
        }
		@Override
		public int getCount() {
			if(islock){
				//���� textview��ʾ
				tv_lock.setText("�Լ���Ӧ��"+mLockAppList.size());
				return mLockAppList.size(); 
			}else{
				tv_unlock.setText("δ����Ӧ��"+mULockAppList.size());
				return mULockAppList.size();
			}
			
		}

		@Override
		public AppInfo getItem(int position) {
			if(islock){
				
				return mLockAppList.get(position);
			}else{
				return mULockAppList.get(position);
			}
			
		}

		@Override
		public long getItemId(int position) {
			
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent){
			//adapter���Ż�
			if(convertView==null){
				convertView=View.inflate(getApplicationContext(), R.layout.listview_islock_item, null);
				hodler = new Hodler();
				hodler.iv_icon=(ImageView) convertView.findViewById(R.id.iv_icon);
				hodler.tv_name= (TextView) convertView.findViewById(R.id.tv_name);
			    hodler.iv_lock=	(ImageView) convertView.findViewById(R.id.iv_lock);
			    convertView.setTag(hodler);
			}else{
				 hodler = (Hodler) convertView.getTag();
			}
			final AppInfo appInfo = getItem(position);
			final View animview=convertView;//���ڶ�����ʾ��������������һƬ�����ռ�
			hodler.iv_icon.setBackgroundDrawable(appInfo.icon);
			hodler.tv_name.setText(appInfo.name);
			
			if(islock){
				hodler.iv_lock.setBackgroundResource(R.drawable.lock);
			}else{
				hodler.iv_lock.setBackgroundResource(R.drawable.unlock);
			}
			
			hodler.iv_lock.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					//��ʼ������ Ϊ�������ü�����������ֵ������Ŀ����һ����Ŀִ�ж������Ƴ���Ŀ�Ͷ���ͬ��ִ�У�
					animview.startAnimation(mTranslateAnimation);
						mTranslateAnimation.setAnimationListener(new AnimationListener() {
							
							@Override
							public void onAnimationStart(Animation animation) {
								//������ʼʱ���õķ���
							}
							
							@Override
							public void onAnimationRepeat(Animation animation) {
								//�����ظ�ʱ���õķ���
							}
							
							@Override
							public void onAnimationEnd(Animation animation) {
								//��������ʱ���õķ���
								if(islock){
									//�Լ���Ӧ����ӵ����ݿ���Լ�����listview
									mULockAppList.add(appInfo);
									mLockAppList.remove(appInfo);
									dao.del(appInfo.packageName);
									lockadapter.notifyDataSetChanged();
									
								}else{
									//Ϊ����Ӧ�ô�listview�Ƴ������Ҵ����ݿ�ɾ��
									mULockAppList.remove(appInfo);
									mLockAppList.add(appInfo);
									dao.insert(appInfo.packageName);
									unadapter.notifyDataSetChanged();
								}
							}
						});
					
					
					
				}
			});
			
			
			return convertView;
		}
		
    }
    class Hodler{
		ImageView iv_icon;
		TextView tv_name;
		ImageView iv_lock;
	}
	@Override
    protected void onCreate(Bundle savedInstanceState) {
    	setContentView(R.layout.activity_clock_app);
    	super.onCreate(savedInstanceState);
    	//��ʼ������
    	intiUI();
    	ininData();
    	initAnnimation();
    }

	/**
	 * ��ʼ��ƽ�ƶ���(x��ƽ������Ŀ��)
	 */
	private void initAnnimation() {
		    
            mTranslateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0,//�������x��0��λ�õ�
            			Animation.RELATIVE_TO_SELF, 1, //�������x�������Ŀ�ȵ�λ��
          				Animation.RELATIVE_TO_SELF, 0, //�������y�� 0��λ�õ�
          				Animation.RELATIVE_TO_SELF, 0);
            mTranslateAnimation.setDuration(500);
            
	}

	private void ininData() {
		//1��ȡ��ǰ����Ӧ�õ������Ϣ
		appInfoList = AppInfoProvider.getAppInfoList(getApplicationContext());
		//2.�����Լ�����δ�����ļ���
		mLockAppList = new ArrayList<AppInfo>();
		mULockAppList = new ArrayList<AppInfo>();
		//3.�����Լ�����Ϊδ���������ļ���
		dao = AppLockDao.getinstance(getApplicationContext());
		List<String> findall = dao.findall();
		for (AppInfo info : appInfoList) {
			 if(findall.contains(info.packageName)){
				 
				 mLockAppList.add(info);
			 }else{
				 mULockAppList.add(info);
			 }
			 
		}
		//4���Ϳ���Ϣ��ʾlistview����
		mHandler.sendEmptyMessage(0);
	}

	private void intiUI(){
		bt_unclock = (Button) findViewById(R.id.bt_unclock);
		bt_clock = (Button) findViewById(R.id.bt_clock);
		
		tv_unlock = (TextView) findViewById(R.id.tv_unlock);
		tv_lock = (TextView) findViewById(R.id.tv_lock);
		
		ll_uncolk_app = (LinearLayout) findViewById(R.id.ll_uncolk_app);
		ll_colk_app = (LinearLayout) findViewById(R.id.ll_colk_app);
		
		lv_unlock = (ListView) findViewById(R.id.lv_unlock);
		lv_lock = (ListView) findViewById(R.id.lv_lock);
		
		bt_unclock.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//1.�Լ����б���ʾ��δ�����б�����
				ll_colk_app.setVisibility(View.GONE);
				ll_uncolk_app.setVisibility(View.VISIBLE);
				//2.��ť���л�
				bt_clock.setBackgroundResource(R.drawable.tab_right_default);
				bt_unclock.setBackgroundResource(R.drawable.tab_left_pressed);
			}
		});
		bt_clock.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//1.�Լ����б���ʾ��δ�����б�����
				ll_uncolk_app.setVisibility(View.GONE);
				ll_colk_app.setVisibility(View.VISIBLE);
				//2.��ť���л�
				bt_unclock.setBackgroundResource(R.drawable.tab_left_default);
				bt_clock.setBackgroundResource(R.drawable.tab_right_pressed);
			}
		});
	}
}
