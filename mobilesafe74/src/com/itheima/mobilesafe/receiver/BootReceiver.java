package com.itheima.mobilesafe.receiver;

import com.itheima.mobilesafe.ConstantValue;
import com.itheima.mobilesafe.utils.SpUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.telephony.gsm.SmsManager;


public class BootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
   		//1��ȡ���ص�sim�����к�
		String locSimnumber = SpUtil.getString(context, ConstantValue.SIM_NUMBER, "")+"xxx";
		//2��ȡ�������sim�����к�
		TelephonyManager manger = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		//2.1��ȡSIM�����к�
		String simSerialNumber = manger.getSimSerialNumber();
		if(!simSerialNumber.equals(locSimnumber)){
			boolean opensafe = SpUtil.getBoolean(context, ConstantValue.OPEN_SECURITY, false);
			
			if(opensafe){
				SmsManager sm = SmsManager.getDefault();
				String safenumber = SpUtil.getString(context, ConstantValue.CONTACT_PHONE, "");
				sm.sendTextMessage(safenumber, null, "sim change", null, null);
				
			}
			
		
			
			
		}
	}

}
