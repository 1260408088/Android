package com.example.fragment;


import com.example.shark.R;
import com.example.shark.SpUtiles;
import com.king.three.FanBtn;
import com.king.three.FanBtn.OnDownActionListener;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import android.widget.Toast;

public class LeftFragment extends Fragment{
	public Activity mActivity;//这个activity就是MainActivity
	private TextView tv_smart;
	public FanBtn fanBtn;

	// Fragment创建
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivity = getActivity();// 获取当前fragment所依赖的activity
		
	}

	// 初始化fragment的布局
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		    View view = initView();
		    return view;
	}
	
	// fragment所依赖的activity的onCreate方法执行结束
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// 初始化数据
		initData();
	}
	
	// 初始化布局,
	public  View initView(){
		View view = View.inflate(mActivity, R.layout.left_menu_fragment, null);
		tv_smart = (TextView) view.findViewById(R.id.tv_smart);
		fanBtn = (FanBtn) view.findViewById(R.id.fanbtn);
		
		
		//Toast.makeText(mActivity, level+"", 0).show();
		
		fanBtn.setOnDownActionListener(new OnDownActionListener() {
			@Override
			public void OnDown(int level) {
				backDisplay(level,false);
			}
		});
		
		//设置调动事件
		return view;
		
	}
	// 初始化数据, 
	public  void initData(){
		//用于回显数据
		int smart = SpUtiles.getInt(mActivity, "smart", 30);
		int level=0;
		if(smart==38){
			level=1;
		}else if(smart==35){
			level=2;
		}else if(smart==30){
			level=3;
		}
		backDisplay(level,true);
		
		
		//测量自定义控件的宽高
		ViewTreeObserver vto = fanBtn.getViewTreeObserver(); 
		vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() { 
			
		public boolean onPreDraw() { 
			fanBtn.getViewTreeObserver().removeOnPreDrawListener(this); 
			int height = fanBtn.getMeasuredHeight();    //getHeight得到的都是px
			int width = fanBtn.getMeasuredWidth(); 
			System.out.println("方法三: height:"+height + ",width:" + width + "..\n"); 
			return true; 
			} 
			
		}); 
	}
	
	public void backDisplay(int level,boolean isback){
		switch (level) {
		case 1:
			SpUtiles.putInt(mActivity, "smart", 38);
			tv_smart.setText("低");
			break;
		case 2:
			SpUtiles.putInt(mActivity, "smart", 35);
			tv_smart.setText("中");					
			break;
		case 3:
			SpUtiles.putInt(mActivity, "smart", 30);
			tv_smart.setText("高");
			break;
		}
		if(isback){
			fanBtn.setLevel(level);
		}
	}
}
