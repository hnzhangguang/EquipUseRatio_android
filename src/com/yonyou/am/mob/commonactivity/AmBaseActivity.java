package com.yonyou.am.mob.commonactivity;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import com.yonyou.am.mob.login.AppContext;
import com.yonyou.am.mob.tool.AmMobCommon;

/**
 * 资产-移动-原生-activity的基类 【想要实现登录必须继承此Activity】
 * 
 * @author zhangguang
 * 
 */
public class AmBaseActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		log(this.getClass().getName() + "--->onCreate");
		// 记录登录日志
		recordLoginLog(AppContext.getCurrent(this));
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		log(this.getClass().getName() + "--->onSaveInstanceState");
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onStart() {
		super.onStart();
		log(this.getClass().getName() + "--->onStart");
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		log(this.getClass().getName() + "--->onRestart");
	}

	@Override
	protected void onResume() {
		super.onResume();
		log(this.getClass().getName() + "--->onResume");
	}

	@Override
	protected void onPause() {
		super.onPause();
		log(this.getClass().getName() + "--->onPause");
	}

	@Override
	protected void onStop() {
		super.onStop();
		log(this.getClass().getName() + "--->onStop");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// android.os.Process.killProcess(android.os.Process.myPid());//关闭应用
		log(this.getClass().getName() + "--->onDestroy");
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		log(this.getClass().getName() + "--->onKeyDown");
		return super.onKeyDown(keyCode, event);

	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		log(this.getClass().getName() + "--->onKeyUp");
		return super.onKeyUp(keyCode, event);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Log.i(AmMobCommon.ammoblog, this.getClass().getName() + "--->onCreateOptionsMenu");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		log(this.getClass().getName() + "--->onContextItemSelected");
		return super.onContextItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		log(this.getClass().getName() + "--->onCreateContextMenu");
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public void finish() {
		super.finish();
		log(this.getClass().getName() + "--->finish");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		log(this.getClass().getName() + "--->onActivityResult");
	}

	@Override
	public void onClick(View v) {
		log(this.getClass().getName() + "--->onClick");

	}

	/**
	 * 记录登录日志
	 * 
	 * @author zhangguang
	 */
	public void recordLoginLog(AppContext appcontext) {
		if (appcontext.getUserid().toString().equals("") && appcontext.getToken().toString().equals("")) {
			log("你是强制登录进来的。。。");
		}
		log("appid:" + appcontext.getAPPID() + "; funcode:" + appcontext.getFuncode() + "; host:"
				+ appcontext.getHost() + ";SessionID:" + appcontext.getSessionID() + "; Userid:"
				+ appcontext.getUserid() + ";Token:" + appcontext.getToken() + ";packagename:"
				+ appcontext.getPackageName() + ",Massotoken:" + appcontext.getMassotoken() + ",GroupID:"
				+ appcontext.getGroupID());

	}

	/**
	 * 判断当前网络是否可用
	 * 
	 * @param activity
	 * @return
	 * @author zhangguang
	 */
	public boolean isNetworkAvailable(AmBaseActivity activity) {
		Context context = activity.getApplicationContext();
		// 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (connectivityManager == null) {
			return false;
		} else {
			// 获取NetworkInfo对象
			NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

			if (networkInfo != null && networkInfo.length > 0) {
				for (int i = 0; i < networkInfo.length; i++) {
					log(i + "===状态===" + networkInfo[i].getState() + ";\n" + i + "===类型==="
							+ networkInfo[i].getTypeName());
					// 判断当前网络状态是否为连接状态
					if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
				NetworkInfo mNetworkInfo = connectivityManager.getActiveNetworkInfo();
				if (mNetworkInfo != null) {
					log("当前活动的网络信息:TypeName=" + mNetworkInfo.getTypeName() + ";SubtypeName="
							+ mNetworkInfo.getSubtypeName());
					return mNetworkInfo.isAvailable();
				}
			}
		}
		return false;
	}

	/**
	 * 
	 * @param msg
	 * @author zhangguang
	 */
	public void log(Object msg) {
		Log.i(AmMobCommon.ammoblog, msg + "");
	}

	/**
	 * 获取当前移动设备屏幕的宽度
	 * 
	 * @return 宽度值
	 * @author zhangguang
	 */
	public int getWindowWidth() {
		DisplayMetrics dm = new DisplayMetrics();
		// 取得窗口属性
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;

		// 窗口的宽度
		// int screenWidth = dm.widthPixels;
		// 窗口高度
		// int screenHeight = dm.heightPixels;
	}

	/**
	 * 判断是否有存储卡？
	 * 
	 * @return
	 * @author zhangguang
	 */
	public boolean ExistSDCard() {
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			return true;
		} else
			return false;
	}

	/**
	 * 如果有存储卡，那么存储卡的剩余空间的大小是多少？
	 * 
	 * @return
	 * @author zhangguang
	 */
	public long getSDFreeSize() {
		// 取得SD卡文件路径
		File path = Environment.getExternalStorageDirectory();
		StatFs sf = new StatFs(path.getPath());
		// 获取单个数据块的大小(Byte)
		long blockSize = sf.getBlockSize();
		// 空闲的数据块的数量
		long freeBlocks = sf.getAvailableBlocks();
		// 返回SD卡空闲大小
		// return freeBlocks * blockSize; //单位Byte
		// return (freeBlocks * blockSize)/1024; //单位KB
		return (freeBlocks * blockSize) / 1024 / 1024; // 单位MB
	}
}
