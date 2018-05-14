package com.itheima.mobilesafe.engine;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Address {
	  private static final String tag = "Address";
	//1.指定数据库的访问路径
      public static String path="data/data/com.itheima.mobilesafe/files/address.db";
      private static String mArea = "未知号码";

      /**传递一个电话号码，并进行查询，再返回查询的归属地
     * @param phone 查询的电话号码
     */
  
    public static String Address(String phone){
    	   mArea = "未知号码";
    	 //2.开启 数据库链接(设置成只读)
    	   SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
    	   String regularExpression="^1[3-8]\\d{9}";
    	   if(phone.matches(regularExpression)){
    		   //截取号码的前七位，和数据库匹配
        	   phone =phone.substring(0, 7);

        	   //3，数据库查询
        	   Cursor cursor = db.query("data1", new String[]{"outkey"}, "id = ?", new String[]{phone}, null, null, null);
        	   Log.i(tag,"outkey =");
        	   //4,查到即可
        	   if(cursor.moveToNext()){
        		   String outkey=cursor.getString(0);
        		   Log.i(tag,"outkey ="+outkey);
        		   //data1表的keyout作为data2的id来查询
        		   Cursor indexcursor = db.query("data2", new String[]{"location"}, "id = ?", new String[]{outkey}, null, null, null);
        		   if(indexcursor.moveToNext()){
        			   mArea=indexcursor.getString(0);
        			   Log.i(tag, "area="+mArea);
        			   
        		   }
        	   }else{
        		   mArea="未知号码";
        	   }
    	     }else{
    	    	 //根据长度去判断号码
   	   	  		int length=phone.length();
   	   	  		switch (length) {
				case 3://119 110 120 114
					mArea="报警电话";
					 Log.i(tag, "area="+mArea);
					break;
				case 4:
					mArea="模拟器";
					break;
				case 5:
				    mArea="服务电话";
					break;
				case 7:
					mArea="本地电话";
					break;
				case 8:
					mArea="本地电话"; 
					break;
				case 11:
				//（3+8）区号+座机号（外地）
					String area=phone.substring(1,3);
					Cursor cursor = db.query("data2", new String[]{"location"}, "area=?", new String[]{area}, null, null, null);
					if(cursor.moveToNext()){
						mArea=cursor.getString(0);
					}else{
						mArea="未知号码";
					}
				    break;
				case 12:
				//(4+8)区号(0791)+座机号码(外地)
					String area1=phone.substring(1,4);
					Cursor cursor1 = db.query("data2", new String[]{"location"}, "area=?", new String[]{area1}, null, null, null);
					if(cursor1.moveToNext()){
						mArea=cursor1.getString(0);
						Log.i(tag, "area="+mArea);
					}else{
						mArea="未知号码";
					}
					break;                                                                 
				}
   	   	  	}
		return mArea;
    	   
      }
   }
  

