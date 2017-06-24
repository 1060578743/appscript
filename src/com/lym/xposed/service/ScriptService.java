package com.lym.xposed.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import com.lym.script.Script;
import com.lym.xposed.aidl.IActivity;
import com.lym.xposed.aidl.IScriptService;
import com.lym.xposed.application.App;

public class ScriptService extends Service {

	public static final String ACTION_START = "start";

	private void startScript() {
		App app = (App) getApplication();
		app.startScript();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return new IScriptService.Stub() {

			@Override
			public void onConnect(String name, IActivity activity, String intent)
					throws RemoteException {
				System.out.println(name + "\n" + intent);
				if (Script.currentScript != null) {
					Script.currentScript.onActivityConnect(name, activity);
				}
			}
		};
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		String action = intent.getStringExtra("action");
		if (action != null) {
			if (action.equals(ACTION_START)) {
				startScript();
			}
		}
		return super.onStartCommand(intent, flags, startId);
	}

}
