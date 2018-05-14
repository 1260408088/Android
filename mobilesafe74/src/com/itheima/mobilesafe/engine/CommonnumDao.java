package com.itheima.mobilesafe.engine;
import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CommonnumDao {
       //1,指定访问数据库的路径
	   public String path="data/data/com.itheima.mobilesafe/files/commonnum.db";
	   ///2开启数据组
	   public List<Group> getGroup(){
		   SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
		   Cursor cursor = db.query("classlist", new String[]{"name","idx"}, null, null, null, null, null);
		   List<Group> groupList = new ArrayList<Group>();
		   while(cursor.moveToNext()){
			   Group group = new Group();
			   group.name=cursor.getString(0);
			   group.idx=cursor.getString(1);
			   group.childlist=getChild(group.idx);
			   groupList.add(group);
		   }
		   cursor.close();
		   db.close();
		   return groupList;
	   }
	   //获取每一个组中的孩子节点
	   public List<Child> getChild(String idx){
		   SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
		   Cursor cursor = db.rawQuery("select * from table"+idx+";", null);
		   List<Child> childList = new ArrayList<Child>();
		   
		   while(cursor.moveToNext()){
			   Child child = new Child();
			   child._id=cursor.getString(0);
			   child.number=cursor.getString(1);
			   child.name=cursor.getString(2);
			   childList.add(child);
		   }
		   
		   cursor.close();
		   db.close();
		   return childList;
	   }
	   //两个javabean对象
	   public class Group{
		   public String name;
		   public String idx;
		   public List<Child> childlist;
	   }
	   public class Child{
		   public String _id;
		   public String number;
		   public String name;
	   }
}
