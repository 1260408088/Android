package com.itheima.mobilesafe;

import android.content.Context;
import android.widget.Toast;

public class Toastutils {
	   //��ӡ��˾
       /**
     * @param con�����Ļ���
     * @param msg ��ӡ���ı�����
     */
    public static void show(Context con,String msg){
    	   Toast.makeText(con, msg, 0).show();
       }
}
