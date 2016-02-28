package com.yonyou.am.mob.login;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.yonyou.am.mob.commonactivity.AmBaseActivity;
import com.yonyou.am.mob.equipinuseratio.EiurListActivity;
import com.yonyou.am.mob.equipinuseratio.R;

/**
 * 功能说明：am-mob-login page
 * 
 * @author zhangg
 * @2015-11-8
 */
public class AmMobLoginActivit extends AmBaseActivity {

	EditText address = null;
	EditText port = null;
	EditText user = null;
	EditText password = null;
	Button denglu = null;
	Button zhoudongfuwu = null;
	Button minxiaoxiafuwu = null;
	Button gonggongfuwu = null;
	Button qiangzhidenglu = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.amlogin);
		// initActionbar();

		address = (EditText) findViewById(R.id.address);
		port = (EditText) findViewById(R.id.port);
		user = (EditText) findViewById(R.id.user);
		password = (EditText) findViewById(R.id.password);

		zhoudongfuwu = (Button) findViewById(R.id.zhoudongfuwu);
		minxiaoxiafuwu = (Button) findViewById(R.id.minxiaoxiafuwu);
		gonggongfuwu = (Button) findViewById(R.id.gonggongfuwu);
		qiangzhidenglu = (Button) findViewById(R.id.qiangzhidenglu);
		zhoudongfuwu.setOnClickListener(this);
		minxiaoxiafuwu.setOnClickListener(this);
		qiangzhidenglu.setOnClickListener(this);
		gonggongfuwu.setOnClickListener(this);

		denglu = (Button) findViewById(R.id.denglu);
		denglu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String addressString = address.getText() + "";
				String portString = port.getText() + "";
				String userString = user.getText() + "";
				String passwordString = password.getText() + "";

				AppContext appContext = AppContext.getCurrent(AmMobLoginActivit.this);

				SharedPreferences sharedPreferences = AmMobLoginActivit.this.getSharedPreferences(
						"am", Activity.MODE_PRIVATE);
				Editor editor = sharedPreferences.edit();
				editor.putString("ttt", "222");
				editor.commit();
				// Toast.makeText(AmMobLoginActivit.this,
				// sharedPreferences.getString("ttt", "111"), Toast.LENGTH_LONG)
				// .show();
				// appContext.setUser(sharedPreferences.getString("USER", ""));
				// appContext.setPassword(sharedPreferences.getString("PASSWORD",
				// ""));
				// appContext.setHost(sharedPreferences.getString("HOST", ""));
				// appContext.setPort(sharedPreferences.getString("PORT", ""));
				appContext.setUser(userString);
				appContext.setPassword(passwordString);
				appContext.setHost(addressString);
				appContext.setPort(portString);
				appContext.setValue("funcid", "A04500");
				appContext.setValue("funcode", "A04500");

				// Toast.makeText(LoginActivity.this, userString, 1).show();
				ServiceUtils.login(AmMobLoginActivit.this);
			}
		});

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		if (v.getId() == R.id.zhoudongfuwu) {
			address.setText("10.1.72.79");
			port.setText("80");

		} else if (v.getId() == R.id.gonggongfuwu) {
			address.setText("219.141.185.73");
			port.setText("9083");
		} else if (v.getId() == R.id.qiangzhidenglu) {
			AppContext appContext = AppContext.getCurrent(AmMobLoginActivit.this);
			appContext.setUser(null);
			appContext.setPassword(null);
			appContext.setHost(null);
			appContext.setPort(null);
			Intent intent = new Intent(this, EiurListActivity.class);
			startActivity(intent);
			// finish();

		} else if (v.getId() == R.id.minxiaoxiafuwu) {
			address.setText("10.1.72.47");
			port.setText("80");
		}
	}

	/**
	 * 导航栏初始化工作
	 */
	private void initActionbar() {
		// 自定义标题栏
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayShowTitleEnabled(false);
		getActionBar().setDisplayShowCustomEnabled(false);

	}

}
