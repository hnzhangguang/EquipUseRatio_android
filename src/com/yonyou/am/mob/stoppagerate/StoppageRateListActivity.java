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
 * �豸����ͣ����-�б����
 * 
 * @author hujieh
 * 
 */

@SuppressLint({ "ClickableViewAccessibility", "InflateParams" })
public class StoppageRateListActivity extends AmBaseActivity {

	private View coverView;
	/** ���� */
	private static final int GRID_COLUMN_NUM = 3;

	/** ����ť */
	private TextViewWithDrawable sort = null;
	/** ��ʾ���� */
	private TextView date = null;
	/** ��񲼾ֿؼ� */
	private TableLayout tablelayout = null;
	/** �����ؼ� */
	private SearchViewPlus searchview;
	/** �������ݣ�keyΪ���ڣ�valueΪ���ڼ�����ݼ��� */
	private Map<String, List<StoppagerateBaseVO>> totalDatas = new HashMap<String, List<StoppagerateBaseVO>>();
	/*** չʾ������ */
	private List<StoppagerateBaseVO> currDatas = null;

	private LayoutParams params = new LayoutParams();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stoppagerate_list);

		// ��ʼ�������ĵ���������
		initActionbar();
		// ��ʼ���ؼ�����
		init();
		// ��¼��¼��־
		recordLoginLog(AppContext.getCurrent(this));

	}

	/**
	 * �첽�������ݣ�ȥ�����ݺ���message
	 * 
	 */
	private void loadDatasAndSendMsg() {
		startLoading();
		final String dateString = date.getText().toString();
		// �����Ӧ���ڵ������Ѿ����أ���ֱ�ӷ�����Ϣ�����ٲ�ѯ����
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
	 * ma�ص���
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
					// ��ʹ�˴�û��ֵ��Ҳ������������ʾ�Ѿ���ѯ��
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
					// ��ʹ�˴�û��ֵ��Ҳ������������ʾ�Ѿ���ѯ��
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
				data.put("name", i % 5 == 1 ? "������֯������֯������֯��������֯������֯" + i : "������֯" + i);
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
	 * �ؼ���ʼ������
	 */
	private void init() {
		((TextView) findViewById(R.id.biaotitextview)).setText(getResources().getString(R.string.stoppagerate));
		tablelayout = (TableLayout) findViewById(R.id.tablelayout);
		sort = (TextViewWithDrawable) findViewById(R.id.sort);
		sort.setOnClickListener(this);

		date = (TextView) findViewById(R.id.date);
		date.setOnClickListener(this);
		// �ս�����ʱ��,����dateΪ��ǰ����
		Calendar calendar = Calendar.getInstance();
		int nowyear = calendar.get(Calendar.YEAR);
		int nowmonth = calendar.get(Calendar.MONTH) + 1;
		setDateString(nowyear, nowmonth);

		searchview = (SearchViewPlus) findViewById(R.id.searchview);
		searchview.setSearchViewPlusListener(mSearchViewLitener);

		// ����
		findViewById(R.id.fanhuibutton).setOnClickListener(this);
		// ���ڵ���һ������
		findViewById(R.id.leftbutton).setOnClickListener(this);
		// ���ڵ���һ������
		findViewById(R.id.rightbutton).setOnClickListener(this);

		coverView = findViewById(R.id.cover_view);
		// ��ʾ�ɰ�ʱ����ֹһ�д����¼�
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
		// ˢ�½�������
		loadDatasAndSendMsg();

	}

	SearchViewPlusListener mSearchViewLitener = new SearchViewPlusListener() {

		/**
		 * ��������������ʱ��������ڵ�������ť�ص�
		 */
		public void onSearch(CharSequence query) {
			/* ��������� */
			InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			if (inputMethodManager.isActive()) {
				inputMethodManager.hideSoftInputFromWindow(StoppageRateListActivity.this.getCurrentFocus()
						.getWindowToken(), 0);
			}
			List<StoppagerateBaseVO> filteredVOs = getShowDataByKeyword(query.toString());
			setupView(filteredVOs);
		}

		/**
		 * ��������������ʱ��������ڵ�ɾ����ť�ص�
		 */
		public void onDeleteBtnClicked() {
			/* ��������� */
			InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			if (inputMethodManager.isActive()) {
				inputMethodManager.hideSoftInputFromWindow(StoppageRateListActivity.this.getCurrentFocus()
						.getWindowToken(), 0);
			}
			// ����رհ�ťʱ�ָ���ǰ���ڵ���������
			setupView(getCurrDatas());
		}

		/**
		 * ������������ϵ��¼���������д��Ϊ�˼���"ȷ��"(����)��ť��ʵ������
		 */
		public boolean onKeyDown(View v, int keyCode, KeyEvent event, String query) {
			if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
				/* ��������� */
				InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				if (inputMethodManager.isActive()) {
					inputMethodManager.hideSoftInputFromWindow(StoppageRateListActivity.this.getCurrentFocus()
							.getWindowToken(), 0);
				}
				// Ҫ�����Ĺؼ���
				List<StoppagerateBaseVO> filteredVOs = getShowDataByKeyword(query);
				setupView(filteredVOs);
				return true;
			}
			return false;
		}

	};

	/**
	 * ȡ�õ�ǰ���ڵ�����
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
	 * ����������ʾ
	 * 
	 * @param dateString
	 *            ��ǰ����
	 * @param isPreDate
	 *            �Ƿ��ǵ�ǰһ����
	 * @return Ϊnull��˵������������Ϊ�������ʾ��Ϣ
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
				return "����������ǰ���ڵĲ�ѯ!";
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
			// �Ƿ����
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
			dialog.setTitle("��ѡ������");
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
				showInfo(StoppageRateListActivity.this, "����������ǰ���ڵĲ�ѯ!");
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
	 *            ������
	 * @param id
	 *            Ҫ��ѯ�����ݵ�id
	 * @return ����
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
	 * �����ǰtablelayout���ݹ���
	 */
	private void clearGridLayoutDatas() {
		// ����С����󻯲���ʱ��Ĵ���
		if (null == tablelayout) {
			return;
		}
		int countChild = tablelayout.getChildCount();
		if (countChild >= 1) {
			// ����Ӻ����ȥ��Ԫ��
			tablelayout.removeAllViews();
		}

	}

	/**
	 * ��������ʼ������
	 */
	private void initActionbar() {
		// �Զ��������
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
	 * ��¼��־
	 * 
	 * @param msg
	 */
	public void log(Object msg) {
		Logger.info(StoppageRateListActivity.this, AmMobCommon.ammoblog, msg + "");
		Log.i(AmMobCommon.ammoblog, msg + "");
	}

	/**
	 * ������
	 * 
	 * @param activity
	 * @param msg
	 */
	public void showInfo(Activity activity, Object msg) {
		Toast.makeText(activity, msg + "", Toast.LENGTH_LONG).show();
	}

	/**
	 * ˢ�½���
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
			// ��������(ʵ��������һ)
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
			// ����
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
	 * ��ʼ��������ʱ����
	 */
	private void startLoading() {
		// ��ʾ�ɰ�
		coverView.setVisibility(View.VISIBLE);
		// ����ť������
		sort.setEnabled(false);
	}

	/**
	 * ���������Ժ󣬽����ػ��Ժ����
	 */
	private void endLoading() {
		// �����ɰ�
		coverView.setVisibility(View.GONE);
		// ����ť����
		sort.setEnabled(true);
	}

	/**
	 * ȡ��grid��item view
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
	 * �ַ���(json��ʽ)������VO����
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
	 * ��List<StoppagerateBaseVO>��������
	 * 
	 * @param vos
	 * @param asc
	 *            �Ƿ�����
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
