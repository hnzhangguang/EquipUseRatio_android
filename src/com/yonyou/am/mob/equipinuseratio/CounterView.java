package com.yonyou.am.mob.equipinuseratio;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * 功能说明：自定义控件的实现 之 【计数器的实现】
 * 
 * 首先我们在CounterView的构造函数中初始化了一些数据，并给这个View的本身注册了点击事件，这样当CounterView被点击的时候
 * ，onClick
 * ()方法就会得到调用。而onClick()方法中的逻辑就更加简单了，只是对mCount这个计数器加1，然后调用invalidate()方法。
 * 
 * @author zhangg
 * @2015-12-3
 */
public class CounterView extends View implements OnClickListener {

	private Paint mPaint;
	private Rect mBounds;
	private int mCount;

	public CounterView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mBounds = new Rect();
		setOnClickListener(this);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// 绘制矩形
		mPaint.setColor(Color.BLUE);
		canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);
		// 绘制字体
		mPaint.setColor(Color.YELLOW);
		mPaint.setTextSize(30);
		// 要显示的文字
		String text = String.valueOf(mCount);
		mPaint.getTextBounds(text, 0, text.length(), mBounds);
		float textWidth = mBounds.width();
		float textHeight = mBounds.height();
		canvas.drawText(text, getWidth() / 2 - textWidth / 2, getHeight() / 2 + textHeight / 2, mPaint);
	}

	@Override
	public void onClick(View v) {
		mCount++;
		invalidate();
	}

}