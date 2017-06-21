package com.lym.xposed.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.lym.xposed.utils.SU;

public class RebootReceiver extends BroadcastReceiver {
	static final String ACTION = "android.intent.action.BOOT_COMPLETED";

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(ACTION)) {
			SU.startActivity("com.lym.xposed/.activity.MainActivity --ei reboot 1");
		}
	}

}
