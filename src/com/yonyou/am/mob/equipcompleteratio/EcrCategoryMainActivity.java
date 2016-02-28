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
	//完好台数
	private int[] goodEquip;
	//不完好台数
	private String[] categoryName;
	//隐藏控件存储传输的信息
	TextView transtext;
	//顶部导航栏
	ActionBar detailActionBar;
	//中间的球
	DoubleTextCircle middlecircle;
	//左边的球
	DoubleTextCircle leftCircle;
	//右边的球
	DoubleTextCircle rightCircle;
	CustomShowBar_V showBar;
	LinearLayout totallinear;
	HorizontalScrollView horizontalscroll;
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ecr_category_main_activity);
		//初始化导航栏
		initializActionBar();
		
		middlecircle = (DoubleTextCircle)findViewById(R.id.circle_middle);
		leftCircle = (DoubleTextCircle)findViewById(R.id.circle_left);
		rightCircle = (DoubleTextCircle)findViewById(R.id.circle_right);
		
		totallinear = (LinearLayout)findViewById(R.id.totallinear);
		horizontalscroll = (HorizontalScrollView)findViewById(R.id.horizontalscroll);
		
		transtext = (TextView)findViewById(R.id.transtext);
		
		//获取默认控件的宽度
		int defWidth = totallinear.getPaddingLeft()+ totallinear.getPaddingRight();
		DisplayMetrics metric = new DisplayMetrics();  
		getWindowManager().getDefaultDisplay().getMetrics(metric);  
		int windowWidth = metric.widthPixels;     // 屏幕宽度（像素）   
		int height = metric.heightPixels;   //屏幕高度
		
		showBar = (CustomShowBar_V)findViewById(R.id.showbar);
		
		categoryName =new String[]{"化工设备","仪器仪表设备","生产电器设备","器具","机电生产设备",
				"冰箱","彩电","洗衣机"};
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
		 * 点击查看所选类别的完好率
		 */
		middlecircle.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Bundle data = new Bundle();
				//添加需要传递的信息
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
		 * 柱状图的触屏事件
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
				//如果为true，将不会传播给控件上绑定的基于回调的onTouch事件
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
	 * 初始化ActionBar,根据上个页面所选择的设备类别更改标题
	 */
	private void initializActionBar(){
		//初始化ActionBar
		detailActionBar = getActionBar();
		detailActionBar.setDisplayShowHomeEnabled(false);
		detailActionBar.setDisplayShowTitleEnabled(false);
		detailActionBar.setDisplayShowCustomEnabled(true);
		LayoutInflater inflater = getLayoutInflater();
		View actionBarView = inflater.inflate(R.layout.ecr_custom_actionbar, null);
		TextView titleText = (TextView)actionBarView.findViewById(R.id.titletext);
		titleText.setText("设备完好率");
		detailActionBar.setCustomView(actionBarView);
			
	}
	
}
