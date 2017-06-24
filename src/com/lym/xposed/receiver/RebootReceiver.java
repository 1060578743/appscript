package com.lym.xposed.receiver;

import com.lym.xposed.service.ScriptService;
import com.lym.xposed.utils.ScreenUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class RebootReceiver extends BroadcastReceiver {
	static final String ACTION = "android.intent.action.BOOT_COMPLETED";

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(ACTION)) {
			// 唤醒屏幕
			ScreenUtil.wakeUpAndUnlock(context);
			// 启动脚本服务
			Intent service = new Intent(context, ScriptService.class);
			service.putExtra("action", ScriptService.ACTION_START);
			context.startService(service);
			// SU.startActivity("com.lym.xposed/.activity.MainActivity --ei reboot 1");
		}

	}

}
