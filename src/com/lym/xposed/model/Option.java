package com.lym.xposed.model;

import java.lang.reflect.Field;

import com.lym.xposed.utils.FileUtil;

public class Option {

	public void read() {
		try {
			String[] data = FileUtil.readAllLines("/mnt/sdcard/option.txt");
			for (int i = 0; i < data.length; i++) {
				try {

					String line = data[i];
					if (line == null) {
						continue;
					}
					String[] kv = line.split("=");
					if (kv.length < 2) {
						continue;
					}
					String key = kv[0].trim();
					String value = kv[kv.length - 1].trim();
					Field field = getClass().getField(key);

					if (int.class.equals(field.getType())) {
						field.set(this, Integer.parseInt(value));
					} else if (String.class.equals(field.getType())) {
						field.set(this, String.valueOf(value));
					} else if (boolean.class.equals(field.getType())) {
						field.set(this, Boolean.parseBoolean(value));
					} else if (double.class.equals(field.getType())) {
						field.set(this, Double.parseDouble(value));
					} else if (float.class.equals(field.getType())) {
						field.set(this, Float.parseFloat(value));
					}
					System.out.println("lym:" + field + " isString:"
							+ String.class.equals(field.getType()));
				} catch (Exception e) {
				}
			}
		} catch (Exception e) {
		}
	}

	public void save() {
		StringBuilder sb = new StringBuilder();
		Field[] fields = getClass().getFields();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			sb.append(field.getName());
			sb.append("=");
			try {
				sb.append(field.get(this).toString());
			} catch (Exception e) {
			}
			sb.append("\n\r");
		}
		FileUtil.write("/mnt/sdcard/option.txt", sb.toString());
	}
}
