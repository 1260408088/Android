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
	  //获取总得进程数
      public static int getProgressCount(Context ctx){
    	  	 //1.获取进程总数的方法(ActivityManager)
    	     ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
    	     //2.获取正在运行进程的集合
    	     List<RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();
    	     //3.返回集合的总数
    	     return runningAppProcesses.size();
      }
      
      /**
     * @param ctx上下文环境
     * @return 返回可用内存数bytes
     */ 
    public static long getAvailSpace(Context ctx){
 	  	     //1.获取进程总数的方法(ActivityManager)
 	         ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
 	         //2.构建存储可用的内存对象
 	         MemoryInfo memoryInfo = new MemoryInfo();
 	         //3.给memoryInfo赋值（可用 内存 值 ）
 	         am.getMemoryInfo(memoryInfo);
 	         //4.获取memoryInfo中相应的可用内存大小
    	     return memoryInfo.availMem;
      }
    
    
    public static long getTotalSpace(Context ctx){
  	     /*//1.获取进程总数的方法(ActivityManager)
         ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
         //2.构建存储可用的内存对象                                        
         MemoryInfo memoryInfo = new MemoryInfo();
         //3.给memoryInfo赋值（可用 内存 值 ）
         am.getMemoryInfo(memoryInfo);
         //4.获取memoryInfo中相应的可用内存大小
	     return memoryInfo.totalMem;  totalMem不兼容低版本，另求他发 */
    	//内存大小写入文件中，读取proc/meminfo文件，读取第一行，获取数字字符。转换成bytes
    	FileReader fileReader = null;
    	BufferedReader bufferedReade=null;
    	try {
    		//读取文件，读取整行
			 fileReader=new FileReader("proc/meminfo");
			 bufferedReade = new BufferedReader(fileReader);
			 String lineone = bufferedReade.readLine();
			//将字符串转换成字符的数组
			char[] charArray = lineone.toCharArray();
			//循环遍历每一个字符，如果此字符的ASCII码在0到 9的区域 内，说明此字符有效 
			StringBuffer stringBuffer = new StringBuffer();
			for (char c : charArray) {
				if(c>'0'&&c<'9'){
					stringBuffer.append(c);
				}
			}
			return Long.parseLong(stringBuffer.toString())*1024; //(返回 的要是 bytes)
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			//关闭流数据
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
		
    	//获取进程相关信息
    	List<ProcessInfo> processInfoList = new ArrayList<ProcessInfo>();
    	//1.activityManager管理者对象
    	ActivityManager am = (ActivityManager)ctx.getSystemService(Context.ACTIVITY_SERVICE);
    	//获取包的管理者对象
    	PackageManager pm = ctx.getPackageManager();
    	//2.获取正在运行的进程的集合
    	List<RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();
    	//3.循环遍历上述集合，获取进程的相关信息（名称，包名，图标 ，使用内存大小，是否为系统内存）
    	for (RunningAppProcessInfo info : runningAppProcesses) {
			 ProcessInfo processInfo = new ProcessInfo();
			 //4.获取进程的名称==应用包名
			 processInfo.packageName = info.processName;
			 
			 //5,获取进程占用的内存大小（传递一个进程的pid数组）
			 android.os.Debug.MemoryInfo[] progressMemoryInfo = am.getProcessMemoryInfo(new int[]{info.pid});
			 //6,返回数组 中索引位置为0的对象，为当前进程的内存信息的对象
			 android.os.Debug.MemoryInfo memoryInfo = progressMemoryInfo[0];
			 //7,获取已使用的大小
			 processInfo.memsize = memoryInfo.getTotalPrivateDirty()*1024;
			 
			 try {
				 ApplicationInfo applicationInfo = pm.getApplicationInfo(processInfo.packageName, 0);
				  //8,获取应用的名称
				  processInfo.name = applicationInfo.loadLabel(pm).toString();
				  //9.获取应用的图标
				   processInfo.icon = applicationInfo.loadIcon(pm);
				  //10.判断是否为系统进程
				   if((applicationInfo.flags&ApplicationInfo.FLAG_SYSTEM)==ApplicationInfo.FLAG_SYSTEM){
					   processInfo.isSystem=true;
				   }else{
					   processInfo.isSystem=false;
				   }
			} catch (NameNotFoundException e) {
				//未找到名称的异常需要处理
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
    	//1.获取activityManager
    	ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
    	//2,杀死指定包名进程（需要权限）
    	am.killBackgroundProcesses(progressInfo.packageName);
    }
    public static void killProgressAll(Context ctx){
    
    	//1.获取activityManager
    	ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
    	//2.获取正在运行进程的集合
	     List<RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();
    	//3循环遍历所有的进程，并且杀死
	     for (RunningAppProcessInfo info : runningAppProcesses) {
	    	 //4除了手机卫士外，的其他全部进程都需要去杀死
	    	 if(info.processName.equals(ctx.getPackageName())){
	    		 //如果匹配上了手机卫士，则需要跳出本次循环，进行下一次循环，继续杀死进程
	    		 continue;
	    	 }
	    	  am.killBackgroundProcesses(info.processName);
	     	}
    	}
}