package com.itheima.mobilesafe.dao;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import com.itheima.mobilesafe.bean.BlackNumberinfo;
import com.itheima.mobilesafe.db.BlackNumberOpenHelper;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class BlackNumberDao {
	  //2.����һ����ǰ��Ķ���
	  private static BlackNumberDao blackNumberDao=null;
      private BlackNumberOpenHelper blackNumberOpenHelper;
	  private List<BlackNumberinfo> mlist;
	 
	  

	  //*****�������ģʽ
      //1.˽�л����췽��
	  private BlackNumberDao(Context context){
		  blackNumberOpenHelper = new BlackNumberOpenHelper(context);
	  }
	  //3.����һ����̬�����������ǰ��Ķ���Ϊ�գ�������һ���µ�
	  public static BlackNumberDao getInstance(Context context){
		  	if(blackNumberDao==null){
		  		blackNumberDao=new BlackNumberDao(context);
		  	}
			return blackNumberDao;  
	  }
	  
	  //���ݿ����ɾ�Ĳ�
	  /**
	 * @param number ���صĵ绰����
	 * @param mode   ���ص�����
	 */
	public void insert(String number,String mode){
		  //�������ݿ⣬׼��д�����
		   SQLiteDatabase db = blackNumberOpenHelper.getWritableDatabase();
		   ContentValues values = new ContentValues();
		   values.put("number", number);
		   values.put("mode",mode);
		   db.insert("blacknumber", null, values);
		   db.close();
	  }
	    
	  	/**
	  	 * @param number ɾ���ĺ���
	  	 */
	  	public void del(String number){
	  		SQLiteDatabase db = blackNumberOpenHelper.getWritableDatabase();
	  		db.delete("blacknumber", "number = ?", new String []{number});
	  		db.close();
	  	}
	  	
	  	/**���ݵ绰�����������ģʽ
	  	 * @param number Ҫ���صĵ绰����
	  	 * @param mode  ���µ����ص�ģʽ
	  	 */
	  	public void update(String number,String mode){
	  		SQLiteDatabase db = blackNumberOpenHelper.getWritableDatabase();
	  		ContentValues values = new ContentValues();
			values.put("mode",mode);
	  		db.update("blacknumber", values, "number = ?", new String []{number});
	  		db.close();
	  	}
	  	
	  	/**
	  	 * @return �������ݿ������еĺ�����������͵ļ���
	  	 */
	  	public List<BlackNumberinfo> findAll(){
	  		SQLiteDatabase db = blackNumberOpenHelper.getWritableDatabase();
	  		Cursor cursor = db.query("blacknumber", new String[]{"number","mode"}, null, null, null, null, "_id desc");
	  	    mlist = new ArrayList<BlackNumberinfo>();
			while(cursor.moveToNext()){
				 BlackNumberinfo bean = new BlackNumberinfo();
				 bean.number=cursor.getString(0);
				 bean.mode=cursor.getString(1);
				 mlist.add(bean);
			}
			
			
	  		cursor.close();
	  		db.close();
	  		
//	  		for (BlackNumberinfo s : mlist) {
//				String mode = s.mode;
//				String number = s.number;
//				System.out.println("mode :"+mode+"  "+"number :"+number);
//			}
			return mlist;
	  	}
	  
	  	//��ҳ��ѯ����ѯ20������
	  	public List<BlackNumberinfo> find(int index){
	  		SQLiteDatabase db = blackNumberOpenHelper.getReadableDatabase();
	  		Cursor cursor = db.rawQuery("select number,mode from  blacknumber order by _id desc limit ?,20", new String[]{index+""});
	  		mlist = new ArrayList<BlackNumberinfo>();
	  		while(cursor.moveToNext()){ 
	  			BlackNumberinfo bean = new BlackNumberinfo();
	  			bean.number=cursor.getString(0);
	  			bean.mode=cursor.getString(1);
	  			mlist.add(bean);
	  		}
	  		db.close();
	  		cursor.close();
			return mlist;
	  	}
	  	
	  	/**
	  	 * @return ���ݿ����ݵ��ܸ���
	  	 */
	  	public int getcount(){
	  		int count = 0;
	  		SQLiteDatabase db = blackNumberOpenHelper.getReadableDatabase();
	  		Cursor cursor = db.rawQuery("select count(*) from  blacknumber", null);
	  		if(cursor.moveToNext()){
	  			count = cursor.getInt(0);
	  		}
	  		db.close();
	  		cursor.close();
	  		
			return count;
	  		
	  	}
	  	
	  	/**
	  	 * @param number Ҫ��ѯmode�ĺ���
	  	 * @return  ����mode������
	  	 */
	  	public int getmode(String number){
	  		int mode =0;
	  		SQLiteDatabase db = blackNumberOpenHelper.getReadableDatabase();
	  		Cursor cursor = db.query("blacknumber", new String[]{"mode"}, "number = ?", new String[]{number}, null, null, null);
	  		if(cursor.moveToNext()){
	  			mode=cursor.getInt(0);
	  		}
	  		return mode;
	  	}
	  	
}
