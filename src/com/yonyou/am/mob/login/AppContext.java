package com.yonyou.am.mob.login;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

/**
 * 功能说明： 移动端登录信息记录
 * 
 * @author zhangg
 * @2015-11-9
 */
public class AppContext {
	private static final String TAG = "APPCONTEXT";
	private static final String CONFIGURE_FILE = "configure/application.configure";
	private static final String USER = "user";
	private static final String PASSWORD = "password";
	private static final String HOST = "host";
	private static final String PORT = "port";
	private static final String TOKEN = "token";
	private static final String APPID = "appid";
	private static final String FUNCID = "funcid";
	private static final String FUNCODE = "funcode";
	private static final String USERID = "userid";

	private SoftReference<Context> mContext = null;

	private static AppContext current = null;
	private Map<String, String> mCache = new HashMap();

	public AppContext(Context context) {
		this.mContext = new SoftReference<Context>(context);
	}

	public void setValue(String key, String value) {
		this.mCache.put(key.toLowerCase(), value);
	}

	public String getValue(String key) {
		return this.mCache.containsKey(key.toLowerCase()) ? this.mCache.get(key.toLowerCase()) : "";
	}

	public static synchronized AppContext getCurrent(Context context) {
		if (current == null) {
			reset(context);
		}

		if (context != null) {
			current.setmContext(context);
		}
		return current;
	}

	private void setmContext(Context context) {
		this.mContext = new SoftReference<Context>(context);
	}

	public static synchronized void reset(Context context) {
		current = new AppContext(context);
		try {
			InputStream is = context.getAssets().open(CONFIGURE_FILE);
			BufferedReader bfReader = new BufferedReader(new InputStreamReader(is));
			String s = null;
			while ((s = bfReader.readLine()) != null) {
				int pos = s.indexOf("=");
				if (pos > 0) {
					String key = s.substring(0, pos).toLowerCase();
					String value = s.substring(pos + 1);
					current.setValue(key, value);
				}
			}
			bfReader.close();
			is.close();
		} catch (IOException e) {
			Log.e(TAG, "00");
			Log.e(TAG, e.getMessage());
		}

		try {
			JSONObject deviceJson = MobileDevice.getDeviceInfo(context);
			current.setValue("deviceid", deviceJson.optString("deviceid"));
			current.setValue("wfaddress", deviceJson.optString("wfaddress"));
			current.setValue("package", context.getPackageName());
			PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			current.setValue("versioncode", String.valueOf(packageInfo.versionCode));
			current.setValue("versionname", packageInfo.versionName);
		} catch (JSONException e) {
			Log.e(TAG, "json瑙ｆ瀽寮傚父");
			Log.e(TAG, e.getMessage());
		} catch (PackageManager.NameNotFoundException e) {
			Log.e(TAG, "鍖呭悕璇诲彇寮傚父");
			Log.e(TAG, e.getMessage());
		}

	}

	public String getWfaddress() {
		return getValue("wfaddress");
	}

	public String getLanguage() {
		if (this.mContext.get() != null) {
			return ((Context) (this.mContext.get())).getResources().getConfiguration().locale.getLanguage();
		} else
			return "";
	}

	public String getAPPID() {
		return getValue("appid");
	}

	public void setAPPID(String appid) {
		setValue("appid", appid);
	}

	public String getFuncid() {
		return getValue("funcid");
	}

	public String getFuncode() {
		return getValue("funcode");
	}

	public String getHost() {
		return getValue("host");
	}

	public void setHost(String host) {
		setValue("host", host);
	}

	public String getPort() {
		return getValue("port");
	}

	public void setPort(String port) {
		setValue("port", port);
	}

	public String getUser() {
		return getValue("user");
	}

	public void setUser(String user) {
		setValue("user", user);
	}

	public String getPassword() {
		return getValue("password");
	}

	public void setPassword(String password) {
		setValue("password", password);
	}

	public String getGroupID() {
		return getValue("groupid");
	}

	public void setGroupID(String groupID) {
		setValue("groupid", groupID);
	}

	public String getPackageName() {
		String packagename = getValue("package");
		if (packagename.trim().equals("")) {
			if (this.mContext.get() == null) {
				return "";
			}
			packagename = this.mContext.get().getPackageName();
			setValue("package", packagename);
		}
		return packagename;
	}

	public String getVersionCode() {
		return getValue("versioncode");
	}

	public String getVersionName() {
		return getValue("versionname");
	}

	public String getDeviceId() {
		return getValue("deviceid");
	}

	public String getToken() {
		return getValue("token");
	}

	public void setToken(String token) {
		setValue("token", token);
	}

	public String getSessionID() {
		return getValue("sessionid");
	}

	public void setSessionID(String sessionID) {
		setValue("sessionid", sessionID);
	}

	public String getMassotoken() {
		return getValue("massotoken");
	}

	public void setMassotoken(String massotoken) {
		setValue("massotoken", massotoken);
	}

	public String getTabid() {
		return getValue("tabid");
	}

	public void setTabid(String tabid) {
		setValue("tabid", tabid);
	}

	public String getUserid() {
		return getValue("userid");
	}

	public void setUserid(String userid) {
		setValue(USERID, userid);
	}

}
