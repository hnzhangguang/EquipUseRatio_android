package com.yonyou.am.mob.stoppagerate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.DatePicker;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.yonyou.am.mob.commonactivity.AmBaseActivity;
import com.yonyou.am.mob.commonactivity.AmDatePickerDialog;
import com.yonyou.am.mob.equipinuseratio.R;
import com.yonyou.am.mob.login.AbstractServiceCallBack;
import com.yonyou.am.mob.login.AppContext;
import com.yonyou.am.mob.login.ServiceUtils;
import com.yonyou.am.mob.stoppagerate.vo.StoppagerateBaseAggVO;
import com.yonyou.am.mob.stoppagerate.vo.StoppagerateBaseVO;
import com.yonyou.am.mob.tool.AmMobCommon;
import com.yonyou.am.mob.tool.AmMobUtils;
import com.yonyou.am.mob.tool.CircleView;
import com.yonyou.am.mob.tool.Logger;

/**
 * 设备故障停机率-部门界面
 * 
 * @author hujieh
 * 
 */
@SuppressLint("ClickableViewAccessibility")
public class StoppageRateBaseSubActivity extends AmBaseActivity {

	/** 需要加载上一期间的比例 */
	private static final int NEED_LAST_PERCENT = 0x1;
	/** 需要加载下一期间的比例 */
	private static final int NEED_NEXT_PERCENT = 0x2;
	/** 需要加载本期间的数据 */
	private static final int NEED_CURR_DATA = 0x4;
	/** 左边圆控件 */
	private CircleView leftcircle = null;
	/** 中间圆控件 */
	private CircleView middlecircle = null;
	/** 右边圆控件 */
	private CircleView rightcircle = null;
	/** 导航栏控件 */
	private TextView biaotitextview = null;
	/** WebView */
	private WebView chartshow_wbusedepart = null;
	/** 显示日期 */
	private TextView date = null;
	private int[] colors = null;
	/** 蒙版 */
	private View coverView;

	private String mUsedunitPK;
	private String mUsedeptPK;

	/** 所有数据，key为date，value为对应的数据类 */
	private Map<String, StoppagerateBaseAggVO> totalDatas = new HashMap<String, StoppagerateBaseAggVO>();

	private MyReceiver mReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.stoppagerate_base_sub);
		// 初始化顶部的导航栏工作
		initActionbar();
		// 初始化控件工作
		init();
		// 记录登录信息
		recordLoginLog(AppContext.getCurrent(this));

	}

	/**
	 * 主要的工作是：界面显示、查询数据等。
	 */
	@Override
	protected void onStart() {
		super.onStart();
		log("registerReceiver");
		registerReceiver(mReceiver, new IntentFilter(AmMobCommon.STOPPAGE_HTML_LOADDED_ACTION));
		// loadDatasAndSendMsg("");
		clearCharts();
		initWebView(chartshow_wbusedepart);
		loadDatasAndSendMsg("");

	}

	@Override
	protected void onStop() {
		log("unregisterReceiver");
		unregisterReceiver(mReceiver);
		super.onStop();
	}

	/**
	 * 异步加载数据，去的数据后发送message
	 * 
	 * @param condition
	 *            加载数据的条件
	 */
	private void loadDatasAndSendMsg(String condition) {
		startLoading();
		final String dateString = getCurrMonthString();
		int type1 = 0x0;
		int type2 = 0x0;
		int type3 = 0x0;
		// 上一周期合法，并且上一周期的数据没有加载
		if (getLastMonthString() != null && !totalDatas.containsKey(getLastMonthString())) {
			type1 = NEED_LAST_PERCENT;
		}
		// 下一周期合法并且下一周期的数据没有加载
		if (getNextMonthString() != null && !totalDatas.containsKey(getNextMonthString())) {
			type2 = NEED_NEXT_PERCENT;
		}
		// 本周期数据没有加载，或者子表数据没有加载
		if (!totalDatas.containsKey(dateString) || totalDatas.get(dateString).getChilds() == null) {
			type3 = NEED_CURR_DATA;
		}
		// 后台可根据此type通过位运算得到需要加载的类型
		final int type = type1 | type2 | type3;
		// 如果数据都存在，不需要重新查询数据，此时直接发送消息加载界面，不经过后台
		if (type == 0x0) {
			setupView();
		} else {
			ServiceUtils.callAction(StoppageRateBaseSubActivity.this, AmMobCommon.NCAMStoppagerateController,
					getQueryMethodName(), getParamJson(type, dateString), new StoppagerateSubCallback(
							StoppageRateBaseSubActivity.this));
		}
	}

	/**
	 * 获取查询数据的方法名，子类实现
	 * 
	 * @return
	 */
	protected String getQueryMethodName() {
		return "queryDepartDatas";
	}

	/**
	 * 取得查询数据的参数，以JSON返回
	 * 
	 * @return
	 */
	protected JSONObject getParamJson(int type, String dateString) {
		JSONObject json = new JSONObject();
		try {
			json.put(AmMobCommon.TYPE_FIELD, type);
			json.put(AmMobCommon.DATE_FIELD, dateString);
			json.put(AmMobCommon.PK_USEDUNIT_FIELD, mUsedunitPK);
			// 部门界面加载数据时不需要传递部门主键，此时mUsedeptPK为空
			if (!TextUtils.isEmpty(mUsedeptPK)) {
				json.put(AmMobCommon.PK_USEDEPT_FIELD, mUsedeptPK);
			}
		} catch (JSONException e) {
			log(e.getMessage());
		}
		return json;
	}

	/**
	 * ma回调类
	 * 
	 * @author hujieh
	 * 
	 */
	class StoppagerateSubCallback extends AbstractServiceCallBack {

		public StoppagerateSubCallback(StoppageRateBaseSubActivity activity) {
			super(activity);
		}

		public void execute(JSONObject arg0) {
			if (arg0 != null) {
				String dateString = arg0.optString(AmMobCommon.DATE_FIELD);
				String json = arg0.optString(AmMobCommon.DATA_FIELD);
				try {
					parse(dateString, json);
					((StoppageRateBaseSubActivity) getmActivity()).setupView();
				} catch (JSONException e) {
					log(e);
				}

			}
		}

		public void error(String s, String s1) {
			super.error(s, s1);
			JSONObject arg0 = createRandomData();
			if (arg0 != null) {
				String dateString = arg0.optString(AmMobCommon.DATE_FIELD);
				String json = arg0.optString(AmMobCommon.DATA_FIELD);
				try {
					parse(dateString, json);
					((StoppageRateBaseSubActivity) getmActivity()).setupView();
				} catch (JSONException e) {
					log(e);
				}

			}
		}
	}

	private JSONObject createRandomData() {
		JSONObject json = new JSONObject();
		try {
			JSONObject data = new JSONObject();
			JSONObject currData = new JSONObject();
			JSONArray array = new JSONArray();
			array.put(createSingleData(0));
			currData.put("data", array);
			array = new JSONArray();
			int count = new Random().nextInt(30);
			if (count == 0) {
				count = 5;
			}
			for (int i = 0; i < count; i++) {
				array.put(createSingleData(i));
			}
			currData.put("child", array);

			JSONObject preData = new JSONObject();
			array = new JSONArray();
			array.put(createSingleData(0));
			preData.put("data", array);

			JSONObject nextData = new JSONObject();
			array = new JSONArray();
			array.put(createSingleData(0));
			nextData.put("data", array);

			if (getCurrMonthString() != null) {
				data.put(getCurrMonthString(), currData);
			}
			if (getLastMonthString() != null) {
				data.put(getLastMonthString(), currData);
			}
			if (getNextMonthString() != null) {
				data.put(getNextMonthString(), currData);
			}
			json.put("data", data);
			json.put("date", getCurrMonthString());
		} catch (JSONException e) {
			log(e);
		}

		return json;
	}

	private JSONObject createSingleData(int i) {
		JSONObject json = new JSONObject();
		try {
			json.put("pk", "basesub" + i);
			json.put("name", i % 8 == 1 ? "使用配置文件将格式使用配置文件将格式" + i : "配置文件" + i);
			json.put("percent", AmMobUtils.getRandom());
		} catch (JSONException e) {
			log(e);
		}
		return json;
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void init() {
		biaotitextview = (TextView) this.findViewById(R.id.biaotitextview);
		chartshow_wbusedepart = (WebView) this.findViewById(R.id.chartshow_wbusedepart);
		// 加载webview
		chartshow_wbusedepart.getSettings().setAllowFileAccess(true);
		chartshow_wbusedepart.getSettings().setJavaScriptEnabled(true);
		// chartshow_wbusedepart.setWebChromeClient(new
		// WebChromeClientSelf(this));
		chartshow_wbusedepart.setWebViewClient(new WebViewClient() {

			// HTML页面加载完以后发送广播，通知界面刷新
			public void onPageFinished(WebView view, String url) {
				log("webview load finished");
				Intent i = new Intent(AmMobCommon.STOPPAGE_HTML_LOADDED_ACTION);
				sendBroadcast(i);
				super.onPageFinished(view, url);
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				log("webview load start");
				super.onPageStarted(view, url, favicon);
			}

		});

		// 返回
		findViewById(R.id.fanhuibutton).setOnClickListener(this);

		// 日期
		date = (TextView) findViewById(R.id.date);
		date.setOnClickListener(this);
		Intent intent = getIntent();
		if (intent != null) {
			Bundle bundle = intent.getExtras();
			if (null != bundle) {
				String dateString = bundle.getString(AmMobCommon.DATE_FIELD);
				date.setText(dateString);
				mUsedunitPK = bundle.getString(AmMobCommon.PK_USEDUNIT_FIELD);
				mUsedeptPK = bundle.getString(AmMobCommon.PK_USEDEPT_FIELD);
				biaotitextview.setText(bundle.getString(AmMobCommon.NAME_FIELD));
			}
		}
		// 日期的上一个周期
		findViewById(R.id.leftbutton).setOnClickListener(this);
		// 日期的下一个周期
		findViewById(R.id.rightbutton).setOnClickListener(this);

		colors = new int[] { R.color.olive, R.color.red, R.color.deeppink, R.color.tomato, R.color.crimson,
				R.color.gainsboro, R.color.plum, R.color.lightcyan, R.color.lightcoral, R.color.royalblue,
				R.color.aqua, R.color.lightskyblue, R.color.olive, R.color.saddlebrown, R.color.palegreen,
				R.color.mediumblue, R.color.gold, R.color.lemonchiffon, R.color.lavenderblush };

		int radius = getWindowWidth() / 10;
		leftcircle = (CircleView) findViewById(R.id.leftcircle);
		leftcircle.setRadius(radius);
		leftcircle.setColor(getResources().getColor(colors[5]));
		leftcircle.setGravity(Gravity.CENTER);
		leftcircle.setOnClickListener(this);

		rightcircle = (CircleView) findViewById(R.id.rightcircle);
		rightcircle.setRadius(radius);
		rightcircle.setColor(getResources().getColor(colors[5]));
		rightcircle.setGravity(Gravity.CENTER);
		rightcircle.setOnClickListener(this);

		middlecircle = (CircleView) findViewById(R.id.middlecircle);
		middlecircle.setRadius(radius * 2);
		middlecircle.setColor(getResources().getColor(colors[2]));
		middlecircle.setGravity(Gravity.CENTER);

		// 显示蒙版时，阻止一切触屏事件
		coverView = findViewById(R.id.cover_view);
		coverView.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});

		mReceiver = new MyReceiver();

		// initWebView(chartshow_wbusedepart);
		// loadDatasAndSendMsg("");
	}

	/**
	 * 初始化webview组件，子类实现加载不同的HTML页面
	 * 
	 * @param webview
	 *            需要设置的webview组件
	 */
	protected void initWebView(WebView webview) {
		webview.loadUrl("file:///android_asset/echart/stoppageratedepart.html");
	}

	public void onClick(View v) {
		if (v.getId() == R.id.leftbutton || v.getId() == R.id.leftcircle) {
			String message = changeDateDispaly(true);
			if (null == message) {
				loadDatasAndSendMsg("");
			} else {
				showInfo(StoppageRateBaseSubActivity.this, message);
				return;
			}
		} else if (v.getId() == R.id.rightbutton || v.getId() == R.id.rightcircle) {
			String message = changeDateDispaly(false);
			if (null == message) {
				loadDatasAndSendMsg("");
			} else {
				showInfo(StoppageRateBaseSubActivity.this, message);
				return;
			}
		} else if (v.getId() == R.id.fanhuibutton) {
			// setResultForDate();
			finish();
		} else if (v.getId() == R.id.date) {
			String currDate = date.getText().toString();
			int year = 2015;
			int month = 11;
			if (currDate.contains("-")) {
				String[] dateArr = currDate.split("-");
				year = Integer.valueOf(dateArr[0]);
				month = Integer.valueOf(dateArr[1]) - 1;
			} else {
				Calendar calendar = Calendar.getInstance();
				year = calendar.get(Calendar.YEAR);
				month = calendar.get(Calendar.MONTH);
			}
			AmDatePickerDialog dialog = new AmDatePickerDialog(this, dateSetListener, year, month, 1);
			dialog.setTitle("请选择日期");
			dialog.show();
		}

	}

	OnDateSetListener dateSetListener = new OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			Calendar calendar = Calendar.getInstance();
			int currYear = calendar.get(Calendar.YEAR);
			int currMonth = calendar.get(Calendar.MONTH);
			if (currYear > year || (currYear == year && currMonth >= monthOfYear)) {
				int trueMonth = monthOfYear + 1;
				setDateString(year, trueMonth);
				loadDatasAndSendMsg("");
			} else {
				showInfo(StoppageRateBaseSubActivity.this, "不允许超过当前日期的查询!");
			}
		}
	};

	private void setDateString(int year, int month) {
		if (month > 9) {
			date.setText(year + "-" + month);
		} else {
			date.setText(year + "-0" + month);
		}
	}

	@Override
	public void onBackPressed() {
		// setResultForDate();
		super.onBackPressed();
	}

	// /**
	// * 关闭activity时设置结果，子类实现
	// */
	// protected void setResultForDate() {
	//
	// }

	/**
	 * 更改日期显示
	 * 
	 * @param dateString
	 *            当前日期
	 * @param isPreDate
	 *            是否是到前一个月
	 * @return 为null是说明正常，否则为错误的提示信息
	 */
	private String changeDateDispaly(boolean isLastDate) {
		String dateString = date.getText().toString();
		String[] dateArr = dateString.split("-");
		int year = Integer.valueOf(dateArr[0]);
		int month = Integer.valueOf(dateArr[1]);
		if (isLastDate) {
			if (month > 1) {
				month--;
			} else {
				year--;
				month = 12;
			}
		} else {
			Calendar calendar = Calendar.getInstance();
			int currYear = calendar.get(Calendar.YEAR);
			int currMonth = calendar.get(Calendar.MONTH);
			if (currYear > year || (currYear == year && currMonth + 1 > month)) {
				if (month == 12) {
					month = 1;
					year++;
				} else {
					month++;
				}
			} else {
				return "不允许超过当前日期的查询!";
			}
		}
		setDateString(year, month);
		return null;
	}

	@SuppressLint("InflateParams")
	private void initActionbar() {
		// 自定义标题栏
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayShowTitleEnabled(false);
		getActionBar().setDisplayShowCustomEnabled(true);
		LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View mTitleView = mInflater.inflate(R.layout.actionbar, null);
		getActionBar().setCustomView(mTitleView,
				new ActionBar.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		mTitleView.findViewById(R.id.sort).setVisibility(View.GONE);

	}

	public void showInfo(Activity content, Object msg) {
		Toast.makeText(content, "" + msg, Toast.LENGTH_SHORT).show();
	}

	public void log(Object msg) {
		Logger.info(StoppageRateBaseSubActivity.this, AmMobCommon.ammoblog, msg + "---" + this.getClass().getName());
		Log.i(AmMobCommon.ammoblog, msg + "---" + this.getClass().getName());
	}

	private void setupView() {
		if (getLastMonthString() != null && totalDatas.get(getLastMonthString()) != null) {
			leftcircle.setText(totalDatas.get(getLastMonthString()).getParent().getPercent() + "%");
		} else {
			leftcircle.setText("");
		}
		if (totalDatas.get(getCurrMonthString()) != null) {
			middlecircle.setText(totalDatas.get(getCurrMonthString()).getParent().getPercent() + "%");
		} else {
			middlecircle.setText("");
		}
		if (getNextMonthString() != null && totalDatas.get(getNextMonthString()) != null) {
			rightcircle.setText(totalDatas.get(getNextMonthString()).getParent().getPercent() + "%");
		} else {
			rightcircle.setText("");
		}
		if (totalDatas.get(getCurrMonthString()) != null) {
			List<StoppagerateBaseVO> childs = totalDatas.get(getCurrMonthString()).getChilds();
			if (childs != null) {
				String[] names = new String[childs.size()];
				Double[] percents = new Double[childs.size()];
				for (int i = 0; i < childs.size(); i++) {
					names[i] = childs.get(i).getName();
					percents[i] = Double.valueOf(childs.get(i).getPercent());
				}
				log(AmMobUtils.buildLoadJsString("bar", names, percents));
				chartshow_wbusedepart.loadUrl(AmMobUtils.buildLoadJsString("bar", names, percents));
			}
		} else {
			clearCharts();
		}
		endLoading();
	}

	private void clearCharts() {
		chartshow_wbusedepart.loadUrl(AmMobUtils.buildLoadJsString("bar", new String[] {}, new Double[] {}));
	}

	/**
	 * 解析json数据
	 * 
	 * @param json
	 *            json格式的string
	 * @throws JSONException
	 */
	private void parse(String date, String json) throws JSONException {
		if (!TextUtils.isEmpty(json)) {
			JSONObject obj = new JSONObject(json);
			String lastDate = getLastMonthString(date);
			if (obj.has(lastDate)) {
				cacheData(lastDate, obj.getJSONObject(lastDate));
			}
			String nextDate = getNextMonthString(date);
			if (obj.has(nextDate)) {
				cacheData(nextDate, obj.getJSONObject(nextDate));
			}
			if (obj.has(date)) {
				cacheData(date, obj.getJSONObject(date));
			}
		}
	}

	private void cacheData(String date, JSONObject obj) throws JSONException {
		if (obj != null && obj.length() > 0) {
			StoppagerateBaseAggVO baseAggVO = null;
			if (!totalDatas.containsKey(date)) {
				baseAggVO = new StoppagerateBaseAggVO();
				totalDatas.put(date, baseAggVO);
			} else {
				baseAggVO = totalDatas.get(date);
			}
			if (obj.has("data")) {
				JSONArray array = obj.getJSONArray("data");
				if (array.length() == 1) {
					baseAggVO.setParent(convertJSON2BaseVO(array.getJSONObject(0)));
				}
			}
			if (obj.has("child")) {
				JSONArray childArray = obj.getJSONArray("child");
				if (childArray.length() > 0) {
					List<StoppagerateBaseVO> childs = new ArrayList<StoppagerateBaseVO>(childArray.length());
					for (int i = 0; i < childArray.length(); i++) {
						childs.add(convertJSON2BaseVO(childArray.getJSONObject(i)));
					}
					baseAggVO.setChilds(childs);
				}
			}
		}
	}

	private StoppagerateBaseVO convertJSON2BaseVO(JSONObject obj) {
		StoppagerateBaseVO baseVO = new StoppagerateBaseVO();
		baseVO.setName(obj.optString(AmMobCommon.NAME_FIELD));
		baseVO.setPk(obj.optString("pk"));
		baseVO.setPercent(obj.optString("percent"));
		return baseVO;
	}

	public void setCurrDateString(String dateString) {
		date.setText(dateString);
	}

	/**
	 * 获取当前数据
	 */
	public StoppagerateBaseAggVO getCurrData() {
		return totalDatas.get(getCurrMonthString());
	}

	/**
	 * 取得当前日期的String
	 * 
	 * @return
	 */
	public String getCurrMonthString() {
		return date.getText().toString();
	}

	/**
	 * 取得上一日期的String
	 * 
	 * @return
	 */
	private String getLastMonthString() {
		String dateString = date.getText().toString();
		return getLastMonthString(dateString);
	}

	/**
	 * 取得指定日期的上一月
	 * 
	 * @param dateString
	 * @return
	 */
	private String getLastMonthString(String dateString) {
		String[] dateArr = dateString.split("-");
		int year = Integer.valueOf(dateArr[0]);
		int month = Integer.valueOf(dateArr[1]);
		if (month > 1) {
			month--;
		} else {
			year--;
			month = 12;
		}
		return getDateString(year, month);
	}

	/**
	 * 取得下一日期的String
	 * 
	 * @return 如果下一周期不合法，则返回空
	 */
	private String getNextMonthString() {
		String dateString = date.getText().toString();
		return getNextMonthString(dateString);
	}

	/**
	 * 取得指定日期的下一月
	 * 
	 * @param dateString
	 * @return 如果下一周期不合法，则返回空
	 */
	private String getNextMonthString(String dateString) {
		String[] dateArr = dateString.split("-");
		int year = Integer.valueOf(dateArr[0]);
		int month = Integer.valueOf(dateArr[1]);
		Calendar calendar = Calendar.getInstance();
		int currYear = calendar.get(Calendar.YEAR);
		int currMonth = calendar.get(Calendar.MONTH);
		if (currYear > year || (currYear == year && currMonth + 1 > month)) {
			if (month == 12) {
				month = 1;
				year++;
			} else {
				month++;
			}
			return getDateString(year, month);
		} else {
			return null;
		}
	}

	/**
	 * 根据传入的年月取得日期的string格式
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	private String getDateString(int year, int month) {
		if (month > 9) {
			return year + "-" + month;
		} else {
			return year + "-0" + month;
		}
	}

	/**
	 * 开始加载数据时调用
	 */
	private void startLoading() {
		// 显示蒙版
		coverView.setVisibility(View.VISIBLE);
		// 上一月和下一月按钮不可用
		// lastMonthBtn.setEnabled(false);
		// nextMonthBtn.setEnabled(false);
	}

	/**
	 * 加载数据以后，界面重绘以后调用
	 */
	private void endLoading() {
		// 隐藏蒙版
		coverView.setVisibility(View.GONE);
		// 上一月和下一月按钮可用
		// lastMonthBtn.setEnabled(true);
		// nextMonthBtn.setEnabled(true);
	}

	class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			log("onReceive");
			setupView();
			// 收到广播后解除本接收器的注册，保证只接收一次
			// unregisterReceiver(this);
		}

	}

}
