package com.itheima.mobilesafe.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SpUtil {
    private static SharedPreferences sp;

	//写入boolean标示到sp中
	/**
	 * @param ctx 上下文环境
	 * @param key 存储节点名称
	 * @param value 存储节点的值boolean
	 */
	public static void putBoolean(Context ctx,String key,boolean value){
		
		if(sp==null){
		sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
		}
		sp.edit().putBoolean(key,value).commit();
	} 
	
	
	/**
	 * @param ctx 上下文环境
	 * @param key 存储节点名称
	 * @param value 没有此节点默认值
	 * @return 默认值或者此节点读取到的值
	 */
	//读取boolean标示从sp中
	public static boolean getBoolean(Context ctx,String key,boolean defvalue){
		if(sp==null){
			sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
			}
			return sp.getBoolean(key, defvalue);
	}
	
	
	//写入String标示到sp中
		/**
		 * @param ctx 上下文环境
		 * @param key 存储节点名称
		 * @param value 存储节点的值String
		 */
		public static void putString(Context ctx,String key,String value){
			
			if(sp==null){
				
			sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
			}
			sp.edit().putString(key, value).commit();
		} 
		
		
		/**
		 * @param ctx 上下文环境
		 * @param key 存储节点名称
		 * @param value 没有此节点默认值
		 * @return 默认值或者此节点读取到的值
		 */
		//读取String标示从sp中
		public static String getString(Context ctx,String key,String defvalue){
			if(sp==null){
				sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
				}
				return sp.getString(key, defvalue);
		}
		
		
		
		//写入String标示到sp中
				/**
				 * @param ctx 上下文环境
				 * @param key 存储节点名称
				 * @param value 存储节点的值int
				 */
				public static void putInt(Context ctx,String key,int value){
					
					if(sp==null){
						
					sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
					}
					sp.edit().putInt(key, value).commit();
				} 
				
				
				/**
				 * @param ctx 上下文环境
				 * @param toastStyle 存储节点名称
				 * @param value 没有此节点默认值
				 * @return 默认值或者此节点读取到的值
				 */
				//读取String标示从sp中
				public static int getInt(Context ctx,String toastStyle,int defvalue){
					if(sp==null){
						sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
						}
						return sp.getInt(toastStyle, defvalue);
				}
				
				
				
		/**
		 * @param ctx 上下文环境
		 * @param simNumber 要移除的值
		 */
		public static void remove(Context ctx, String simNumber) {
			if(sp==null){
				
				sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
				}
			    //要更改值会用到edit，最后要记得提交commit
				sp.edit().remove(simNumber).commit();
		}
}
