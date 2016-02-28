package com.yonyou.am.mob.login;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.yonyou.am.mob.commonactivity.AmBaseActivity;
import com.yonyou.am.mob.tool.AmMobCommon;
import com.yonyou.uap.um.service.ServiceCallback;
import com.yonyou.uap.um.service.ServiceProxy;

/**
 * 功能说明：移动端调用后台工具类
 * 
 * @author
 * @2015-11-9
 */
public class ServiceUtils {
	private static final String TAG = AmMobCommon.ammoblog;

	public enum ServiceType {
		ncLoginService, umCommonService
	}

	private static final Set<String> serviceReservedKeys = new HashSet<String>(Arrays.asList(new String[] {
			"deviceinfo", "appcontext", "servicecontext", "serviceid", "viewid", "controllerid", "actionname",
			"callback" }));

	/**
	 * callaction服务
	 * 
	 * @param viewid
	 * @param actionname
	 * @param paramJson
	 * @param serviceCallback
	 */
	public static void callAction(AmBaseActivity activity, String viewid, String actionname, JSONObject paramJson,
			ServiceCallback serviceCallback) {
		callAction(activity, viewid, actionname, paramJson, serviceCallback, false, 15000);
	}

	/**
	 * 
	 * @param activity
	 * @param viewid
	 * @param actionname
	 * @param paramJson
	 * @param serviceCallback
	 * @param isSync
	 *            是否阻塞主U，默认为不阻塞，即异步加载
	 * @param timeout
	 */
	public static void callAction(AmBaseActivity activity, String viewid, String actionname, JSONObject paramJson,
			ServiceCallback serviceCallback, boolean isSync, int timeout) {
		Log.d(TAG, "viewid:" + viewid + ",actionname:" + actionname + ",params:" + paramJson.toString());
		AppContext appContext = AppContext.getCurrent(activity);
		String host = appContext.getHost();
		String port = appContext.getPort();
		StringBuffer urlBuffer = new StringBuffer();
		urlBuffer.append("http://").append(host).append(":").append(port).append("/umserver/core/");
		HashMap<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("tp", "des");
		try {
			paramMap.put("data",
					generateRequestData(ServiceType.umCommonService, activity, paramJson, viewid, actionname));
		} catch (JSONException e) {
			Log.e(TAG, "callaction服务出现异常");
			Log.e(TAG, e.getMessage());
		}
		new ServiceProxy(activity).start(urlBuffer.toString(), paramMap, serviceCallback, isSync, timeout);
	}

	public static void login(AmBaseActivity activity) {
		AppContext appContext = AppContext.getCurrent(activity);
		String host = appContext.getHost();
		String port = appContext.getPort();
		StringBuffer urlBuffer = new StringBuffer();
		urlBuffer.append("http://").append(host).append(":").append(port).append("/umserver/core/");
		HashMap<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("tp", "des");
		try {
			paramMap.put("data", generateRequestData(ServiceType.ncLoginService, activity, new JSONObject(), "", ""));
		} catch (JSONException e) {
			Log.e(TAG, "callaction服务出现异常");
			Log.e(TAG, e.getMessage());
		}
		ServiceCallback loginserviceCallback = new NCLoginServiceCallBack(activity);
		new ServiceProxy(activity).start(urlBuffer.toString(), paramMap, loginserviceCallback, false, 20000);
	}

	private static String generateRequestData(ServiceType serviceType, Context context, JSONObject paramJson,
			String viewid, String actionname) throws JSONException {
		JSONObject json = new JSONObject();
		json.put("serviceid", serviceType.toString());

		json.put("deviceinfo", MobileDevice.getDeviceInfo(context));

		json.put("appcontext", generateAppContextInfo(serviceType, context));

		json.put("servicecontext", generateServiceContextInfo(serviceType, context, paramJson, viewid, actionname));

		return json.toString();
	}

	private static JSONObject generateAppContextInfo(ServiceType serviceType, Context context) throws JSONException {
		AppContext appContext = AppContext.getCurrent(context);
		JSONObject json = new JSONObject();
		json.put("devid", appContext.getDeviceId());
		json.put("appid", appContext.getAPPID());

		json.put("token", appContext.getToken());
		json.put("userid", appContext.getUserid());
		json.put("sessionid", appContext.getSessionID());
		json.put("user", appContext.getUser());
		json.put("pass", appContext.getPassword());
		json.put("groupid", appContext.getGroupID());
		json.put("funcid", appContext.getFuncid());
		json.put("funcode", appContext.getFuncode());
		json.put("massotoken", appContext.getMassotoken());
		json.put("tabid", appContext.getTabid());
		return json;
	}

	private static JSONObject generateServiceContextInfo(ServiceType serviceType, Context context,
			JSONObject paramJson, String viewid, String actionname) throws JSONException {
		AppContext appContext = AppContext.getCurrent(context);

		JSONObject json = new JSONObject();
		switch (serviceType) {
		case ncLoginService:
			json.put("viewid", "");
			json.put("type", "nc");
			json.put("actionname", "umLogin");
			json.put("pass", appContext.getPassword());
			json.put("actionid", "");
			json.put("containerName", "");
			json.put("action", "umLogin");
			json.put("callback", "");
			json.put("user", appContext.getUser());
			break;
		case umCommonService:
			json.put("actionid", serviceType.toString());
			json.put("callback", "nothing");
			json.put("viewid", viewid);
			json.put("controllerid", viewid);
			json.put("actionname", actionname);
			JSONObject params = new JSONObject();
			Iterator iterator = paramJson.keys();
			do {
				if (!iterator.hasNext())
					break;
				String key = (String) iterator.next();
				if (!serviceReservedKeys.contains(key)) {
					params.put(key, paramJson.optString(key));
				}

			} while (true);
			json.put("params", params);
		}
		return json;
	}

}
