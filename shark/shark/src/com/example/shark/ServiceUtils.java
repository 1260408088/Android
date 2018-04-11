package com.example.shark;
import java.util.List;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
public class ServiceUtils {
	/**
	 * @param ctx 上下文环境
	 * @param serviceName 传入要判断的服务名称
	 * @return 存在返回true，不存在返回false
	 */
	public static boolean isRunning(Context ctx,String serviceName){
		//1.获取activityManageer管理者对象，可以去获取当前手机正在运行的所有服务
		ActivityManager mAm = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
		//2.获取手机正在运行的服务的集合(最多获取的数量)
		List<RunningServiceInfo> services = mAm.getRunningServices(100);
		//3.遍历出每一个服务，和传入的服务名称作比较，存在则返回true
		for (RunningServiceInfo runningService : services) {
			 if(runningService.service.getClassName().equals(serviceName)){
				 return true;
			 }
		}
		return false;
	} 
}
