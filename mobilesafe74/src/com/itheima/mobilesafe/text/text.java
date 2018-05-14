package com.itheima.mobilesafe.text;

import java.util.Random;

import com.itheima.mobilesafe.dao.BlackNumberDao;

import android.test.AndroidTestCase;

public class text extends AndroidTestCase {
       public void add(){
    	   BlackNumberDao dao = BlackNumberDao.getInstance(getContext());
    	   for(int i=0;i<100;i++){
   			if(i<10){
   				dao.insert("1860000000"+i, 1+new Random().nextInt(3)+"");
   			}else{
   				dao.insert("186000000"+i, 1+new Random().nextInt(3)+"");
   			}
   		}
       }
//       public void del(){
//    	   BlackNumberDao dao = BlackNumberDao.getInstance(getContext());
//    	   dao.del("1860000000");
//       }
//       
//       public void update(){
//    	   BlackNumberDao dao = BlackNumberDao.getInstance(getContext());
//    	   dao.update("186000000", "2");
//       }
//       
//       public void query(){
//    	   BlackNumberDao dao = BlackNumberDao.getInstance(getContext());
//    	   dao.findAll();
//       }
}
