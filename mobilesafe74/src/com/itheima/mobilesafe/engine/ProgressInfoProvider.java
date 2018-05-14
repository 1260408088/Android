package com.itheima.mobilesafe.engine;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.bean.ProcessInfo;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;

public class ProgressInfoProvider {
	  //��ȡ�ܵý�����
      public static int getProgressCount(Context ctx){
    	  	 //1.��ȡ���������ķ���(ActivityManager)
    	     ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
    	     //2.��ȡ�������н��̵ļ���
    	     List<RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();
    	     //3.���ؼ��ϵ�����
    	     return runningAppProcesses.size();
      }
      
      /**
     * @param ctx�����Ļ���
     * @return ���ؿ����ڴ���bytes
     */ 
    public static long getAvailSpace(Context ctx){
 	  	     //1.��ȡ���������ķ���(ActivityManager)
 	         ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
 	         //2.�����洢���õ��ڴ����
 	         MemoryInfo memoryInfo = new MemoryInfo();
 	         //3.��memoryInfo��ֵ������ �ڴ� ֵ ��
 	         am.getMemoryInfo(memoryInfo);
 	         //4.��ȡmemoryInfo����Ӧ�Ŀ����ڴ��С
    	     return memoryInfo.availMem;
      }
    
    
    public static long getTotalSpace(Context ctx){
  	     /*//1.��ȡ���������ķ���(ActivityManager)
         ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
         //2.�����洢���õ��ڴ����                                        
         MemoryInfo memoryInfo = new MemoryInfo();
         //3.��memoryInfo��ֵ������ �ڴ� ֵ ��
         am.getMemoryInfo(memoryInfo);
         //4.��ȡmemoryInfo����Ӧ�Ŀ����ڴ��С
	     return memoryInfo.totalMem;  totalMem�����ݵͰ汾���������� */
    	//�ڴ��Сд���ļ��У���ȡproc/meminfo�ļ�����ȡ��һ�У���ȡ�����ַ���ת����bytes
    	FileReader fileReader = null;
    	BufferedReader bufferedReade=null;
    	try {
    		//��ȡ�ļ�����ȡ����
			 fileReader=new FileReader("proc/meminfo");
			 bufferedReade = new BufferedReader(fileReader);
			 String lineone = bufferedReade.readLine();
			//���ַ���ת�����ַ�������
			char[] charArray = lineone.toCharArray();
			//ѭ������ÿһ���ַ���������ַ���ASCII����0�� 9������ �ڣ�˵�����ַ���Ч 
			StringBuffer stringBuffer = new StringBuffer();
			for (char c : charArray) {
				if(c>'0'&&c<'9'){
					stringBuffer.append(c);
				}
			}
			return Long.parseLong(stringBuffer.toString())*1024; //(���� ��Ҫ�� bytes)
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			//�ر�������
			try {
				fileReader.close();
				bufferedReade.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return 0;
 }
    public static List<ProcessInfo> getProcessInfo(Context ctx){
		
    	//��ȡ���������Ϣ
    	List<ProcessInfo> processInfoList = new ArrayList<ProcessInfo>();
    	//1.activityManager�����߶���
    	ActivityManager am = (ActivityManager)ctx.getSystemService(Context.ACTIVITY_SERVICE);
    	//��ȡ���Ĺ����߶���
    	PackageManager pm = ctx.getPackageManager();
    	//2.��ȡ�������еĽ��̵ļ���
    	List<RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();
    	//3.ѭ�������������ϣ���ȡ���̵������Ϣ�����ƣ�������ͼ�� ��ʹ���ڴ��С���Ƿ�Ϊϵͳ�ڴ棩
    	for (RunningAppProcessInfo info : runningAppProcesses) {
			 ProcessInfo processInfo = new ProcessInfo();
			 //4.��ȡ���̵�����==Ӧ�ð���
			 processInfo.packageName = info.processName;
			 
			 //5,��ȡ����ռ�õ��ڴ��С������һ�����̵�pid���飩
			 android.os.Debug.MemoryInfo[] progressMemoryInfo = am.getProcessMemoryInfo(new int[]{info.pid});
			 //6,�������� ������λ��Ϊ0�Ķ���Ϊ��ǰ���̵��ڴ���Ϣ�Ķ���
			 android.os.Debug.MemoryInfo memoryInfo = progressMemoryInfo[0];
			 //7,��ȡ��ʹ�õĴ�С
			 processInfo.memsize = memoryInfo.getTotalPrivateDirty()*1024;
			 
			 try {
				 ApplicationInfo applicationInfo = pm.getApplicationInfo(processInfo.packageName, 0);
				  //8,��ȡӦ�õ�����
				  processInfo.name = applicationInfo.loadLabel(pm).toString();
				  //9.��ȡӦ�õ�ͼ��
				   processInfo.icon = applicationInfo.loadIcon(pm);
				  //10.�ж��Ƿ�Ϊϵͳ����
				   if((applicationInfo.flags&ApplicationInfo.FLAG_SYSTEM)==ApplicationInfo.FLAG_SYSTEM){
					   processInfo.isSystem=true;
				   }else{
					   processInfo.isSystem=false;
				   }
			} catch (NameNotFoundException e) {
				//δ�ҵ����Ƶ��쳣��Ҫ����
				processInfo.name=info.processName;
				processInfo.icon=ctx.getResources().getDrawable(R.drawable.ic_launcher);
				processInfo.isSystem=true;
				e.printStackTrace();
			}
			 
			 processInfoList.add(processInfo);
		}
    	return processInfoList;
    }
    
    public static void killProgress(Context ctx,ProcessInfo progressInfo){
    	//1.��ȡactivityManager
    	ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
    	//2,ɱ��ָ���������̣���ҪȨ�ޣ�
    	am.killBackgroundProcesses(progressInfo.packageName);
    }
    public static void killProgressAll(Context ctx){
    
    	//1.��ȡactivityManager
    	ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
    	//2.��ȡ�������н��̵ļ���
	     List<RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();
    	//3ѭ���������еĽ��̣�����ɱ��
	     for (RunningAppProcessInfo info : runningAppProcesses) {
	    	 //4�����ֻ���ʿ�⣬������ȫ�����̶���Ҫȥɱ��
	    	 if(info.processName.equals(ctx.getPackageName())){
	    		 //���ƥ�������ֻ���ʿ������Ҫ��������ѭ����������һ��ѭ��������ɱ������
	    		 continue;
	    	 }
	    	  am.killBackgroundProcesses(info.processName);
	     	}
    	}
}