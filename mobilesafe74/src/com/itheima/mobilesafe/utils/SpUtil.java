package com.itheima.mobilesafe.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SpUtil {
    private static SharedPreferences sp;

	//д��boolean��ʾ��sp��
	/**
	 * @param ctx �����Ļ���
	 * @param key �洢�ڵ�����
	 * @param value �洢�ڵ��ֵboolean
	 */
	public static void putBoolean(Context ctx,String key,boolean value){
		
		if(sp==null){
		sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
		}
		sp.edit().putBoolean(key,value).commit();
	} 
	
	
	/**
	 * @param ctx �����Ļ���
	 * @param key �洢�ڵ�����
	 * @param value û�д˽ڵ�Ĭ��ֵ
	 * @return Ĭ��ֵ���ߴ˽ڵ��ȡ����ֵ
	 */
	//��ȡboolean��ʾ��sp��
	public static boolean getBoolean(Context ctx,String key,boolean defvalue){
		if(sp==null){
			sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
			}
			return sp.getBoolean(key, defvalue);
	}
	
	
	//д��String��ʾ��sp��
		/**
		 * @param ctx �����Ļ���
		 * @param key �洢�ڵ�����
		 * @param value �洢�ڵ��ֵString
		 */
		public static void putString(Context ctx,String key,String value){
			
			if(sp==null){
				
			sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
			}
			sp.edit().putString(key, value).commit();
		} 
		
		
		/**
		 * @param ctx �����Ļ���
		 * @param key �洢�ڵ�����
		 * @param value û�д˽ڵ�Ĭ��ֵ
		 * @return Ĭ��ֵ���ߴ˽ڵ��ȡ����ֵ
		 */
		//��ȡString��ʾ��sp��
		public static String getString(Context ctx,String key,String defvalue){
			if(sp==null){
				sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
				}
				return sp.getString(key, defvalue);
		}
		
		
		
		//д��String��ʾ��sp��
				/**
				 * @param ctx �����Ļ���
				 * @param key �洢�ڵ�����
				 * @param value �洢�ڵ��ֵint
				 */
				public static void putInt(Context ctx,String key,int value){
					
					if(sp==null){
						
					sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
					}
					sp.edit().putInt(key, value).commit();
				} 
				
				
				/**
				 * @param ctx �����Ļ���
				 * @param toastStyle �洢�ڵ�����
				 * @param value û�д˽ڵ�Ĭ��ֵ
				 * @return Ĭ��ֵ���ߴ˽ڵ��ȡ����ֵ
				 */
				//��ȡString��ʾ��sp��
				public static int getInt(Context ctx,String toastStyle,int defvalue){
					if(sp==null){
						sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
						}
						return sp.getInt(toastStyle, defvalue);
				}
				
				
				
		/**
		 * @param ctx �����Ļ���
		 * @param simNumber Ҫ�Ƴ���ֵ
		 */
		public static void remove(Context ctx, String simNumber) {
			if(sp==null){
				
				sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
				}
			    //Ҫ����ֵ���õ�edit�����Ҫ�ǵ��ύcommit
				sp.edit().remove(simNumber).commit();
		}
}
