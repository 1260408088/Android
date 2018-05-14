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
			tv_dec.setText("�û����̣�(" + mCustomerpro.size() + ")");
			}
		};
	};
	
	private long availspace;
	private long totalspace;
	private String strtotalspace;
	
	
    
	
	public class Myadapter extends BaseAdapter{
		

		private viewHolderview holderview;

		//��ȡ��������������Ŀ�����ͣ��޸ĳ����֣����ı���ͼƬ+���֣�
		@Override
		public int getViewTypeCount() {
			return super.getViewTypeCount()+1;
		}
		
		//ָ������ָ�����Ŀ���ͣ���Ŀ����״̬��ָ����0������ϵͳ��1��
        @Override
        public int getItemViewType(int position) {
            if(position==0||position==mCustomerpro.size()+1){
               //����0�������ı���Ŀ��״̬��
            	return 0;
            }else{
            	//����1������ͼƬ+�ı���Ŀ״̬��
            	return 1;
            }
        	
        }
		
		@Override
		public int getCount() {
			//�����Ƿ���ʾϵͳӦ�ã����жϷ��ض���������Ŀ��
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
			//��ȡ����
			int type = getItemViewType(position);
			//�ж�����Ϊ���ı����� ͼƬ����������
			if(type==0){
				if(convertView==null){
					convertView = View.inflate(getApplicationContext(), R.layout.listview_progress_item, null);
					viewHolder = new ViewHolder();
					viewHolder.tv_pro_title = (TextView)convertView.findViewById(R.id.tv_pro_title);
					//�ǳ���Ҫ����
					convertView.setTag(viewHolder);
				}else{
					viewHolder = (ViewHolder) convertView.getTag();
				}
					if(position==0){
						viewHolder.tv_pro_title.setText("Ӧ�ý���("+mCustomerpro.size()+")");	
					}else{
						viewHolder.tv_pro_title.setText("ϵͳ����("+mSystemrpro.size()+")");	
					}
				
				
				return convertView;	
			}else{
				if(convertView==null){
					//����ͼƬ���� 
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
				holderview.tv_dirty.setText("�ڴ�ռ�ã�"+strmemsize);
				
				//��Ӧ�ò��ܱ�ѡ�У������Ƚ�checkBox����
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
           			//��ȡapp�������Ϣ
					public void run(){
           				processInfo = new ProgressInfoProvider().getProcessInfo(getApplicationContext());
           				mCustomerpro = new ArrayList<ProcessInfo>();
           				mSystemrpro = new ArrayList<ProcessInfo>();
           				for (ProcessInfo info : processInfo) {
							if(info.isSystem){
								//ϵͳ����
								mSystemrpro.add(info);
							}else{
								//Ӧ�ý���
								mCustomerpro.add(info);
							}
						}
           				mHandler.sendEmptyMessage(0);
           			};
           		}.start();
	}

	private void initTitalData() {
		progressCount = ProgressInfoProvider.getProgressCount(this);
		tv_progress.setText("����������"+"("+progressCount+")");
		//��ȡ��ǰ�Ŀ����˴�����˴�
		availspace = ProgressInfoProvider.getAvailSpace(this);
		String Stravailspace = Formatter.formatFileSize(this, availspace);
		
		totalspace = ProgressInfoProvider.getTotalSpace(this);
		strtotalspace = Formatter.formatFileSize(this, totalspace);
		tv_total.setText("���� /ʣ�� ��"+strtotalspace+"/"+Stravailspace);
	}

	private void intiUI() {
		//���TextView
		tv_progress = (TextView) findViewById(R.id.tv_progress);
		tv_total = (TextView) findViewById(R.id.tv_total);
		tv_dec = (TextView) findViewById(R.id.tv_dec);
		//���listview
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
			//״̬�ı�ʱ����
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				
			}
			//����ʱ����
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if (mCustomerpro != null && mSystemrpro != null) {
					if (firstVisibleItem > mCustomerpro.size()+1) {
						tv_dec.setText("ϵͳ���̣�(" + mSystemrpro.size() + ")");
					} else {
						tv_dec.setText("�û����̣�(" + mCustomerpro.size() + ")");
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
	 * ȫѡ��������mCustomerpro��mSystemrpro������ischeck������Ϊtrue
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
	 * ��ѡ
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
	 * һ������
	 */
	private void clearAll() {
		List<ProcessInfo> killproInfo=new ArrayList<ProcessInfo>();
		for (ProcessInfo info : mCustomerpro){
			if(info.packageName.equals(getPackageName())){
				continue ;
			}
			//1,�����ڼ���ѭ����ʱ���Ƴ�������Ķ���
			//2.��¼Ҫɱ�����û�����
			if(info.isCheck){
				killproInfo.add(mProcessInfo);
			}
		}
		
		for (ProcessInfo info : mSystemrpro) {
			//3.��¼Ҫɱ����ϵͳ����
			if(info.isCheck){
				killproInfo.add(info);
			}
		}
		//4.����killproInfo������ݣ����mCustomerpro��mSystemrpro�д���info���������Ƴ�
		totalsoace=0;
		for (ProcessInfo info : killproInfo) {
			 //�жϵ�ǰ�������Ǹ������У������ڼ������Ƴ�
			 if(mCustomerpro.contains(info)){
				 mCustomerpro.remove(info);
			 }
			 if(mSystemrpro.contains(info)){
				 mSystemrpro.remove(info);
			 }
			 //��ȡҪ�����ĵĶ���������Ӧ��ռ�ڴ�Ĵ�С
			 totalsoace+= info.memsize;
			 //5,����ɱ�����̵ķ���
			 ProgressInfoProvider.killProgress(getApplicationContext(), info);
			
		}
		progressCount=progressCount-killproInfo.size();
		
		tv_progress.setText("����������"+(progressCount));
		
		availspace=availspace+totalsoace;
		String stringavailspace = Formatter.formatFileSize(getApplicationContext(), availspace);
		tv_total.setText("���� /ʣ�� ��"+strtotalspace+"/"+stringavailspace);
		
		Toastutils.show(getApplicationContext(), "ɱ����"+killproInfo.size()+"���̣߳��ͷ���"+Formatter.formatFileSize(getApplicationContext(), totalsoace));
		//6.֪ͨ����adapter
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
		//����һ�����ý���
		Intent intent = new Intent(getApplicationContext(),ProgressSetingActivity.class);
		startActivityForResult(intent, 0);
	}
	//activity�رյ�ʱ����ô˷���
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(myadapter!=null){
			myadapter.notifyDataSetChanged();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
