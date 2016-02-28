package com.yonyou.am.mob.login;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.yonyou.am.mob.commonactivity.AmBaseActivity;
import com.yonyou.am.mob.equipinuseratio.R;

public class LoginActivity extends AmBaseActivity {

	EditText address = null;
	EditText port = null;
	EditText user = null;
	EditText password = null;
	Button denglu = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		address = (EditText) findViewById(R.id.address);
		port = (EditText) findViewById(R.id.port);
		user = (EditText) findViewById(R.id.user);
		password = (EditText) findViewById(R.id.password);

		denglu = (Button) findViewById(R.id.denglu);
		denglu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String addressString = address.getText() + "";
				String portString = port.getText() + "";
				String userString = user.getText() + "";
				String passwordString = password.getText() + "";

				AppContext appContext = AppContext.getCurrent(LoginActivity.this);

				SharedPreferences sharedPreferences = LoginActivity.this.getSharedPreferences("am", 0);
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
				ServiceUtils.login(LoginActivity.this);
			}
		});

	}

}
