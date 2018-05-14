package com.itheima.mobilesafe;

import com.itheima.mobilesafe.utils.SpUtil;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;

public class ToastLocationActivity extends Activity {
       private ImageView iv_drag;
	private Button bt_top;
	private Button bt_bottom;
	private WindowManager mWM;
	private int mScreenHeight;
	private int mScreenWidth;
	private long startTime=0;
	private long[] mHits=new long[2];

	@Override
    protected void onCreate(Bundle savedInstanceState) {
    	setContentView(R.layout.activity_toast_location);
    	super.onCreate(savedInstanceState);
    	initUI();
    }

	private void initUI() {
		  iv_drag = (ImageView) findViewById(R.id.iv_drag);
		  bt_top = (Button) findViewById(R.id.bt_top);
		  bt_bottom = (Button) findViewById(R.id.bt_bottom);
		  //��ô��ڵĹ����ߣ���Ļ�Ŀ�Ⱥ͸߶ȣ�
		  mWM = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		  mScreenHeight = mWM.getDefaultDisplay().getHeight();
		  mScreenWidth = mWM.getDefaultDisplay().getWidth();
		  
		  //imageviewλ�õĻ���
		  int locationX = SpUtil.getInt(getApplicationContext(), ConstantValue.LOCATION_X, 0);
		  int locationY = SpUtil.getInt(getApplicationContext(), ConstantValue.LOCATION_Y, 0);
		  //���Ͻ�����������iv_drag��
		  //iv_drag����Բ�����,���������������Ҫ����Բ������ṩ
		  
		  //ָ��߶�Ϊwrap_content
		  LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
				  RelativeLayout.LayoutParams.WRAP_CONTENT);
		  
		  //�����Ͻǵ�����������iv_drag��Ӧ���������
		  layoutParams.leftMargin=locationX;
		  layoutParams.topMargin=locationY;
		  //�����Ϲ���������iv_drag�� 
		  iv_drag.setLayoutParams(layoutParams);
		  
		  //buttonλ�õĻ���
		  if(locationY>(mScreenHeight-22)/2){
				bt_top.setVisibility(View.VISIBLE);
				bt_bottom.setVisibility(View.INVISIBLE);
			}else{
				bt_top.setVisibility(View.INVISIBLE);
				bt_bottom.setVisibility(View.VISIBLE);
			}
		  
		  iv_drag.setOnClickListener(new OnClickListener() {
			
			 //������ξ�����ʾ 
			@Override
			public void onClick(View v) {
				 //1,ԭ����(Ҫ�����ĵ�����)
				  //2,ԭ����Ŀ�����ʼλ������ֵ
				  //3,Ŀ������(ԭ��������ݡ���������������Ŀ������)
				  //4,Ŀ�����������ֵ����ʼ����λ��
			      //5�������ĳ���
				  System.arraycopy(mHits, 1, mHits, 0, mHits.length-1);
				  mHits[mHits.length-1]=SystemClock.uptimeMillis();
				  if(mHits[mHits.length-1]-mHits[0]<500){
					  int top=mScreenHeight/2 -iv_drag.getHeight()/2;
					  int bottom=mScreenHeight/2 +iv_drag.getHeight()/2;
					  int left=mScreenWidth/2-iv_drag.getWidth()/2;
					  int right=mScreenWidth/2+iv_drag.getWidth()/2;
					  
					  //�ؼ�������ԭ����ʾ
					  iv_drag.layout(left, top, right, bottom);
					  
					  //�洢��ǰ��״̬��
					  SpUtil.putInt(getApplicationContext(), ConstantValue.LOCATION_X, iv_drag.getLeft());
					  SpUtil.putInt(getApplicationContext(), ConstantValue.LOCATION_Y, iv_drag.getTop());
					  
				  }
				
			}
		});
		  
		  //ΪiamgeView���ô��������¼������£��ƶ���̧��
		  iv_drag.setOnTouchListener(new OnTouchListener() {
			  
			private int rawX;
			private int rawY;
			private int disx;
			private int disy;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					//1��ȡ��ʼ������
					rawX = (int) event.getRawX();
					rawY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_MOVE:
					//2����ƶ��е�����
					int moveX = (int)event.getRawX();
					int moveY = (int)event.getRawY();
					//3�����뿪ʼ����Ĳ�ֵ
					
					disx = moveX-rawX;
					disy = moveY-rawY;
					
					//3,��ȡ��ǰ�ؼ�������Ļ�ģ����ϣ��ǵ�λ��
					int left=iv_drag.getLeft()+disx;
					int top=iv_drag.getTop()+disy;
					int right=iv_drag.getRight()+disx;
					int bottom=iv_drag.getBottom()+disy;
					
					//����imageview��λ�ã��л�button����ʾλ��
					if(top>(mScreenHeight-22)/2){
						bt_top.setVisibility(View.VISIBLE);
						bt_bottom.setVisibility(View.INVISIBLE);
					}else{
						bt_top.setVisibility(View.INVISIBLE);
						bt_bottom.setVisibility(View.VISIBLE);
					}
					//�ݴ���(iv_drag���������ֻ���Ļ)
					if(left<0){
						return true;
					}if(right>mScreenWidth){
						return true;
					}if(top<0){
						return true;
					}if(bottom>mScreenHeight-22){
						return true;
					}
					
					//2��֪�ƶ��Ŀؼ������������������ȥ��չʾ
					iv_drag.layout(left,top,right,bottom);
					//4.���³�ʼ����
					rawX = (int) event.getRawX();
					rawY = (int) event.getRawY();
					
					break;
				case MotionEvent.ACTION_UP:
					//�洢���ƶ���λ��
					SpUtil.putInt(getApplicationContext(), ConstantValue.LOCATION_X, iv_drag.getLeft());
					SpUtil.putInt(getApplicationContext(), ConstantValue.LOCATION_Y, iv_drag.getTop());
					
					break;

				
				}
				//����true��Ӧ�¼�������false����Ӧ�¼�
				//�����д����¼��͵���¼�����Ҫ��Ϊfalse
				return false;
			}
		});
		  
		  
	}
}
