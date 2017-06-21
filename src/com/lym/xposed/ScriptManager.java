package com.lym.xposed;

import java.util.ArrayList;

import android.app.Activity;

public class ScriptManager {
	ArrayList<Script> scripts;
	private static ScriptManager instance;

	private ScriptManager() {
		scripts = new ArrayList<Script>();
	}

	public synchronized static ScriptManager getInstance() {
		if (instance == null) {
			instance = new ScriptManager();
		}
		return instance;
	}

	public void activityResume(Activity activity) {
		Script script = new Script(activity);
		scripts.add(script);
	}

	public void activityPause(Activity activity) {
		for (Script s : scripts) {
			if (s.topActivity.equals(activity)) {
				s.pause();
				scripts.remove(s);
				return;
			}
		}
	}
}
