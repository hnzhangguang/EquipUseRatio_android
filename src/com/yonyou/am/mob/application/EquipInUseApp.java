package com.yonyou.am.mob.application;

import android.app.Application;

/**
 * Ӧ��application��������app�򿪵�һ��ʵ�������࣬���Խ���һЩ��ʼ������
 * 
 * @author hujieh
 * 
 */

public class EquipInUseApp extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		CrashHandler crashHandler = CrashHandler.getInstance(this);
		crashHandler.init(getApplicationContext());
	}
}
