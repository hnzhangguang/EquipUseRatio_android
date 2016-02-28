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
 * ����˵�����ʲ��豸������ - �б����
 * 
 * @author zhangg 2015-11-4
 */
public class EiurListActivity extends AmBaseActivity implements OnLongClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eiur_list);
		// ��ʼ�������ĵ���������
		initActionbar();
		// ��ʼ���ؼ�����
		init();

		Map<String, Object> map = new HashMap<String, Object>();
		// ���ú�̨
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
			paramJson.put("yearMonth", date.getText().toString());
			if (!map.isEmpty()) {
				for (String key : map.keySet()) {
					paramJson.put(key, map.get(key));
				}
			}
			// ���ú�̨
			callAction("queryOrgEquipRationByUser", paramJson, new EiurListCallBack(this));
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
					// ������Ϣ
					sendEmptyMessage(0x5554);
					log(jsonArray);
					return;
				}
				// ��NC��ѯ������
				if (map.get("info") != null) {
					jsonArray = new JSONArray(map.get("info").toString());
				} else {
					// ����Ӻ�̨���������,����ɾ���˵�
					jsonArray = new JSONArray();
					for (int i = 0; i < 32; i++) {
						JSONObject jsonObject = new JSONObject();
						jsonObject.put("name", i + "�Ż��");
						jsonObject.put("percent", AmMobUtils.getRandom(2));
						jsonObject.put("id", i + "");
						jsonArray.put(jsonObject);
					}
				}
				if (0 == jsonArray.length()) {
					showInfo(EiurListActivity.this, "��̨����Ϊ��!");
					log("��̨����Ϊ��!");
					return;
				}
				// ������Ϣ
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
			Toast.makeText(EiurListActivity.this, s + "->" + s1 + "���ڵ�������α����", Toast.LENGTH_LONG).show();
		}

	}

	/**
	 * ������Ϣ���½�������
	 * 
	 * @param what
	 *            ��Ϣ����
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
			orderButton.setText("����");
		}
		// Ϊ�˲�����,�Ժ����л�ע�͵�
		// loadDataForJSONArray(new JSONArray(), true);
		// ˢ�½�������
		refreshDataForActivity();
	}

	@Override
	protected void onResume() {
		super.onResume();
		handler.sendEmptyMessage(0x123);
		// handler.sendEmptyMessage(0x5554);
	}

	/**
	 * ˢ�½�������
	 */
	public void refreshDataForActivity() {
		// ��װ��ʾ����
		addGridLayoutDatas(totalLists, getSortType());
	}

	/**
	 * �ؼ���ʼ������
	 */
	@SuppressLint("InflateParams")
	private void init() {

		// ������ɫ
		colors_asc = new int[] { R.color.redlight, R.color.redmid, R.color.redem, R.color.sealight, R.color.seamid,
				R.color.seaem, R.color.yellolight, R.color.yellomid, R.color.yelloem, R.color.lhlight, R.color.lhmid,
				R.color.lhem, R.color.touming2light, R.color.touming2mid, R.color.touming2em, R.color.touming1light,
				R.color.touming1mid, R.color.touming1em, R.color.crimson, R.color.gainsboro, R.color.plum,
				R.color.lightcyan, R.color.lightcoral, R.color.royalblue, R.color.aqua };
		// ������ɫ
		colors_desc = new int[] { R.color.redem, R.color.redmid, R.color.redlight, R.color.seaem, R.color.seamid,
				R.color.sealight, R.color.yelloem, R.color.yellomid, R.color.yellolight, R.color.lhem, R.color.lhmid,
				R.color.lhlight, R.color.touming2em, R.color.touming2mid, R.color.touming2light, R.color.touming1em,
				R.color.touming1mid, R.color.touming1light, R.color.crimson, R.color.gainsboro, R.color.plum,
				R.color.lightcyan, R.color.lightcoral, R.color.royalblue, R.color.aqua };

		// �ؼ���ʼ��
		tablelayout = (TableLayout) findViewById(R.id.tablelayout);

		orderimage = (ImageView) findViewById(R.id.orderimage);
		orderButton = (Button) findViewById(R.id.orderButton);
		orderButton.setOnClickListener(this);
		fanhuibutton = (Button) findViewById(R.id.fanhuibutton);
		fanhuibutton.setOnClickListener(this);

		// ����-������layout
		linearlayoutdate = (LinearLayout) findViewById(R.id.linearlayoutdate);
		LayoutInflater inflater = LayoutInflater.from(this);
		// ��̬���ز���
		LinearLayout datelayout = (LinearLayout) inflater.inflate(R.layout.dateselectview, null);
		linearlayoutdate.addView(datelayout);
		date = (TextView) findViewById(R.id.date);
		// date.setEnabled(false);
		date.setOnClickListener(this);
		// �ս�����ʱ��,����dateΪ��ǰ����
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

		// ������ĸ�����
		searchLinearlayout = (LinearLayout) findViewById(R.id.searchLinearlayout);
		LinearLayout searchviewbarLayout = (LinearLayout) inflater.inflate(R.layout.searcviewbar, null);
		searchLinearlayout.addView(searchviewbarLayout);

		main_search_bt = (ImageView) findViewById(R.id.main_search_bt);
		main_search_et = (EditText) findViewById(R.id.main_search_et);
		searchbuttonex = (Button) findViewById(R.id.searchbuttonex);
		searchbuttonex.setOnClickListener(this);
		// ��ʼ������list
		totalLists = new ArrayList<Map<String, String>>();
		// ��ʼ��handler
		initHandler();
	}

	/**
	 * ��ʼ��handler
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
	 * ����item �� ��䵽�̶�����λ��
	 * 
	 * @param datalist
	 *            Ҫ��װ������
	 * @param isAsc
	 *            �Ƿ�����������
	 */
	private void addGridLayoutDatas(List<Map<String, String>> datalist, boolean isAsc) {

		// ����С����󻯲���ʱ��Ĵ���
		clearGridLayoutDatas();
		tablelayout.setStretchAllColumns(true);

		int size = datalist.size();
		if (size <= 0) {
			return;
		}
		datalist = AmMobUtils.exeSort(datalist, isAsc ? "asc" : "desc", "percent", true);
		// ÿ��չʾ3��
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
	 * ����item����
	 * 
	 * @param singleMap
	 *            ����singleMap��������item
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
		// Բ��
		final CircleView circle = new CircleView(EiurListActivity.this);
		// ��ɫ�����ʱ���������
		int seq = rowseq * 3 + colseq - 1;
		if (singleMap.isEmpty()) {
			circle.setColor(getResources().getColor(R.color.white));
			circle.setText("");
		} else {
			// ����20���������ɫ
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
		// Ϊ��������id��
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
					// Ϊ��ǰѡ���Բ��Ϣ
					String textString = ((CircleView) v).getText().toString();
					String nameString = getDepartNameById(totalLists, ((CircleView) v).getTag().toString());
					String tagString = ((CircleView) v).getTag() == null ? "" : ((CircleView) v).getTag().toString();
					Bundle bundle = new Bundle();
					bundle.putString("date", dateString);
					// ���������
					bundle.putString("tag", tagString);
					// ��ʾ�ٷֱ�
					bundle.putString("text", textString);
					bundle.putString("departname", nameString);
					intent.putExtras(bundle);
					startActivity(intent);
					// �����л����������ұ߽��룬����˳�
					overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
					// finish();
					log("��ǰѡ���item��ϢΪdate:" + dateString + "; tag:" + tagString + "; text:" + textString);
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
			commitDate();
		}

		return null;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View v) {
		// ȡ�õ�ǰ������ʾ������ eg "2015-10"
		String dateString = date.getText().toString();
		// ����󷭰�ť
		if (v.getId() == R.id.leftbutton) {
			if (null == changeDateDispaly(dateString, true)) {
				// null ΪҪչʾ�����ݣ��Ӻ�̨��ѯ�����ģ�
				// loadDatasAndRefreshView(null, true);

				Map<String, Object> map = new HashMap<String, Object>();
				call(map);
			} else {
				showInfo(EiurListActivity.this, changeDateDispaly(dateString, true));
				return;
			}
			// ����ҷ���ť
		} else if (v.getId() == R.id.rightbutton) {
			// �Ƿ����
			if (null == changeDateDispaly(dateString, false)) {
				// null ΪҪչʾ�����ݣ��Ӻ�̨��ѯ�����ģ�
				// loadDatasAndRefreshView(null, true);

				Map<String, Object> map = new HashMap<String, Object>();
				call(map);
			} else {
				showInfo(EiurListActivity.this, changeDateDispaly(dateString, false));
				return;
			}

			// ���ذ�ť
		} else if (v.getId() == R.id.fanhuibutton) {
			// Intent intent = new Intent(EiurListActivity.this,
			// AmMobLoginActivit.class);
			// startActivity(intent);
			finish();
			// ����
		} else if (v.getId() == R.id.orderButton) {

			// �����Ҫ��Ϊ�˲�������ma�˵�ʵ��
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
				orderButton.setText("����");
				orderimage.setImageDrawable(getResources().getDrawable(R.drawable.up));
			} else {
				orderButton.setTag("asc");
				orderButton.setText("����");
				orderimage.setImageDrawable(getResources().getDrawable(R.drawable.down2));
			}
			addGridLayoutDatas(totalLists, getSortType());

		} else if (v.getId() == R.id.searchbuttonex) {
			// ǰһ�ε��������������nullʱ��
			if (isNullSearchPre) {
				if (null == main_search_et.getText() || main_search_et.getText().toString().equals("")) {
					showInfo(EiurListActivity.this, "�ؼ���Ϊ��!");
					isNullSearchPre = true;
				} else {
					// Ҫ�����Ĺؼ���
					String keyword = main_search_et.getText().toString();
					// ���ݹؼ��ֽ��й���
					loadDataForJSONArray(totalLists, keyword);
					isNullSearchPre = false;
				}
			} else {
				if (null == main_search_et.getText() || main_search_et.getText().toString().equals("")) {
					showInfo(EiurListActivity.this, "�ؼ���Ϊ��,���л�ԭ����!");
					// ����ԭ�����ݻ�ԭ����������
					refreshDataForActivity();
					isNullSearchPre = true;
				} else {
					// Ҫ�����Ĺؼ���
					String keyword = main_search_et.getText().toString();
					// ���ݹؼ��ֽ��й���
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
				if (trueMonth >= 10) {
					date.setText(year + "-" + trueMonth);
				} else {
					date.setText(year + "-0" + trueMonth);
				}
				commitDate();
				// �жϵ�ǰ�����Ƿ����
				if (!isNetworkAvailable(EiurListActivity.this)) {
					showInfo(EiurListActivity.this, "��ǰ���粻����,��������!");
					return;
				}
				// ���²�ѯ����
				loadDatasAndRefreshView(null, true);
			} else {
				showInfo(EiurListActivity.this, "����������ǰ���ڵĲ�ѯ!");
			}
		}
	};

	/**
	 * һ��Ӧ���� �Ӻ�̨��ѯ���������ݺ��Ѵ˷�����ִ��
	 * 
	 * @param nowmonthlist
	 *            ����Ҫ��ʾ������
	 * @param isFasle
	 *            �Ƿ��Ǽ�����
	 */
	private void loadDatasAndRefreshView(JSONArray jsonArray, boolean isFalseData) {
		// �µ�����
		// List<Map<String, String>> nowmonthlist = new ArrayList<Map<String,
		// String>>();
		// ��װ���ݣ��ڶ�������Ϊcest����,trueΪ������
		loadDataForJSONArray(jsonArray, isFalseData);
		// ��ʾ
		refreshDataForActivity();
	}

	/**
	 * �Ѻ�̨�����������װ�ɽ�����ʾ������
	 * 
	 * @param jsonArray
	 * @param isFalse
	 *            �Ƿ��Ǽ�����
	 */
	private void loadDataForJSONArray(JSONArray jsonArray, boolean isFalseData) {
		totalLists = new ArrayList<Map<String, String>>();
		int count = 0;
		// Ŀǰֻ��Ϊ�˲�����
		if (isFalseData) {
			count = 28;
			for (int i = 0; i < count; i++) {
				Map<String, String> tempMap = new HashMap<String, String>();
				tempMap.put("name", ((i % 2 == 0) ? "�л����񹲺͹������ܿ���" : "������") + i + "�Ż��");
				tempMap.put("percent", AmMobUtils.getRandom(2));
				tempMap.put("id", i + "");
				totalLists.add(tempMap);
			}
		} else if (null == jsonArray || jsonArray.length() == 0) {
			// showInfo(EiurListActivity.this, "������Ҫ��ʾ");
			return;
		} else {
			try {// ��ʵ����
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
				log(e.getMessage() + "\n��־����:" + e);
			}
		}

	}

	/**
	 * �Ѻ�̨�����������װ�ɽ�����ʾ������(�ؼ��ֹ��ˣ����Զ�ˢ�½��湦��)
	 * 
	 * @param jsonArray
	 *            ��̨����
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
			// ������
			addGridLayoutDatas(searchlist, getSortType());
		} catch (Exception e) {
			e.printStackTrace();
			log(e.getMessage() + "\n��־����:" + e);
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
	 * �ύ��Ҫ���������
	 */
	private void commitDate() {
		Editor editor = preference.edit();
		editor.putString("eiur_date", date.getText().toString());
		editor.commit();
	}

	/**
	 * �ֻ�����ȷ����ť�ļ����¼�
	 */
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
			/* ��������� */
			InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			if (inputMethodManager.isActive()) {
				inputMethodManager.hideSoftInputFromWindow(EiurListActivity.this.getCurrentFocus().getWindowToken(), 0);
			}
			// Ҫ�����Ĺؼ���
			String keyword = main_search_et.getText().toString();
			// ���ݹؼ��ֽ��й���
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
	 * registerForContextMenu(getListView()); ���������Ĳ˵� ��Ҫoncreateע��
	 * ��onCreateContextMenu�����û�ÿһ�γ���Viewʱ������
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {

		super.onCreateContextMenu(menu, v, menuInfo);

	}

	/**
	 * ��Ӧ�����Ĳ˵�
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		return super.onContextItemSelected(item);
	}

	/**
	 * �õ���ǰ�������͡� asc��desc��
	 * 
	 * @return true = asc ; false = desc
	 */
	private boolean getSortType() {
		return orderButton.getTag().toString().trim().equals("asc") ? true : false;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {// ���ؼ�
			onClick(fanhuibutton);
			// �����Ӧ����
			return true;
		}

		return super.onKeyDown(keyCode, event);
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

	/**
	 * ��¼��־
	 * 
	 * @param msg
	 */
	public void log(Object msg) {
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

	/** �󷭰�ť */
	Button leftbutton = null;
	/** �ҷ���ť */
	Button rightbutton = null;
	/** ���ذ�ť */
	Button fanhuibutton = null;
	/** ����ť */
	Button orderButton = null;
	/** ��ʾ���� */
	TextView date = null;
	/** GridLayout��adapter */
	ArrayAdapter<String> adapter;
	LinearLayout searchLinearlayout = null;
	/** ��񲼾ֿؼ� */
	TableLayout tablelayout = null;
	LinearLayout linearlayoutdate = null;
	/** ��ǰ��ʾ���� */
	// private static String yearmonth = "";
	/** ����id�ĳ�ʼ��ֵ */
	int id = -1;
	/** ���������� */
	ArrayList<String> mAllList = new ArrayList<String>();
	/** �����ؼ� */
	// SearchView searchview = null;
	ImageView main_search_bt = null;
	EditText main_search_et = null;
	Button searchbuttonex = null;
	ImageView orderimage = null;
	/*** Ҫչʾ������ */
	List<Map<String, String>> totalLists = null;
	AppContext appcontext = null;
	/** �����ؼ��� */
	// String key = null;
	// static int nownumber = 1;
	static int tempid = 1;
	int[] colors_asc = null;
	int[] colors_desc = null;
	static boolean isNullSearchPre = true;
	SharedPreferences preference = null;
	Handler handler = null;
	JSONArray jsonArray = null;
	// ���õ���������ʽ eg: \n

}
