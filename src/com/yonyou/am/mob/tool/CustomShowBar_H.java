package com.yonyou.am.mob.tool;

import android.content.Context;
import android.graphics.Color;
import android.text.InputType;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 设备完好率自定义纵向滑动柱状图标
 * 包括三个文本控件：显示类别名称、分隔线、柱状条
 * 柱状条添加点击事件
 * @author shizhsh1
 *
 */
public class CustomShowBar_H extends LinearLayout{
	private String name;
	private int barHeight;
	private int goodEquipNum;
	private int badEquipNum;
	//一个线性布局
	LinearLayout linear;
	//显示名字文本
	TextView textView_name;
	//显示分割竖线
	TextView textView_line;
	//显示柱状条
	TextView textView_bar;
	EditText barView;
	
	public int getBarHeight() {
		return barHeight;
	}
	
	public CustomShowBar_H(Context context,String name,int barHeight){
		super(context);
		this.name = name;
		this.barHeight = barHeight;
		this.textView_name = initTextView(context,this.name);
		this.textView_line = initTextLine(context);
		//this.textView_bar = initTextBar(context,barHeight);
		this.barView = initBarView(context,barHeight);
		this.linear = this;
		//初始化线性布局
		initView(name,barHeight);
		
			 
	}

	

	/**
	 * 初始化线性布局
	 */
	private void initView(String name ,int barHeight){
		//设置为横向布局
		setOrientation(LinearLayout.HORIZONTAL);
		//线性布局宽度和高度
		setLayoutParams(new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				75
			));
		setGravity(Gravity.CENTER_VERTICAL);
		
		
//		//加入名字
//		TextView textview = initTextView(getContext(),name);
//		//加入竖线
//		TextView textline = initTextLine(getContext());
//		//加入柱状图
//		//TextView textbar  = initTextBar(getContext(),barHeight);
//		EditText barView  = initBarView(getContext(),barHeight);
		addView(textView_name);
		addView(textView_line);
		//this.addView(textbar);
		addView(barView);

	}
	
	/**
	 * 末级设备类别名字控件
	 * @param context
	 * @param name
	 * @return
	 */
	private TextView initTextView(Context context,String name){
		//创建一个TextView
		TextView textview = new TextView(context);
		//文本控件宽和高
		textview.setLayoutParams(new ViewGroup.LayoutParams(
						240,
						ViewGroup.LayoutParams.MATCH_PARENT
					));
		textview.setText(name);
		textview.setTextSize(16);
		//文本居中
		textview.setGravity(Gravity.CENTER);
		return textview;
	}
	/**
	 * 创建分隔线
	 * @param context
	 * @return
	 */
	private TextView initTextLine(Context context){
		TextView textview = new TextView(context);
		textview.setLayoutParams(new ViewGroup.LayoutParams(
				
					2,ViewGroup.LayoutParams.MATCH_PARENT
				));
		
		textview.setBackgroundColor(Color.GRAY);
		return textview;
	}	
//	/**
//	 * 创建柱状条
//	 * @param context
//	 * @param barheight
//	 * @return
//	 */
//	private TextView initTextBar(Context context,int barheight){
//		TextView textview = new TextView(context);
//		textview.setLayoutParams(new ViewGroup.LayoutParams(
//				//显示值为穿入值的4倍
//				barheight*4,35
//			));
//		textview.setBackgroundColor(Color.RED);
////		textview.setOnClickListener(new MyClickListener());
////		class MyClickListener implements View.OnClickListener{
////
////			@Override
////			public void onClick(View v) {
////				if(textView_bar == getTextView_bar()){
////					v.setBackgroundColor(Color.BLUE);
////				}else{
////					v.setBackgroundColor(Color.RED);
////				}
////				
////			}
////			
////			
////		}
//		
//		//为柱状条添加点击事件
//		textview.setOnClickListener(new OnClickListener(){
//			//为柱状条添加单击事件(基于监听的点击事件)
//		
//			@Override
//			public void onClick(View v) {
//				if(v.isFocusable()){
//					v.setBackgroundColor(Color.BLUE);
//				}else{
//					v.setBackgroundColor(Color.RED);
//				}
//				
//				
//			}
//
//		});

	/**
	 * 创建柱状条
	 * @param context
	 * @param barheight
	 * @return
	 */
	private  EditText initBarView(Context context,int barheight){
		EditText barView = new EditText(context);
		barView.setLayoutParams(new ViewGroup.LayoutParams(
				//显示值为穿入值的4倍
				barheight*4,40
			));
		barView.setBackgroundColor(Color.RED);
		barView.setCursorVisible(false);
		//barView.setInputType(InputType.TYPE_NULL);
//		
		InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(barView.getWindowToken(),0);

//		barView.setOnFocusChangeListener(new OnFocusChangeListener(){
//		
//			@Override
//			public void onFocusChange(View v, boolean hasFocus) {
//				if(hasFocus){
//					v.setBackgroundColor(Color.BLACK);
//					
//					//Toast.makeText(this, "获得了焦点", Toast.LENGTH_LONG).show();
//				}else{
//					v.setBackgroundColor(Color.RED);
//				}
//				
//			}
//
//		});

		return barView;
	};
	
	
	
	
	
	public TextView getTextView_bar() {
		return textView_bar;
	}
	public void setTextView_bar(TextView textView_bar) {
		this.textView_bar = textView_bar;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setBarHeight(int barHeight) {
		this.barHeight = barHeight;
	}
	public CustomShowBar_H(Context context){
		super(context);
	}
	public LinearLayout getLinear() {
		return linear;
	}

	public void setLinear(LinearLayout linear) {
		this.linear = linear;
	}

	public EditText getBarView() {
		return barView;
	}

	public void setBarView(EditText barView) {
		this.barView = barView;
	}
}

