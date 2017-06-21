package com.lym.xposed.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.lym.xposed.R;
import com.lym.xposed.application.App;
import com.lym.xposed.utils.FileUtil;

public class MainActivity extends RebootActivity {
	// 重启时间输入框，设置输入框
	App app;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_main);
		app = (App) getApplication();
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	// 开机自启动调用
	@Override
	public void onReboot() {
		app.startScript();
	}

	@Override
	protected void onStart() {
		super.onStart();
		read();
	}

	// 读取文件
	@SuppressLint("NewApi")
	public void read() {
		EditText edit = (EditText) findViewById(R.id.editOption);
		String option = FileUtil.read("/mnt/sdcard/option.txt");
		if (option != null && !option.isEmpty()) {
			edit.setText(option);
		}
	}

	// 保存文件
	public void save() {
		EditText edit = (EditText) findViewById(R.id.editOption);
		String option = edit.getText().toString();
		FileUtil.write("/mnt/sdcard/option.txt", option);
	}

	// 开始按钮事件
	public void start(View v) {
		// 保存数据
		save();
		app.createFloatView();
	}

	// 停止按钮事件
	public void stop(View v) {
		app.stopScript();
		app.removeFloatView();
	}

}
