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
	  //2.声明一个当前类的对象
	  private static BlackNumberDao blackNumberDao=null;
      private BlackNumberOpenHelper blackNumberOpenHelper;
	  private List<BlackNumberinfo> mlist;
	 
	  

	  //*****单例设计模式
      //1.私有化构造方法
	  private BlackNumberDao(Context context){
		  blackNumberOpenHelper = new BlackNumberOpenHelper(context);
	  }
	  //3.声明一个静态方法，如果当前类的对象为空，则声明一个新的
	  public static BlackNumberDao getInstance(Context context){
		  	if(blackNumberDao==null){
		  		blackNumberDao=new BlackNumberDao(context);
		  	}
			return blackNumberDao;  
	  }
	  
	  //数据库的增删改查
	  /**
	 * @param number 拦截的电话号码
	 * @param mode   拦截的类型
	 */
	public void insert(String number,String mode){
		  //开启数据库，准备写入操作
		   SQLiteDatabase db = blackNumberOpenHelper.getWritableDatabase();
		   ContentValues values = new ContentValues();
		   values.put("number", number);
		   values.put("mode",mode);
		   db.insert("blacknumber", null, values);
		   db.close();
	  }
	    
	  	/**
	  	 * @param number 删除的号码
	  	 */
	  	public void del(String number){
	  		SQLiteDatabase db = blackNumberOpenHelper.getWritableDatabase();
	  		db.delete("blacknumber", "number = ?", new String []{number});
	  		db.close();
	  	}
	  	
	  	/**根据电话号码更新拦截模式
	  	 * @param number 要拦截的电话好吗
	  	 * @param mode  更新的拦截的模式
	  	 */
	  	public void update(String number,String mode){
	  		SQLiteDatabase db = blackNumberOpenHelper.getWritableDatabase();
	  		ContentValues values = new ContentValues();
			values.put("mode",mode);
	  		db.update("blacknumber", values, "number = ?", new String []{number});
	  		db.close();
	  	}
	  	
	  	/**
	  	 * @return 返回数据库中所有的号码和拦截类型的集合
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
	  
	  	//分页查询，查询20个数据
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
	  	 * @return 数据库数据的总个数
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
	  	 * @param number 要查询mode的号码
	  	 * @return  返回mode的类型
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
