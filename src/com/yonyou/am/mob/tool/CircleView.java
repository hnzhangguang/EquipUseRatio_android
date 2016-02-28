package com.yonyou.am.mob.tool;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 
 * @author Administrator
 *
 */
public class CircleView extends TextView {

	public void setColor(int color) {
		this.color = color;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	public int getColor() {
		return color;
	}

	private int color;
	private float radius;
	private Paint paint;

	public CircleView(Context context) {
		this(context, null);
	}

	public CircleView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CircleView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		int[] aa = new int[] { 3, 4 };
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs, aa, defStyle, 0);
		int n = a.getIndexCount();
		for (int i = 0; i < n; i++) {
			int attr = a.getIndex(i);
			switch (attr) {
			case 1:
				radius = a.getDimension(attr, 20);
				break;
			case 2:
				color = a.getColor(attr, Color.RED);
				break;
			}
		}
		a.recycle();

		paint = new Paint();
	}

	@Override
	protected void onDraw(Canvas canvas) {

		paint.setColor(color);
		paint.setAntiAlias(true);
		canvas.drawCircle(radius, radius, radius, paint);

		super.onDraw(canvas);

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		int width = 0;
		int height = 0;

		if (widthMode == MeasureSpec.EXACTLY)
			width = widthSize;
		else {
			width = (int) (getPaddingLeft() + 2 * radius + getPaddingRight());
		}
		if (heightMode == MeasureSpec.EXACTLY)
			height = heightSize;
		else {
			height = (int) (getPaddingTop() + 2 * radius + getPaddingBottom());
		}
		setMeasuredDimension(width, height);
	}
}
