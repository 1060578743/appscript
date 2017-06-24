package com.lym.script;

import java.io.FileInputStream;
import java.util.Properties;

import android.os.Environment;

import com.lym.xposed.aidl.IView;
import com.lym.xposed.utils.SU;

public class BBKnowScript extends Script {
	private void readOption() throws Exception {
		// ��ȡ�����ļ�
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
		// ����
		IView edit = getTopActivity().findViewById(baby.id.key_word);
		edit.input("Ϻ������");
		Thread.sleep(500);
		// ����
		IView btn = getTopActivity().findViewById(baby.id.app_search_button);
		btn.click();
		Thread.sleep(500);
		// �ȴ�������������һ���û�
		IView first_user = waitId(baby.id.first_layout);
		if (first_user.exist()) {
			first_user.click();
		}
		// �ȴ��û�����
		waitActivity("UserArticleListActivity");
		// �ȴ���˿��ť
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
