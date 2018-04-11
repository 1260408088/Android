package com.example.shark;
import android.content.SharedPreferences;
import android.content.Context;

public class SpUtiles {
	   private static SharedPreferences sp;
	   
      /**
     * @param ctx 上下文环境
     * @param key 存储数据的名称
     * @param value 存储的值
     */
    public static void putBoolean(Context ctx,String key,boolean value){
    	     //判断是否存在
    	     if(sp==null){
    	    	  sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
    	     }
    	     sp.edit().putBoolean(key, value).commit();
      }
    
    /**
     * @param ctx 上下问环境
     * @param key 存储的数据的名称
     * @param defvalue 默认的值 
     * @return
     */
    public static boolean getBoolean(Context ctx,String key,boolean defvalue){
	     //判断是否存在
	     if(sp==null){
	    	 sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
	     }
	     
	     return sp.getBoolean(key, defvalue);
 }
    /**
     * @param ctx 上下问环境
     * @param key 存储的数据的名称
     * @param value 默认的值 
     * @return
     */
    
    public static void putString(Context ctx,String key,String value){
	     //判断是否存在
	     if(sp==null){
	    	  sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
	     }
	     sp.edit().putString(key, value).commit();
 }
  
    /**
     * @param ctx 上下问环境
     * @param key 存储的数据的名称
     * @param value 默认的值 
     * @return
     */
    public static String getString(Context ctx,String key,String value){
	     //判断是否存在
	     if(sp==null){
	    	  sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
	     }
	     return sp.getString(key, value);
}
    
    public static void putInt(Context ctx,String key,int value){
	     //判断是否存在
	     if(sp==null){
	    	  sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
	     }
	     sp.edit().putInt(key, value).commit();
}
 
   /**
    * @param ctx 上下问环境
    * @param key 存储的数据的名称
    * @param value 默认的值 
    * @return
    */
   public static int getInt(Context ctx,String key,int value){
	     //判断是否存在
	     if(sp==null){
	    	  sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
	     }
	     return sp.getInt(key, value);
}
  
}
