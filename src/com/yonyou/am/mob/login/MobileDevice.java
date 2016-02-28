package com.yonyou.am.mob.login;

import java.lang.reflect.Field;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.yonyou.am.mob.commonactivity.AmBaseActivity;

/**
 * 功能说明：
 * 
 * @author zhang
 * @2015-11-9
 */
public class MobileDevice {
	/**
	 * get the device info for the call action
	 * 
	 * @param context
	 * @return
	 * @throws JSONException
	 */
	public static JSONObject getDeviceInfo(Context context) throws JSONException {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String deviceid = tm.getDeviceId();
		WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifi.getConnectionInfo();
		String wfaddress = wifiInfo.getMacAddress();

		JSONObject deviceJson = new JSONObject();
		if (Build.VERSION.SDK_INT > 8) {
			deviceJson.put("deviceid", Build.SERIAL + deviceid);
		} else {
			deviceJson.put("deviceid", deviceid);
		}
		deviceJson.put("wfaddress", wfaddress);
		deviceJson.put("imei", deviceid);
		deviceJson.put("model", Build.MODEL);
		deviceJson.put("os", "android");
		deviceJson.put("style", "android");
		deviceJson.put("version", Build.VERSION.SDK_INT);
		deviceJson.put("kversion", "3.4.5");
		deviceJson.put("cpu", Build.CPU_ABI);
		deviceJson.put("manufacture", Build.MANUFACTURER);
		deviceJson.put("bootloader", Build.BOOTLOADER);
		DisplayMetrics displayMetrics = new DisplayMetrics();
		// if (context instanceof Activity) {
		// Activity ac = (Activity) context;
		// if (Build.VERSION.SDK_INT > 17) {
		// ac.getWindowManager().getDefaultDisplay().getRealMetrics(displayMetrics);
		// } else {
		// ac.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		// }
		// float density = displayMetrics.scaledDensity;
		// JSONObject scr = new JSONObject();
		// scr.put("width", displayMetrics.widthPixels);
		// scr.put("height", displayMetrics.heightPixels);
		// scr.put("density", density);
		// deviceJson.put("screen", scr);
		// }

		return deviceJson;
	}

	/**
	 * get the status bar height for caculating 鐢甸噺鏍忛珮搴� * @return
	 */
	public static int getStatusBarHeight(Context context) {
		Class<?> c = null;
		Object obj = null;
		Field field = null;
		int x = 0, sbar = 0;
		try {
			c = Class.forName("com.android.internal.R$dimen");
			obj = c.newInstance();
			field = c.getField("status_bar_height");
			x = Integer.parseInt(field.get(obj).toString());
			sbar = context.getResources().getDimensionPixelSize(x);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		return sbar;
	}

	/**
	 * get the screen height but not with nav height
	 * 
	 * @param activity
	 * @return
	 */
	public static int getScreenHeightWithoutNav(AmBaseActivity activity) {
		DisplayMetrics dMetrics = new DisplayMetrics();
		WindowManager windowManager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
		Display display = windowManager.getDefaultDisplay();
		display.getMetrics(dMetrics);

		int screenh = dMetrics.heightPixels;

		int screenh_nonavbar = screenh;

		int ver = Build.VERSION.SDK_INT;

		// 鏂扮増鏈殑android 绯荤粺鏈夊鑸爮锛岄�鎴愭棤娉曟纭幏鍙栭珮搴� if (ver == 13){
		// try {
		// Method mt = display.getClass().getMethod("getRealHeight");
		// screenh_nonavbar = (Integer)mt.invoke(display);
		// }catch (Exception e){
		// }
		// } else if (ver > 13){
		// try{
		// Method mt = display.getClass().getMethod("getRawHeight");
		// screenh_nonavbar = (Integer)mt.invoke(display);
		// }catch (Exception e){
		// }
		// }
		//
		// int real_scontenth = screenh_nonavbar -
		// MobileDevice.getStatusBarHeight(activity);
		// return real_scontenth;
		return 22;
	}

}
