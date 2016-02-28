package com.yonyou.am.mob.login;

import org.json.JSONObject;

import android.util.Log;

import com.yonyou.am.mob.commonactivity.AmBaseActivity;
import com.yonyou.am.mob.tool.AmMobCommon;

/**
 * ����˵�����ƶ��˵���MA��ص�
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
	 * ���óɹ��ص�
	 */
	@Override
	public void execute(JSONObject response) {
		Log.d(TAG, response.toString());
	}

	/**
	 * ����MAʧ�ܺ��¼��־
	 */
	@Override
	public void error(String s, String s1) {
		Log.e(AmMobCommon.ammoblog, s + "->" + s1);
	}

}
