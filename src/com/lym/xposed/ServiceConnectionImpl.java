package com.lym.xposed;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.lym.xposed.aidl.IScriptService;
import com.lym.xposed.utils.StringUtil;

import de.robv.android.xposed.XposedBridge;

public class ServiceConnectionImpl implements ServiceConnection {
	Activity activity;

	public ServiceConnectionImpl(Activity activity) {
		this.activity = activity;
		Intent intent = activity.getIntent();
		String data = StringUtil.getIntentData(intent);
		XposedBridge.log(data);
	}

	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		IScriptService iss = IScriptService.Stub.asInterface(service);
		try {
			iss.onConnect(activity.getClass().getName(), new IActivityImpl(
					activity), StringUtil.getIntentData(activity.getIntent()));
		} catch (RemoteException e) {
		}
	}

	@Override
	public void onServiceDisconnected(ComponentName name) {

	}

}
