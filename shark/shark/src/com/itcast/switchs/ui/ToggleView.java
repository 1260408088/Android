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
 * �Զ��忪��
 * @author poplar
 * 
 * Android �Ľ����������
 * ����			 �ڷ�		����
 * measure	->	layout	->	draw
 * 	  | 		  |			 |
 * onMeasure -> onLayout -> onDraw ��д��Щ����, ʵ���Զ���ؼ�
 * 
 * onResume()֮��ִ��
 * 
 * View
 * onMeasure() (�����������ָ���Լ��Ŀ��) -> onDraw() (�����Լ�������)
 * 
 * ViewGroup
 * onMeasure() (ָ���Լ��Ŀ��, ������View�Ŀ��)-> onLayout() (�ڷ�������View) -> onDraw() (��������)
 */
public class ToggleView extends View {

	private Bitmap switchBackgroupBitmap;
	private Bitmap slideButtonBitmap;
	private Paint paint;
    private boolean mSwitchState=false;
    private float current;
    private OnSwitchStateUpdateListener onSwitchStateUpdateListener2;
	/**
	 * ���ڴ��봴���ؼ�
	 * @param context
	 */
	public ToggleView(Context context) {
		super(context);
		init();
	}

	/**������xml��ʹ�ã���ָ���Զ�������
	 * @param context
	 * @param attrs
	 */
	public ToggleView(Context context, AttributeSet attrs){
		super(context, attrs);
		init();
		String namespace="http://schemas.android.com/apk/res/com.itcast.switchs";
		//��ȡ���õ��Զ�������
		int backgroundResource=attrs.getAttributeResourceValue(namespace, "switch_background",-1);
		int buttonResource= attrs.getAttributeResourceValue(namespace, "slide_button",-1);
		//���ñ���ͼƬ���ͻ����ͼƬ
		mSwitchState= attrs.getAttributeBooleanValue(namespace, "switch_state", false);
		
		setSwitchBackgroundResource(backgroundResource);
		setSlideButtonRessource(buttonResource);
	}

	/**
	 * ������xml��ʹ�ã���ָ���Զ�������
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public ToggleView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	
	//����һ������
	private void init(){
		paint = new Paint();
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		//���ÿ��
		setMeasuredDimension(switchBackgroupBitmap.getWidth(), switchBackgroupBitmap.getHeight());
	}
	
	
	//Canvas ���廭������������Ƶ����ݶ�����ʾ��������
	@Override
	protected void onDraw(Canvas canvas) {
		//1.���Ʊ���
		canvas.drawBitmap(switchBackgroupBitmap, 0, 0, paint);
		//2.���ƻ���
		//���ݵ�ǰ�û�������λ�û�
		
		if(isTouchMode){
			//���ݵ�ǰ�û���������λ�û�����
			float newleft=current-slideButtonBitmap.getWidth()/2.0f;
			
			int Maxleft=switchBackgroupBitmap.getWidth()-slideButtonBitmap.getWidth();
			// �޶�����ķ�Χ
			if(newleft<0){
				newleft=0;
			}else if(newleft > Maxleft){
				newleft=Maxleft;
			}
			canvas.drawBitmap(slideButtonBitmap,newleft, 0, paint);
		}else{
		if(mSwitchState){
			//���ݿ���״̬     �ñ���ͼƬ�Ŀ��-������
			int newleft=switchBackgroupBitmap.getWidth()-slideButtonBitmap.getWidth();
			canvas.drawBitmap(slideButtonBitmap, newleft, 0, paint);
			
		}else{
			canvas.drawBitmap(slideButtonBitmap, 0, 0, paint);
		 }
		}
		super.onDraw(canvas);
	}
	
	boolean isTouchMode=false;
	
	
	
	
	
	//��д�����¼�����Ӧ�û��Ĵ���
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
			//�ȽϿؼ���λ�ã��ж��Ƿ�Ϊ���ڰ�ť��һ��
			boolean state=current>center;
			
			//�������״̬�仯��,֪ͨ���棬��߿���״̬����
			if(state!=mSwitchState&&onSwitchStateUpdateListener2!=null){
				// �����µ�boolean, ״̬����ȥ��
				onSwitchStateUpdateListener2.onStateUpdate(state);
			}
			
			mSwitchState=state;
			break;
		}
		
		//�ػ�������
		invalidate();//������ondraw�����ã�����ı�����������Ч����������
		
		return true; //���Ѵ����¼�����Ӧ�û��Ĵ���
	}
	
	/**
	 * ���ñ���ͼƬ
	 * @param switchBackground
	 */
	public void setSwitchBackgroundResource(int switchBackground){
		switchBackgroupBitmap = BitmapFactory.decodeResource(getResources(), switchBackground);
	}

	/**
	 * ���û���ͼƬ��Դ
	 * @param slideButton
	 */
	public void setSlideButtonRessource(int slideButton) {
		slideButtonBitmap = BitmapFactory.decodeResource(getResources(),slideButton);
	}

	/**
	 * ���ÿ���״̬
	 * @param b
	 */
	public void setSwitchState(boolean mSwitchState) {
		        this.mSwitchState=mSwitchState;
	}
	
	
	
	
	//�ص��Ľӿ�
	public interface OnSwitchStateUpdateListener{
		        void onStateUpdate(boolean state);
	}

	//�ص��ķ���
	public void setOnSwitchStateUpdateListener(OnSwitchStateUpdateListener onSwitchStateUpdateListener2) {
		
				this.onSwitchStateUpdateListener2 = onSwitchStateUpdateListener2;
		
	}
	
}
	

