package com.lym.xposed.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.lym.xposed.R;
import com.lym.xposed.application.App;
import com.lym.xposed.utils.FileUtil;

public class MainActivity extends RebootActivity {
	// ����ʱ����������������
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

	// ��������������
	@Override
	public void onReboot() {
		app.startScript();
	}

	@Override
	protected void onStart() {
		super.onStart();
		read();
	}

	// ��ȡ�ļ�
	@SuppressLint("NewApi")
	public void read() {
		EditText edit = (EditText) findViewById(R.id.editOption);
		String option = FileUtil.read("/mnt/sdcard/option.txt");
		if (option != null && !option.isEmpty()) {
			edit.setText(option);
		}
	}

	// �����ļ�
	public void save() {
		EditText edit = (EditText) findViewById(R.id.editOption);
		String option = edit.getText().toString();
		FileUtil.write("/mnt/sdcard/option.txt", option);
	}

	// ��ʼ��ť�¼�
	public void start(View v) {
		// ��������
		save();
		app.createFloatView();
	}

	// ֹͣ��ť�¼�
	public void stop(View v) {
		app.stopScript();
		app.removeFloatView();
	}

}
