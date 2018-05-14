package com.itheima.mobilesafe;

import android.content.Context;
import android.widget.Toast;

public class Toastutils {
	   //打印吐司
       /**
     * @param con上下文环境
     * @param msg 打印的文本内容
     */
    public static void show(Context con,String msg){
    	   Toast.makeText(con, msg, 0).show();
       }
}
