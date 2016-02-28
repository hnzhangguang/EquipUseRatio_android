package com.yonyou.am.mob.login;

import android.util.Log;
import android.widget.Toast;

import com.yonyou.am.mob.commonactivity.AmBaseActivity;
import com.yonyou.am.mob.tool.AmMobCommon;
import com.yonyou.uap.um.service.ServiceCallback;

/**
 * 功能说明：
 * 
 * @author zhang
 * @2015-11-9
 */
public abstract class AbstractServiceCallBack implements ServiceCallback {
	private static final String TAG = AmMobCommon.ammoblog;
	private AmBaseActivity mActivity;

	public AmBaseActivity getmActivity() {
		return this.mActivity;
	}

	public AbstractServiceCallBack(AmBaseActivity activity) {
		this.mActivity = activity;
	}

	@Override
	public void error(String s, String s1) {
		Log.e(TAG, s + "->" + s1);
		Toast.makeText(this.mActivity, s + "->" + s1, Toast.LENGTH_LONG).show();
	}
}
