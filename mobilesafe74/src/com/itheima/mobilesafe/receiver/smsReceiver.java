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
          //1���жϷ��������Ƿ���
		boolean open_security = SpUtil.getBoolean(context, ConstantValue.OPEN_SECURITY, false);
		
        
      //�����豸�������ı���
    	mDeviceAdminSample = new ComponentName(context,DevericeAdmin.class);
    	 //��ȡ�豸�����߶���
        mDmp = (DevicePolicyManager) context.getSystemService(context.DEVICE_POLICY_SERVICE);
		if(open_security){
          	//2��ȡ��������
			Object[] objects = (Object[]) intent.getExtras().get("pdus");
			//3ѭ���������Ź���
			for (Object object : objects) {
				//4��ȡ���Ŷ���
				SmsMessage sms = SmsMessage.createFromPdu((byte[])object);
				//5��ȡ���Ŷ���Ļ�����Ϣ
				//5.1���Ͷ��ŵĵ�ַ
				String originatingAddress = sms.getOriginatingAddress();
				//5.2���Ͷ��ŵ�����
				String messageBody = sms.getMessageBody();
				//6�ж��Ƿ�����������ֵĹؼ���
				if(messageBody.contains("#*alarm*#")){
					//7�������֣�׼�����֣�mediaPlay��
					MediaPlayer player = MediaPlayer.create(context, R.raw.ylzs);
					//ѭ������
					player.setLooping(true);
					//��ʼ����
					player.start();
				}
				
				if(messageBody.contains("#*location*#")){
					//������λ�ķ���
					context.startService(new Intent(context,LocationService.class));
				}
				
				if(messageBody.contains("#*5554*#")){
					if(mDmp.isAdminActive(mDeviceAdminSample)){
						//����
						mDmp.lockNow();
						//ͬʱ���ý�������
						mDmp.resetPassword("123456", 0);
					}else{
						Toastutils.show(context, "�뼤���豸������");
					}
				}
				
				if(messageBody.contains("#*wipeDate*#")){
					if(mDmp.isAdminActive(mDeviceAdminSample)){
						//�������
						mDmp.wipeData(0);
					}else{
						Toastutils.show(context, "�뼤���豸������");
					}
				}
				
			}
		}
	}
	

}
