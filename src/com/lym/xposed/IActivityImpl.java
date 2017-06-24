package com.lym.xposed;

import java.lang.reflect.Method;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.RemoteException;
import android.view.View;
import android.view.ViewGroup;

import com.lym.xposed.aidl.IActivity;
import com.lym.xposed.aidl.IView;

public class IActivityImpl extends IActivity.Stub {
	Activity activity;

	public IActivityImpl(Activity activity) {
		super();
		this.activity = activity;
	}

	@Override
	public IView findText(String text) throws RemoteException {
		return new IViewImpl(findViewByText(text, activity.getWindow()
				.getDecorView()), activity);
	}

	@Override
	public IView findViewById(int id) throws RemoteException {
		return new IViewImpl(activity.findViewById(id), activity);
	}

	private View findViewByText(String text, View view) {
		try {
			Method method = view.getClass().getMethod("getText");
			String txt = method.invoke(view).toString();
			if (text.equals(txt)) {
				return view;
			}
		} catch (Exception e) {
		}
		if (view instanceof ViewGroup) {
			int count = ((ViewGroup) view).getChildCount();
			for (int i = 0; i < count; i++) {
				View child = ((ViewGroup) view).getChildAt(i);
				View find = findViewByText(text, child);
				if (find != null) {
					return find;
				}
			}
		}
		return null;
	}

	private View findViewId(int id, View view) {
		if (view.getId() == id) {
			return view;
		}
		if (view instanceof ViewGroup) {
			int count = ((ViewGroup) view).getChildCount();
			for (int i = 0; i < count; i++) {
				View child = ((ViewGroup) view).getChildAt(i);
				View find = findViewId(id, child);
				if (find != null) {
					return find;
				}
			}
		}
		return null;
	}

	@SuppressLint("NewApi")
	@Override
	public void finishAll() throws RemoteException {
		activity.finishAffinity();
	}

	public Activity getActivity() {
		return activity;
	}

	@Override
	public IView getRootView() throws RemoteException {
		return new IViewImpl(activity.getWindow().getDecorView(), activity);
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	@Override
	public IView waitText(String text, long timeout) throws RemoteException {
		long time = System.currentTimeMillis() + timeout;
		while (true) {
			View view = findViewByText(text, activity.getWindow()
					.getDecorView());
			if (view != null) {
				return new IViewImpl(view, activity);
			}
			if (System.currentTimeMillis() > time) {
				throw new RemoteException();
			}
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public IView waitViewId(int id, long timeout) throws RemoteException {
		long time = System.currentTimeMillis() + timeout;
		while (true) {
			View view = findViewId(id, activity.getWindow().getDecorView());
			if (view != null) {
				return new IViewImpl(view, activity);
			}
			if (System.currentTimeMillis() > time) {
				throw new RemoteException();
			}
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
