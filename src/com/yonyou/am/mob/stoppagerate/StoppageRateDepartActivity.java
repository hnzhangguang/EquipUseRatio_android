package com.yonyou.am.mob.stoppagerate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.yonyou.am.mob.stoppagerate.vo.StoppagerateBaseAggVO;
import com.yonyou.am.mob.stoppagerate.vo.StoppagerateBaseVO;
import com.yonyou.am.mob.tool.AmMobCommon;

/**
 * 设备故障停机率-部门界面
 * 
 * @author hujieh
 * 
 */
@SuppressLint("ClickableViewAccessibility")
public class StoppageRateDepartActivity extends StoppageRateBaseSubActivity {

	@Override
	protected void initWebView(WebView webview) {
		log("initWebView");
		webview.loadUrl("file:///android_asset/echart/stoppageratedepart.html");
		webview.addJavascriptInterface(new JSOjbect(), "stoppage");
	}

	@Override
	protected String getQueryMethodName() {
		return "queryDepartDatas";
	}

	/**
	 * 功能说明：js 回调
	 * 
	 */
	class JSOjbect {
		public JSOjbect() {
		}

		@JavascriptInterface
		public void execute(String obj) {
			int index = Integer.valueOf(obj);
			StoppagerateBaseAggVO baseAggVO = getCurrData();
			StoppagerateBaseVO baseVO = baseAggVO.getChilds().get(index);
			Intent intent = new Intent(StoppageRateDepartActivity.this, StoppageRateDetailActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString(AmMobCommon.NAME_FIELD, baseVO.getName());
			bundle.putString(AmMobCommon.DATE_FIELD, getCurrMonthString());
			bundle.putString(AmMobCommon.PK_USEDEPT_FIELD, baseVO.getPk());
			bundle.putString(AmMobCommon.PK_USEDUNIT_FIELD, baseAggVO.getParent().getPk());
			intent.putExtras(bundle);
			startActivity(intent);

		}
	}

	// @Override
	// protected void setResultForDate() {
	// Intent intent = this.getIntent();
	// intent.putExtra(AmMobCommon.DATE_FIELD, getCurrMonthString());
	// this.setResult(AmMobCommon.DEPART2LIST_RETURNCODE, intent);
	// }
	//
	// @Override
	// protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	// super.onActivityResult(requestCode, resultCode, data);
	// switch (resultCode) {
	// case AmMobCommon.DETAIL2DEPART_RETURNCODE:
	// setCurrDateString(data.getExtras().getString(AmMobCommon.DATE_FIELD));
	// }
	// }

}
