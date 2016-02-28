package com.yonyou.am.mob.equipcompleteratio;




import java.util.Timer;
import java.util.TimerTask;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yonyou.am.mob.commonactivity.AmBaseActivity;
import com.yonyou.am.mob.equipinuseratio.R;
import com.yonyou.am.mob.tool.CustomShowBar_H;
import com.yonyou.am.mob.tool.DoubleTextCircle;

public class EcrCategoryDetailActivity extends AmBaseActivity{
	
	//顶部导航栏那
	ActionBar detailActionBar;
	//
	LinearLayout linearLayoutbar;
	//自定义柱状图线性布局
	CustomShowBar_H customShowBar;
	TextView goodrate;
	//末级类别完好率
	TextView detailgoodrate;
	//末级类别完好台数
	TextView detailgoodnum;
	//末级类别不完好台数
	TextView detailbadnum;
	//中间的球
	DoubleTextCircle middlecircle;
	
	private String categoryName;
	private String goodRate;

	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ecr_category_detail_activity);
		//取出传递来的信息
		Intent intent = getIntent();
		String categoryName = intent.getExtras().getString("categoryName");
		String goodRate = intent.getExtras().getString("goodRate");
		this.categoryName = categoryName;
		this.goodRate = goodRate;
		
		//初始化导航栏
		initializActionBar();
		linearLayoutbar = (LinearLayout)findViewById(R.id.linearlayoutbar);
		detailgoodrate = (TextView)findViewById(R.id.detailgoodrate);
		detailgoodnum = (TextView)findViewById(R.id.detailgoodnum);
		detailbadnum = (TextView)findViewById(R.id.detailbadnum);
		middlecircle = (DoubleTextCircle)findViewById(R.id.onetextcircle_m);
		middlecircle.setFirst_text(goodRate);
		
		
		
		String[] str = new String[]{"制冷","传质","传热","输送","分离","存储","反应",
				"包装","干燥","泵阀类","破碎","混合","安全","输出","输入"};
		int[] height = new int[]{34,55,60,75,66,82,48,63,50,80,90,85,55,76,85};
		int[] goodnum =  new int[]{11,22,33,44,66,77,88,99,33,22,90,85,55,76,85};
		int[] badnum =  new int[]{22,33,44,55,66,77,88,99,33,22,22,85,65,76,23};
		
		//生成柱状图，并设置点击事件
		for( int i=0;i<str.length;i++){
			//获取完好率
			final String goodrate = height[i]+"";
			final String goodnums = goodnum[i]+"";
			final String badnums = badnum[i]+"";
			CustomShowBar_H customBar = new CustomShowBar_H(this,str[i],height[i]);
			linearLayoutbar.addView(customBar);
		     final EditText edittext = customBar.getBarView();
		     final int barheight = customBar.getBarHeight();
		     final LinearLayout linear =customBar.getLinear();
		     
//		     InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//		     imm.hideSoftInputFromWindow(edittext.getWindowToken(),0);
		     //柱子设置监听事件
			 edittext.setOnFocusChangeListener(new OnFocusChangeListener(){					
					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						if(hasFocus){
							linear.setBackgroundColor(getResources().getColor(R.color.mistyrose));
							//上端显示区域显示柱状图的具体值
							detailgoodrate.setText(goodrate+"%");
							detailgoodnum.setText(goodnums+"");
							detailbadnum.setText(badnums);
							
						}else{
							edittext.setBackgroundColor(Color.RED);
							linear.setBackgroundColor(Color.WHITE);
						}						
					}
				});
			edittext.setOnTouchListener(new OnTouchListener() {  	             
		             public boolean onTouch(View v, MotionEvent event) {  
		                 //记住EditText的InputType现在是password   
		                 int inType = edittext.getInputType(); // backup the input type  
		                 edittext.setInputType(InputType.TYPE_NULL); // disable soft input      
		                 edittext.onTouchEvent(event); // call native handler      
		                 edittext.setInputType(inType); // restore input type     
		                 edittext.setSelection(edittext.getText().length());  
		                 return true;  //不传播
		             }
		         }); 
			
		}
		goodrate = (TextView)findViewById(R.id.goodrate);
		goodrate.setOnClickListener(new OnClickListener(){
		
			@Override
			public void onClick(View arg0) {			
				
				//删除之前的所有组件
				linearLayoutbar.removeAllViews();
				String[] str = new String[]{"制冷","传质","传热","输送","分离","存储","反应",
						"包装"};
				int[] height = new int[]{34,55,60,75,66,82,48,63};
				for(int i=0;i<str.length;i++){
					CustomShowBar_H customBar = new CustomShowBar_H(EcrCategoryDetailActivity.this,str[i],height[i]);
					linearLayoutbar.addView(customBar);
					 final EditText edittext = customBar.getBarView();
				     final LinearLayout linear =customBar.getLinear();
				     //柱子设置监听事件
					 edittext.setOnFocusChangeListener(new OnFocusChangeListener(){					
							@Override
							public void onFocusChange(View v, boolean hasFocus) {
								if(hasFocus){
									edittext.setBackgroundColor(Color.BLACK);
									linear.setBackgroundColor(Color.GRAY);
									//Toast.makeText(this, "获得了焦点", Toast.LENGTH_LONG).show();
								}else{
									edittext.setBackgroundColor(Color.RED);
									linear.setBackgroundColor(Color.WHITE);
								}						
							}
						});
					edittext.setOnTouchListener(new OnTouchListener() {  	             
				             public boolean onTouch(View v, MotionEvent event) {  
				                 //记住EditText的InputType现在是password   
				                 int inType = edittext.getInputType(); // backup the input type  
				                 edittext.setInputType(InputType.TYPE_NULL); // disable soft input      
				                 edittext.onTouchEvent(event); // call native handler      
				                 edittext.setInputType(inType); // restore input type     
				                 edittext.setSelection(edittext.getText().length());  
				                 return true;  //不传播
				             }
				         }); 
				}			
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
		titleText.setText(categoryName);
		detailActionBar.setCustomView(actionBarView);
			
	}
	class ClickListener implements View.OnClickListener{

		@Override
		public void onClick(View arg0) {
			
			
		}
		
		
		
	}
}
