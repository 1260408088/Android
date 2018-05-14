package com.itheima.mobilesafe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class contactlistActivity extends Activity {
	protected static final String tag = "contactlistActivity";
	private ListView lv_contast;
	private myAdapter mMyadapter;
	/**
	 * ʹ��map���洢ÿ����ϵ�˵�  name��number
	 */

	
	/**
	 * ����listȥ�洢mapȥ�洢ÿ����ϵ�˵���Ϣ
	 */
	 List<HashMap<String,String>> contactlist=new ArrayList<HashMap<String,String>>();
	
	private Handler mHandler=new Handler(){
		

		public void handleMessage(Message msg) {
			 mMyadapter = new myAdapter();
			
			lv_contast.setAdapter(mMyadapter);
		};
	};
	
	class myAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return contactlist.size();
		}

		@Override
		public HashMap<String,String> getItem(int position) {
			return contactlist.get(position);
		}

		@Override
		public long getItemId(int position) {
			
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
                 View view=null;
                 //view �ĸ���
                 if(convertView!=null){
                	 view=convertView;
                 }else{
                	 
			     view = View.inflate(getApplicationContext(), R.layout.listview_contast_iteam, null);
                 }
				 TextView tv_name= (TextView) view.findViewById(R.id.tv_name);
				 TextView tv_phone= (TextView) view.findViewById(R.id.tv_phone);
				 
				 tv_name.setText(getItem(position).get("name"));
				 tv_phone.setText(getItem(position).get("phone"));
			
			return view;
		}
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contastlist);
		
		initUI();
		
		initDate();
		
	}

	/**
	 * ��������
	 */
	private void initDate() {
		//���ܻ��ʱ��Ҫ�����߳��н���
		new Thread(){
			public void run() {
				//1��ȡ���ݽ����ߵĶ���
				ContentResolver contentResolver = getContentResolver();
				//2����ѯϵͳ��ϵ�����ݿ�Ĺ���(�Ӷ�ȡ��ϵ��Ȩ��)
				Cursor cursor = contentResolver.query(Uri.parse("content://com.android.contacts/raw_contacts"), 
						new String[]{"contact_id"}, null, null,null);
				//��֮ǰ��Ҫ��list����Է�ֹ�����ݵ��ظ�
				contactlist.clear();
				while(cursor.moveToNext()){
					String id=cursor.getString(0);
					Log.i(tag,id);
					Cursor indexcuesor = contentResolver.query(Uri.parse("content://com.android.contacts/data"), 
							new String[]{"mimetype","data1"}, "raw_contact_id= ?", 
							new String[]{id},
							null);
					HashMap<String,String> hashmap=new HashMap<String,String>();
					while(indexcuesor.moveToNext()){
						String type = indexcuesor.getString(0);
						String data = indexcuesor.getString(1);
						//�ж����ͰѶ�Ӧ�����ݸ�ֵ
						if(type.equals("vnd.android.cursor.item/phone_v2")){
							hashmap.put("phone", data);	
							Log.i(tag, data);
						}else if(type.equals("vnd.android.cursor.item/name")){
							hashmap.put("name", data);	
							Log.i(tag, data);
						}
						
					}
					indexcuesor.close();
					//��map��ӵ�list��
					contactlist.add(hashmap);
				}
				cursor.close();
				//7��Ϣ����,����һ������Ϣ����֪���̣߳�����ʹ�����̵߳����õ����ݼ���
				mHandler.sendEmptyMessage(0);
				
			};
		}.start();
		}
		
		


	/**
	 * ���ز���
	 */
	private void initUI() {
		lv_contast = (ListView) findViewById(R.id.lv_contast);
		lv_contast.setOnItemClickListener(new OnItemClickListener() {
            
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//1��ȡ�������
				if(mMyadapter!=null){
					HashMap<String,String> map = mMyadapter.getItem(position);
					//2.��ȡ����
					String phone = map.get("phone");
					//3.���˺��������������ʹ��
					Intent intent = new Intent();
					intent.putExtra("phone", phone);
					setResult(0, intent);
					
					finish();
				}
				    
			}
		});
	}
}
