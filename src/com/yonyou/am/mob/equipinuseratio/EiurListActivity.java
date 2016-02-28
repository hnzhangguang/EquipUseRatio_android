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
import android.app.Activity;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.yonyou.am.mob.commonactivity.AmBaseActivity;
import com.yonyou.am.mob.commonactivity.AmDatePickerDialog;
import com.yonyou.am.mob.login.AbstractServiceCallBack;
import com.yonyou.am.mob.login.AppContext;
import com.yonyou.am.mob.login.ServiceUtils;
import com.yonyou.am.mob.tool.AmMobCommon;
import com.yonyou.am.mob.tool.AmMobUtils;
import com.yonyou.am.mob.tool.CircleView;

/**
 * 本类说明：资产设备利用率 - 列表界面
 * 
 * @author zhangg 2015-11-4
 */
public class EiurListActivity extends AmBaseActivity implements OnLongClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eiur_list);
		// 初始化顶部的导航栏工作
		initActionbar();
		// 初始化控件工作
		init();

		Map<String, Object> map = new HashMap<String, Object>();
		// 调用后台
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
			paramJson.put("yearMonth", date.getText().toString());
			if (!map.isEmpty()) {
				for (String key : map.keySet()) {
					paramJson.put(key, map.get(key));
				}
			}
			// 调用后台
			callAction("queryOrgEquipRationByUser", paramJson, new EiurListCallBack(this));
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
	class EiurListCallBack extends AbstractServiceCallBack {

		public EiurListCallBack(AmBaseActivity activity) {
			super(activity);
		}

		@Override
		public void execute(JSONObject json) {
			showInfo(EiurListActivity.this, "NC datas...");
			try {
				Map<String, Object> map = AmMobUtils.jsonToMap(json);
				if (map.get("msg") != null) {
					showInfo(EiurListActivity.this, json.get("msg"));
					jsonArray = new JSONArray();
					// 发送消息
					sendEmptyMessage(0x5554);
					log(jsonArray);
					return;
				}
				// 从NC查询的数据
				if (map.get("info") != null) {
					jsonArray = new JSONArray(map.get("info").toString());
				} else {
					// 假设从后台查出的数据,最后会删除此的
					jsonArray = new JSONArray();
					for (int i = 0; i < 32; i++) {
						JSONObject jsonObject = new JSONObject();
						jsonObject.put("name", i + "号火箭");
						jsonObject.put("percent", AmMobUtils.getRandom(2));
						jsonObject.put("id", i + "");
						jsonArray.put(jsonObject);
					}
				}
				if (0 == jsonArray.length()) {
					showInfo(EiurListActivity.this, "后台数据为空!");
					log("后台数据为空!");
					return;
				}
				// 发送消息
				sendEmptyMessage(0x5554);
				log(jsonArray);

			} catch (JSONException e) {
				e.printStackTrace();
				log(e);
			}
		}

		@Override
		public void error(String s, String s1) {
			Log.e(AmMobCommon.ammoblog, s + "->" + s1);
			loadDataForJSONArray(new JSONArray(), true);
			refreshDataForActivity();
			Toast.makeText(EiurListActivity.this, s + "->" + s1 + "现在的数据是伪数据", Toast.LENGTH_LONG).show();
		}

	}

	/**
	 * 发送消息更新界面数据
	 * 
	 * @param what
	 *            消息类型
	 */
	private void sendEmptyMessage(final int what) {
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				handler.sendEmptyMessage(what);
			}
		}, 100);
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (null == orderButton.getTag()) {
			orderButton.setTag("desc");
			orderButton.setText("升序");
		}
		// 为了测试用,以后这行会注释掉
		// loadDataForJSONArray(new JSONArray(), true);
		// 刷新界面数据
		refreshDataForActivity();
	}

	@Override
	protected void onResume() {
		super.onResume();
		handler.sendEmptyMessage(0x123);
		// handler.sendEmptyMessage(0x5554);
	}

	/**
	 * 刷新界面数据
	 */
	public void refreshDataForActivity() {
		// 组装显示数据
		addGridLayoutDatas(totalLists, getSortType());
	}

	/**
	 * 控件初始化工作
	 */
	@SuppressLint("InflateParams")
	private void init() {

		// 正序颜色
		colors_asc = new int[] { R.color.redlight, R.color.redmid, R.color.redem, R.color.sealight, R.color.seamid,
				R.color.seaem, R.color.yellolight, R.color.yellomid, R.color.yelloem, R.color.lhlight, R.color.lhmid,
				R.color.lhem, R.color.touming2light, R.color.touming2mid, R.color.touming2em, R.color.touming1light,
				R.color.touming1mid, R.color.touming1em, R.color.crimson, R.color.gainsboro, R.color.plum,
				R.color.lightcyan, R.color.lightcoral, R.color.royalblue, R.color.aqua };
		// 倒序颜色
		colors_desc = new int[] { R.color.redem, R.color.redmid, R.color.redlight, R.color.seaem, R.color.seamid,
				R.color.sealight, R.color.yelloem, R.color.yellomid, R.color.yellolight, R.color.lhem, R.color.lhmid,
				R.color.lhlight, R.color.touming2em, R.color.touming2mid, R.color.touming2light, R.color.touming1em,
				R.color.touming1mid, R.color.touming1light, R.color.crimson, R.color.gainsboro, R.color.plum,
				R.color.lightcyan, R.color.lightcoral, R.color.royalblue, R.color.aqua };

		// 控件初始化
		tablelayout = (TableLayout) findViewById(R.id.tablelayout);

		orderimage = (ImageView) findViewById(R.id.orderimage);
		orderButton = (Button) findViewById(R.id.orderButton);
		orderButton.setOnClickListener(this);
		fanhuibutton = (Button) findViewById(R.id.fanhuibutton);
		fanhuibutton.setOnClickListener(this);

		// 日期-翻动栏layout
		linearlayoutdate = (LinearLayout) findViewById(R.id.linearlayoutdate);
		LayoutInflater inflater = LayoutInflater.from(this);
		// 动态加载布局
		LinearLayout datelayout = (LinearLayout) inflater.inflate(R.layout.dateselectview, null);
		linearlayoutdate.addView(datelayout);
		date = (TextView) findViewById(R.id.date);
		// date.setEnabled(false);
		date.setOnClickListener(this);
		// 刚进来的时候,设置date为当前年月
		Calendar calendar = Calendar.getInstance();
		int nowyear = calendar.get(Calendar.YEAR);
		int nowmonth = calendar.get(Calendar.MONTH);
		preference = this.getSharedPreferences("yonyou_am_eiur", MODE_APPEND);
		date.setText(preference.getString("eiur_date", nowyear + "-" + ((nowmonth == 0) ? "0" : "") + (nowmonth + 1)));

		leftbutton = (Button) findViewById(R.id.leftbutton);
		rightbutton = (Button) findViewById(R.id.rightbutton);
		leftbutton.setOnClickListener(this);
		rightbutton.setOnClickListener(this);
		// rightbutton.setEnabled(false);

		// 搜索框的父布局
		searchLinearlayout = (LinearLayout) findViewById(R.id.searchLinearlayout);
		LinearLayout searchviewbarLayout = (LinearLayout) inflater.inflate(R.layout.searcviewbar, null);
		searchLinearlayout.addView(searchviewbarLayout);

		main_search_bt = (ImageView) findViewById(R.id.main_search_bt);
		main_search_et = (EditText) findViewById(R.id.main_search_et);
		searchbuttonex = (Button) findViewById(R.id.searchbuttonex);
		searchbuttonex.setOnClickListener(this);
		// 初始化数据list
		totalLists = new ArrayList<Map<String, String>>();
		// 初始化handler
		initHandler();
	}

	/**
	 * 初始化handler
	 */
	private void initHandler() {
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if (msg.what == 0x123) {
					if (null == totalLists || totalLists.isEmpty()) {
						loadDatasAndRefreshView(null, true);
					} else {
						refreshDataForActivity();
					}
				} else if (msg.what == 0x5554) {
					loadDataForJSONArray(jsonArray, false);
					refreshDataForActivity();
				}
			}
		};
	}

	/**
	 * 生成item 并 填充到固定布局位置
	 * 
	 * @param datalist
	 *            要组装的数据
	 * @param isAsc
	 *            是否是升序排序
	 */
	private void addGridLayoutDatas(List<Map<String, String>> datalist, boolean isAsc) {

		// 做最小化最大化操作时候的处理
		clearGridLayoutDatas();
		tablelayout.setStretchAllColumns(true);

		int size = datalist.size();
		if (size <= 0) {
			return;
		}
		datalist = AmMobUtils.exeSort(datalist, isAsc ? "asc" : "desc", "percent", true);
		// 每行展示3列
		int display = 3;
		int total = size / display;
		int yushu = size % display;
		if (0 != yushu) {
			total = total + 1;
		}
		List<Map<String, String>> tmp = null;
		for (int i = 0; i < total; i++) {
			TableRow row = new TableRow(this);
			if (size <= display) {
				tmp = datalist;
				datalist = new ArrayList<Map<String, String>>();
				size = datalist.size();
			} else {
				if (datalist.size() < display) {
					tmp = datalist;
					datalist = new ArrayList<Map<String, String>>();
					size = datalist.size();
				} else {
					tmp = datalist.subList(0, display);
					datalist = datalist.subList(display, datalist.size());
					size = datalist.size();
				}
			}
			int j = 0;
			for (; j < tmp.size(); j++) {
				row.addView(generateLayout(tmp.get(j), i, j + 1));
			}
			if (j == 2) {
				row.addView(generateLayout(new HashMap<String, String>(), i, 3));
			}
			tablelayout.addView(row);
		}
	}

	/**
	 * 生成item工作
	 * 
	 * @param singleMap
	 *            根据singleMap数据生成item
	 * @return
	 */
	@SuppressLint("InflateParams")
	private View generateLayout(Map<String, String> singleMap, int rowseq, int colseq) {
		LayoutInflater inflater = getLayoutInflater();
		LinearLayout layout2 = (LinearLayout) inflater.inflate(R.layout.griditem, null);

		LinearLayout gridnerlinearlayout = (LinearLayout) layout2.findViewById(R.id.gridnerlinearlayout);
		gridnerlinearlayout.setOrientation(LinearLayout.HORIZONTAL);

		@SuppressWarnings("deprecation")
		int width = getWindowManager().getDefaultDisplay().getWidth();
		LayoutParams paramsForLayout = new LayoutParams(width / 3, width / 3);
		gridnerlinearlayout.setLayoutParams(paramsForLayout);
		// 圆球
		final CircleView circle = new CircleView(EiurListActivity.this);
		// 颜色渐变的时候用下面的
		int seq = rowseq * 3 + colseq - 1;
		if (singleMap.isEmpty()) {
			circle.setColor(getResources().getColor(R.color.white));
			circle.setText("");
		} else {
			// 超过20的用随机颜色
			if (seq > 20) {
				circle.setColor(getResources().getColor(colors_desc[(Math.abs(new Random().nextInt())) % 16]));
			} else {
				if (getSortType()) {
					circle.setColor(getResources().getColor(colors_asc[seq % 24]));
				} else {
					circle.setColor(getResources().getColor(colors_desc[seq % 24]));
				}
			}
			circle.setText(singleMap.get("percent").toString().trim().contains("%") ? singleMap.get("percent")
					: singleMap.get("percent") + "%");
		}

		circle.setTag(singleMap.get("id"));
		circle.setRadius(getWindowWidth() / 7 - 10);
		gridnerlinearlayout.addView(circle);
		// 为保存数据id用
		circle.setId(View.generateViewId());
		circle.setGravity(Gravity.CENTER);
		if (!singleMap.isEmpty()) {
			circle.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					circle.setColor(getResources().getColor(R.color.navajowhite1));
					circle.setBackgroundColor(getResources().getColor(R.color.touming));
					Intent intent = new Intent(EiurListActivity.this, EiurDepartActivity.class);
					String dateString = date.getText().toString();
					// 为当前选择的圆信息
					String textString = ((CircleView) v).getText().toString();
					String nameString = getDepartNameById(totalLists, ((CircleView) v).getTag().toString());
					String tagString = ((CircleView) v).getTag() == null ? "" : ((CircleView) v).getTag().toString();
					Bundle bundle = new Bundle();
					bundle.putString("date", dateString);
					// 存放主键用
					bundle.putString("tag", tagString);
					// 显示百分比
					bundle.putString("text", textString);
					bundle.putString("departname", nameString);
					intent.putExtras(bundle);
					startActivity(intent);
					// 设置切换动画，从右边进入，左边退出
					overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
					// finish();
					log("当前选择的item信息为date:" + dateString + "; tag:" + tagString + "; text:" + textString);
				}
			});
		}

		TextView textView = (TextView) layout2.findViewById(R.id.textviewitem);
		textView.setText((singleMap.get("name") == null || singleMap.get("name").equals("")) ? "" : singleMap
				.get("name"));
		// LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
		// LayoutParams.WRAP_CONTENT);
		LayoutParams params = new LayoutParams(width / 3, LayoutParams.WRAP_CONTENT);

		textView.setLayoutParams(params);
		textView.setGravity(Gravity.CENTER_HORIZONTAL);
		return layout2;
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
			commitDate();
		}

		return null;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View v) {
		// 取得当前界面显示的日期 eg "2015-10"
		String dateString = date.getText().toString();
		// 点击左翻按钮
		if (v.getId() == R.id.leftbutton) {
			if (null == changeDateDispaly(dateString, true)) {
				// null 为要展示的数据（从后台查询出来的）
				// loadDatasAndRefreshView(null, true);

				Map<String, Object> map = new HashMap<String, Object>();
				call(map);
			} else {
				showInfo(EiurListActivity.this, changeDateDispaly(dateString, true));
				return;
			}
			// 点击右翻按钮
		} else if (v.getId() == R.id.rightbutton) {
			// 是否合理
			if (null == changeDateDispaly(dateString, false)) {
				// null 为要展示的数据（从后台查询出来的）
				// loadDatasAndRefreshView(null, true);

				Map<String, Object> map = new HashMap<String, Object>();
				call(map);
			} else {
				showInfo(EiurListActivity.this, changeDateDispaly(dateString, false));
				return;
			}

			// 返回按钮
		} else if (v.getId() == R.id.fanhuibutton) {
			// Intent intent = new Intent(EiurListActivity.this,
			// AmMobLoginActivit.class);
			// startActivity(intent);
			finish();
			// 排序
		} else if (v.getId() == R.id.orderButton) {

			// 这个主要是为了测试连接ma端的实现
			JSONObject paramJson = new JSONObject();
			try {
				paramJson.put("transitype", "4B36-01");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			// ServiceUtils.callAction(this,
			// "nc.mob.ui.am.controller.NCAMWorkOrderController",
			// "queryRefFSymptomInfo", paramJson, new EiurListCallBack(this));

			if (null == orderButton.getTag()) {
				orderButton.setTag("asc");
			} else if (getSortType()) {
				orderButton.setTag("desc");
				orderButton.setText("升序");
				orderimage.setImageDrawable(getResources().getDrawable(R.drawable.up));
			} else {
				orderButton.setTag("asc");
				orderButton.setText("降序");
				orderimage.setImageDrawable(getResources().getDrawable(R.drawable.down2));
			}
			addGridLayoutDatas(totalLists, getSortType());

		} else if (v.getId() == R.id.searchbuttonex) {
			// 前一次点击了搜索并且是null时候
			if (isNullSearchPre) {
				if (null == main_search_et.getText() || main_search_et.getText().toString().equals("")) {
					showInfo(EiurListActivity.this, "关键字为空!");
					isNullSearchPre = true;
				} else {
					// 要搜索的关键字
					String keyword = main_search_et.getText().toString();
					// 根据关键字进行过滤
					loadDataForJSONArray(totalLists, keyword);
					isNullSearchPre = false;
				}
			} else {
				if (null == main_search_et.getText() || main_search_et.getText().toString().equals("")) {
					showInfo(EiurListActivity.this, "关键字为空,进行还原操作!");
					// 利用原有数据还原操作排序处理
					refreshDataForActivity();
					isNullSearchPre = true;
				} else {
					// 要搜索的关键字
					String keyword = main_search_et.getText().toString();
					// 根据关键字进行过滤
					loadDataForJSONArray(totalLists, keyword);
					isNullSearchPre = false;
				}
			}

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
				if (trueMonth >= 10) {
					date.setText(year + "-" + trueMonth);
				} else {
					date.setText(year + "-0" + trueMonth);
				}
				commitDate();
				// 判断当前网络是否可用
				if (!isNetworkAvailable(EiurListActivity.this)) {
					showInfo(EiurListActivity.this, "当前网络不可用,请检查网络!");
					return;
				}
				// 重新查询数据
				loadDatasAndRefreshView(null, true);
			} else {
				showInfo(EiurListActivity.this, "不允许超过当前日期的查询!");
			}
		}
	};

	/**
	 * 一般应该是 从后台查询出来新数据后唤醒此方法的执行
	 * 
	 * @param nowmonthlist
	 *            界面要显示的数据
	 * @param isFasle
	 *            是否是假数据
	 */
	private void loadDatasAndRefreshView(JSONArray jsonArray, boolean isFalseData) {
		// 新的数据
		// List<Map<String, String>> nowmonthlist = new ArrayList<Map<String,
		// String>>();
		// 组装数据：第二个参数为cest参数,true为测试用
		loadDataForJSONArray(jsonArray, isFalseData);
		// 显示
		refreshDataForActivity();
	}

	/**
	 * 把后台查出的数据组装成界面显示的数据
	 * 
	 * @param jsonArray
	 * @param isFalse
	 *            是否是假数据
	 */
	private void loadDataForJSONArray(JSONArray jsonArray, boolean isFalseData) {
		totalLists = new ArrayList<Map<String, String>>();
		int count = 0;
		// 目前只是为了测试用
		if (isFalseData) {
			count = 28;
			for (int i = 0; i < count; i++) {
				Map<String, String> tempMap = new HashMap<String, String>();
				tempMap.put("name", ((i % 2 == 0) ? "中华人民共和国河南周口市" : "三里屯") + i + "号火箭");
				tempMap.put("percent", AmMobUtils.getRandom(2));
				tempMap.put("id", i + "");
				totalLists.add(tempMap);
			}
		} else if (null == jsonArray || jsonArray.length() == 0) {
			// showInfo(EiurListActivity.this, "无数据要显示");
			return;
		} else {
			try {// 真实数据
				JSONObject jsonObject = null;
				for (int i = 0; i < jsonArray.length(); i++) {
					jsonObject = jsonArray.getJSONObject(i);
					Map<String, String> tempMap = new HashMap<String, String>();
					tempMap.put("name", jsonObject.getString("name"));
					tempMap.put("percent", jsonObject.getString("percent"));
					tempMap.put("id", jsonObject.getString("id"));
					totalLists.add(tempMap);
				}
			} catch (JSONException e) {
				e.printStackTrace();
				log(e.getMessage() + "\n日志详情:" + e);
			}
		}

	}

	/**
	 * 把后台查出的数据组装成界面显示的数据(关键字过滤，带自动刷新界面功能)
	 * 
	 * @param jsonArray
	 *            后台数据
	 */
	private void loadDataForJSONArray(List<Map<String, String>> list, String keyword) {
		if (null == list || list.size() == 0) {
			return;
		}
		try {
			Map<String, String> tempmap = null;
			List<Map<String, String>> searchlist = new ArrayList<Map<String, String>>();
			for (int i = 0; i < list.size(); i++) {
				if (null == list.get(i)) {
					continue;
				}
				tempmap = list.get(i);
				if (tempmap.get("name").contains(keyword)) {
					searchlist.add(tempmap);
				}
			}
			// 排序处理
			addGridLayoutDatas(searchlist, getSortType());
		} catch (Exception e) {
			e.printStackTrace();
			log(e.getMessage() + "\n日志详情:" + e);
		}

	}

	/**
	 * 
	 * @param totalList
	 *            总数据
	 * @param id
	 *            要查询的数据的id
	 * @return 名称
	 */
	public String getDepartNameById(List<Map<String, String>> totalList, String id) {

		if (null == totalList) {
			return "";
		}
		Map<String, String> tempMap = null;
		for (int i = 0; i < totalList.size(); i++) {
			tempMap = totalList.get(i);
			if (tempMap.get("id").equals(id)) {
				return tempMap.get("name");
			}
		}
		return "";
	}

	/**
	 * 清除当前tablelayout数据工作
	 */
	private void clearGridLayoutDatas() {
		// 做最小化最大化操作时候的处理
		if (null == tablelayout) {
			return;
		}
		int countChild = tablelayout.getChildCount();
		if (countChild >= 1) {
			// 必须从后面减去子元素
			tablelayout.removeAllViews();
		}

	}

	/**
	 * 导航栏初始化工作
	 */
	private void initActionbar() {
		// 自定义标题栏
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayShowTitleEnabled(false);
		getActionBar().setDisplayShowCustomEnabled(true);
		LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View mTitleView = mInflater.inflate(R.layout.navigationbar_right, null);
		getActionBar().setCustomView(
				mTitleView,
				new ActionBar.LayoutParams(android.app.ActionBar.LayoutParams.MATCH_PARENT,
						android.app.ActionBar.LayoutParams.MATCH_PARENT));

	}

	@Override
	public boolean onLongClick(View v) {
		return false;
	}

	/**
	 * 提交需要保存的数据
	 */
	private void commitDate() {
		Editor editor = preference.edit();
		editor.putString("eiur_date", date.getText().toString());
		editor.commit();
	}

	/**
	 * 手机键盘确定按钮的监听事件
	 */
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
			/* 隐藏软键盘 */
			InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			if (inputMethodManager.isActive()) {
				inputMethodManager.hideSoftInputFromWindow(EiurListActivity.this.getCurrentFocus().getWindowToken(), 0);
			}
			// 要搜索的关键字
			String keyword = main_search_et.getText().toString();
			// 根据关键字进行过滤
			loadDataForJSONArray(totalLists, keyword);
			return true;
		}
		return super.dispatchKeyEvent(event);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * registerForContextMenu(getListView()); 生成上下文菜单 需要oncreate注册
	 * 而onCreateContextMenu会在用户每一次长按View时被调用
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {

		super.onCreateContextMenu(menu, v, menuInfo);

	}

	/**
	 * 相应上下文菜单
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		return super.onContextItemSelected(item);
	}

	/**
	 * 得到当前排序类型【 asc、desc】
	 * 
	 * @return true = asc ; false = desc
	 */
	private boolean getSortType() {
		return orderButton.getTag().toString().trim().equals("asc") ? true : false;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {// 返回键
			onClick(fanhuibutton);
			// 相关响应代码
			return true;
		}

		return super.onKeyDown(keyCode, event);
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

	/**
	 * 记录日志
	 * 
	 * @param msg
	 */
	public void log(Object msg) {
		Log.i(AmMobCommon.ammoblog, msg + "");
	}

	/**
	 * 测试用
	 * 
	 * @param activity
	 * @param msg
	 */
	public void showInfo(Activity activity, Object msg) {
		Toast.makeText(activity, msg + "", Toast.LENGTH_LONG).show();
	}

	/** 左翻按钮 */
	Button leftbutton = null;
	/** 右翻按钮 */
	Button rightbutton = null;
	/** 返回按钮 */
	Button fanhuibutton = null;
	/** 排序按钮 */
	Button orderButton = null;
	/** 显示日期 */
	TextView date = null;
	/** GridLayout的adapter */
	ArrayAdapter<String> adapter;
	LinearLayout searchLinearlayout = null;
	/** 表格布局控件 */
	TableLayout tablelayout = null;
	LinearLayout linearlayoutdate = null;
	/** 当前显示日期 */
	// private static String yearmonth = "";
	/** 生成id的初始化值 */
	int id = -1;
	/** 总数据容器 */
	ArrayList<String> mAllList = new ArrayList<String>();
	/** 搜索控件 */
	// SearchView searchview = null;
	ImageView main_search_bt = null;
	EditText main_search_et = null;
	Button searchbuttonex = null;
	ImageView orderimage = null;
	/*** 要展示的数据 */
	List<Map<String, String>> totalLists = null;
	AppContext appcontext = null;
	/** 搜索关键字 */
	// String key = null;
	// static int nownumber = 1;
	static int tempid = 1;
	int[] colors_asc = null;
	int[] colors_desc = null;
	static boolean isNullSearchPre = true;
	SharedPreferences preference = null;
	Handler handler = null;
	JSONArray jsonArray = null;
	// 利用的是这种形式 eg: \n

}
