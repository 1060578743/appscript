package com.lym.xposed.utils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ViewUtil {
	public static View findViewByText(View v, String str) {
		if (v instanceof TextView) {
			String text = ((TextView) v).getText().toString();
			if (str.equals(text)) {
				return v;
			}
		}
		if (v instanceof ViewGroup) {
			ViewGroup vg = (ViewGroup) v;
			int len = vg.getChildCount();
			for (int i = 0; i < len; i++) {
				View child = vg.getChildAt(i);
				View find = findViewByText(child, str);
				if (find != null) {
					return find;
				}
			}
		}
		return null;
	}
}
