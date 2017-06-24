package com.lym.xposed.aidl;
import com.lym.xposed.aidl.IView;
interface IActivity {
	void finishAll();
	IView findViewById(int id);
	IView getRootView();
	IView findText(String text);
	IView waitViewId(int id,long timeout);
	IView waitText(String text,long timeout);
}
