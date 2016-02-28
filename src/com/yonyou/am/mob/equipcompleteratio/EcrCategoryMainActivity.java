package com.yonyou.am.mob.equipcompleteratio;

import java.util.Map;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yonyou.am.mob.commonactivity.AmBaseActivity;
import com.yonyou.am.mob.equipinuseratio.R;
import com.yonyou.am.mob.tool.CreateRectFHandler;
import com.yonyou.am.mob.tool.CustomShowBar_V;
import com.yonyou.am.mob.tool.DoubleTextCircle;

public class EcrCategoryMainActivity extends AmBaseActivity {
	//���̨��
	private int[] goodEquip;
	//�����̨��
	private String[] categoryName;
	//���ؿؼ��洢�������Ϣ
	TextView transtext;
	//����������
	ActionBar detailActionBar;
	//�м����
	DoubleTextCircle middlecircle;
	//��ߵ���
	DoubleTextCircle leftCircle;
	//�ұߵ���
	DoubleTextCircle rightCircle;
	CustomShowBar_V showBar;
	LinearLayout totallinear;
	HorizontalScrollView horizontalscroll;
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ecr_category_main_activity);
		//��ʼ��������
		initializActionBar();
		
		middlecircle = (DoubleTextCircle)findViewById(R.id.circle_middle);
		leftCircle = (DoubleTextCircle)findViewById(R.id.circle_left);
		rightCircle = (DoubleTextCircle)findViewById(R.id.circle_right);
		
		totallinear = (LinearLayout)findViewById(R.id.totallinear);
		horizontalscroll = (HorizontalScrollView)findViewById(R.id.horizontalscroll);
		
		transtext = (TextView)findViewById(R.id.transtext);
		
		//��ȡĬ�Ͽؼ��Ŀ��
		int defWidth = totallinear.getPaddingLeft()+ totallinear.getPaddingRight();
		DisplayMetrics metric = new DisplayMetrics();  
		getWindowManager().getDefaultDisplay().getMetrics(metric);  
		int windowWidth = metric.widthPixels;     // ��Ļ��ȣ����أ�   
		int height = metric.heightPixels;   //��Ļ�߶�
		
		showBar = (CustomShowBar_V)findViewById(R.id.showbar);
		
		categoryName =new String[]{"�����豸","�����Ǳ��豸","���������豸","����","���������豸",
				"����","�ʵ�","ϴ�»�"};
		float[] goodRate = new float[]{72,56,25,48,76,90,84,67};
		String[] barsTopText ={"72%","56%","25%","48%","76%","90%","84%","67%"};
		goodEquip =new int[]{50,60,70,80,90,100,110,120};
		
		
		float chartViewWidth = windowWidth - defWidth;
		float chartViewHeight = 600;
		
		CreateRectFHandler chartRectF = new CreateRectFHandler(chartViewWidth,chartViewHeight,
				goodRate,categoryName);
		showBar.setBarsTopText(barsTopText);
		showBar.setChartRectF(chartRectF);
		showBar.setCategoryName(categoryName);
		showBar.setWindowWidth(windowWidth-defWidth);
		
		/**
		 * ����鿴��ѡ���������
		 */
		middlecircle.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Bundle data = new Bundle();
				//�����Ҫ���ݵ���Ϣ
				data.putString("categoryName",transtext.getText().toString());
				data.putString("goodRate", middlecircle.getFirst_text().toString());
				data.putStringArray("categoryArray", categoryName);
				Intent intent = new Intent(EcrCategoryMainActivity.this,
						EcrCategoryDetailActivity.class);
				intent.putExtras(data);
				startActivity(intent);
				
			}
	
		});	
		/**
		 * ��״ͼ�Ĵ����¼�
		 * 
		 */
		showBar.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				
				if(event.getAction()== MotionEvent.ACTION_UP){
					
					Map<String,String> returnValue = showBar.getGetReturnValue();
					if(returnValue.isEmpty()){
						
					}else{
						String goodRate = returnValue.get("goodRate");
						String categoryName = returnValue.get("categoryName");
						int index = showBar.getClickIndex();
						middlecircle.setFirst_text(goodRate);
						leftCircle.setFirst_text(goodEquip[index]+"");
						
						transtext.setText(categoryName);
						
						
						
						Log.v("-showBar-", "the event on out_UP");
						//Toast.makeText(EcrCategoryMainActivity.this, categoryName, Toast.LENGTH_SHORT).show();
					}
				}
				//���Ϊtrue�������ᴫ�����ؼ��ϰ󶨵Ļ��ڻص���onTouch�¼�
				return false;
			}

		});	
		rightCircle.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				
				Intent intent = new Intent(EcrCategoryMainActivity.this,
						EcrBadEquipListActivity.class);
				startActivity(intent);
			}
			
		});
	}
	
	
	
	
	/**
	 * ��ʼ��ActionBar,�����ϸ�ҳ����ѡ����豸�����ı���
	 */
	private void initializActionBar(){
		//��ʼ��ActionBar
		detailActionBar = getActionBar();
		detailActionBar.setDisplayShowHomeEnabled(false);
		detailActionBar.setDisplayShowTitleEnabled(false);
		detailActionBar.setDisplayShowCustomEnabled(true);
		LayoutInflater inflater = getLayoutInflater();
		View actionBarView = inflater.inflate(R.layout.ecr_custom_actionbar, null);
		TextView titleText = (TextView)actionBarView.findViewById(R.id.titletext);
		titleText.setText("�豸�����");
		detailActionBar.setCustomView(actionBarView);
			
	}
	
}
