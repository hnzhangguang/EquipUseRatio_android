package com.yonyou.am.mob.equipcompleteratio;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.yonyou.am.mob.commonactivity.AmBaseActivity;
import com.yonyou.am.mob.equipinuseratio.R;

public class EcrBadEquipListActivity extends AmBaseActivity{
	
private ActionBar equipListBar;
	
	protected void onCreate(Bundle saveInstanceState){
		super.onCreate(saveInstanceState);
		setContentView(R.layout.ecr_badequip_list_activity);
		//��ʼ��������
		initializActionBar();
		
		List<Map<String, Object>> listItems = getBadEquipList();
		
		//����ListView
		SimpleAdapter badEquipAdapter = new SimpleAdapter(this,
				listItems,
				R.layout.bad_equip_list_layout,
				new String[]{"category_code","equip_name","equip_code","type_code"},
				new int[]{R.id.category_code, R.id.equip_name, R.id.equip_code, R.id.type_code});
		ListView equiplist = (ListView)findViewById(R.id.equiplistview);			
		equiplist.setAdapter(badEquipAdapter);
	}
	/**
	 * ��װ�б�����
	 * @return
	 */
	private List<Map<String, Object>> getBadEquipList(){
		List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
		String categoryCode[] = new String[]{"A201511140001","A201511140001","A201511140001","A201511140001",
												"A201511140001","A201511140001","A201511140001"};
		String	equipName[] = new String[]{"�յ�","����","����","��ˮ��",
				"����","΢��¯","��ԡ��"};
		String  equipCode[] = new String[]{"E201511140001","E201511140001","E201511140001","E201511140001",
				"E201511140001","E201511140001","E201511140001"};
		String  typeCode[] = new String[]{"T20151114","T20151114","T20151114","T20151114",
				"T20151114","T20151114","T20151114"};
		
		for(int i=0;i<equipCode.length;i++){
			Map<String, Object> listItem = new HashMap<String, Object>();
			listItem.put("category_code", categoryCode[i]);
			listItem.put("equip_name", equipName[i]);
			listItem.put("equip_code", equipCode[i]);
			listItem.put("type_code", typeCode[i]);
			listItems.add(listItem);
		}
		return listItems;
	}
	
	/**
	 * ��ʼ��ActionBar,�����ϸ�ҳ����ѡ����豸�����ı���
	 */
	private void initializActionBar(){
		//��ʼ��ActionBar
		equipListBar = getActionBar();
		equipListBar.setDisplayShowHomeEnabled(false);
		equipListBar.setDisplayShowTitleEnabled(false);
		equipListBar.setDisplayShowCustomEnabled(true);
		LayoutInflater inflater = getLayoutInflater();
		View actionBarView = inflater.inflate(R.layout.ecr_custom_actionbar, null);
		TextView titleText = (TextView)actionBarView.findViewById(R.id.titletext);
		titleText.setText("������豸�б�");
		equipListBar.setCustomView(actionBarView);
			
	}
	
}
