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
 * 功能说明：资产-设备利用率 详情界面
 * 
 * @author zhangg
 * @2015-11-6
 */
public class EiurDetailActivity extends AmBaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eiur_detail);
		// 初始化顶部的导航栏工作
		initActionBar();
		// 初始化控件工作
		init();
		// 调用后台
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orgid", orgid);
		map.put("departid", departid);
		// call(map);

	}

	/**
	 * 调用后台
	 * 
	 * @param map
	 */
	public void call(Map<String, Object> map) {
		// 这个主要是为了测试连接ma端的实现
		JSONObject paramJson = new JSONObject();
		try {
			paramJson.put("transitype", AmMobCommon.EquipInUseRation_transitype);
			paramJson.put(yearMonth, date.getText().toString());
			if (!map.isEmpty()) {
				for (String key : map.keySet()) {
					paramJson.put(key, map.get(key));
				}
			}
			// 调用后台
			callAction("queryEquipRationByUser", paramJson, new EiurDetailCallBack(this));
		} catch (JSONException e) {
			e.printStackTrace();
			log(e);
		}

	}

	/**
	 * 功能说明：调用后台的callback实现类
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
					// 发送消息
					sendMessage(0x5554);
					log(jsonArray);
					return;
				}
				// 假设从后台查出的数据
				if (json.get("info") != null) {
					jsonArray = new JSONArray(json.get("info").toString());
					if (0 == jsonArray.length()) {
						showInfo(EiurDetailActivity.this, "后台数据为空!");
						log("后台数据为空!");
					}
					// 当前月、上一个月、下一个月的百分比要展现的数据
					nowMonth = map.get("nowMonth") + "";
					preMonth = map.get("preMonth") + "";
					nextMonth = map.get("nextMonth") + "";
				} else {
					jsonArray = new JSONArray();
					for (int i = 0; i < 22; i++) {
						JSONObject jsonObject = new JSONObject();
						jsonObject.put("name", "nb" + i + "号火箭");
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
			Toast.makeText(EiurDetailActivity.this, s + "->" + s1 + "现在的数据是伪数据", Toast.LENGTH_LONG).show();
			try {
				// 假设从后台查出的数据
				jsonArray = new JSONArray();
				for (int i = 0; i < 32; i++) {
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("name", i + "号火箭");
					jsonObject.put("percent", AmMobUtils.getRandom(2));
					jsonObject.put("id", i + "");
					jsonArray.put(jsonObject);
				}
				if (0 == jsonArray.length()) {
					showInfo(EiurDetailActivity.this, "数据为空!");
					log("数据为空!");
					return;
				}
				// 发送消息展示数据
				sendMessage(0x5554);
			} catch (JSONException e) {
				e.printStackTrace();
				log(e);
			}

		}

	}

	/**
	 * 发送消息更新界面数据
	 * 
	 * @param what
	 *            消息类型
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
	 * 功能说明：js 回调
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
		biaotitextview.setText("设备利用率详情");
		chartshow_wbusedepart = (WebView) this.findViewById(R.id.chartshow_wbusedepart);
		// 设置可以访问文件及其执行调用JS、加载HTML等
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

		// 日期-翻动栏layout
		linearlayoutdate = (LinearLayout) findViewById(R.id.linearlayoutdate);
		LayoutInflater inflater = LayoutInflater.from(this);
		// 动态加载布局
		LinearLayout datelayout = (LinearLayout) inflater.inflate(R.layout.dateselectview, null);
		linearlayoutdate.addView(datelayout);
		date = (TextView) findViewById(R.id.date);

		// 为测试用直接启动该页面时Intent是为null的
		if (null == getIntent()) {
			log("intent is null");
		} else {
			Bundle bundle = getIntent().getExtras();
			if (null != bundle) {
				String dateString = bundle.getString(AmMobCommon.date);
				departid = bundle.getString("departid");
				orgid = bundle.getString("orgid");
				date.setText(dateString);
				biaotitextview.setText("设备利用率详情");
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
		// 左边圆
		leftcircle.setRadius(radius + radius / 5);
		leftcircle.setColor(getResources().getColor(colors[8]));
		leftcircle.setGravity(Gravity.CENTER);

		// 右边圆
		rightcircle.setRadius(radius + radius / 5);
		rightcircle.setColor(getResources().getColor(colors[5]));
		rightcircle.setGravity(Gravity.CENTER);

		// 中间圆
		middlecircle.setRadius(radius + radius / 2);
		middlecircle.setColor(getResources().getColor(colors[2]));
		middlecircle.setGravity(Gravity.CENTER);

		/*** 线形图（测试用） */
		middlecircle.setOnClickListener(this);
		// 初始化handler
		initHandler();

	}

	/**
	 * 初始化handler
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
						String[] nameArray = new String[] { "课桌A", "躺椅B", "笔记本C", "台式机D", "传呼机E", "兔子机F", "课桌A2",
								"躺椅B2", "笔记本C2", "台式机D2", "传呼机E2", "兔子机F2", "课桌A3", "躺椅B3", "笔记本C3", "台式机D3" };
						chartshow_wbusedepart.loadUrl(AmMobUtils.buildLoadJsString("bar", nameArray, doubletemp));
						Log.i(AmMobCommon.ammoblog, AmMobUtils.buildLoadJsString("bar", nameArray, doubletemp));
					}
					// 从后台查询更新数据
				} else if (msg.what == 0x5554) {
					loadDataForJSONArray(preMonth, nowMonth, nextMonth, jsonArray);
				}
			}
		};
	}

	/**
	 * 把后台查出的数据组装成界面显示的数据
	 * 
	 * @param leftcircledisplay
	 *            左圆显示
	 * @param middlecircledisplay
	 *            中间圆显示
	 * @param rightcircledisplay
	 *            右圆显示
	 * @param jsonArray
	 *            柱状图要展示的数据
	 */
	protected void loadDataForJSONArray(String preMonth, String nowMonth, String nextMonth, JSONArray jsonArray) {
		rightcircleDisplay = nextMonth.contains("%") ? nextMonth : nextMonth + "%";
		middlecircleDisplay = nowMonth.contains("%") ? nowMonth : nowMonth + "%";
		leftcircleDisplay = preMonth.contains("%") ? preMonth : preMonth + "%";
		rightcircle.setText(rightcircleDisplay);
		middlecircle.setText(middlecircleDisplay);
		leftcircle.setText(leftcircleDisplay);

		try {
			// 封装好数据,到Handler直接展现即可
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
	 * 更改日期显示
	 * 
	 * @param dateString
	 *            当前日期
	 * @param isPreDate
	 *            是否是到前一个月
	 * @return 为null是说明正常，否则为错误的提示信息
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
				// "不允许超过当前日期的查询!"
				Calendar calendar = Calendar.getInstance();
				int nowyear = calendar.get(Calendar.YEAR);
				int nowmonth = calendar.get(Calendar.MONTH);
				if (nowyear <= year && nowmonth < month) {
					return "不允许超过当前日期的查询!";
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
	 * 上一个月的数据
	 */
	private void loadPreMonthData(List<Map<String, String>> nowmonthlist) {
		// 更改颜色
		rightcircle.setColor(middlecircle.getColor());
		// 获取中间圆的颜色
		middlecircle.setColor(leftcircle.getColor());
		leftcircle.setColor(getResources().getColor(colors[Math.abs(new Random().nextInt()) % 18]));

		// 更改显示
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
	 * 下一个月的数据
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
		String[] nameArray = new String[] { "课桌A", "躺椅B", "笔记本C", "台式机D", "传呼机E", "兔子机F", "课桌A2", "躺椅B2", "笔记本C2",
				"台式机D2", "传呼机E2", "兔子机F2", "课桌A3", "躺椅B3", "笔记本C3", "台式机D3" };
		chartshow_wbusedepart.loadUrl(AmMobUtils.buildLoadJsString("bar", nameArray, doubletemp));
		Log.i(AmMobCommon.ammoblog, AmMobUtils.buildLoadJsString("bar", nameArray, doubletemp));

	}

	@Override
	public void onClick(View v) {
		String dateString = date.getText().toString();

		// 点击左翻按钮
		if (v.getId() == R.id.leftbutton || v.getId() == R.id.leftcircle) {
			if (isInitProjectEtc()) {
				showInfo(EiurDetailActivity.this, "初始化界面过程中...");
				return;
			}
			// 更改显示日期
			if (null == changeDateDispaly(dateString, true)) {
				// 上一个月的数据
				List<Map<String, String>> nowmonthlist = new ArrayList<Map<String, String>>();
				for (int i = 0; i < 19; i++) {
					Map<String, String> temp = new HashMap<String, String>();
					temp.put("id", i + "");
					String nameString = "台式机" + i;
					if (i % 3 == 0) {
						nameString = "笔记本" + i;
					} else if (i % 7 == 0) {
						nameString = "课桌" + i;
					} else if (i % 5 == 0) {
						nameString = "躺椅" + i;
					}
					temp.put("name", nameString);
					temp.put("percent", AmMobUtils.getRandom(2) + "%");
					nowmonthlist.add(temp);
				}
				loadPreMonthData(nowmonthlist);

				// 调用NC后台查询数据
				Map<String, Object> map = new HashMap<String, Object>();
				map.put(yearMonth, date.getText().toString());
				map.put("orgid", orgid);
				map.put("departid", departid);
				// 调用后台
				// call(map);

			} else {
				showInfo(EiurDetailActivity.this, changeDateDispaly(dateString, true));
				return;
			}

			// 点击右翻按钮
		} else if (v.getId() == R.id.rightbutton || v.getId() == R.id.rightcircle) {
			// 是否合理
			if (null == changeDateDispaly(dateString, false)) {
				// 右翻按钮
				// loadNextMonthData();

				Map<String, Object> map = new HashMap<String, Object>();
				map.put(yearMonth, date.getText().toString());
				map.put("orgid", orgid);
				map.put("departid", departid);
				// 调用后台
				// call(map);

				loadNextMonthData();

			} else {
				showInfo(EiurDetailActivity.this, changeDateDispaly(dateString, false));
				return;
			}
			// 返回按钮
		} else if (v.getId() == R.id.middlecircle) {
			Log.i(AmMobCommon.ammoblog, "你点击了中间的圆...");
		} else if (v.getId() == R.id.fanhuibutton) {
			finish();
		}

	}

	/**
	 * 
	 * @param countValues
	 *            总数组数据
	 * @param decimal
	 *            保留的小树位数
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
	 * 设置显示
	 * 
	 * @param textView
	 * @param displaycontont
	 */
	protected void setCircleDisplay(TextView textView, String displaycontont) {
		textView.setText(displaycontont);

	}

	@SuppressLint("InflateParams")
	private void initActionBar() {
		// 自定义标题栏
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayShowTitleEnabled(false);
		getActionBar().setDisplayShowCustomEnabled(true);
		LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View mTitleView = mInflater.inflate(R.layout.navigationbar, null);
		getActionBar().setCustomView(mTitleView,
				new ActionBar.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

	}

	/**
	 * 是否是初始化数据的过程中
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
	 * 发送消息更新界面数据
	 * 
	 * @param what
	 *            消息类型
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
	 * 调用NC服务取数据
	 * 
	 * @param methodName
	 *            -MAmethod
	 * @param paramJson
	 *            传参数json
	 * @param callBack
	 *            回调对象
	 * @author zhangg
	 */
	public void callAction(String methodName, JSONObject paramJson, AbstractServiceCallBack callBack) {
		ServiceUtils.callAction(this, AmMobCommon.NCAMEquipInUseRationController, methodName, paramJson, callBack);
	}

	/** 左翻按钮控件 */
	Button leftbutton = null;
	/** 右翻按钮 控件 */
	Button rightbutton = null;
	/** 返回按钮控件 */
	Button fanhuibutton = null;
	/** 左边圆控件 */
	CircleView leftcircle = null;
	/** 中间圆控件 */
	CircleView middlecircle = null;
	/** 右边圆控件 */
	CircleView rightcircle = null;
	/** 导航栏控件 */
	TextView biaotitextview = null;
	/** 导航栏显示 */
	String departNameDisplay = null;
	/** 左边圆显示 */
	String leftcircleDisplay = null;
	/** 中间圆显示 */
	String middlecircleDisplay = null;
	/** 右边圆显示 */
	String rightcircleDisplay = null;
	/** 当前显示日期 */
	// private static String yearmonth = "";
	/** WebView */
	WebView chartshow_wbusedepart = null;
	JSONArray jsonArray = null;
	/** 总组织数（先默认10个） */
	int totalOrgNum = 20;
	/** 当前显示的组织序号（先默认第二个） */
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
