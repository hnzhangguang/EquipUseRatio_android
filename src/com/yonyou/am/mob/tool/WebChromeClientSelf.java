/**
 * 
 */
package com.yonyou.am.mob.tool;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.webkit.JsResult;
import android.webkit.WebView;

/**
 * js和Android交互帮助类(测试用)
 * 
 * @author hujieh
 * 
 */
public class WebChromeClientSelf extends android.webkit.WebChromeClient {
	Context context;
	Map<String, String> msTitle = new HashMap<String, String>();

	private void replaceMap(Map<String, String> map) {
		Set<String> elSet = map.keySet();
		for (String el : elSet) {
			msTitle.put(el, map.get(el));
		}
	}

	public WebChromeClientSelf(Context ctx) {
		super();
		this.context = ctx;
		msTitle.put("alert", "alert");
		msTitle.put("confirm", "confirm");
		msTitle.put("Prompt", "Prompt");
	}

	public WebChromeClientSelf(Context ctx, Map<String, String> map) {
		this.context = ctx;
		replaceMap(map);
	}

	@Override
	public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
		// TODO Auto-generated method stub
		Builder builder = new Builder(this.context);
		builder.setTitle(msTitle.get("alert"));
		builder.setMessage(message);
		final JsResult nresult = result;
		builder.setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				nresult.confirm();
			}
		});
		builder.setCancelable(false);
		builder.create();
		// builder.show();
		// Log.i(AmMobCommon.ammoblog, "context:" + context.getClass().getName() + ", url:" + url + "message:" +
		// message);
		// Logger.info(context, "js", "context:" + context.getClass().getName() + ", url:" + url + "message:" +
		// message);
		return true;
	};

	public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
		Builder builder = new Builder(this.context);
		builder.setTitle(msTitle.get("confirm"));
		builder.setMessage(message);
		builder.setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				result.confirm();
			}
		});
		builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				result.cancel();
			}
		});
		builder.setCancelable(false);
		builder.create();
		builder.show();
		return true;
	};

}
