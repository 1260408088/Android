package com.itheima.mobilesafe.receiver;

import com.itheima.mobilesafe.ConstantValue;
import com.itheima.mobilesafe.DevericeAdmin;
import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.Toastutils;
import com.itheima.mobilesafe.service.LocationService;
import com.itheima.mobilesafe.utils.SpUtil;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.provider.MediaStore.Audio.Media;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class smsReceiver extends BroadcastReceiver {
	private ComponentName mDeviceAdminSample;
    private DevicePolicyManager mDmp;
	@Override
	public void onReceive(Context context, Intent intent) {
          //1先判断防盗保护是否开启
		boolean open_security = SpUtil.getBoolean(context, ConstantValue.OPEN_SECURITY, false);
		
        
      //开启设备管理器的变量
    	mDeviceAdminSample = new ComponentName(context,DevericeAdmin.class);
    	 //获取设备管理者对象
        mDmp = (DevicePolicyManager) context.getSystemService(context.DEVICE_POLICY_SERVICE);
		if(open_security){
          	//2获取短信内容
			Object[] objects = (Object[]) intent.getExtras().get("pdus");
			//3循环遍历短信过程
			for (Object object : objects) {
				//4获取短信对象
				SmsMessage sms = SmsMessage.createFromPdu((byte[])object);
				//5获取短信对象的基本信息
				//5.1发送短信的地址
				String originatingAddress = sms.getOriginatingAddress();
				//5.2发送短信的内容
				String messageBody = sms.getMessageBody();
				//6判断是否包含播放音乐的关键字
				if(messageBody.contains("#*alarm*#")){
					//7播放音乐（准备音乐，mediaPlay）
					MediaPlayer player = MediaPlayer.create(context, R.raw.ylzs);
					//循环播放
					player.setLooping(true);
					//开始播放
					player.start();
				}
				
				if(messageBody.contains("#*location*#")){
					//开启定位的服务
					context.startService(new Intent(context,LocationService.class));
				}
				
				if(messageBody.contains("#*5554*#")){
					if(mDmp.isAdminActive(mDeviceAdminSample)){
						//锁屏
						mDmp.lockNow();
						//同时设置解锁密码
						mDmp.resetPassword("123456", 0);
					}else{
						Toastutils.show(context, "请激活设备管理器");
					}
				}
				
				if(messageBody.contains("#*wipeDate*#")){
					if(mDmp.isAdminActive(mDeviceAdminSample)){
						//清除数据
						mDmp.wipeData(0);
					}else{
						Toastutils.show(context, "请激活设备管理器");
					}
				}
				
			}
		}
	}
	

}
