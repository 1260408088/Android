package com.itcast.switchs.ui;

import com.itcast.switchs.ui.ToggleView.OnSwitchStateUpdateListener;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


/**
 * 自定义开关
 * @author poplar
 * 
 * Android 的界面绘制流程
 * 测量			 摆放		绘制
 * measure	->	layout	->	draw
 * 	  | 		  |			 |
 * onMeasure -> onLayout -> onDraw 重写这些方法, 实现自定义控件
 * 
 * onResume()之后执行
 * 
 * View
 * onMeasure() (在这个方法里指定自己的宽高) -> onDraw() (绘制自己的内容)
 * 
 * ViewGroup
 * onMeasure() (指定自己的宽高, 所有子View的宽高)-> onLayout() (摆放所有子View) -> onDraw() (绘制内容)
 */
public class ToggleView extends View {

	private Bitmap switchBackgroupBitmap;
	private Bitmap slideButtonBitmap;
	private Paint paint;
    private boolean mSwitchState=false;
    private float current;
    private OnSwitchStateUpdateListener onSwitchStateUpdateListener2;
	/**
	 * 用于代码创建控件
	 * @param context
	 */
	public ToggleView(Context context) {
		super(context);
		init();
	}

	/**用于在xml里使用，可指定自定义属性
	 * @param context
	 * @param attrs
	 */
	public ToggleView(Context context, AttributeSet attrs){
		super(context, attrs);
		init();
		String namespace="http://schemas.android.com/apk/res/com.itcast.switchs";
		//获取配置的自定义属性
		int backgroundResource=attrs.getAttributeResourceValue(namespace, "switch_background",-1);
		int buttonResource= attrs.getAttributeResourceValue(namespace, "slide_button",-1);
		//设置背景图片，和滑块的图片
		mSwitchState= attrs.getAttributeBooleanValue(namespace, "switch_state", false);
		
		setSwitchBackgroundResource(backgroundResource);
		setSlideButtonRessource(buttonResource);
	}

	/**
	 * 用于在xml里使用，可指定自定义属性
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public ToggleView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	
	//创建一个画板
	private void init(){
		paint = new Paint();
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		//设置宽高
		setMeasuredDimension(switchBackgroupBitmap.getWidth(), switchBackgroupBitmap.getHeight());
	}
	
	
	//Canvas 画板画布，在上面绘制的内容都会显示到界面上
	@Override
	protected void onDraw(Canvas canvas) {
		//1.绘制背景
		canvas.drawBitmap(switchBackgroupBitmap, 0, 0, paint);
		//2.绘制滑块
		//根据当前用户触摸到位置画
		
		if(isTouchMode){
			//根据当前用户触摸到的位置画滑块
			float newleft=current-slideButtonBitmap.getWidth()/2.0f;
			
			int Maxleft=switchBackgroupBitmap.getWidth()-slideButtonBitmap.getWidth();
			// 限定滑块的范围
			if(newleft<0){
				newleft=0;
			}else if(newleft > Maxleft){
				newleft=Maxleft;
			}
			canvas.drawBitmap(slideButtonBitmap,newleft, 0, paint);
		}else{
		if(mSwitchState){
			//根据开关状态     用背景图片的宽度-滑块宽度
			int newleft=switchBackgroupBitmap.getWidth()-slideButtonBitmap.getWidth();
			canvas.drawBitmap(slideButtonBitmap, newleft, 0, paint);
			
		}else{
			canvas.drawBitmap(slideButtonBitmap, 0, 0, paint);
		 }
		}
		super.onDraw(canvas);
	}
	
	boolean isTouchMode=false;
	
	
	
	
	
	//重写触摸事件，相应用户的触摸
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()){
		case MotionEvent.ACTION_DOWN :
			isTouchMode=true;
			current=event.getX();
			
			break;

		case MotionEvent.ACTION_MOVE :
			current=event.getX();
			
			break;
			
		case MotionEvent.ACTION_UP :
			isTouchMode=false;
			current=event.getX();
			float center=switchBackgroupBitmap.getWidth()/2.0f;
			//比较控件的位置，判断是否为大于按钮的一半
			boolean state=current>center;
			
			//如果开关状态变化了,通知界面，里边开关状态更新
			if(state!=mSwitchState&&onSwitchStateUpdateListener2!=null){
				// 把最新的boolean, 状态传出去了
				onSwitchStateUpdateListener2.onStateUpdate(state);
			}
			
			mSwitchState=state;
			break;
		}
		
		//重绘主界面
		invalidate();//会引发ondraw被调用，里面的变量会重新生效，界面会更新
		
		return true; //消费触摸事件，响应用户的触摸
	}
	
	/**
	 * 设置背景图片
	 * @param switchBackground
	 */
	public void setSwitchBackgroundResource(int switchBackground){
		switchBackgroupBitmap = BitmapFactory.decodeResource(getResources(), switchBackground);
	}

	/**
	 * 设置滑块图片资源
	 * @param slideButton
	 */
	public void setSlideButtonRessource(int slideButton) {
		slideButtonBitmap = BitmapFactory.decodeResource(getResources(),slideButton);
	}

	/**
	 * 设置开关状态
	 * @param b
	 */
	public void setSwitchState(boolean mSwitchState) {
		        this.mSwitchState=mSwitchState;
	}
	
	
	
	
	//回调的接口
	public interface OnSwitchStateUpdateListener{
		        void onStateUpdate(boolean state);
	}

	//回调的方法
	public void setOnSwitchStateUpdateListener(OnSwitchStateUpdateListener onSwitchStateUpdateListener2) {
		
				this.onSwitchStateUpdateListener2 = onSwitchStateUpdateListener2;
		
	}
	
}
	

