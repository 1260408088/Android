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
    		//为未加锁设置adapter
    		unadapter = new Myadapter(false);
    		lv_unlock.setAdapter(unadapter);
    		//为加锁应用设置adapter
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
		 * @param isLock	用于区分已加锁和未加锁应用的标示	true已加锁数据适配器	false未加锁数据适配器
		 */
        public Myadapter(boolean islock){
        	this.islock=islock;
        }
		@Override
		public int getCount() {
			if(islock){
				//设置 textview显示
				tv_lock.setText("以加锁应用"+mLockAppList.size());
				return mLockAppList.size(); 
			}else{
				tv_unlock.setText("未加锁应用"+mULockAppList.size());
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
			//adapter的优化
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
			final View animview=convertView;//用于动画显示，两个变量共用一片变量空间
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
					//开始动画并 为动画设置监听，避免出现点击此条目，下一个条目执行动画（移除条目和动画同步执行）
					animview.startAnimation(mTranslateAnimation);
						mTranslateAnimation.setAnimationListener(new AnimationListener() {
							
							@Override
							public void onAnimationStart(Animation animation) {
								//动画开始时调用的方法
							}
							
							@Override
							public void onAnimationRepeat(Animation animation) {
								//动画重复时调用的方法
							}
							
							@Override
							public void onAnimationEnd(Animation animation) {
								//动画结束时调用的方法
								if(islock){
									//以加锁应用添加到数据库和以加锁的listview
									mULockAppList.add(appInfo);
									mLockAppList.remove(appInfo);
									dao.del(appInfo.packageName);
									lockadapter.notifyDataSetChanged();
									
								}else{
									//为加锁应用从listview移除，并且从数据库删除
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
    	//初始化界面
    	intiUI();
    	ininData();
    	initAnnimation();
    }

	/**
	 * 初始化平移动画(x轴平移自身的宽度)
	 */
	private void initAnnimation() {
		    
            mTranslateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0,//从自身的x轴0的位置到
            			Animation.RELATIVE_TO_SELF, 1, //到自身的x轴的自身的宽度的位置
          				Animation.RELATIVE_TO_SELF, 0, //从自身的y轴 0的位置到
          				Animation.RELATIVE_TO_SELF, 0);
            mTranslateAnimation.setDuration(500);
            
	}

	private void ininData() {
		//1获取当前所有应用的相关信息
		appInfoList = AppInfoProvider.getAppInfoList(getApplicationContext());
		//2.创建以加锁和未加锁的集合
		mLockAppList = new ArrayList<AppInfo>();
		mULockAppList = new ArrayList<AppInfo>();
		//3.区分以加锁和为未加锁包名的集合
		dao = AppLockDao.getinstance(getApplicationContext());
		List<String> findall = dao.findall();
		for (AppInfo info : appInfoList) {
			 if(findall.contains(info.packageName)){
				 
				 mLockAppList.add(info);
			 }else{
				 mULockAppList.add(info);
			 }
			 
		}
		//4发送空消息提示listview更新
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
				//1.以加锁列表显示，未加锁列表隐藏
				ll_colk_app.setVisibility(View.GONE);
				ll_uncolk_app.setVisibility(View.VISIBLE);
				//2.按钮的切换
				bt_clock.setBackgroundResource(R.drawable.tab_right_default);
				bt_unclock.setBackgroundResource(R.drawable.tab_left_pressed);
			}
		});
		bt_clock.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//1.以加锁列表显示，未加锁列表隐藏
				ll_uncolk_app.setVisibility(View.GONE);
				ll_colk_app.setVisibility(View.VISIBLE);
				//2.按钮的切换
				bt_unclock.setBackgroundResource(R.drawable.tab_left_default);
				bt_clock.setBackgroundResource(R.drawable.tab_right_pressed);
			}
		});
	}
}
