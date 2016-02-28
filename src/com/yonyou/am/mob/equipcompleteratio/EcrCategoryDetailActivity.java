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
	
	//������������
	ActionBar detailActionBar;
	//
	LinearLayout linearLayoutbar;
	//�Զ�����״ͼ���Բ���
	CustomShowBar_H customShowBar;
	TextView goodrate;
	//ĩ����������
	TextView detailgoodrate;
	//ĩ��������̨��
	TextView detailgoodnum;
	//ĩ��������̨��
	TextView detailbadnum;
	//�м����
	DoubleTextCircle middlecircle;
	
	private String categoryName;
	private String goodRate;

	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ecr_category_detail_activity);
		//ȡ������������Ϣ
		Intent intent = getIntent();
		String categoryName = intent.getExtras().getString("categoryName");
		String goodRate = intent.getExtras().getString("goodRate");
		this.categoryName = categoryName;
		this.goodRate = goodRate;
		
		//��ʼ��������
		initializActionBar();
		linearLayoutbar = (LinearLayout)findViewById(R.id.linearlayoutbar);
		detailgoodrate = (TextView)findViewById(R.id.detailgoodrate);
		detailgoodnum = (TextView)findViewById(R.id.detailgoodnum);
		detailbadnum = (TextView)findViewById(R.id.detailbadnum);
		middlecircle = (DoubleTextCircle)findViewById(R.id.onetextcircle_m);
		middlecircle.setFirst_text(goodRate);
		
		
		
		String[] str = new String[]{"����","����","����","����","����","�洢","��Ӧ",
				"��װ","����","�÷���","����","���","��ȫ","���","����"};
		int[] height = new int[]{34,55,60,75,66,82,48,63,50,80,90,85,55,76,85};
		int[] goodnum =  new int[]{11,22,33,44,66,77,88,99,33,22,90,85,55,76,85};
		int[] badnum =  new int[]{22,33,44,55,66,77,88,99,33,22,22,85,65,76,23};
		
		//������״ͼ�������õ���¼�
		for( int i=0;i<str.length;i++){
			//��ȡ�����
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
		     //�������ü����¼�
			 edittext.setOnFocusChangeListener(new OnFocusChangeListener(){					
					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						if(hasFocus){
							linear.setBackgroundColor(getResources().getColor(R.color.mistyrose));
							//�϶���ʾ������ʾ��״ͼ�ľ���ֵ
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
		                 //��סEditText��InputType������password   
		                 int inType = edittext.getInputType(); // backup the input type  
		                 edittext.setInputType(InputType.TYPE_NULL); // disable soft input      
		                 edittext.onTouchEvent(event); // call native handler      
		                 edittext.setInputType(inType); // restore input type     
		                 edittext.setSelection(edittext.getText().length());  
		                 return true;  //������
		             }
		         }); 
			
		}
		goodrate = (TextView)findViewById(R.id.goodrate);
		goodrate.setOnClickListener(new OnClickListener(){
		
			@Override
			public void onClick(View arg0) {			
				
				//ɾ��֮ǰ���������
				linearLayoutbar.removeAllViews();
				String[] str = new String[]{"����","����","����","����","����","�洢","��Ӧ",
						"��װ"};
				int[] height = new int[]{34,55,60,75,66,82,48,63};
				for(int i=0;i<str.length;i++){
					CustomShowBar_H customBar = new CustomShowBar_H(EcrCategoryDetailActivity.this,str[i],height[i]);
					linearLayoutbar.addView(customBar);
					 final EditText edittext = customBar.getBarView();
				     final LinearLayout linear =customBar.getLinear();
				     //�������ü����¼�
					 edittext.setOnFocusChangeListener(new OnFocusChangeListener(){					
							@Override
							public void onFocusChange(View v, boolean hasFocus) {
								if(hasFocus){
									edittext.setBackgroundColor(Color.BLACK);
									linear.setBackgroundColor(Color.GRAY);
									//Toast.makeText(this, "����˽���", Toast.LENGTH_LONG).show();
								}else{
									edittext.setBackgroundColor(Color.RED);
									linear.setBackgroundColor(Color.WHITE);
								}						
							}
						});
					edittext.setOnTouchListener(new OnTouchListener() {  	             
				             public boolean onTouch(View v, MotionEvent event) {  
				                 //��סEditText��InputType������password   
				                 int inType = edittext.getInputType(); // backup the input type  
				                 edittext.setInputType(InputType.TYPE_NULL); // disable soft input      
				                 edittext.onTouchEvent(event); // call native handler      
				                 edittext.setInputType(inType); // restore input type     
				                 edittext.setSelection(edittext.getText().length());  
				                 return true;  //������
				             }
				         }); 
				}			
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
		titleText.setText(categoryName);
		detailActionBar.setCustomView(actionBarView);
			
	}
	class ClickListener implements View.OnClickListener{

		@Override
		public void onClick(View arg0) {
			
			
		}
		
		
		
	}
}
