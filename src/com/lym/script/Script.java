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
	// topActivity�����ӿ�
	private IActivity topActivity;
	private String topActivityName;

	public IActivity getTopActivity() {
		return topActivity;
	}

	public String getTopActivityName() {
		return topActivityName;
	}

	/**
	 * ������Ҫ���еĽű�������Ѿ��нű������У���ô���׳��쳣
	 * 
	 * @param script
	 *            ��Ҫ���صĽű���
	 * @throws Exception
	 */
	public static void init(Context context) throws Exception {
		if (currentScript != null) {
			if (currentScript.isRunning) {
				throw new Exception("can't stop script when it's running.");
			}
		}
		// �����ʵ�ֶ�̬���ؽű�
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
	 * δʵ�ֵķ������������Ƶ�ʱ����ʵ��
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
				// �ȴ�3�룬����̳߳���δ������ֱ������
				scriptThread.join(3000);
			} catch (InterruptedException e) {
				// ֪ͨ�ػ����������ű�����(δʵ��)
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
