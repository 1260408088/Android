package com.itheima.mobilesafe;

import java.util.List;

import com.itheima.mobilesafe.bean.BlackNumberinfo;
import com.itheima.mobilesafe.dao.BlackNumberDao;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;


//1.adapter�Ż�
//2.findviewbyId()�Ż�
//3.��ҳ��ʾ�Ż����Ż���ѯ��
public class BlackNumberActivity extends Activity {
       private Button bt_add;
       private ListView lv_display;
       private BlackNumberDao mDao;
       private List<BlackNumberinfo> list;
       private int mode;
       private Myadapter myadapter;
       private boolean isload=false; 
       private int mCount;
       private Handler mHandler=new Handler(){
       

			public void handleMessage(android.os.Message msg) {
				if(myadapter==null){
					//����adapter
    	    	    myadapter = new Myadapter();
    	    	    lv_display.setAdapter(myadapter); 
				}else{
					myadapter.notifyDataSetChanged();
				}
    	    	    
    	      };
       };
       //������������������
	   class Myadapter extends BaseAdapter{

		@Override
			public int getCount() {
				return list.size();
			}

			@Override
			public Object getItem(int position) {
				return list.get(position);
			}

			@Override
			public long getItemId(int position) {
				
				return position;
			}
			
			@Override
			public View getView(final int position, View convertView, ViewGroup parent) {
				//����viewholderһ
				ViewHolder viewholder=null;
				//1����converttView
				if(convertView==null){
					convertView=View.inflate(getApplicationContext(), R.layout.listview_item_activity, null);
					//2����findview�Ĵ˴���
					
					//����viewholder��
					viewholder=new ViewHolder();
					//����viewholder��
					viewholder.tv_number = (TextView) convertView.findViewById(R.id.tv_number);
					viewholder.tv_mode = (TextView) convertView.findViewById(R.id.tv_mode);
					//����Ͱ
					viewholder.iv_del=(ImageView) convertView.findViewById(R.id.iv_del); 
					convertView.setTag(viewholder);
				}else{
					//����viewholder��
					 viewholder = (ViewHolder) convertView.getTag();
				}
				//��listview���һ������
				
				     
				//������Ͱ���õ���¼�
				viewholder.iv_del.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						//1ɾ�����ݿ������
						mDao.del(list.get(position).number);
						//2����list����
						list.remove(position);
						//3֪ͨadapter��������
						if(myadapter!=null){
							myadapter.notifyDataSetChanged();
						}
					}
				});
				
				viewholder.tv_number.setText(list.get(position).number);
				//ת����int���ͣ�����switch�ж�
				int modes = Integer.parseInt(list.get(position).mode);
				switch (modes) {
				case 1:
					viewholder.tv_mode.setText("���ص绰");
					break;
				case 2:
					viewholder.tv_mode.setText("���ض���");
					break;
				case 3:
					viewholder.tv_mode.setText("��������");
					break;
				}
				return convertView;
			}
	   }
	   
	    
	   //����viewholder��
	   static class ViewHolder{
		   TextView tv_number;
		   TextView tv_mode;
		   ImageView iv_del;
	   }
	   
	   
	@Override
    protected void onCreate(Bundle savedInstanceState) {
    	setContentView(R.layout.activity_black_number);
    	super.onCreate(savedInstanceState);
    	intiUI();
    	initData();
    }
    //��ʼ������
	private void initData() {
		//��ѯ�������߳�
		new Thread(){

			public void run(){
				//1��ȡ���� ���������ݿ�ĵĶ���
				mDao = BlackNumberDao.getInstance(getApplicationContext());
				//2��ѯ��������
				list = mDao.find(0);
				mCount = mDao.getcount();
//				for (BlackNumberinfo s : list) {
//					 String mode =s.mode;
//					 String number =s.number;
//					 System.out.println("mode :"+mode+"  "+"number :"+number);
//				}
				//��֪adapter����ȥ����list��
				mHandler.sendEmptyMessage(0);
			};
		}.start();
		
	}
        
	private void intiUI() {  
		//�ҵ�����Ҫ�Ŀؼ�
		bt_add = (Button) findViewById(R.id.bt_add);
		lv_display = (ListView) findViewById(R.id.lv_display);
		//��ť����������Ի���
		bt_add.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			       showDialog();
			}
		});
		//����listview�Ĺ���״̬
		lv_display.setOnScrollListener(new OnScrollListener() {
			//���������У�״̬�����任���� �ķ���
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				//OnScrollListener.SCROLL_STATE_FLING ���ٹ���
				//OnScrollListener.SCROLL_STATE_IDLE  ����״̬
				//OnScrollListener.SCROLL_STATE_TOUCH_SCROLL  ���ִ�����ȥ����
				//�ݴ���
				if(list!=null){
				//����һ��������ֹͣ״̬
				//�����������������һ����Ŀ�ɼ������һ����Ŀ������>=�����������Ŀ����-1��
				if(scrollState==OnScrollListener.SCROLL_STATE_IDLE&&lv_display.getLastVisiblePosition()>=list.size()-1
						&&!isload){
					/*isload��ֹ�ظ����صı���
					 * ������ڼ���isload����true��֮���ٸ�Ϊfalse
					 * ��һ�μ��ص�ʱ���ж�isload�ı������Ƿ�Ϊfalse�����Ϊtrue����Ҫ����һ�μ�����ɣ�Ȼ���Ϊfalse
					 * ��ȥ����
					 */
					
					if(mCount>list.size()){
					//������һҳ����
					//��ѯ�������߳�
					new Thread(){
						public void run(){
							//1��ȡ���� ���������ݿ�ĵĶ���
							mDao = BlackNumberDao.getInstance(getApplicationContext());
							//2��ѯ��������
							List<BlackNumberinfo> morelist = mDao.find(list.size());
							//3��morelist��ӵ�list��
							list.addAll(morelist);
							//��֪adapter����ȥ����list��
							mHandler.sendEmptyMessage(0);
						};
					}.start();
					
				}
				}
			}
			}
			//���������е��õķ��� 
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				
			}
		});
	}
	
	private void showDialog(){
			Builder builder = new AlertDialog.Builder(this);
			final AlertDialog dialog = builder.create();
			View view = View.inflate(getApplicationContext(), R.layout.dialog_insert_blacknumber, null);
			//���ݵͰ汾Ϊ�������߾�
			dialog.setView(view,0,0,0,0);
			dialog.show();
			//ȡ����ȷ����ť
			Button bt_submit = (Button) view.findViewById(R.id.bt_submit);
			Button bt_cancel = (Button) view.findViewById(R.id.bt_cancel);
			//��ť���editText
			RadioGroup rg_select = (RadioGroup) view.findViewById(R.id.rg_select);
			final EditText et_addnumber = (EditText) view.findViewById(R.id.et_addnumber);
			rg_select.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(RadioGroup group, int checkedId) {
					   switch (checkedId) {
					   		case R.id.rb_number:
					   			mode=1;
					   			break;
					   		case  R.id.rb_sms:
					   			mode=2;
					   			break;
					   		case  R.id.rb_all:
					   			mode=3;
					   			break;
					}
				}
			});
			
			bt_submit.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					//1��ȡ���еĵ绰����
					String number = et_addnumber.getText().toString();  
					if(!TextUtils.isEmpty(number)){
					//2���ݿ��в��뵱ǰ���صĺ���
					mDao.insert(number, mode+"");
				    //3���ݿ�ͼ��ϱ���ͬ�� ��1���¶�ȡһ�����ݿ�,2�ֶ��򼯺����һ�����󣨲������ݿ⹹���Ķ��󣩣�
					BlackNumberinfo blackNumberinfo = new BlackNumberinfo();
					blackNumberinfo.number=number;
					blackNumberinfo.mode=mode+"";
					//��ӵ����ϵ����ϲ�
					list.add(0,blackNumberinfo);
					//��adapter��������
					if(myadapter!=null){
					   myadapter.notifyDataSetChanged();
					}
					}else{
						Toastutils.show(getApplicationContext(), "���������غ���");
					}
					dialog.dismiss();
					
				}
			});
			bt_cancel.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
	}

}
