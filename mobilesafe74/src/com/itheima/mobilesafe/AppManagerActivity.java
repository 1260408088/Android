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
					tv_desc.setText("�û�Ӧ��("+mCustomerApp.size()+")");
				}
		 };
	 };
	private PopupWindow popupWindow;
	
	 //����������
     class Myadapter extends BaseAdapter{
        
		//��ȡ��������������Ŀ���������޸ĳ����֣����ı���ͼƬ+���֣�
		@Override
		public int getViewTypeCount() {
			return super.getViewTypeCount()+1;
		}
		
		//ָ������ָ�����Ŀ���ͣ���Ŀ����״̬��ָ����0������ϵͳ��1��
        @Override
        public int getItemViewType(int position) {
            if(position==0||position==mCustomerApp.size()+1){
               //����0�������ı���Ŀ��״̬��
            	return 0;
            }else{
            	//����1������ͼƬ+�ı���Ŀ״̬��
            	return 1;
            }
        	
        }
		@Override
		public int getCount() {
			//listView�������������ɫ��Ŀ�����Լ�2
			return mSystemApp.size()+mCustomerApp.size()+2;
		}

		@Override
		public AppInfo getItem(int position) {
			//���λ�� Ϊ�û�Ӧ�õĵ�һ����ϵͳӦ�õĵ�һ��������Ϊnull����չʾ��Ŀ����ʾ��ɫ��Ŀ
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
			//�ж�״̬
			if(type==0){
				//չʾ��ɫ���ı���Ŀ
				if(convertView==null){
					
					convertView=View.inflate(getApplicationContext(), R.layout.listview_app_item_title, null);
					//�Ż�����
					viewHolderHolder = new ViewHolderHolder();
					viewHolderHolder.tv_title=(TextView) convertView.findViewById(R.id.tv_title);
					
					//�ǳ���Ҫ
					convertView.setTag(viewHolderHolder);
				}else{
					viewHolderHolder=(ViewHolderHolder) convertView.getTag();
				}if(position == 0){
					viewHolderHolder.tv_title.setText("�û�Ӧ��("+mCustomerApp.size()+")");
				}else{
					viewHolderHolder.tv_title.setText("ϵͳӦ��("+mSystemApp.size()+")");
				}
				
				return convertView;
			}
			else{
				if(convertView==null){
					
					convertView=View.inflate(getApplicationContext(), R.layout.listview_app_view, null);
					//�Ż�����
					holder = new ViewHolder();
					holder.iv_icon=(ImageView) convertView.findViewById(R.id.iv_icon);
					holder.tv_name=(TextView) convertView.findViewById(R.id.tv_name);
					holder.tv_local=(TextView) convertView.findViewById(R.id.tv_local);
					//�ǳ���Ҫ
					convertView.setTag(holder);
				}else{
					holder=(ViewHolder) convertView.getTag();
				}
				
				holder.iv_icon.setBackgroundDrawable(getItem(position).icon);
				holder.tv_name.setText("Ӧ������:"+getItem(position).name);
				if(getItem(position).isSdCard){
					holder.tv_local.setText("sd��Ӧ��");
				}else{
					holder.tv_local.setText("�ֻ�Ӧ��");
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
     //Ӧ��ж����ϣ����ڻ��Խ������»�ȡ����
     @Override
    protected void onResume() {
    	getData();
    	super.onResume();
    }
     
	private void getData() {
		//��ȡapp�������Ϣ
 		 new Thread(){
			public void run(){
 				appInfolist = new AppInfoProvider().getAppInfoList(getApplicationContext());
 				mSystemApp = new ArrayList<AppInfo>();
 				mCustomerApp = new ArrayList<AppInfo>();
 				for (AppInfo appinfo : appInfolist) {
					 if(appinfo.isSystem){
						 //ϵͳӦ��
						 mSystemApp.add(appinfo);
					 }else{
						 //�û�Ӧ��
						 mCustomerApp.add(appinfo);
					 }
					 
				}
 				mHandler.sendEmptyMessage(0);
 			 };
 		 }.start();
	}

	/**
	 * ��ʼ��listView
	 */
	private void initList() {
			//�ҵ��ؼ�
		     tv_desc = (TextView) findViewById(R.id.tv_desc);
     		 lv_app = (ListView) findViewById(R.id.lv_app);
     		 //��ȡapp�������Ϣ
     		 new Thread(){
				public void run(){
     				appInfolist = new AppInfoProvider().getAppInfoList(getApplicationContext());
     				mSystemApp = new ArrayList<AppInfo>();
     				mCustomerApp = new ArrayList<AppInfo>();
     				for (AppInfo appinfo : appInfolist) {
						 if(appinfo.isSystem){
							 //ϵͳӦ��
							 mSystemApp.add(appinfo);
						 }else{
							 //�û�Ӧ��
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
					    //���������е��÷���
					    //AbsListView��view�������listview����
					    //firstVisibleItem�ǵ�һ���ɼ���Ŀ����ֵ
					    //visibleItemCount��ǰһ����Ļ�Ŀɼ���Ŀ��
						//����Ŀ��
					if(mCustomerApp!=null&&mSystemApp!=null){
					    if(firstVisibleItem>=mCustomerApp.size()+1){
					        //��������ϵͳ��Ŀ
					    	tv_desc.setText("ϵͳӦ��("+mSystemApp.size() +")");
					    }else{
					    	tv_desc.setText("�û�Ӧ��("+mCustomerApp.size() +")");
					    }
					}
				}
			});
     		 
		lv_app.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long id) {
				// ���λ�� Ϊ�û�Ӧ�õĵ�һ����ϵͳӦ�õĵ�һ��������Ϊnull����չʾ��Ŀ����ʾ��ɫ��Ŀ
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
		//ת����view
		View popuview = View.inflate(getApplicationContext(),
				R.layout.popupwindow_layout, null);
		//�ҵ��ؼ�
		TextView rv_uninstall = (TextView) popuview
				.findViewById(R.id.rv_uninstall);
		TextView rv_start = (TextView) popuview.findViewById(R.id.rv_start);
		TextView rv_share = (TextView) popuview.findViewById(R.id.rv_share);
		//��ӵ���¼�
		rv_uninstall.setOnClickListener(this);
		rv_start.setOnClickListener(this);
		rv_share.setOnClickListener(this);
		
		//͸���Ķ�����͸��--->��͸����
		AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
		alphaAnimation.setDuration(1000);
		alphaAnimation.setFillAfter(true);
		
		//���Ŷ��������޵��У����������굽x��y��һ���С��
		ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		scaleAnimation.setDuration(1000);
		scaleAnimation.setFillAfter(true);
		//���ö�������
		AnimationSet set = new AnimationSet(true);
		//�����������
		set.addAnimation(alphaAnimation);
		set.addAnimation(scaleAnimation);
		
		//1�����������ָ�����
		popupWindow = new PopupWindow(popuview,LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT,true);
		//2����һ��͸���ı�����new colorDrawable()��
		popupWindow.setBackgroundDrawable(new ColorDrawable());
		//3,ָ������λ��(����50������һ����Ŀ�ĸ߶�);
		popupWindow.showAsDropDown(view, 50, -view.getHeight()/2-20);
		//��ʼһ������
		popuview.startAnimation(set);
		
	}
	private void initTitle() {
		//��ȡ����·��
		String path = Environment.getDataDirectory().getAbsolutePath();
		//��ȡsd��·��
		String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
		//��ȡ·�����ļ���ʣ���С
		String pathSize = Formatter.formatFileSize(this, getAvailSpace(path));          
		String sdPathSize = Formatter.formatFileSize(this,  getAvailSpace(sdPath));
		//�ҵ��ؼ�
		TextView tv_path = (TextView) findViewById(R.id.tv_path);
		TextView tv_cdpath = (TextView)findViewById(R.id.tv_cdpath);
		tv_path.setText("���̿��ã�"+pathSize);
		tv_cdpath.setText("sd������:"+sdPathSize);
	}

    
	/**���ؽ��Ϊbyte/1024/1024=MB
	 * @param path
	 * @return
	 */
	private long getAvailSpace(String path) {
		//��ȡ���̿��ô�С��
		StatFs statFs = new StatFs(path);
		//��ȡ��������ĸ���
		int count = statFs.getAvailableBlocks();
		//��ȡ��ÿ������Ĵ�С
		int size = statFs.getBlockSize();
		return  count*size;
	}

	@Override
	public void onClick(View v) {
          		switch (v.getId()) {
				case R.id.rv_uninstall:
					if(mAppinfo.isSystem){
						Toastutils.show(getApplicationContext(), "ϵͳӦ���޷�ɾ��");
					}else{
				    //�鿴Դ�룬����ɾ��Ӧ�ý���
					Intent intent = new Intent("android.intent.action.DELETE");
					intent.addCategory("android.intent.category.DEFAULT");
					intent.setData(Uri.parse("package:"+mAppinfo.packageName));
					startActivity(intent);
					}
					break;
					
				case R.id.rv_start:
					//ͨ������ȥ��ó������
					PackageManager pm = getPackageManager();
					//ͨ��luanchȥ�������ư�������ͼ����ȥ����Ӧ��
					Intent Lanchintent = pm.getLaunchIntentForPackage(mAppinfo.packageName);
					if(Lanchintent!=null){
						startActivity(Lanchintent);
					}else{
						Toastutils.show(getApplicationContext(), "��Ӧ���޷������� ");
					}
					break;

				case R.id.rv_share:
	                Intent intent = new Intent("android.intent.action.SEND");
	                intent.putExtra(Intent.EXTRA_TEXT, "����һ��Ӧ��"+mAppinfo.name);
	                intent.setType("text/plain");
	                startActivity(intent);
					break;
				}
          		popupWindow.dismiss();
	}
}
