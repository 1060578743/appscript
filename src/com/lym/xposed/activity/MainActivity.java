package com.lym.xposed.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.lym.script.Script;
import com.lym.xposed.R;
import com.lym.xposed.application.App;

@SuppressLint("SdCardPath")
public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_main);
		super.onCreate(savedInstanceState);
	}

	// 开始按钮事件
	public void start(View v) {
		// 加载脚本
		App app = (App) getApplication();
		app.createFloatView();
	}

	// 停止按钮事件
	public void stop(View v) {
		try {
			Script.currentScript.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
		App app = (App) getApplication();
		app.removeFloatView();
	}

}
