package com.itheima.mobilesafe.global;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.Thread.UncaughtExceptionHandler;

import android.app.Application;
import android.os.Environment;
import android.util.Log;

public class MyApplication extends Application {
	protected static final String tag = "MyApplication";

	@Override
    public void onCreate() {
    	   Thread.setDefaultUncaughtExceptionHandler(new  UncaughtExceptionHandler() {
			
			@Override
			public void uncaughtException(Thread thread, Throwable ex) {
				//�ڻ�ȡ����δ������쳣��,����ķ���
				ex.printStackTrace();
				Log.i(tag, "����һ���쳣");
				String path=Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"error74.log";
				File file = new File(path);
				try {
					PrintWriter printWriter = new PrintWriter(file);
					ex.printStackTrace(printWriter);
					printWriter.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				//�ϴ���˾�ķ�����
				//����Ӧ��
				System.exit(0);
			}
		});
    	super.onCreate();
    }
}
