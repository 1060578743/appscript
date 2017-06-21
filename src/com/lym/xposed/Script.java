package com.lym.xposed;

import java.io.IOException;
import java.lang.reflect.Method;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lym.xposed.utils.SocketUtil;

import de.robv.android.xposed.XposedBridge;

public class Script implements Runnable {
	Activity topActivity;
	Thread scriptThread;
	SocketUtil socketUtil;
	Gson gson;

	public Script(Activity activity) {
		topActivity = activity;
		gson = new Gson();
		resume();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (o instanceof Script) {
			return topActivity.equals(((Script) o).topActivity);
		}
		return false;
	}

	public boolean inputTextById(int id, String text) {
		EditText tv = (EditText) topActivity.findViewById(id);
		if (tv != null) {
			tv.setText(text);
			return true;
		}
		return false;
	}

	public boolean clickViewById(int id) {
		View view = topActivity.findViewById(id);
		if (view != null) {
			view.performClick();
			return true;
		}
		return false;
	}

	private boolean clickViewByText(String text, View view) {
		
		return false;
	}

	public boolean clickViewByText(String text) {
		View topView = topActivity.getWindow().getDecorView();
		return clickViewByText(text, topView);
	}

	public String getActivityName() {
		XposedBridge.log("getActivityName");
		XposedBridge.log(topActivity.getClass().getName());
		return topActivity.getClass().getSimpleName();
	}

	public void resume() {
		scriptThread = new Thread(this);
		scriptThread.start();
	}

	public void pause() {
		if (socketUtil != null) {
			socketUtil.close();
		}
		scriptThread.interrupt();
	}

	private Object dealMsg(String msg) {
		JsonParser jp = new JsonParser();
		JsonObject je = jp.parse(msg).getAsJsonObject();
		String methodName = je.get("method").getAsString();

		if (methodName == null) {
			return null;
		}

		JsonElement jtypes = je.get("types");
		if (jtypes == null) {
			try {
				Method method = this.getClass().getMethod(methodName);
				return method.invoke(this);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		JsonArray types = jtypes.getAsJsonArray();
		// 运行有参数的方法
		JsonArray params = je.get("params").getAsJsonArray();
		if (params == null) {
			return null;
		}
		Class<?> typeClz[] = new Class<?>[types.size()];
		Object args[] = new Object[params.size()];
		try {
			for (int i = 0; i < types.size(); i++) {
				typeClz[i] = Class.forName(types.get(i).getAsString());
				args[i] = gson.fromJson(params.get(i), typeClz[i]);
			}
			Method method = this.getClass().getMethod(methodName, typeClz);
			return method.invoke(this, args);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void run() {
		XposedBridge.log("connect server");
		try {
			socketUtil = new SocketUtil("127.0.0.1", 9999);
		} catch (IOException e) {
			return;
		}
		XposedBridge.log("connect server success!");
		String line = null;
		try {
			while ((line = socketUtil.readLine()) != null) {
				// 处理消息
				XposedBridge.log(line);
				Object result = dealMsg(line);
				if (result == null) {
					continue;
				}
				XposedBridge.log(gson.toJson(result));
				// 发送处理结果
				socketUtil.sendLine(gson.toJson(result));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		socketUtil.close();
	}
}
