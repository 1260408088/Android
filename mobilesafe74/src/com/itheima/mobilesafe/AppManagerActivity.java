package com.itheima.mobilesafe;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import com.itheima.mobilesafe.bean.AppInfo;
import com.itheima.mobilesafe.engine.AppInfoProvider;
import com.lidroid.xutils.view.annotation.event.OnItemClick;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class AppManagerActivity extends Activity implements OnClickListener {
	 private List<AppInfo> appInfolist;
	 private AppInfo mAppinfo;
	 private ListView lv_app;
	 private TextView tv_desc;
	 private List<AppInfo> mSystemApp;
	 private List<AppInfo> mCustomerApp;
	 private ViewHolder holder;
	 private ViewHolderHolder viewHolderHolder;
	 private Handler mHandler=new Handler(){
		 private Myadapter myadapter;

		public void handleMessage(android.os.Message msg) {
			 myadapter = new Myadapter();
			 lv_app.setAdapter(myadapter);
			 
			 if(tv_desc!=null && mCustomerApp!=null){
					tv_desc.setText("用户应用("+mCustomerApp.size()+")");
				}
		 };
	 };
	private PopupWindow popupWindow;
	
	 //数据适配器
     class Myadapter extends BaseAdapter{
        
		//获取数据适配器中条目的总数，修改成两种（纯文本，图片+文字）
		@Override
		public int getViewTypeCount() {
			return super.getViewTypeCount()+1;
		}
		
		//指定索引指向的条目类型，条目类型状态码指定（0（复用系统）1）
        @Override
        public int getItemViewType(int position) {
            if(position==0||position==mCustomerApp.size()+1){
               //返回0，代表纯文本条目的状态码
            	return 0;
            }else{
            	//返回1，代表图片+文本条目状态码
            	return 1;
            }
        	
        }
		@Override
		public int getCount() {
			//listView中添加了两个灰色条目，所以加2
			return mSystemApp.size()+mCustomerApp.size()+2;
		}

		@Override
		public AppInfo getItem(int position) {
			//如果位置 为用户应用的第一个和系统应用的第一个，返回为null，不展示条目，显示灰色条目
			if(position==0||position==mCustomerApp.size()+1){
				return null;
			}else{
			if(position<mCustomerApp.size()+1){
			     return mCustomerApp.get(position-1);
			}else{
				 return mSystemApp.get(position-mCustomerApp.size()-2);
			}
				  }
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			int type=getItemViewType(position);
			//判断状态
			if(type==0){
				//展示灰色纯文本条目
				if(convertView==null){
					
					convertView=View.inflate(getApplicationContext(), R.layout.listview_app_item_title, null);
					//优化代码
					viewHolderHolder = new ViewHolderHolder();
					viewHolderHolder.tv_title=(TextView) convertView.findViewById(R.id.tv_title);
					
					//非常重要
					convertView.setTag(viewHolderHolder);
				}else{
					viewHolderHolder=(ViewHolderHolder) convertView.getTag();
				}if(position == 0){
					viewHolderHolder.tv_title.setText("用户应用("+mCustomerApp.size()+")");
				}else{
					viewHolderHolder.tv_title.setText("系统应用("+mSystemApp.size()+")");
				}
				
				return convertView;
			}
			else{
				if(convertView==null){
					
					convertView=View.inflate(getApplicationContext(), R.layout.listview_app_view, null);
					//优化代码
					holder = new ViewHolder();
					holder.iv_icon=(ImageView) convertView.findViewById(R.id.iv_icon);
					holder.tv_name=(TextView) convertView.findViewById(R.id.tv_name);
					holder.tv_local=(TextView) convertView.findViewById(R.id.tv_local);
					//非常重要
					convertView.setTag(holder);
				}else{
					holder=(ViewHolder) convertView.getTag();
				}
				
				holder.iv_icon.setBackgroundDrawable(getItem(position).icon);
				holder.tv_name.setText("应用名称:"+getItem(position).name);
				if(getItem(position).isSdCard){
					holder.tv_local.setText("sd卡应用");
				}else{
					holder.tv_local.setText("手机应用");
				}
				return convertView;
				}
			}
			
			
    	 
     }
     static class ViewHolder{
 		ImageView iv_icon;
 		TextView  tv_name;
 		TextView tv_local;
 	}
     
     static class ViewHolderHolder{
    	 TextView tv_title;
     }
     
     
     @Override
    protected void onCreate(Bundle savedInstanceState) {
    	setContentView(R.layout.activity_app_manager);
    	super.onCreate(savedInstanceState);
    	
    	initTitle();  
    	initList();
    }
     //应用卸载完毕，用于回显界面重新获取焦点
     @Override
    protected void onResume() {
    	getData();
    	super.onResume();
    }
     
	private void getData() {
		//获取app的相关信息
 		 new Thread(){
			public void run(){
 				appInfolist = new AppInfoProvider().getAppInfoList(getApplicationContext());
 				mSystemApp = new ArrayList<AppInfo>();
 				mCustomerApp = new ArrayList<AppInfo>();
 				for (AppInfo appinfo : appInfolist) {
					 if(appinfo.isSystem){
						 //系统应用
						 mSystemApp.add(appinfo);
					 }else{
						 //用户应用
						 mCustomerApp.add(appinfo);
					 }
					 
				}
 				mHandler.sendEmptyMessage(0);
 			 };
 		 }.start();
	}

	/**
	 * 初始化listView
	 */
	private void initList() {
			//找到控件
		     tv_desc = (TextView) findViewById(R.id.tv_desc);
     		 lv_app = (ListView) findViewById(R.id.lv_app);
     		 //获取app的相关信息
     		 new Thread(){
				public void run(){
     				appInfolist = new AppInfoProvider().getAppInfoList(getApplicationContext());
     				mSystemApp = new ArrayList<AppInfo>();
     				mCustomerApp = new ArrayList<AppInfo>();
     				for (AppInfo appinfo : appInfolist) {
						 if(appinfo.isSystem){
							 //系统应用
							 mSystemApp.add(appinfo);
						 }else{
							 //用户应用
							 mCustomerApp.add(appinfo);
						 }
						 
					}
     				mHandler.sendEmptyMessage(0);
     			 };
     		 }.start();
     		 lv_app.setOnScrollListener(new OnScrollListener() {
				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) {
					
				}
				@Override
				public void onScroll(AbsListView view, int firstVisibleItem,
						int visibleItemCount, int totalItemCount) {
					    //滚动过程中调用方法
					    //AbsListView中view对象就是listview对象
					    //firstVisibleItem是第一个可见条目索引值
					    //visibleItemCount当前一个屏幕的可见条目数
						//总条目数
					if(mCustomerApp!=null&&mSystemApp!=null){
					    if(firstVisibleItem>=mCustomerApp.size()+1){
					        //滚动到了系统条目
					    	tv_desc.setText("系统应用("+mSystemApp.size() +")");
					    }else{
					    	tv_desc.setText("用户应用("+mCustomerApp.size() +")");
					    }
					}
				}
			});
     		 
		lv_app.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long id) {
				// 如果位置 为用户应用的第一个和系统应用的第一个，返回为null，不展示条目，显示灰色条目
				if (position == 0 || position == mCustomerApp.size() + 1) {
					return;
				} else {
					if (position < mCustomerApp.size() + 1) {
						mAppinfo = mCustomerApp.get(position - 1);
					} else {
						mAppinfo = mSystemApp.get(position - mCustomerApp.size()
								- 2);
					}
					showpopuwindow(view);
				}

			}

		});
	}
    
	private void showpopuwindow(View view) {
		//转化成view
		View popuview = View.inflate(getApplicationContext(),
				R.layout.popupwindow_layout, null);
		//找到控件
		TextView rv_uninstall = (TextView) popuview
				.findViewById(R.id.rv_uninstall);
		TextView rv_start = (TextView) popuview.findViewById(R.id.rv_start);
		TextView rv_share = (TextView) popuview.findViewById(R.id.rv_share);
		//添加点击事件
		rv_uninstall.setOnClickListener(this);
		rv_start.setOnClickListener(this);
		rv_share.setOnClickListener(this);
		
		//透明的动画（透明--->不透明）
		AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
		alphaAnimation.setDuration(1000);
		alphaAnimation.setFillAfter(true);
		
		//缩放动画（从无到有，从自身坐标到x与y的一半大小）
		ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		scaleAnimation.setDuration(1000);
		scaleAnimation.setFillAfter(true);
		//设置动画集合
		AnimationSet set = new AnimationSet(true);
		//添加两个动画
		set.addAnimation(alphaAnimation);
		set.addAnimation(scaleAnimation);
		
		//1创建窗体对象，指定宽高
		popupWindow = new PopupWindow(popuview,LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT,true);
		//2设置一个透明的背景（new colorDrawable()）
		popupWindow.setBackgroundDrawable(new ColorDrawable());
		//3,指定窗体位置(向右50，向上一个条目的高度);
		popupWindow.showAsDropDown(view, 50, -view.getHeight()/2-20);
		//开始一个动画
		popuview.startAnimation(set);
		
	}
	private void initTitle() {
		//获取磁盘路径
		String path = Environment.getDataDirectory().getAbsolutePath();
		//获取sd卡路径
		String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
		//获取路径下文件夹剩余大小
		String pathSize = Formatter.formatFileSize(this, getAvailSpace(path));          
		String sdPathSize = Formatter.formatFileSize(this,  getAvailSpace(sdPath));
		//找到控件
		TextView tv_path = (TextView) findViewById(R.id.tv_path);
		TextView tv_cdpath = (TextView)findViewById(R.id.tv_cdpath);
		tv_path.setText("磁盘可用："+pathSize);
		tv_cdpath.setText("sd卡可用:"+sdPathSize);
	}

    
	/**返回结果为byte/1024/1024=MB
	 * @param path
	 * @return
	 */
	private long getAvailSpace(String path) {
		//获取磁盘可用大小类
		StatFs statFs = new StatFs(path);
		//获取可用区块的个数
		int count = statFs.getAvailableBlocks();
		//获取我每个区块的大小
		int size = statFs.getBlockSize();
		return  count*size;
	}

	@Override
	public void onClick(View v) {
          		switch (v.getId()) {
				case R.id.rv_uninstall:
					if(mAppinfo.isSystem){
						Toastutils.show(getApplicationContext(), "系统应用无法删除");
					}else{
				    //查看源码，调用删除应用界面
					Intent intent = new Intent("android.intent.action.DELETE");
					intent.addCategory("android.intent.category.DEFAULT");
					intent.setData(Uri.parse("package:"+mAppinfo.packageName));
					startActivity(intent);
					}
					break;
					
				case R.id.rv_start:
					//通过桌面去获得程序包名
					PackageManager pm = getPackageManager();
					//通过luanch去开启定制包名的意图，再去开启应用
					Intent Lanchintent = pm.getLaunchIntentForPackage(mAppinfo.packageName);
					if(Lanchintent!=null){
						startActivity(Lanchintent);
					}else{
						Toastutils.show(getApplicationContext(), "此应用无法被开启 ");
					}
					break;

				case R.id.rv_share:
	                Intent intent = new Intent("android.intent.action.SEND");
	                intent.putExtra(Intent.EXTRA_TEXT, "分享一个应用"+mAppinfo.name);
	                intent.setType("text/plain");
	                startActivity(intent);
					break;
				}
          		popupWindow.dismiss();
	}
}
