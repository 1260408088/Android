package com.itheima.mobilesafe.engine;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Address {
	  private static final String tag = "Address";
	//1.ָ�����ݿ�ķ���·��
      public static String path="data/data/com.itheima.mobilesafe/files/address.db";
      private static String mArea = "δ֪����";

      /**����һ���绰���룬�����в�ѯ���ٷ��ز�ѯ�Ĺ�����
     * @param phone ��ѯ�ĵ绰����
     */
  
    public static String Address(String phone){
    	   mArea = "δ֪����";
    	 //2.���� ���ݿ�����(���ó�ֻ��)
    	   SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
    	   String regularExpression="^1[3-8]\\d{9}";
    	   if(phone.matches(regularExpression)){
    		   //��ȡ�����ǰ��λ�������ݿ�ƥ��
        	   phone =phone.substring(0, 7);

        	   //3�����ݿ��ѯ
        	   Cursor cursor = db.query("data1", new String[]{"outkey"}, "id = ?", new String[]{phone}, null, null, null);
        	   Log.i(tag,"outkey =");
        	   //4,�鵽����
        	   if(cursor.moveToNext()){
        		   String outkey=cursor.getString(0);
        		   Log.i(tag,"outkey ="+outkey);
        		   //data1���keyout��Ϊdata2��id����ѯ
        		   Cursor indexcursor = db.query("data2", new String[]{"location"}, "id = ?", new String[]{outkey}, null, null, null);
        		   if(indexcursor.moveToNext()){
        			   mArea=indexcursor.getString(0);
        			   Log.i(tag, "area="+mArea);
        			   
        		   }
        	   }else{
        		   mArea="δ֪����";
        	   }
    	     }else{
    	    	 //���ݳ���ȥ�жϺ���
   	   	  		int length=phone.length();
   	   	  		switch (length) {
				case 3://119 110 120 114
					mArea="�����绰";
					 Log.i(tag, "area="+mArea);
					break;
				case 4:
					mArea="ģ����";
					break;
				case 5:
				    mArea="����绰";
					break;
				case 7:
					mArea="���ص绰";
					break;
				case 8:
					mArea="���ص绰"; 
					break;
				case 11:
				//��3+8������+�����ţ���أ�
					String area=phone.substring(1,3);
					Cursor cursor = db.query("data2", new String[]{"location"}, "area=?", new String[]{area}, null, null, null);
					if(cursor.moveToNext()){
						mArea=cursor.getString(0);
					}else{
						mArea="δ֪����";
					}
				    break;
				case 12:
				//(4+8)����(0791)+��������(���)
					String area1=phone.substring(1,4);
					Cursor cursor1 = db.query("data2", new String[]{"location"}, "area=?", new String[]{area1}, null, null, null);
					if(cursor1.moveToNext()){
						mArea=cursor1.getString(0);
						Log.i(tag, "area="+mArea);
					}else{
						mArea="δ֪����";
					}
					break;                                                                 
				}
   	   	  	}
		return mArea;
    	   
      }
   }
  

