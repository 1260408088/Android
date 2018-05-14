package com.itheima.mobilesafe.engine;

import java.util.ArrayList;
import java.util.List;

import com.itheima.mobilesafe.bean.AppInfo;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class AppInfoProvider {
      /**
     * ���ص�ǰ�ռ����е�Ӧ�õ������Ϣ�����ƣ�������ͼ�꣬���ֻ��ڴ棩��
     * ��ϵͳ���û���
     */
    public static List<AppInfo> getAppInfoList(Context ctx){
    	  //1���Ĺ����߶���
    	   PackageManager pm = ctx.getPackageManager();
    	   //2��ȡ��װ���ֻ���Ӧ�������Ϣ�ļ���
    	   List<PackageInfo> packList=pm.getInstalledPackages(0);
    	   //����list�з������listView
    	   ArrayList<AppInfo> appinfoList = new ArrayList<AppInfo>();
    	   //3.ѭ������Ӧ����Ϣ�ļ���
    		for (PackageInfo packageInfo : packList) {
				AppInfo appInfo = new AppInfo();
				//4��ȡӦ�õİ���
				appInfo.packageName = packageInfo.packageName;
				//5��ȡӦ������
				ApplicationInfo applicationInfo = packageInfo.applicationInfo;
				appInfo.name=applicationInfo.loadLabel(pm).toString();
				//6��ȡͼ��
				appInfo.icon=applicationInfo.loadIcon(pm);
				//7�ж��Ƿ�ΪϵͳӦ�ã�ÿһ���ֻ��ϵ�Ӧ������Ӧ��flage����һ����
				if((applicationInfo.flags&ApplicationInfo.FLAG_SYSTEM)==ApplicationInfo.FLAG_SYSTEM){
					//ϵͳӦ��
					appInfo.isSystem=true;
				}else{
					//�û�Ӧ��
					appInfo.isSystem=false;
				}
				if((applicationInfo.flags&ApplicationInfo.FLAG_EXTERNAL_STORAGE)==ApplicationInfo.FLAG_EXTERNAL_STORAGE){
					//ϵͳӦ��
					appInfo.isSdCard=true;
				}else{
					//�û�Ӧ��
					appInfo.isSdCard=false;
				}
				appinfoList.add(appInfo);
			}
    	   return appinfoList;
      }
}
