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
	// 定义浮动窗口布局
	LinearLayout floatLayout;
	WindowManager.LayoutParams wmLayout;
	// 创建浮动窗口设置布局参数的对象
	WindowManager wm;
	// 悬浮按钮
	ImageButton floatBtn;
	// 脚本是否运行
	boolean isRunning;
	// 重启timer
	Timer rebootTimer;
	// 脚本服务器
	ScriptServer scriptServer;

	public void startScript() {
		if (isRunning) {
			return;
		}
		isRunning = true;
		scriptServer = new ScriptServer();
		// 停止自动重启timer
		try {
			rebootTimer.cancel();
		} catch (Exception e) {
		}
		// 启动新的timer
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
		// 创建悬浮按钮
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
			// 移除悬浮窗口
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
		// 通过getApplication获取的是WindowManagerImpl.CompatModeWrapper
		wm = (WindowManager) getSystemService(WINDOW_SERVICE);
		// 设置window type
		wmLayout.type = LayoutParams.TYPE_PHONE;
		// 设置图片格式，效果为背景透明
		wmLayout.format = PixelFormat.RGBA_8888;
		// 设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
		wmLayout.flags = LayoutParams.FLAG_NOT_FOCUSABLE;
		// 调整悬浮窗显示的停靠位置为左侧置顶
		wmLayout.gravity = Gravity.LEFT | Gravity.TOP;
		// 以屏幕左上角为原点，设置x、y初始值，相对于gravity

		// 设置悬浮窗口长宽数据
		wmLayout.width = WindowManager.LayoutParams.WRAP_CONTENT;
		wmLayout.height = WindowManager.LayoutParams.WRAP_CONTENT;

		LayoutInflater inflater = LayoutInflater.from(this);
		// 获取浮动窗口视图所在布局
		floatLayout = (LinearLayout) inflater.inflate(
				R.layout.alert_window_button, null);

		// 添加mFloatLayout
		wm.addView(floatLayout, wmLayout);

		// 浮动窗口按钮
		floatBtn = (ImageButton) floatLayout
				.findViewById(R.id.alert_window_imagebtn);

		// 测量布局
		floatLayout.measure(View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
				.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

		// 设置监听浮动窗口的触摸移动
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
					// getRawX是触摸位置相对于屏幕的坐标，getX是相对于按钮的坐标
					wmLayout.x = (int) (startX - downX + rx);
					// 减25为状态栏的高度
					wmLayout.y = (int) (startY - downY + ry);
					// 刷新
					wm.updateViewLayout(floatLayout, wmLayout);
					break;
				case MotionEvent.ACTION_UP:
					if (isClick) {
						// 判断是启动还是关闭
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
