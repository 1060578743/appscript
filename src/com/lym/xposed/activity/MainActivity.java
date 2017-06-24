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

	// ��ʼ��ť�¼�
	public void start(View v) {
		// ���ؽű�
		App app = (App) getApplication();
		app.createFloatView();
	}

	// ֹͣ��ť�¼�
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
