package com.yonyou.am.mob.stoppagerate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
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
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.yonyou.am.mob.commonactivity.AmBaseActivity;
import com.yonyou.am.mob.commonactivity.AmDatePickerDialog;
import com.yonyou.am.mob.equipinuseratio.R;
import com.yonyou.am.mob.login.AbstractServiceCallBack;
import com.yonyou.am.mob.login.AppContext;
import com.yonyou.am.mob.login.ServiceUtils;
import com.yonyou.am.mob.stoppagerate.vo.StoppagerateBaseVO;
import com.yonyou.am.mob.tool.AmMobCommon;
import com.yonyou.am.mob.tool.AmMobUtils;
import com.yonyou.am.mob.tool.CircleView;
import com.yonyou.am.mob.tool.Logger;
import com.yonyou.am.mob.view.SearchViewPlus;
import com.yonyou.am.mob.view.SearchViewPlus.SearchViewPlusListener;
import com.yonyou.am.mob.view.TextViewWithDrawable;

/**
 * 设备故障停机率-列表界面
 * 
 * @author hujieh
 * 
 */

@SuppressLint({ "ClickableViewAccessibility", "InflateParams" })
public class StoppageRateListActivity extends AmBaseActivity {

	private View coverView;
	/** 列数 */
	private static final int GRID_COLUMN_NUM = 3;

	/** 排序按钮 */
	private TextViewWithDrawable sort = null;
	/** 显示日期 */
	private TextView date = null;
	/** 表格布局控件 */
	private TableLayout tablelayout = null;
	/** 搜索控件 */
	private SearchViewPlus searchview;
	/** 所有数据，key为日期，value为该期间的数据集合 */
	private Map<String, List<StoppagerateBaseVO>> totalDatas = new HashMap<String, List<StoppagerateBaseVO>>();
	/*** 展示的数据 */
	private List<StoppagerateBaseVO> currDatas = null;

	private LayoutParams params = new LayoutParams();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stoppagerate_list);

		// 初始化顶部的导航栏工作
		initActionbar();
		// 初始化控件工作
		init();
		// 记录登录日志
		recordLoginLog(AppContext.getCurrent(this));

	}

	/**
	 * 异步加载数据，去的数据后发送message
	 * 
	 */
	private void loadDatasAndSendMsg() {
		startLoading();
		final String dateString = date.getText().toString();
		// 如果对应日期的数据已经加载，则直接发送消息，不再查询数据
		if (totalDatas.containsKey(dateString)) {
			setupView(totalDatas.get(dateString));
		} else {
			JSONObject paramJson = new JSONObject();
			try {
				paramJson.put("date", dateString);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			ServiceUtils.callAction(StoppageRateListActivity.this, AmMobCommon.NCAMStoppagerateController,
					"queryOrgDatas", paramJson, new StoppagerateListCallback(StoppageRateListActivity.this));
		}
	}

	/**
	 * ma回调类
	 * 
	 * @author hujieh
	 * 
	 */
	class StoppagerateListCallback extends AbstractServiceCallBack {

		public StoppagerateListCallback(StoppageRateListActivity activity) {
			super(activity);
		}

		public void execute(JSONObject arg0) {
			if (arg0 != null) {
				String json = arg0.optString(AmMobCommon.DATA_FIELD);
				String dateString = date.getText().toString();
				if (dateString.equals(arg0.optString(AmMobCommon.DATE_FIELD))) {
					List<StoppagerateBaseVO> vos = parse(json);
					// 即使此处没有值，也缓存下来。表示已经查询过
					totalDatas.put(dateString, vos);
					setupView(vos);
				}
			}
		}

		@Override
		public void error(String s, String s1) {
			// super.error(s, s1);
			JSONObject arg0 = createRandomJSON();
			if (arg0 != null) {
				String json = arg0.optString(AmMobCommon.DATA_FIELD);
				String dateString = date.getText().toString();
				if (dateString.equals(arg0.optString(AmMobCommon.DATE_FIELD))) {
					List<StoppagerateBaseVO> vos = parse(json);
					// 即使此处没有值，也缓存下来。表示已经查询过
					totalDatas.put(dateString, vos);
					setupView(vos);
				}
			}
		}

	}

	private JSONObject createRandomJSON() {
		JSONObject json = new JSONObject();
		try {
			JSONArray datas = new JSONArray();
			int count = new Random().nextInt(30);
			if (count == 0) {
				count = 20;
			}
			for (int i = 0; i < count; i++) {
				JSONObject data = new JSONObject();
				data.put("pk", "list" + i);
				data.put("name", i % 5 == 1 ? "虚拟组织虚拟组织虚拟组织虚拟组组织虚拟组织" + i : "虚拟组织" + i);
				data.put("percent", AmMobUtils.getRandom());
				datas.put(data);
			}
			json.put("date", date.getText().toString());
			json.put("data", datas);
		} catch (JSONException e) {
			log(e);
		}

		return json;
	}

	/**
	 * 控件初始化工作
	 */
	private void init() {
		((TextView) findViewById(R.id.biaotitextview)).setText(getResources().getString(R.string.stoppagerate));
		tablelayout = (TableLayout) findViewById(R.id.tablelayout);
		sort = (TextViewWithDrawable) findViewById(R.id.sort);
		sort.setOnClickListener(this);

		date = (TextView) findViewById(R.id.date);
		date.setOnClickListener(this);
		// 刚进来的时候,设置date为当前年月
		Calendar calendar = Calendar.getInstance();
		int nowyear = calendar.get(Calendar.YEAR);
		int nowmonth = calendar.get(Calendar.MONTH) + 1;
		setDateString(nowyear, nowmonth);

		searchview = (SearchViewPlus) findViewById(R.id.searchview);
		searchview.setSearchViewPlusListener(mSearchViewLitener);

		// 返回
		findViewById(R.id.fanhuibutton).setOnClickListener(this);
		// 日期的上一个周期
		findViewById(R.id.leftbutton).setOnClickListener(this);
		// 日期的下一个周期
		findViewById(R.id.rightbutton).setOnClickListener(this);

		coverView = findViewById(R.id.cover_view);
		// 显示蒙版时，阻止一切触屏事件
		coverView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});
		int width = getWindowWidth();
		params = new LayoutParams(width / 3, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);

		sort.setTag("desc");
		searchview.setFocuse(false);
		// 刷新界面数据
		loadDatasAndSendMsg();

	}

	SearchViewPlusListener mSearchViewLitener = new SearchViewPlusListener() {

		/**
		 * 当搜索框有文字时，点击框内的搜索按钮回调
		 */
		public void onSearch(CharSequence query) {
			/* 隐藏软键盘 */
			InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			if (inputMethodManager.isActive()) {
				inputMethodManager.hideSoftInputFromWindow(StoppageRateListActivity.this.getCurrentFocus()
						.getWindowToken(), 0);
			}
			List<StoppagerateBaseVO> filteredVOs = getShowDataByKeyword(query.toString());
			setupView(filteredVOs);
		}

		/**
		 * 当搜索框有文字时，点击框内的删除按钮回调
		 */
		public void onDeleteBtnClicked() {
			/* 隐藏软键盘 */
			InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			if (inputMethodManager.isActive()) {
				inputMethodManager.hideSoftInputFromWindow(StoppageRateListActivity.this.getCurrentFocus()
						.getWindowToken(), 0);
			}
			// 点击关闭按钮时恢复当前日期的所有数据
			setupView(getCurrDatas());
		}

		/**
		 * 焦点在软键盘上的事件监听，重写是为了监听"确定"(搜索)按钮，实现搜索
		 */
		public boolean onKeyDown(View v, int keyCode, KeyEvent event, String query) {
			if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
				/* 隐藏软键盘 */
				InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				if (inputMethodManager.isActive()) {
					inputMethodManager.hideSoftInputFromWindow(StoppageRateListActivity.this.getCurrentFocus()
							.getWindowToken(), 0);
				}
				// 要搜索的关键字
				List<StoppagerateBaseVO> filteredVOs = getShowDataByKeyword(query);
				setupView(filteredVOs);
				return true;
			}
			return false;
		}

	};

	/**
	 * 取得当前日期的数据
	 * 
	 * @return
	 */
	private List<StoppagerateBaseVO> getCurrDatas() {
		String dateString = date.getText().toString();
		if (totalDatas.containsKey(dateString)) {
			return totalDatas.get(dateString);
		}
		return null;
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

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.leftbutton) {
			String message = changeDateDispaly(true);
			if (null == message) {
				loadDatasAndSendMsg();
			} else {
				showInfo(StoppageRateListActivity.this, message);
				return;
			}
		} else if (v.getId() == R.id.rightbutton) {
			// 是否合理
			String message = changeDateDispaly(false);
			if (null == message) {
				loadDatasAndSendMsg();
			} else {
				showInfo(StoppageRateListActivity.this, message);
				return;
			}
		} else if (v.getId() == R.id.fanhuibutton) {
			finish();
		} else if (v.getId() == R.id.sort) {
			if (null == sort.getTag()) {
				sort.setTag("desc");
				// sort.setText(getResources().getString(R.string.desc));
			} else if ("asc".equals(sort.getTag().toString().trim())) {
				sort.setTag("desc");
				sort.setText(getResources().getString(R.string.desc));
				sort.setCompoundDrawables(null, null, getResources().getDrawable(R.drawable.btn_down), null);
				// sort.setImageResource(R.drawable.down2);
			} else {
				sort.setTag("asc");
				sort.setText(getResources().getString(R.string.asc));
				sort.setCompoundDrawables(null, null, getResources().getDrawable(R.drawable.btn_up), null);
				// sort.setImageResource(R.drawable.up);
			}
			setupView(currDatas);
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
				loadDatasAndSendMsg();
			} else {
				showInfo(StoppageRateListActivity.this, "不允许超过当前日期的查询!");
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
		View mTitleView = mInflater.inflate(R.layout.actionbar, null);
		getActionBar().setCustomView(
				mTitleView,
				new ActionBar.LayoutParams(android.app.ActionBar.LayoutParams.MATCH_PARENT,
						android.app.ActionBar.LayoutParams.MATCH_PARENT));

	}

	private List<StoppagerateBaseVO> getShowDataByKeyword(String keyword) {
		if (TextUtils.isEmpty(keyword)) {
			return getCurrDatas();
		} else {
			if (getCurrDatas() != null) {
				List<StoppagerateBaseVO> result = new ArrayList<StoppagerateBaseVO>();
				for (StoppagerateBaseVO vo : getCurrDatas()) {
					if (vo.getName().contains(keyword)) {
						result.add(vo);
					}
				}
				return result;
			}
			return null;
		}
	}

	/**
	 * 记录日志
	 * 
	 * @param msg
	 */
	public void log(Object msg) {
		Logger.info(StoppageRateListActivity.this, AmMobCommon.ammoblog, msg + "");
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

	/**
	 * 刷新界面
	 * 
	 * @param json
	 */
	private void setupView(List<StoppagerateBaseVO> vos) {
		boolean asc = sort.getTag().toString().trim().equals("asc");
		clearGridLayoutDatas();
		sortVOs(vos, asc);
		currDatas = vos;
		if (vos != null && vos.size() > 0) {
			int count = vos.size();
			// 完整行数(实际行数减一)
			int rowNum = count / GRID_COLUMN_NUM;
			for (int i = 0; i < rowNum; i++) {
				TableRow row = new TableRow(StoppageRateListActivity.this);
				row.addView(generateGridItemView(vos.get(i * GRID_COLUMN_NUM), i * GRID_COLUMN_NUM, asc), params);
				row.addView(generateGridItemView(vos.get(i * GRID_COLUMN_NUM + 1), i * GRID_COLUMN_NUM + 1, asc),
						params);
				row.addView(generateGridItemView(vos.get(i * GRID_COLUMN_NUM + 2), i * GRID_COLUMN_NUM + 2, asc),
						params);
				tablelayout.addView(row);
			}
			// 余数
			int remainder = count % GRID_COLUMN_NUM;
			if (remainder > 0) {
				TableRow row = new TableRow(StoppageRateListActivity.this);
				for (int i = 0; i < remainder; i++) {
					row.addView(generateGridItemView(vos.get(count - i - 1), count - i + 1, asc));
				}
				tablelayout.addView(row);
			}
		}
		endLoading();
	}

	/**
	 * 开始加载数据时调用
	 */
	private void startLoading() {
		// 显示蒙版
		coverView.setVisibility(View.VISIBLE);
		// 排序按钮不可用
		sort.setEnabled(false);
	}

	/**
	 * 加载数据以后，界面重绘以后调用
	 */
	private void endLoading() {
		// 隐藏蒙版
		coverView.setVisibility(View.GONE);
		// 排序按钮可用
		sort.setEnabled(true);
	}

	/**
	 * 取得grid的item view
	 * 
	 * @param vo
	 * @param index
	 * @return
	 */
	private View generateGridItemView(final StoppagerateBaseVO vo, int index, boolean isAsc) {
		LayoutInflater inflater = getLayoutInflater();
		final View view = (ViewGroup) inflater.inflate(R.layout.griditem_new, null);
		view.setTag(vo);
		final CircleView circle = (CircleView) view.findViewById(R.id.circle_view);
		TextView orgNameTV = (TextView) view.findViewById(R.id.org_name_tv);
		orgNameTV.setText(vo.getName());
		circle.setText(vo.getPercent() + "%");
		circle.setGravity(Gravity.CENTER);
		circle.setRadius(getWindowWidth() / 7 - 10);
		if (isAsc) {
			circle.setColor(getResources().getColor(ColorConst.colors_asc[index % ColorConst.colors_asc.length]));
		} else {
			circle.setColor(getResources().getColor(ColorConst.colors_desc[index % ColorConst.colors_asc.length]));
		}
		circle.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(StoppageRateListActivity.this, StoppageRateDepartActivity.class);
				String dateString = date.getText().toString();
				String orgName = vo.getName();
				String pk_usedunit = vo.getPk();
				Bundle bundle = new Bundle();
				bundle.putString(AmMobCommon.DATE_FIELD, dateString);
				bundle.putString(AmMobCommon.NAME_FIELD, orgName);
				bundle.putString(AmMobCommon.PK_USEDUNIT_FIELD, pk_usedunit);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		return view;
	}

	/**
	 * 字符串(json格式)解析成VO集合
	 * 
	 * @param json
	 * @return
	 */
	private List<StoppagerateBaseVO> parse(String jsonStr) {
		List<StoppagerateBaseVO> vos = new ArrayList<StoppagerateBaseVO>();
		if (jsonStr != null && !jsonStr.trim().equals("")) {
			try {
				JSONArray jsonArray = new JSONArray(jsonStr);
				if (jsonArray.length() > 0) {
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObj = jsonArray.getJSONObject(i);
						StoppagerateBaseVO vo = new StoppagerateBaseVO();
						vo.setName(jsonObj.getString("name"));
						vo.setPercent(jsonObj.getString("percent"));
						vo.setPk(jsonObj.getString("pk"));
						vos.add(vo);
					}
				}
			} catch (JSONException e) {
				log(e.getMessage());
			}

		}
		return vos;
	}

	/**
	 * 对List<StoppagerateBaseVO>进行排序
	 * 
	 * @param vos
	 * @param asc
	 *            是否升序
	 */
	private void sortVOs(List<StoppagerateBaseVO> vos, final boolean asc) {
		if (vos != null && vos.size() > 0) {
			Collections.sort(vos, new Comparator<StoppagerateBaseVO>() {

				@Override
				public int compare(StoppagerateBaseVO vo1, StoppagerateBaseVO vo2) {
					float percent1 = Float.valueOf(vo1.getPercent());
					float percent2 = Float.valueOf(vo2.getPercent());
					if (percent1 == percent2)
						return 0;
					if (asc) {
						return percent1 > percent2 ? 1 : -1;
					} else {
						return percent1 < percent2 ? 1 : -1;
					}
				}

			});
		}
	}

}
