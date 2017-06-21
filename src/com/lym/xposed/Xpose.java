package com.lym.xposed;

import java.lang.reflect.Method;

import android.app.Activity;

import com.lym.xposed.utils.XposedUtil;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class Xpose implements IXposedHookLoadPackage {
	public static final String PKG = "com.baidu.mbaby";
	public static final int MAX_VIEW_COUNT = 1000;

	// �������ذ���ʱ������
	@Override
	public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
		// ȥ��root��ʾ
		XposedUtil.removeToast("�����û�");
		if (!PKG.equals(lpparam.packageName)) {
			// return;
		}
		Method onResume = Activity.class.getDeclaredMethod("onResume");
		Method onPause = Activity.class.getDeclaredMethod("onPause");
		XposedBridge.hookMethod(onResume, new MH() {

			@Override
			protected void before(MethodHookParam param) throws Throwable {

			}

			@Override
			protected void after(MethodHookParam param) throws Throwable {
				ScriptManager.getInstance().activityResume(
						(Activity) param.thisObject);
			}
		});
		XposedBridge.hookMethod(onPause, new MH() {

			@Override
			protected void before(MethodHookParam param) throws Throwable {

			}

			@Override
			protected void after(MethodHookParam param) throws Throwable {
				ScriptManager.getInstance().activityPause(
						(Activity) param.thisObject);
			}
		});
	}

}
