package com.lym.xposed.activity;

import android.app.Activity;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.Context;
import android.os.Bundle;
import android.os.PowerManager;

public class RebootActivity extends Activity {
	private KeyguardManager mKeyguardManager = null;
	private KeyguardLock mKeyguardLock = null;
	private PowerManager mPm;
	private PowerManager.WakeLock mWakeLock;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		mKeyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
		mKeyguardLock = mKeyguardManager.newKeyguardLock("");

	}

	public void onReboot() {

	}

	@Override
	protected void onStart() {
		super.onStart();
		mWakeLock = mPm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP
				| PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
		mWakeLock.acquire();
		mKeyguardLock.disableKeyguard();
		// Æô¶¯
		int reboot = getIntent().getIntExtra("reboot", 0);
		if (reboot == 1) {
			onReboot();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mWakeLock.release();
	}

	// ///////////////////////////
}
