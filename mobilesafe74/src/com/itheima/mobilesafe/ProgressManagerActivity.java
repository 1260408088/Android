package com.itheima.mobilesafe;

import java.util.ArrayList;
import java.util.List;

import com.itheima.mobilesafe.bean.ProcessInfo;
import com.itheima.mobilesafe.engine.ProgressInfoProvider;
import com.itheima.mobilesafe.utils.SpUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ProgressManagerActivity extends Activity implements OnClickListener{
      
	private TextView tv_progress,tv_total;
	private Button bt_all ;
	private Button bt_unall;
	private Button bt_clean;
	private Button bt_set;
	
	private ListView dia_play;
	private List<ProcessInfo> mCustomerpro;
	private List<ProcessInfo> mSystemrpro;
	private List<ProcessInfo> processInfo;
	private ViewHolder viewHolder;
	private TextView tv_dec;
	private ProcessInfo mProcessInfo;
	private Myadapter myadapter;
	private int progressCount;
	private long totalsoace=0;
	private Handler mHandler=new Handler(){
		

		public void handleMessage(android.os.Message msg) {
			myadapter = new Myadapter();
			dia_play.setAdapter(myadapter);
			if(tv_dec!=null&&mCustomerpro!=null){
			tv_dec.setText("用户进程：(" + mCustomerpro.size() + ")");
			}
		};
	};
	
	private long availspace;
	private long totalspace;
	private String strtotalspace;
	
	
    
	
	public class Myadapter extends BaseAdapter{
		

		private viewHolderview holderview;

		//获取数据适配器中条目的类型，修改成两种（纯文本，图片+文字）
		@Override
		public int getViewTypeCount() {
			return super.getViewTypeCount()+1;
		}
		
		//指定索引指向的条目类型，条目类型状态码指定（0（复用系统）1）
        @Override
        public int getItemViewType(int position) {
            if(position==0||position==mCustomerpro.size()+1){
               //返回0，代表纯文本条目的状态码
            	return 0;
            }else{
            	//返回1，代表图片+文本条目状态码
            	return 1;
            }
        	
        }
		
		@Override
		public int getCount() {
			//根据是否显示系统应用，来判断返回多少数据条目！
			if(SpUtil.getBoolean(getApplicationContext(), ConstantValue.SHOW_SYSTEM, false)){
				return mCustomerpro.size()+1;
			}else{
				return mCustomerpro.size()+mSystemrpro.size()+2;
			}
		}
		@Override
		public ProcessInfo getItem(int position) {
			if(position==0||position==mCustomerpro.size()+1){
				return null;
			}else{
				if(position<mCustomerpro.size()+1){
					return mCustomerpro.get(position-1);
				}else{
					return mSystemrpro.get(position-mCustomerpro.size()-2);
				}
			}
			
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			//获取类型
			int type = getItemViewType(position);
			//判断类型为纯文本还是 图片加文字类型
			if(type==0){
				if(convertView==null){
					convertView = View.inflate(getApplicationContext(), R.layout.listview_progress_item, null);
					viewHolder = new ViewHolder();
					viewHolder.tv_pro_title = (TextView)convertView.findViewById(R.id.tv_pro_title);
					//非常重要··
					convertView.setTag(viewHolder);
				}else{
					viewHolder = (ViewHolder) convertView.getTag();
				}
					if(position==0){
						viewHolder.tv_pro_title.setText("应用进程("+mCustomerpro.size()+")");	
					}else{
						viewHolder.tv_pro_title.setText("系统进程("+mSystemrpro.size()+")");	
					}
				
				
				return convertView;	
			}else{
				if(convertView==null){
					//加载图片文字 
					convertView=View.inflate(getApplicationContext(), R.layout.listview_progres_det_view, null);
					holderview = new viewHolderview();
					holderview.iv_icons=(ImageView) convertView.findViewById(R.id.iv_icons);
					holderview.tv_name=(TextView) convertView.findViewById(R.id.tv_name);
					holderview.tv_dirty=(TextView) convertView.findViewById(R.id.tv_dirty);
					holderview.cb_select=(CheckBox) convertView.findViewById(R.id.cb_select);
					convertView.setTag(holderview);
				}else{
					holderview=(viewHolderview) convertView.getTag();
				}
				holderview.iv_icons.setBackgroundDrawable(getItem(position).icon);
				holderview.tv_name.setText(getItem(position).name);
				long memsize = getItem(position).memsize;
				String strmemsize = Formatter.formatFileSize(getApplicationContext(), memsize);
				holderview.tv_dirty.setText("内存占用："+strmemsize);
				
				//本应用不能被选中，所以先将checkBox隐藏
				if(getItem(position).packageName.equals(getPackageName())){
					holderview.cb_select.setVisibility(View.GONE);
				}else{
					holderview.cb_select.setVisibility(View.VISIBLE);
				}
				holderview.cb_select.setChecked(getItem(position).isCheck);
				return convertView;
			}
			
			
		}
		
	}
	
	 static class ViewHolder{
		 TextView tv_pro_title;
	}
	static class  viewHolderview{
		ImageView iv_icons;
		TextView tv_name;
		TextView tv_dirty;
		CheckBox cb_select;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_progress);
		super.onCreate(savedInstanceState);
		intiUI();
		initTitalData();
		initListDate();
	}

	private void initListDate() {
           		new Thread(){
           			//获取app的相关信息
					public void run(){
           				processInfo = new ProgressInfoProvider().getProcessInfo(getApplicationContext());
           				mCustomerpro = new ArrayList<ProcessInfo>();
           				mSystemrpro = new ArrayList<ProcessInfo>();
           				for (ProcessInfo info : processInfo) {
							if(info.isSystem){
								//系统进程
								mSystemrpro.add(info);
							}else{
								//应用进程
								mCustomerpro.add(info);
							}
						}
           				mHandler.sendEmptyMessage(0);
           			};
           		}.start();
	}

	private void initTitalData() {
		progressCount = ProgressInfoProvider.getProgressCount(this);
		tv_progress.setText("进程总数："+"("+progressCount+")");
		//获取当前的可用运存和总运存
		availspace = ProgressInfoProvider.getAvailSpace(this);
		String Stravailspace = Formatter.formatFileSize(this, availspace);
		
		totalspace = ProgressInfoProvider.getTotalSpace(this);
		strtotalspace = Formatter.formatFileSize(this, totalspace);
		tv_total.setText("总数 /剩余 ："+strtotalspace+"/"+Stravailspace);
	}

	private void intiUI() {
		//获得TextView
		tv_progress = (TextView) findViewById(R.id.tv_progress);
		tv_total = (TextView) findViewById(R.id.tv_total);
		tv_dec = (TextView) findViewById(R.id.tv_dec);
		//获得listview
		dia_play = (ListView) findViewById(R.id.dia_play);
		
		bt_all = (Button) findViewById(R.id.bt_all);
		bt_unall = (Button) findViewById(R.id.bt_unall);
		bt_clean = (Button) findViewById(R.id.bt_clean);
		bt_set = (Button) findViewById(R.id.bt_set);
		
		bt_all.setOnClickListener(this);
		bt_unall.setOnClickListener(this);
		bt_clean.setOnClickListener(this);
		bt_set.setOnClickListener(this);
		
		dia_play.setOnScrollListener(new OnScrollListener() {
			//状态改变时调用
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				
			}
			//滑动时调用
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if (mCustomerpro != null && mSystemrpro != null) {
					if (firstVisibleItem > mCustomerpro.size()+1) {
						tv_dec.setText("系统进程：(" + mSystemrpro.size() + ")");
					} else {
						tv_dec.setText("用户进程：(" + mCustomerpro.size() + ")");
					}
				}
			}
		});
		
		dia_play.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(position==0||position==mCustomerpro.size()+1){
					return ;
				}else{
					if(position<mCustomerpro.size()+1){
						mProcessInfo = mCustomerpro.get(position-1);
					}else{
						mProcessInfo = mSystemrpro.get(position-mCustomerpro.size()-2);
					}
				}
				if(mProcessInfo!=null){
					if(!mProcessInfo.packageName.equals(getPackageName())){
						mProcessInfo.isCheck=!mProcessInfo.isCheck;
						CheckBox cb_select = (CheckBox) view.findViewById(R.id.cb_select);
						cb_select.setChecked(mProcessInfo.isCheck);
					}
				}
				
			}
			
		});
	}


	/**
	 * 全选，将两个mCustomerpro和mSystemrpro遍历将ischeck都设置为true
	 */
	private void selectAll(){
		for (ProcessInfo info : mCustomerpro) {
			if(info.packageName.equals(getPackageName())){
				continue ;
			}
			   info.isCheck=true;
		
		}
		for (ProcessInfo info : mSystemrpro) {
			info.isCheck=true;
		}
		if(myadapter!=null){
		   myadapter.notifyDataSetChanged();
		}
	}
    
	/**
	 * 反选
	 */
	private void selectReverse() {
		for (ProcessInfo info : mCustomerpro) {
			if(info.packageName.equals(getPackageName())){
				continue ;
			}
			   info.isCheck=!info.isCheck;
		
		}
		for (ProcessInfo info : mSystemrpro) {
			info.isCheck=!info.isCheck;
		}
		if(myadapter!=null){
		   myadapter.notifyDataSetChanged();
		}
	}
	
	/**
	 * 一键清理
	 */
	private void clearAll() {
		List<ProcessInfo> killproInfo=new ArrayList<ProcessInfo>();
		for (ProcessInfo info : mCustomerpro){
			if(info.packageName.equals(getPackageName())){
				continue ;
			}
			//1,不能在集合循环的时候移除集合里的对象
			//2.记录要杀死的用户进程
			if(info.isCheck){
				killproInfo.add(mProcessInfo);
			}
		}
		
		for (ProcessInfo info : mSystemrpro) {
			//3.记录要杀死的系统进程
			if(info.isCheck){
				killproInfo.add(info);
			}
		}
		//4.遍历killproInfo里的内容，如果mCustomerpro和mSystemrpro中存在info的内容则移除
		totalsoace=0;
		for (ProcessInfo info : killproInfo) {
			 //判断当前进程在那个集合中，从所在集合中移除
			 if(mCustomerpro.contains(info)){
				 mCustomerpro.remove(info);
			 }
			 if(mSystemrpro.contains(info)){
				 mSystemrpro.remove(info);
			 }
			 //获取要消除的的队列中所有应用占内存的大小
			 totalsoace+= info.memsize;
			 //5,调用杀死进程的方法
			 ProgressInfoProvider.killProgress(getApplicationContext(), info);
			
		}
		progressCount=progressCount-killproInfo.size();
		
		tv_progress.setText("进程总数："+(progressCount));
		
		availspace=availspace+totalsoace;
		String stringavailspace = Formatter.formatFileSize(getApplicationContext(), availspace);
		tv_total.setText("总数 /剩余 ："+strtotalspace+"/"+stringavailspace);
		
		Toastutils.show(getApplicationContext(), "杀死了"+killproInfo.size()+"个线程，释放了"+Formatter.formatFileSize(getApplicationContext(), totalsoace));
		//6.通知更新adapter
		if(myadapter!=null){
		   myadapter.notifyDataSetChanged();
		}
		
	}

	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch (id) {
		case R.id.bt_all:
			selectAll();
			break;
		case R.id.bt_unall:
			selectReverse();
			break;
		case R.id.bt_clean:
			clearAll();
			break;
		case R.id.bt_set:
	        setting();
			break;
		}
		
	}

	private void setting() {
		//开启一个设置界面
		Intent intent = new Intent(getApplicationContext(),ProgressSetingActivity.class);
		startActivityForResult(intent, 0);
	}
	//activity关闭的时候调用此方法
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(myadapter!=null){
			myadapter.notifyDataSetChanged();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
