package com.lym.xposed.utils;

import com.lym.xposed.utils.ShellUtils.CommandResult;

public class SU {
	public static void startMainActivity() {
		startActivity("com.lym.script/com.lym.activity.MainActivity");
	}

	public static void startActivity(String name) {
		ShellUtils.execCommand("am start -n " + name, true);
	}

	public static void stopPackage(String pkg) {
		ShellUtils.execCommand("am force-stop " + pkg, true);
	}

	public static void startInput() {
		ShellUtils.execCommand(new String[] {
				"ime set com.lym.script/com.lym.service.InputService",
				"ime enable com.lym.script/com.lym.service.InputService" },
				true);
	}

	public static void stopInput() {
		ShellUtils
				.execCommand(
						new String[] { "ime disable com.lym.script/com.lym.service.InputService" },
						true);
	}

	public static void refreshPic(String picFileName) throws Exception {
		ShellUtils
				.execCommand(
						new String[] { "am broadcast -a android.intent.action.MEDIA_SCANNER_SCAN_FILE -d file://"
								+ picFileName }, true);
	}

	public static void mv(String fromFile, String toFile) throws Exception {
		ShellUtils.execCommand(
				new String[] { "mv " + fromFile + " " + toFile }, true);
	}

	public static void cp(String fromFile, String toFile) throws Exception {
		ShellUtils.execCommand(
				new String[] { "cp " + fromFile + " " + toFile }, true);
	}

	public static void rm(String file) throws Exception {
		ShellUtils.execCommand(new String[] { "rm " + file }, true);
	}

	public static void startScriptService() {
		ShellUtils
				.execCommand(
						new String[] {
								"settings put secure enabled_accessibility_services com.lym.script/com.lym.service.ScriptService",
								"settings put secure accessibility_enabled 1" },
						true);
	}

	public static void stopScriptService() {
		ShellUtils.execCommand(
				new String[] { "settings put secure accessibility_enabled 0" },
				true);
	}

	public static String getTopActivity() throws Exception {
		CommandResult result = ShellUtils.execCommand(
				new String[] { "dumpsys activity |grep 'mFocusedActivity'" },
				true);
		return result.successMsg;
	}

	public static void reboot() {
		ShellUtils.execCommand(new String[] { "reboot" }, true);
	}
}
