package com.lym.script;

import android.content.Context;

import com.lym.xposed.aidl.IActivity;
import com.lym.xposed.aidl.IView;

public class Script implements Runnable {
	public interface Callback {
		public void onStart();

		public void onStop();
	}

	public static Script currentScript;
	private Callback callback;

	private boolean isRunning;

	private Thread scriptThread;
	// topActivity操作接口
	private IActivity topActivity;
	private String topActivityName;

	public IActivity getTopActivity() {
		return topActivity;
	}

	public String getTopActivityName() {
		return topActivityName;
	}

	/**
	 * 载入需要运行的脚本，如果已经有脚本在运行，那么将抛出异常
	 * 
	 * @param script
	 *            需要加载的脚本类
	 * @throws Exception
	 */
	public static void init(Context context) throws Exception {
		if (currentScript != null) {
			if (currentScript.isRunning) {
				throw new Exception("can't stop script when it's running.");
			}
		}
		// 这里待实现动态加载脚本
		currentScript = new BBKnowScript();
	}

	public boolean isRunning() {
		return isRunning;
	}

	public void onActivityConnect(String activityName, IActivity activity) {
		this.topActivityName = activityName;
		this.topActivity = activity;
	}

	/**
	 * 未实现的方法，后期完善的时候来实现
	 */
	public void restart() {

	}

	@Override
	public void run() {
		setRunning(true);
		if (callback != null) {
			callback.onStart();
		}
		try {
			scriptMain();
		} catch (Exception e) {
		}
		if (callback != null) {
			callback.onStop();
		}
		setRunning(false);
	}

	public void scriptMain() throws Exception {
		while (true) {
			Thread.sleep(1000);
			System.out.println("script running");
		}
	}

	public synchronized void setCallback(Callback callback) {
		this.callback = callback;
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}

	public void start() {
		setRunning(true);
		topActivityName = null;
		stop();
		scriptThread = new Thread(this);
		scriptThread.start();
	}

	public void stop() {
		if (scriptThread != null) {
			scriptThread.interrupt();
			try {
				// 等待3秒，如果线程程序未结束，直接重启
				scriptThread.join(3000);
			} catch (InterruptedException e) {
				// 通知守护进程重启脚本程序(未实现)
				restart();
			}
		}
		setRunning(false);
	}

	public void waitActivity(String name) throws Exception {
		waitForActivity(name, 10 * 1000);
	}

	public void waitForActivity(String name, long timeout) throws Exception {
		long time = System.currentTimeMillis() + timeout;
		while (true) {
			if (System.currentTimeMillis() > time) {
				throw new Exception("time out");
			}

			String top = getTopActivityName();
			if (top != null) {
				if (top.contains(name)) {
					break;
				}
			}

			Thread.sleep(1000);
		}
	}

	public IView waitId(int id) throws Exception {
		return waitId(id, 10 * 1000);
	}

	public IView waitId(int id, long timeout) throws Exception {
		return getTopActivity().waitViewId(id, timeout);
	}

	public IView waitText(String text) throws Exception {
		return waitText(text, 10 * 1000);
	}

	public IView waitText(String text, long timeout) throws Exception {
		return getTopActivity().waitText(text, timeout);
	}

	public void finishAll() throws Exception {
		getTopActivity().finishAll();
	}
}
