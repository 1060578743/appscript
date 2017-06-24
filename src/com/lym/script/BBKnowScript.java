package com.lym.script;

import java.io.FileInputStream;
import java.util.Properties;

import android.os.Environment;

import com.lym.xposed.aidl.IView;
import com.lym.xposed.utils.SU;

public class BBKnowScript extends Script {
	private void readOption() throws Exception {
		// 读取配置文件
		FileInputStream in = null;
		try {
			Properties pro = new Properties();
			in = new FileInputStream(Environment.getExternalStorageDirectory()
					+ "/" + "option.txt");
			pro.load(in);

		} catch (Exception e) {
			throw e;
		} finally {
			in.close();
		}

	}

	void attention() throws Exception {
		SU.startActivity("com.baidu.mbaby/.activity.search.NewSearchActivity");
		waitActivity("NewSearchActivity");
		Thread.sleep(1000);
		// 输入
		IView edit = getTopActivity().findViewById(baby.id.key_word);
		edit.input("虾米妈咪");
		Thread.sleep(500);
		// 搜索
		IView btn = getTopActivity().findViewById(baby.id.app_search_button);
		btn.click();
		Thread.sleep(500);
		// 等待搜索结果点击第一个用户
		IView first_user = waitId(baby.id.first_layout);
		if (first_user.exist()) {
			first_user.click();
		}
		// 等待用户界面
		waitActivity("UserArticleListActivity");
		// 等待粉丝按钮
		IView fans_linear = waitId(baby.id.fans_linear);
		if (fans_linear.exist()) {
			fans_linear.click();
		}
		waitActivity("UserFansListActivity");
		
	}

	@Override
	public void scriptMain() throws Exception {
		attention();
	}

}
