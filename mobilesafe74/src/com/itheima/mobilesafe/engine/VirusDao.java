package com.itheima.mobilesafe.engine;
import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class VirusDao {
       //1,指定访问数据库的路径
	   public static String path="data/data/com.itheima.mobilesafe/files/antivirus.db";
	   
	   //2.查询数据库的中对应的MD5码
	   public static List<String>  getVirusList(){
		     SQLiteDatabase Dao = SQLiteDatabase.openDatabase(path, null,SQLiteDatabase.OPEN_READONLY);
		     Cursor cursor = Dao.query("datable", new String[]{"md5"}, null, null, null, null, null);
		     List<String> virusList = new ArrayList<String>();
		     while(cursor.moveToNext()){
		    	    String string = cursor.getString(0);
		    	    virusList.add(string);
		     }
		     cursor.close();
		     return virusList;
	   }

}
