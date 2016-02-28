package com.yonyou.am.mob.login;

import org.json.JSONObject;

import android.util.Log;

import com.yonyou.am.mob.commonactivity.AmBaseActivity;
import com.yonyou.am.mob.tool.AmMobCommon;

/**
 * 功能说明：移动端调用MA后回调
 * 
 * @author zhang
 * @2015-11-9
 */
public class AMServiceCallBack extends AbstractServiceCallBack {

	private final static String TAG = AmMobCommon.ammoblog;

	public AMServiceCallBack(AmBaseActivity activity) {
		super(activity);
	}

	/**
	 * 调用成功回调
	 */
	@Override
	public void execute(JSONObject response) {
		Log.d(TAG, response.toString());
	}

	/**
	 * 调用MA失败后记录日志
	 */
	@Override
	public void error(String s, String s1) {
		Log.e(AmMobCommon.ammoblog, s + "->" + s1);
	}

}
