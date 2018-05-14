package com.itheima.mobilesafe.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class AppLockOpenHelper extends SQLiteOpenHelper {

	public AppLockOpenHelper(Context context) {
		
		super(context, "appLock.db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// 生成数据库
		db.execSQL("create table applock (_id integer primary key autoincrement , "
				+ "packagename varchar(50));");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
