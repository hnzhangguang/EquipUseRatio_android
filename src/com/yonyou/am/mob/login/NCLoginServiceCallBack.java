package com.yonyou.am.mob.login;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Message;
import android.util.Log;

import com.yonyou.am.mob.commonactivity.AmBaseActivity;
import com.yonyou.am.mob.equipinuseratio.EiurListActivity;

/**
 * Created by zhang
 */
public class NCLoginServiceCallBack extends AbstractServiceCallBack {
	private static final String TAG = "NCLOGINSERVICECALLBACK";

	public NCLoginServiceCallBack(AmBaseActivity activity) {
		super(activity);
	}

	/**
	 * 登录成功了会执行这个方法,实现接下来的跳转或者其他逻辑
	 */
	@Override
	public void execute(JSONObject response) {
		Log.d(TAG, response.toString());
		AppContext appContext = AppContext.getCurrent(getmActivity());
		appContext.setToken(response.optString("token"));
		appContext.setGroupID(response.optString("groupid"));
		appContext.setSessionID(response.optString("sessionid"));
		appContext.setUserid(response.optString("userid"));
		appContext.setValue("resultcode", response.optString("resultcode"));
		Message message = new Message();
		if (response.optString("resultcode").equals("1")) {
			message.what = 1;
		} else {
			message.what = 0;
		}

		Intent intent = new Intent(getmActivity(), EiurListActivity.class);
		getmActivity().startActivity(intent);
		// getmActivity().finish();

	}
}
