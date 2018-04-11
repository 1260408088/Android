package com.example.shark;
import java.util.List;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
public class ServiceUtils {
	/**
	 * @param ctx �����Ļ���
	 * @param serviceName ����Ҫ�жϵķ�������
	 * @return ���ڷ���true�������ڷ���false
	 */
	public static boolean isRunning(Context ctx,String serviceName){
		//1.��ȡactivityManageer�����߶��󣬿���ȥ��ȡ��ǰ�ֻ��������е����з���
		ActivityManager mAm = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
		//2.��ȡ�ֻ��������еķ���ļ���(����ȡ������)
		List<RunningServiceInfo> services = mAm.getRunningServices(100);
		//3.������ÿһ�����񣬺ʹ���ķ����������Ƚϣ������򷵻�true
		for (RunningServiceInfo runningService : services) {
			 if(runningService.service.getClassName().equals(serviceName)){
				 return true;
			 }
		}
		return false;
	} 
}
