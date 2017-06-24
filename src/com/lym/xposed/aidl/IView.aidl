package com.lym.xposed.aidl;
interface IView{
	void click();
	void input(String text);
	IView getChild(int index);
	IView findId(int id);
	IView findViewById(int id);
	IView findViewByText(String text);
	boolean exist();
	
}