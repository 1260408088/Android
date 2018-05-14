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
     * 返回当前收集所有的应用的相关信息（名称，包名，图标，（手机内存））
     * （系统，用户）
     */
    public static List<AppInfo> getAppInfoList(Context ctx){
    	  //1包的管理者对象
    	   PackageManager pm = ctx.getPackageManager();
    	   //2获取安装在手机上应用相关信息的集合
    	   List<PackageInfo> packList=pm.getInstalledPackages(0);
    	   //放入list中方便放入listView
    	   ArrayList<AppInfo> appinfoList = new ArrayList<AppInfo>();
    	   //3.循环遍历应用信息的集合
    		for (PackageInfo packageInfo : packList) {
				AppInfo appInfo = new AppInfo();
				//4获取应用的包名
				appInfo.packageName = packageInfo.packageName;
				//5获取应用名称
				ApplicationInfo applicationInfo = packageInfo.applicationInfo;
				appInfo.name=applicationInfo.loadLabel(pm).toString();
				//6获取图标
				appInfo.icon=applicationInfo.loadIcon(pm);
				//7判断是否为系统应用（每一个手机上的应用所对应的flage都不一样）
				if((applicationInfo.flags&ApplicationInfo.FLAG_SYSTEM)==ApplicationInfo.FLAG_SYSTEM){
					//系统应用
					appInfo.isSystem=true;
				}else{
					//用户应用
					appInfo.isSystem=false;
				}
				if((applicationInfo.flags&ApplicationInfo.FLAG_EXTERNAL_STORAGE)==ApplicationInfo.FLAG_EXTERNAL_STORAGE){
					//系统应用
					appInfo.isSdCard=true;
				}else{
					//用户应用
					appInfo.isSdCard=false;
				}
				appinfoList.add(appInfo);
			}
    	   return appinfoList;
      }
}
