package com.yonyou.am.mob.tool;

import com.yonyou.am.mob.equipinuseratio.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.TextView;
/**
 * �����������ı������οؼ�
 * @author shizhsh1
 *
 */
public class DoubleTextCircle extends TextView{
	//�����ı�
	private String first_text;
	//�ڶ����ı�
	private String second_text;
	//Բ�������ɫ
	private int circle_color;
	//Բ�뾶
	private float circle_radius;
	//�����С
	private float textsize;
	
	public DoubleTextCircle(Context context) {
		super(context);
		
	}
	public DoubleTextCircle(Context context, AttributeSet attrs) {
		super(context, attrs);
		//����XML���������
		TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.DoubleTextCircle);
		int n = a.getIndexCount();
		for(int i=0;i<n;i++){
			int attr = a.getIndex(i);
			switch(attr){
			case R.styleable.DoubleTextCircle_circle_color:
				circle_color = a.getColor(attr, 0);
				break;
			case R.styleable.DoubleTextCircle_circle_radius:
				circle_radius = a.getDimension(attr, 0);
				break;
			case R.styleable.DoubleTextCircle_first_text:
				first_text = a.getString(attr);
				break;
			case R.styleable.DoubleTextCircle_second_text:
				if(a.getString(attr).equals(null)||a.getString(attr).equals("")){
					second_text="";
				}else{
					second_text = a.getString(attr);
				}
				break;
			case R.styleable.DoubleTextCircle_textsize:
				textsize = a.getDimension(attr, 0);
//				textsize = a.getDimensionPixelOffset(attr, 0);
//   			textsize = a.getDimensionPixelOffset(attr, 0);
			}
			
		}
		//����ִ��recycle
		a.recycle();
	}

	public DoubleTextCircle(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		
		
	}
	/**
	 * ��дonDraw���������ؼ�
	 * 
	 */
	protected void onDraw(Canvas canvas){
		//��������
		Paint paint = new Paint();
		//ȥ���
		paint.setAntiAlias(true);
		//������ɫ
		paint.setColor(circle_color);
		//��������Ϊ���
		paint.setStyle(Paint.Style.FILL);
		
		//����һ�Ű�ɫ����
		canvas.drawColor(Color.WHITE);
		//��Բ(�����꣬�����꣬�뾶�����ʣ���Բ��Ϊ���)
		canvas.drawCircle(circle_radius, circle_radius, circle_radius, paint);	
		//���������С:
		paint.setTextSize(textsize);
		paint.setColor(Color.WHITE);
		//��������ˮƽ����
		paint.setTextAlign(Align.CENTER);
		//���ָ߶�
		FontMetrics textMetrics = paint.getFontMetrics();
		
//		float textHeight = textMetrics.bottom - textMetrics.top;
//		float baseLineY = textMetrics.descent+textMetrics.ascent;
		//xmlû�����õڶ������֣�������ʾ��һ������
		if(second_text.equals("")){
			canvas.drawText(first_text,circle_radius,circle_radius+textsize/2 , paint);
			return;
		}
		//������Ϊwidth/2
		canvas.drawText(first_text,circle_radius,circle_radius , paint);
		
		paint.setTextSize(textsize);
		paint.setColor(Color.WHITE);
		canvas.drawText(second_text,circle_radius ,circle_radius+textsize, paint);
		
	}
	
	protected void onMeasure(int widthMeasureSpec , int heightMeasureSpec){
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		
		int width;
		int height;
		
		if (widthMode == MeasureSpec.EXACTLY)
			width = widthSize;
		else {
			width = (int) (getPaddingLeft() + 2 * circle_radius + getPaddingRight());
		}
		if (heightMode == MeasureSpec.EXACTLY)
			height = heightSize;
		else {
			height = (int) (getPaddingTop() + 2 * circle_radius + getPaddingBottom());
		}
		setMeasuredDimension(width, height);
		
	}

	
	public String getFirst_text() {
		return first_text;
	}
	public void setFirst_text(String first_text) {
		this.first_text = first_text;
		invalidate();
	}
	public String getSecond_text() {
		return second_text;
	}
	public void setSecond_text(String second_text) {
		this.second_text = second_text;
	}
	public int getCircle_color() {
		return circle_color;
	}
	public void setCircle_color(int circle_color) {
		this.circle_color = circle_color;
	}
	public float getCircle_radius() {
		return circle_radius;
	}
	public void setCircle_radius(int circle_radius) {
		this.circle_radius = circle_radius;
	}

}
