package com.example.shark;
import android.content.SharedPreferences;
import android.content.Context;

public class SpUtiles {
	   private static SharedPreferences sp;
	   
      /**
     * @param ctx �����Ļ���
     * @param key �洢���ݵ�����
     * @param value �洢��ֵ
     */
    public static void putBoolean(Context ctx,String key,boolean value){
    	     //�ж��Ƿ����
    	     if(sp==null){
    	    	  sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
    	     }
    	     sp.edit().putBoolean(key, value).commit();
      }
    
    /**
     * @param ctx �����ʻ���
     * @param key �洢�����ݵ�����
     * @param defvalue Ĭ�ϵ�ֵ 
     * @return
     */
    public static boolean getBoolean(Context ctx,String key,boolean defvalue){
	     //�ж��Ƿ����
	     if(sp==null){
	    	 sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
	     }
	     
	     return sp.getBoolean(key, defvalue);
 }
    /**
     * @param ctx �����ʻ���
     * @param key �洢�����ݵ�����
     * @param value Ĭ�ϵ�ֵ 
     * @return
     */
    
    public static void putString(Context ctx,String key,String value){
	     //�ж��Ƿ����
	     if(sp==null){
	    	  sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
	     }
	     sp.edit().putString(key, value).commit();
 }
  
    /**
     * @param ctx �����ʻ���
     * @param key �洢�����ݵ�����
     * @param value Ĭ�ϵ�ֵ 
     * @return
     */
    public static String getString(Context ctx,String key,String value){
	     //�ж��Ƿ����
	     if(sp==null){
	    	  sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
	     }
	     return sp.getString(key, value);
}
    
    public static void putInt(Context ctx,String key,int value){
	     //�ж��Ƿ����
	     if(sp==null){
	    	  sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
	     }
	     sp.edit().putInt(key, value).commit();
}
 
   /**
    * @param ctx �����ʻ���
    * @param key �洢�����ݵ�����
    * @param value Ĭ�ϵ�ֵ 
    * @return
    */
   public static int getInt(Context ctx,String key,int value){
	     //�ж��Ƿ����
	     if(sp==null){
	    	  sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
	     }
	     return sp.getInt(key, value);
}
  
}
