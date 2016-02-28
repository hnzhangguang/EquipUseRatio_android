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
 * �ʲ�-�ƶ�-ԭ��-activity�Ļ��� ����Ҫʵ�ֵ�¼����̳д�Activity��
 * 
 * @author zhangguang
 * 
 */
public class AmBaseActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		log(this.getClass().getName() + "--->onCreate");
		// ��¼��¼��־
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
		// android.os.Process.killProcess(android.os.Process.myPid());//�ر�Ӧ��
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
	 * ��¼��¼��־
	 * 
	 * @author zhangguang
	 */
	public void recordLoginLog(AppContext appcontext) {
		if (appcontext.getUserid().toString().equals("") && appcontext.getToken().toString().equals("")) {
			log("����ǿ�Ƶ�¼�����ġ�����");
		}
		log("appid:" + appcontext.getAPPID() + "; funcode:" + appcontext.getFuncode() + "; host:"
				+ appcontext.getHost() + ";SessionID:" + appcontext.getSessionID() + "; Userid:"
				+ appcontext.getUserid() + ";Token:" + appcontext.getToken() + ";packagename:"
				+ appcontext.getPackageName() + ",Massotoken:" + appcontext.getMassotoken() + ",GroupID:"
				+ appcontext.getGroupID());

	}

	/**
	 * �жϵ�ǰ�����Ƿ����
	 * 
	 * @param activity
	 * @return
	 * @author zhangguang
	 */
	public boolean isNetworkAvailable(AmBaseActivity activity) {
		Context context = activity.getApplicationContext();
		// ��ȡ�ֻ��������ӹ�����󣨰�����wi-fi,net�����ӵĹ���
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (connectivityManager == null) {
			return false;
		} else {
			// ��ȡNetworkInfo����
			NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

			if (networkInfo != null && networkInfo.length > 0) {
				for (int i = 0; i < networkInfo.length; i++) {
					log(i + "===״̬===" + networkInfo[i].getState() + ";\n" + i + "===����==="
							+ networkInfo[i].getTypeName());
					// �жϵ�ǰ����״̬�Ƿ�Ϊ����״̬
					if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
				NetworkInfo mNetworkInfo = connectivityManager.getActiveNetworkInfo();
				if (mNetworkInfo != null) {
					log("��ǰ���������Ϣ:TypeName=" + mNetworkInfo.getTypeName() + ";SubtypeName="
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
	 * ��ȡ��ǰ�ƶ��豸��Ļ�Ŀ��
	 * 
	 * @return ���ֵ
	 * @author zhangguang
	 */
	public int getWindowWidth() {
		DisplayMetrics dm = new DisplayMetrics();
		// ȡ�ô�������
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;

		// ���ڵĿ��
		// int screenWidth = dm.widthPixels;
		// ���ڸ߶�
		// int screenHeight = dm.heightPixels;
	}

	/**
	 * �ж��Ƿ��д洢����
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
	 * ����д洢������ô�洢����ʣ��ռ�Ĵ�С�Ƕ��٣�
	 * 
	 * @return
	 * @author zhangguang
	 */
	public long getSDFreeSize() {
		// ȡ��SD���ļ�·��
		File path = Environment.getExternalStorageDirectory();
		StatFs sf = new StatFs(path.getPath());
		// ��ȡ�������ݿ�Ĵ�С(Byte)
		long blockSize = sf.getBlockSize();
		// ���е����ݿ������
		long freeBlocks = sf.getAvailableBlocks();
		// ����SD�����д�С
		// return freeBlocks * blockSize; //��λByte
		// return (freeBlocks * blockSize)/1024; //��λKB
		return (freeBlocks * blockSize) / 1024 / 1024; // ��λMB
	}
}
