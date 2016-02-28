package com.yonyou.am.mob.application;

import android.app.Application;

/**
 * 应用application，此类事app打开第一个实例化的类，可以进行一些初始化操作
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
