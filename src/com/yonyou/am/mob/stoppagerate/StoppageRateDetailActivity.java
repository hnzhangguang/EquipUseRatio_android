package com.yonyou.am.mob.stoppagerate;

import android.webkit.WebView;

/**
 * 设备故障停机率-详细界面
 * 
 * @author hujieh
 * 
 */
public class StoppageRateDetailActivity extends StoppageRateBaseSubActivity {

	@Override
	protected void initWebView(WebView webview) {
		webview.loadUrl("file:///android_asset/echart/stoppageratedetail.html");
	}

	@Override
	protected String getQueryMethodName() {
		return "queryDetails";
	}

	// @Override
	// protected void setResultForDate() {
	// Intent intent = this.getIntent();
	// intent.putExtra(AmMobCommon.DATE_FIELD, getCurrMonthString());
	// this.setResult(AmMobCommon.DETAIL2DEPART_RETURNCODE, intent);
	// }

}
