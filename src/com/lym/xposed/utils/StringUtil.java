package com.lym.xposed.utils;

import java.util.Random;

public class StringUtil {
	static Random rd;
	static {
		rd = new Random(System.currentTimeMillis());
	}

	public static String randString(int length) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			char s = (char) (rd.nextInt('Z' - 'A') + 'A');
			sb.append(s);
		}
		return sb.toString();
	}
}
