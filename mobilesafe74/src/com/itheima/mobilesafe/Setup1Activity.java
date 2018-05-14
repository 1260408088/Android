package com.itheima.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Setup1Activity extends Activity {
     @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_setup1);
    	
    }
     
    public void nexBut(View v){
    	//开启第二个设置页面
    	Intent intent = new Intent(this,Setup2Activity.class);
    	startActivity(intent);
    	//下一页的移入移出动画
    	overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);
    	finish();
    }

	

}
