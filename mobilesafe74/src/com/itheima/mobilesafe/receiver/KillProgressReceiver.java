package com.itheima.mobilesafe.receiver;

import com.itheima.mobilesafe.engine.ProgressInfoProvider;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class KillProgressReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		//��ɱ���������ڴ�
		ProgressInfoProvider.killProgressAll(context);

	}

}
