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
 * �豸������Զ������򻬶���״ͼ��
 * ���������ı��ؼ�����ʾ������ơ��ָ��ߡ���״��
 * ��״����ӵ���¼�
 * @author shizhsh1
 *
 */
public class CustomShowBar_H extends LinearLayout{
	private String name;
	private int barHeight;
	private int goodEquipNum;
	private int badEquipNum;
	//һ�����Բ���
	LinearLayout linear;
	//��ʾ�����ı�
	TextView textView_name;
	//��ʾ�ָ�����
	TextView textView_line;
	//��ʾ��״��
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
		//��ʼ�����Բ���
		initView(name,barHeight);
		
			 
	}

	

	/**
	 * ��ʼ�����Բ���
	 */
	private void initView(String name ,int barHeight){
		//����Ϊ���򲼾�
		setOrientation(LinearLayout.HORIZONTAL);
		//���Բ��ֿ�Ⱥ͸߶�
		setLayoutParams(new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				75
			));
		setGravity(Gravity.CENTER_VERTICAL);
		
		
//		//��������
//		TextView textview = initTextView(getContext(),name);
//		//��������
//		TextView textline = initTextLine(getContext());
//		//������״ͼ
//		//TextView textbar  = initTextBar(getContext(),barHeight);
//		EditText barView  = initBarView(getContext(),barHeight);
		addView(textView_name);
		addView(textView_line);
		//this.addView(textbar);
		addView(barView);

	}
	
	/**
	 * ĩ���豸������ֿؼ�
	 * @param context
	 * @param name
	 * @return
	 */
	private TextView initTextView(Context context,String name){
		//����һ��TextView
		TextView textview = new TextView(context);
		//�ı��ؼ���͸�
		textview.setLayoutParams(new ViewGroup.LayoutParams(
						240,
						ViewGroup.LayoutParams.MATCH_PARENT
					));
		textview.setText(name);
		textview.setTextSize(16);
		//�ı�����
		textview.setGravity(Gravity.CENTER);
		return textview;
	}
	/**
	 * �����ָ���
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
//	 * ������״��
//	 * @param context
//	 * @param barheight
//	 * @return
//	 */
//	private TextView initTextBar(Context context,int barheight){
//		TextView textview = new TextView(context);
//		textview.setLayoutParams(new ViewGroup.LayoutParams(
//				//��ʾֵΪ����ֵ��4��
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
//		//Ϊ��״����ӵ���¼�
//		textview.setOnClickListener(new OnClickListener(){
//			//Ϊ��״����ӵ����¼�(���ڼ����ĵ���¼�)
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
	 * ������״��
	 * @param context
	 * @param barheight
	 * @return
	 */
	private  EditText initBarView(Context context,int barheight){
		EditText barView = new EditText(context);
		barView.setLayoutParams(new ViewGroup.LayoutParams(
				//��ʾֵΪ����ֵ��4��
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
//					//Toast.makeText(this, "����˽���", Toast.LENGTH_LONG).show();
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

