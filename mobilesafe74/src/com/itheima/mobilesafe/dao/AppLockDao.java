package com.itheima.mobilesafe.dao;

import java.util.ArrayList;
import java.util.List;

import com.itheima.mobilesafe.ConstantValue;
import com.itheima.mobilesafe.bean.AppInfo;
import com.itheima.mobilesafe.db.AppLockOpenHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class AppLockDao {
     private static AppLockDao appLockDao=null;
     private AppLockOpenHelper appLockOpenHelper=null;
     private Context context;
     
     private AppLockDao(Context ctx){
    	 this.context=ctx;
    	 appLockOpenHelper=new AppLockOpenHelper(ctx);
     }
     
	 //单例设置模式
	 public static AppLockDao getinstance(Context ctx){
		 if(appLockDao==null){
			 appLockDao=new AppLockDao(ctx);
		 }
		return appLockDao;
	 }
	 
	 //数据库的插入
	 public void insert(String packageName){
		 SQLiteDatabase db = appLockOpenHelper.getWritableDatabase();
			
			ContentValues contentValues = new ContentValues();
			contentValues.put("packagename", packageName);
			db.insert("applock", null, contentValues);
			db.close();
			//得到内容管擦者的信息
			context.getContentResolver().notifyChange(Uri.parse("content://applock/change"), null);
	 }
	 //数据的删除
	 public void del(String packageName){
		 SQLiteDatabase db = appLockOpenHelper.getWritableDatabase();
		 db.delete("applock", "packagename = ?", new String[]{packageName});
		 db.close();
		 context.getContentResolver().notifyChange(Uri.parse("content://applock/change"), null);
	 }
	 //数据的查找
	public  List<String> findall(){
		 SQLiteDatabase db = appLockOpenHelper.getWritableDatabase();
		 Cursor cursor = db.query("applock", new String[]{"packagename"}, null, null, null, null, null);
		 List<String>  lockPackageList =new ArrayList<String>();
		 while(cursor.moveToNext()){
			 
			 lockPackageList.add(cursor.getString(0));
			 
		 }
		 cursor.close();
		 db.close();
		 return lockPackageList;
	}
}
