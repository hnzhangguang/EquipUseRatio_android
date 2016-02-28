package com.yonyou.am.mob.equipinuseratio;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yonyou.am.mob.commonactivity.AmBaseActivity;
import com.yonyou.am.mob.login.AbstractServiceCallBack;
import com.yonyou.am.mob.login.ServiceUtils;
import com.yonyou.am.mob.tool.AmMobCommon;
import com.yonyou.am.mob.tool.AmMobUtils;
import com.yonyou.am.mob.tool.CircleView;

/**
 * ����˵�����ʲ�-�豸������ �������
 * 
 * @author zhangg
 * @2015-11-6
 */
public class EiurDetailActivity extends AmBaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eiur_detail);
		// ��ʼ�������ĵ���������
		initActionBar();
		// ��ʼ���ؼ�����
		init();
		// ���ú�̨
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orgid", orgid);
		map.put("departid", departid);
		// call(map);

	}

	/**
	 * ���ú�̨
	 * 
	 * @param map
	 */
	public void call(Map<String, Object> map) {
		// �����Ҫ��Ϊ�˲�������ma�˵�ʵ��
		JSONObject paramJson = new JSONObject();
		try {
			paramJson.put("transitype", AmMobCommon.EquipInUseRation_transitype);
			paramJson.put(yearMonth, date.getText().toString());
			if (!map.isEmpty()) {
				for (String key : map.keySet()) {
					paramJson.put(key, map.get(key));
				}
			}
			// ���ú�̨
			callAction("queryEquipRationByUser", paramJson, new EiurDetailCallBack(this));
		} catch (JSONException e) {
			e.printStackTrace();
			log(e);
		}

	}

	/**
	 * ����˵�������ú�̨��callbackʵ����
	 * 
	 * @author zhangg
	 * @2015-11-9
	 */
	class EiurDetailCallBack extends AbstractServiceCallBack {

		public EiurDetailCallBack(AmBaseActivity activity) {
			super(activity);
		}

		@Override
		public void execute(JSONObject json) {
			// showInfo(EiurDetailActivity.this, "NC datas...");
			try {
				Map<String, Object> map = AmMobUtils.jsonToMap(json);
				if (map.get("msg") != null) {
					showInfo(EiurDetailActivity.this, json.get("msg"));
					jsonArray = new JSONArray();
					// ������Ϣ
					sendMessage(0x5554);
					log(jsonArray);
					return;
				}
				// ����Ӻ�̨���������
				if (json.get("info") != null) {
					jsonArray = new JSONArray(json.get("info").toString());
					if (0 == jsonArray.length()) {
						showInfo(EiurDetailActivity.this, "��̨����Ϊ��!");
						log("��̨����Ϊ��!");
					}
					// ��ǰ�¡���һ���¡���һ���µİٷֱ�Ҫչ�ֵ�����
					nowMonth = map.get("nowMonth") + "";
					preMonth = map.get("preMonth") + "";
					nextMonth = map.get("nextMonth") + "";
				} else {
					jsonArray = new JSONArray();
					for (int i = 0; i < 22; i++) {
						JSONObject jsonObject = new JSONObject();
						jsonObject.put("name", "nb" + i + "�Ż��");
						jsonObject.put("percent", AmMobUtils.getRandom(2));
						jsonObject.put("id", i + "");
						jsonArray.put(jsonObject);
					}
				}
				sendMessage(0x5554);
				log(jsonArray);
			} catch (JSONException e) {
				e.printStackTrace();
				log(e);
			}
		}

		@Override
		public void error(String s, String s1) {
			Log.e(AmMobCommon.ammoblog, s + "->" + s1);
			Toast.makeText(EiurDetailActivity.this, s + "->" + s1 + "���ڵ�������α����", Toast.LENGTH_LONG).show();
			try {
				// ����Ӻ�̨���������
				jsonArray = new JSONArray();
				for (int i = 0; i < 32; i++) {
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("name", i + "�Ż��");
					jsonObject.put("percent", AmMobUtils.getRandom(2));
					jsonObject.put("id", i + "");
					jsonArray.put(jsonObject);
				}
				if (0 == jsonArray.length()) {
					showInfo(EiurDetailActivity.this, "����Ϊ��!");
					log("����Ϊ��!");
					return;
				}
				// ������Ϣչʾ����
				sendMessage(0x5554);
			} catch (JSONException e) {
				e.printStackTrace();
				log(e);
			}

		}

	}

	/**
	 * ������Ϣ���½�������
	 * 
	 * @param what
	 *            ��Ϣ����
	 */
	private void sendMessage(final int what) {
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				handler.sendEmptyMessage(what);
			}
		}, 1000);
	}

	@Override
	protected void onStart() {
		super.onStart();

	}

	@Override
	protected void onResume() {
		super.onResume();
		sendMessage(0x123);// 0x5554
		// sendMessage(0x5554);

	}

	/**
	 * ����˵����js �ص�
	 * 
	 * @author zhangg
	 * @2015-11-11
	 */
	class EiurOjbect {
		public EiurOjbect() {
		}

		@JavascriptInterface
		public void execute(String obj) {
			showInfo(EiurDetailActivity.this, obj);
		}

		@JavascriptInterface
		public void aa() {
			showInfo(EiurDetailActivity.this, "aa");
		}
	}

	@SuppressLint({ "SetJavaScriptEnabled", "InflateParams" })
	private void init() {

		biaotitextview = (TextView) this.findViewById(R.id.biaotitextview);
		biaotitextview.setText("�豸����������");
		chartshow_wbusedepart = (WebView) this.findViewById(R.id.chartshow_wbusedepart);
		// ���ÿ��Է����ļ�����ִ�е���JS������HTML��
		chartshow_wbusedepart.getSettings().setAllowFileAccess(true);
		chartshow_wbusedepart.getSettings().setJavaScriptEnabled(true);
		chartshow_wbusedepart.loadUrl("file:///android_asset/eiur/eiurechartdetail.html");
		chartshow_wbusedepart.addJavascriptInterface(new EiurOjbect(), "eiur");

		fanhuibutton = (Button) findViewById(R.id.fanhuibutton);
		leftcircle = (CircleView) findViewById(R.id.leftcircle);
		middlecircle = (CircleView) findViewById(R.id.middlecircle);
		rightcircle = (CircleView) findViewById(R.id.rightcircle);
		leftcircle.setOnClickListener(this);
		middlecircle.setOnClickListener(this);
		rightcircle.setOnClickListener(this);
		fanhuibutton.setOnClickListener(this);

		// ����-������layout
		linearlayoutdate = (LinearLayout) findViewById(R.id.linearlayoutdate);
		LayoutInflater inflater = LayoutInflater.from(this);
		// ��̬���ز���
		LinearLayout datelayout = (LinearLayout) inflater.inflate(R.layout.dateselectview, null);
		linearlayoutdate.addView(datelayout);
		date = (TextView) findViewById(R.id.date);

		// Ϊ������ֱ��������ҳ��ʱIntent��Ϊnull��
		if (null == getIntent()) {
			log("intent is null");
		} else {
			Bundle bundle = getIntent().getExtras();
			if (null != bundle) {
				String dateString = bundle.getString(AmMobCommon.date);
				departid = bundle.getString("departid");
				orgid = bundle.getString("orgid");
				date.setText(dateString);
				biaotitextview.setText("�豸����������");
			} else {
				log("bundle is null");
			}
		}

		leftbutton = (Button) findViewById(R.id.leftbutton);
		rightbutton = (Button) findViewById(R.id.rightbutton);
		leftbutton.setOnClickListener(this);
		rightbutton.setOnClickListener(this);

		colors = new int[] { R.color.olive, R.color.red, R.color.deeppink, R.color.tomato, R.color.crimson,
				R.color.gainsboro, R.color.plum, R.color.lightcyan, R.color.lightcoral, R.color.royalblue,
				R.color.aqua, R.color.lightskyblue, R.color.olive, R.color.saddlebrown, R.color.palegreen,
				R.color.mediumblue, R.color.gold, R.color.lemonchiffon, R.color.lavenderblush };

		int radius = getWindowWidth() / 9;
		// ���Բ
		leftcircle.setRadius(radius + radius / 5);
		leftcircle.setColor(getResources().getColor(colors[8]));
		leftcircle.setGravity(Gravity.CENTER);

		// �ұ�Բ
		rightcircle.setRadius(radius + radius / 5);
		rightcircle.setColor(getResources().getColor(colors[5]));
		rightcircle.setGravity(Gravity.CENTER);

		// �м�Բ
		middlecircle.setRadius(radius + radius / 2);
		middlecircle.setColor(getResources().getColor(colors[2]));
		middlecircle.setGravity(Gravity.CENTER);

		/*** ����ͼ�������ã� */
		middlecircle.setOnClickListener(this);
		// ��ʼ��handler
		initHandler();

	}

	/**
	 * ��ʼ��handler
	 */
	private void initHandler() {
		handler = new Handler() {
			@SuppressLint("HandlerLeak")
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if (msg.what == 0x123) {
					if (isInitProjectEtc()) {
						rightcircle.setText(AmMobUtils.getRandom(1) + "%");
						middlecircle.setText(AmMobUtils.getRandom(1) + "%");
						leftcircle.setText(AmMobUtils.getRandom(1) + "%");
						Double[] doubletemp = AmMobUtils.getDoubleDatas(16, 2);
						String[] nameArray = new String[] { "����A", "����B", "�ʼǱ�C", "̨ʽ��D", "������E", "���ӻ�F", "����A2",
								"����B2", "�ʼǱ�C2", "̨ʽ��D2", "������E2", "���ӻ�F2", "����A3", "����B3", "�ʼǱ�C3", "̨ʽ��D3" };
						chartshow_wbusedepart.loadUrl(AmMobUtils.buildLoadJsString("bar", nameArray, doubletemp));
						Log.i(AmMobCommon.ammoblog, AmMobUtils.buildLoadJsString("bar", nameArray, doubletemp));
					}
					// �Ӻ�̨��ѯ��������
				} else if (msg.what == 0x5554) {
					loadDataForJSONArray(preMonth, nowMonth, nextMonth, jsonArray);
				}
			}
		};
	}

	/**
	 * �Ѻ�̨�����������װ�ɽ�����ʾ������
	 * 
	 * @param leftcircledisplay
	 *            ��Բ��ʾ
	 * @param middlecircledisplay
	 *            �м�Բ��ʾ
	 * @param rightcircledisplay
	 *            ��Բ��ʾ
	 * @param jsonArray
	 *            ��״ͼҪչʾ������
	 */
	protected void loadDataForJSONArray(String preMonth, String nowMonth, String nextMonth, JSONArray jsonArray) {
		rightcircleDisplay = nextMonth.contains("%") ? nextMonth : nextMonth + "%";
		middlecircleDisplay = nowMonth.contains("%") ? nowMonth : nowMonth + "%";
		leftcircleDisplay = preMonth.contains("%") ? preMonth : preMonth + "%";
		rightcircle.setText(rightcircleDisplay);
		middlecircle.setText(middlecircleDisplay);
		leftcircle.setText(leftcircleDisplay);

		try {
			// ��װ������,��Handlerֱ��չ�ּ���
			List<String> namelist = new ArrayList<String>();
			List<Double> percentlist = new ArrayList<Double>();
			if (jsonArray.length() != 0) {
				for (int i = 0; i < jsonArray.length(); i++) {
					namelist.add(new JSONObject(jsonArray.get(i) + "").get("name") + "");
					percentlist.add(Double.valueOf(new JSONObject(jsonArray.get(i) + "").get("percent") + ""));
				}
			}
			doubletemp = percentlist.toArray(new Double[0]);
			nameArray = namelist.toArray(new String[0]);
			chartshow_wbusedepart.loadUrl(AmMobUtils.buildLoadJsString("bar", nameArray, doubletemp));
			Log.i(AmMobCommon.ammoblog, AmMobUtils.buildLoadJsString("bar", nameArray, doubletemp));
		} catch (JSONException e) {
			e.printStackTrace();
			Log.e(AmMobCommon.ammoblog, e + "");
		}

	}

	/**
	 * ����������ʾ
	 * 
	 * @param dateString
	 *            ��ǰ����
	 * @param isPreDate
	 *            �Ƿ��ǵ�ǰһ����
	 * @return Ϊnull��˵������������Ϊ�������ʾ��Ϣ
	 */
	private Object changeDateDispaly(String dateString, boolean isPreDate) {

		if (dateString != null && dateString.length() == 7) {
			int month = Integer.parseInt(dateString.substring(5));
			int year = Integer.parseInt(dateString.substring(0, 4));
			if (isPreDate) {
				if (01 == month) {
					year = year - 1;
					month = 12;
				} else {
					month = month - 1;
				}
				if (("" + month).length() == 1) {
					date.setText(year + "-0" + month);
				} else {
					date.setText(year + "-" + month);
				}
			} else {
				// "����������ǰ���ڵĲ�ѯ!"
				Calendar calendar = Calendar.getInstance();
				int nowyear = calendar.get(Calendar.YEAR);
				int nowmonth = calendar.get(Calendar.MONTH);
				if (nowyear <= year && nowmonth < month) {
					return "����������ǰ���ڵĲ�ѯ!";
				}
				if (12 == month) {
					year = year + 1;
					month = 1;
				} else {
					month = month + 1;
				}
				if (("" + month).length() == 1) {
					date.setText(year + "-0" + month);
				} else {
					date.setText(year + "-" + month);
				}
			}
		}
		return null;
	}

	/**
	 * ��һ���µ�����
	 */
	private void loadPreMonthData(List<Map<String, String>> nowmonthlist) {
		// ������ɫ
		rightcircle.setColor(middlecircle.getColor());
		// ��ȡ�м�Բ����ɫ
		middlecircle.setColor(leftcircle.getColor());
		leftcircle.setColor(getResources().getColor(colors[Math.abs(new Random().nextInt()) % 18]));

		// ������ʾ
		rightcircle.setText(middlecircle.getText().toString());
		middlecircle.setText(leftcircle.getText().toString());
		leftcircle.setText(AmMobUtils.getRandom(1) + "%");

		if (null != nowmonthlist) {

			List<Map<String, String>> templist = AmMobUtils.exeSort(nowmonthlist, "asc", "percent", true);
			List<String> nameList = new ArrayList<String>();
			Double[] perList = new Double[templist.size()];

			Map<String, String> tempMap = null;
			for (int i = 0; i < templist.size(); i++) {
				tempMap = templist.get(i);
				nameList.add(tempMap.get("name"));
				perList[i] = (Double.valueOf(tempMap.get("percent").toString()
						.substring(0, tempMap.get("percent").toString().length() - 1)));
			}
			chartshow_wbusedepart
					.loadUrl(AmMobUtils.buildLoadJsString("bar", nameList.toArray(new String[0]), perList));
			Log.i(AmMobCommon.ammoblog, AmMobUtils.buildLoadJsString("bar", nameList.toArray(new String[0]), perList));
		}

	}

	/**
	 * ��һ���µ�����
	 */
	private void loadNextMonthData() {
		leftcircle.setColor(middlecircle.getColor());
		middlecircle.setColor(rightcircle.getColor());
		rightcircle.setColor(getResources().getColor(colors[Math.abs(new Random().nextInt()) % 18]));

		leftcircle.setText(middlecircle.getText().toString());
		middlecircle.setText(rightcircle.getText().toString());
		rightcircle.setText(AmMobUtils.getRandom(1) + "%");

		// double[] doubletemp = AmMobUtils.getDoubleDatas(6, 2);
		// chartshow_wbusedepart.loadUrl(AmMobUtils.buildLoadJsString("bar",
		// doubletemp));
		// Log.i(AmMobCommon.ammoblog, AmMobUtils.buildLoadJsString("bar",
		// doubletemp));
		Double[] doubletemp = AmMobUtils.getDoubleDatas(16, 2);
		String[] nameArray = new String[] { "����A", "����B", "�ʼǱ�C", "̨ʽ��D", "������E", "���ӻ�F", "����A2", "����B2", "�ʼǱ�C2",
				"̨ʽ��D2", "������E2", "���ӻ�F2", "����A3", "����B3", "�ʼǱ�C3", "̨ʽ��D3" };
		chartshow_wbusedepart.loadUrl(AmMobUtils.buildLoadJsString("bar", nameArray, doubletemp));
		Log.i(AmMobCommon.ammoblog, AmMobUtils.buildLoadJsString("bar", nameArray, doubletemp));

	}

	@Override
	public void onClick(View v) {
		String dateString = date.getText().toString();

		// ����󷭰�ť
		if (v.getId() == R.id.leftbutton || v.getId() == R.id.leftcircle) {
			if (isInitProjectEtc()) {
				showInfo(EiurDetailActivity.this, "��ʼ�����������...");
				return;
			}
			// ������ʾ����
			if (null == changeDateDispaly(dateString, true)) {
				// ��һ���µ�����
				List<Map<String, String>> nowmonthlist = new ArrayList<Map<String, String>>();
				for (int i = 0; i < 19; i++) {
					Map<String, String> temp = new HashMap<String, String>();
					temp.put("id", i + "");
					String nameString = "̨ʽ��" + i;
					if (i % 3 == 0) {
						nameString = "�ʼǱ�" + i;
					} else if (i % 7 == 0) {
						nameString = "����" + i;
					} else if (i % 5 == 0) {
						nameString = "����" + i;
					}
					temp.put("name", nameString);
					temp.put("percent", AmMobUtils.getRandom(2) + "%");
					nowmonthlist.add(temp);
				}
				loadPreMonthData(nowmonthlist);

				// ����NC��̨��ѯ����
				Map<String, Object> map = new HashMap<String, Object>();
				map.put(yearMonth, date.getText().toString());
				map.put("orgid", orgid);
				map.put("departid", departid);
				// ���ú�̨
				// call(map);

			} else {
				showInfo(EiurDetailActivity.this, changeDateDispaly(dateString, true));
				return;
			}

			// ����ҷ���ť
		} else if (v.getId() == R.id.rightbutton || v.getId() == R.id.rightcircle) {
			// �Ƿ����
			if (null == changeDateDispaly(dateString, false)) {
				// �ҷ���ť
				// loadNextMonthData();

				Map<String, Object> map = new HashMap<String, Object>();
				map.put(yearMonth, date.getText().toString());
				map.put("orgid", orgid);
				map.put("departid", departid);
				// ���ú�̨
				// call(map);

				loadNextMonthData();

			} else {
				showInfo(EiurDetailActivity.this, changeDateDispaly(dateString, false));
				return;
			}
			// ���ذ�ť
		} else if (v.getId() == R.id.middlecircle) {
			Log.i(AmMobCommon.ammoblog, "�������м��Բ...");
		} else if (v.getId() == R.id.fanhuibutton) {
			finish();
		}

	}

	/**
	 * 
	 * @param countValues
	 *            ����������
	 * @param decimal
	 *            ������С��λ��
	 * @return
	 */
	private double getAverageValue(double[] countValues, int decimal) {
		if (countValues.length == 0) {
			return 0.0;
		}
		double average = 0;
		for (int i = 0; i < countValues.length; i++) {
			average += countValues[i];
		}
		return Double.valueOf(((average / countValues.length) + "").substring(0, 3 + decimal));
	}

	/**
	 * ������ʾ
	 * 
	 * @param textView
	 * @param displaycontont
	 */
	protected void setCircleDisplay(TextView textView, String displaycontont) {
		textView.setText(displaycontont);

	}

	@SuppressLint("InflateParams")
	private void initActionBar() {
		// �Զ��������
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayShowTitleEnabled(false);
		getActionBar().setDisplayShowCustomEnabled(true);
		LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View mTitleView = mInflater.inflate(R.layout.navigationbar, null);
		getActionBar().setCustomView(mTitleView,
				new ActionBar.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

	}

	/**
	 * �Ƿ��ǳ�ʼ�����ݵĹ�����
	 * 
	 * @return
	 */
	public boolean isInitProjectEtc() {
		if (leftcircle.getText().toString().equals("") && rightcircle.getText().toString().equals("")
				&& middlecircle.getText().toString().equals("")) {
			return true;
		}
		return false;
	}

	public void showInfo(Activity content, Object msg) {
		Toast.makeText(content, "" + msg, 1).show();
	}

	public void log(Object msg) {
		Log.i(AmMobCommon.ammoblog, msg + "");
	}

	/**
	 * ������Ϣ���½�������
	 * 
	 * @param what
	 *            ��Ϣ����
	 */
	private void sendEmptyMessage(int what) {
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				handler.sendEmptyMessage(0x5554);
			}
		}, 100);
	}

	/**
	 * ����NC����ȡ����
	 * 
	 * @param methodName
	 *            -MAmethod
	 * @param paramJson
	 *            ������json
	 * @param callBack
	 *            �ص�����
	 * @author zhangg
	 */
	public void callAction(String methodName, JSONObject paramJson, AbstractServiceCallBack callBack) {
		ServiceUtils.callAction(this, AmMobCommon.NCAMEquipInUseRationController, methodName, paramJson, callBack);
	}

	/** �󷭰�ť�ؼ� */
	Button leftbutton = null;
	/** �ҷ���ť �ؼ� */
	Button rightbutton = null;
	/** ���ذ�ť�ؼ� */
	Button fanhuibutton = null;
	/** ���Բ�ؼ� */
	CircleView leftcircle = null;
	/** �м�Բ�ؼ� */
	CircleView middlecircle = null;
	/** �ұ�Բ�ؼ� */
	CircleView rightcircle = null;
	/** �������ؼ� */
	TextView biaotitextview = null;
	/** ��������ʾ */
	String departNameDisplay = null;
	/** ���Բ��ʾ */
	String leftcircleDisplay = null;
	/** �м�Բ��ʾ */
	String middlecircleDisplay = null;
	/** �ұ�Բ��ʾ */
	String rightcircleDisplay = null;
	/** ��ǰ��ʾ���� */
	// private static String yearmonth = "";
	/** WebView */
	WebView chartshow_wbusedepart = null;
	JSONArray jsonArray = null;
	/** ����֯������Ĭ��10���� */
	int totalOrgNum = 20;
	/** ��ǰ��ʾ����֯��ţ���Ĭ�ϵڶ����� */
	// int nowOrgSequence = 8;
	TextView date = null;
	LinearLayout linearlayoutdate = null;
	int[] colors = null;
	Handler handler = null;
	Double[] doubletemp = null;
	String[] nameArray = null;
	String yearMonth = "yearMonth";
	String nowMonth = null;
	String preMonth = null;
	String nextMonth = null;
	String departid = null;
	String orgid = null;
	SharedPreferences preference = null;

}
