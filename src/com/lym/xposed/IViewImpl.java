package com.lym.xposed;

import java.lang.reflect.Method;

import android.app.Activity;
import android.os.RemoteException;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.lym.xposed.aidl.IView;

public class IViewImpl extends IView.Stub {
	Activity activity;
	View view;

	public IViewImpl(View view, Activity activity) {
		this.view = view;
		this.activity = activity;
	}

	@Override
	public void click() throws RemoteException {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				view.performClick();
			}
		});
	}

	@Override
	public boolean exist() throws RemoteException {
		return view != null;
	}

	@Override
	public IView findId(int id) throws RemoteException {
		View find = findViewId(id, view);
		return new IViewImpl(find, activity);
	}

	@Override
	public IView findViewById(int id) throws RemoteException {
		return new IViewImpl(view.findViewById(id), activity);
	}

	@Override
	public IView findViewByText(String text) throws RemoteException {
		View find = findViewByText(text, view);
		return new IViewImpl(find, activity);
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

	@Override
	public IView getChild(int index) throws RemoteException {
		View child = null;
		if (view instanceof ViewGroup) {
			child = ((ViewGroup) view).getChildAt(index);
		}
		return new IViewImpl(child, activity);
	}

	@Override
	public void input(final String text) throws RemoteException {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				EditText edit = (EditText) view;
				edit.setText(text);
			}
		});
	}

}
