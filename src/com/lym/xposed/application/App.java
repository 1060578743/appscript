package com.lym.xposed.application;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Application;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.lym.xposed.R;
import com.lym.xposed.ScriptServer;
import com.lym.xposed.utils.SU;

public class App extends Application {
	// ���帡�����ڲ���
	LinearLayout floatLayout;
	WindowManager.LayoutParams wmLayout;
	// ���������������ò��ֲ����Ķ���
	WindowManager wm;
	// ������ť
	ImageButton floatBtn;
	// �ű��Ƿ�����
	boolean isRunning;
	// ����timer
	Timer rebootTimer;
	// �ű�������
	ScriptServer scriptServer;

	public void startScript() {
		if (isRunning) {
			return;
		}
		isRunning = true;
		scriptServer = new ScriptServer();
		// ֹͣ�Զ�����timer
		try {
			rebootTimer.cancel();
		} catch (Exception e) {
		}
		// �����µ�timer
		rebootTimer = new Timer();
		long rebootTime = 60 * 60 * 1000;
		if (rebootTime < 1000 * 60) {
			rebootTime = 60 * 1000;
		}
		rebootTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				Runnable run = new Runnable() {
					public void run() {
						SU.reboot();
					}
				};
				new Thread(run).start();
			}
		}, rebootTime);
		// ����������ť
		createFloatView();
	}

	public void stopScript() {
		if (!isRunning) {
			return;
		}
		isRunning = false;
		try {
			scriptServer.close();
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		try {
			rebootTimer.cancel();
		} catch (Exception e) {
		}
		try {
			floatBtn.setBackgroundResource(R.drawable.circle_cyan);
		} catch (Exception e1) {
		}

	}

	public void removeFloatView() {
		if (floatLayout != null) {
			// �Ƴ���������
			try {
				wm.removeView(floatLayout);
			} catch (Exception e) {
			}
		}
	}

	public void createFloatView() {
		removeFloatView();
		int w = getResources().getDisplayMetrics().widthPixels;
		int h = getResources().getDisplayMetrics().heightPixels;
		wmLayout = new WindowManager.LayoutParams();
		// ͨ��getApplication��ȡ����WindowManagerImpl.CompatModeWrapper
		wm = (WindowManager) getSystemService(WINDOW_SERVICE);
		// ����window type
		wmLayout.type = LayoutParams.TYPE_PHONE;
		// ����ͼƬ��ʽ��Ч��Ϊ����͸��
		wmLayout.format = PixelFormat.RGBA_8888;
		// ���ø������ڲ��ɾ۽���ʵ�ֲ���������������������ɼ����ڵĲ�����
		wmLayout.flags = LayoutParams.FLAG_NOT_FOCUSABLE;
		// ������������ʾ��ͣ��λ��Ϊ����ö�
		wmLayout.gravity = Gravity.LEFT | Gravity.TOP;
		// ����Ļ���Ͻ�Ϊԭ�㣬����x��y��ʼֵ�������gravity

		// �����������ڳ�������
		wmLayout.width = WindowManager.LayoutParams.WRAP_CONTENT;
		wmLayout.height = WindowManager.LayoutParams.WRAP_CONTENT;

		LayoutInflater inflater = LayoutInflater.from(this);
		// ��ȡ����������ͼ���ڲ���
		floatLayout = (LinearLayout) inflater.inflate(
				R.layout.alert_window_button, null);

		// ���mFloatLayout
		wm.addView(floatLayout, wmLayout);

		// �������ڰ�ť
		floatBtn = (ImageButton) floatLayout
				.findViewById(R.id.alert_window_imagebtn);

		// ��������
		floatLayout.measure(View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
				.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

		// ���ü����������ڵĴ����ƶ�
		floatBtn.setOnTouchListener(new OnTouchListener() {
			boolean isClick;
			float downX, downY;
			float startX, startY;

			public boolean onTouch(View v, MotionEvent event) {
				float rx = event.getRawX();
				float ry = event.getRawY();

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					isClick = true;
					downX = rx;
					downY = ry;
					startX = wmLayout.x;
					startY = wmLayout.y;
					break;
				case MotionEvent.ACTION_MOVE:
					if (Math.abs(rx - downX) > 10 || Math.abs(ry - downY) > 10) {
						isClick = false;
					}
					// getRawX�Ǵ���λ���������Ļ�����꣬getX������ڰ�ť������
					wmLayout.x = (int) (startX - downX + rx);
					// ��25Ϊ״̬���ĸ߶�
					wmLayout.y = (int) (startY - downY + ry);
					// ˢ��
					wm.updateViewLayout(floatLayout, wmLayout);
					break;
				case MotionEvent.ACTION_UP:
					if (isClick) {
						// �ж����������ǹر�
						if (isRunning) {
							stopScript();
							floatBtn.setBackgroundResource(R.drawable.circle_cyan);
						} else {
							startScript();
							floatBtn.setBackgroundResource(R.drawable.circle_red);
						}
					}
					break;
				}
				return false;
			}
		});
		wmLayout.x = w - floatBtn.getWidth();
		wmLayout.y = h / 2;
		wm.updateViewLayout(floatLayout, wmLayout);
	}

}
