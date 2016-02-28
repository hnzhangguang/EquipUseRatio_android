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
 * 可配置两行文本的球形控件
 * @author shizhsh1
 *
 */
public class DoubleTextCircle extends TextView{
	//首行文本
	private String first_text;
	//第二行文本
	private String second_text;
	//圆的填充颜色
	private int circle_color;
	//圆半径
	private float circle_radius;
	//字体大小
	private float textsize;
	
	public DoubleTextCircle(Context context) {
		super(context);
		
	}
	public DoubleTextCircle(Context context, AttributeSet attrs) {
		super(context, attrs);
		//解析XML定义的属性
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
		//必须执行recycle
		a.recycle();
	}

	public DoubleTextCircle(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		
		
	}
	/**
	 * 重写onDraw方法，画控件
	 * 
	 */
	protected void onDraw(Canvas canvas){
		//创建画笔
		Paint paint = new Paint();
		//去锯齿
		paint.setAntiAlias(true);
		//设置颜色
		paint.setColor(circle_color);
		//设置类型为填充
		paint.setStyle(Paint.Style.FILL);
		
		//设置一张白色画布
		canvas.drawColor(Color.WHITE);
		//画圆(横坐标，纵坐标，半径，画笔；以圆心为起点)
		canvas.drawCircle(circle_radius, circle_radius, circle_radius, paint);	
		//设置字体大小:
		paint.setTextSize(textsize);
		paint.setColor(Color.WHITE);
		//设置文字水平居中
		paint.setTextAlign(Align.CENTER);
		//文字高度
		FontMetrics textMetrics = paint.getFontMetrics();
		
//		float textHeight = textMetrics.bottom - textMetrics.top;
//		float baseLineY = textMetrics.descent+textMetrics.ascent;
		//xml没有配置第二行文字，居中显示第一行文字
		if(second_text.equals("")){
			canvas.drawText(first_text,circle_radius,circle_radius+textsize/2 , paint);
			return;
		}
		//字体宽度为width/2
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
