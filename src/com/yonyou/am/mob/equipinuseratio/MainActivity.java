package com.yonyou.am.mob.equipinuseratio;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.ant.liao.GifView;
import com.ant.liao.GifView.GifImageType;
import com.yonyou.am.mob.commonactivity.AmBaseActivity;

public class MainActivity extends AmBaseActivity {

	GifView gf1;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.yonyou.am.mob.commonactivity.AmBaseActivity#onCreate(android.os.Bundle
	 * )
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Button button = (Button) findViewById(R.id.button1);

		gf1 = (GifView) findViewById(R.id.gif);
		// ����GifͼƬԴ
		gf1.setGifImage(R.drawable.gif1);
		// ��Ӽ�����
		gf1.setOnClickListener(this);
		// ������ʾ�Ĵ�С���������ѹ��
		gf1.setShowDimension(400, 400);
		// ���ü��ط�ʽ���ȼ��غ���ʾ���߼��ر���ʾ��ֻ��ʾ��һ֡����ʾ
		gf1.setGifImageType(GifImageType.COVER);

		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}

		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
