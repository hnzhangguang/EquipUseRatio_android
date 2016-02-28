package com.yonyou.am.mob.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.yonyou.am.mob.equipinuseratio.R;

/**
 * �Զ����ͼƬ��textview����ͨ��xml�ļ�����ͼƬ�Ĵ�С
 * 
 * @author hujieh
 * 
 */
public class TextViewWithDrawable extends TextView {
	// ��Ҫ��xml�ж�ȡ�ĸ�������ͼƬ�Ŀ�͸�
	private int leftHeight = -1;
	private int leftWidth = -1;
	private int rightHeight = -1;
	private int rightWidth = -1;
	private int topHeight = -1;
	private int topWidth = -1;
	private int bottomHeight = -1;
	private int bottomWidth = -1;
	private final static int LEFT = 0;
	private final static int TOP = 1;
	private final static int RIGHT = 2;
	private final static int BOTTOM = 3;

	public TextViewWithDrawable(Context context) {
		super(context);
	}

	public TextViewWithDrawable(Context context, AttributeSet attrs) {
		super(context, attrs);
		// superһ��Ҫ�����ǵĴ���֮ǰ�����ļ�
		init(context, attrs, 0);
	}

	public TextViewWithDrawable(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// superһ��Ҫ�����ǵĴ���֮ǰ�����ļ�
		init(context, attrs, defStyle);
	}

	/**
	 * ��ʼ����ȡ����
	 * */
	@SuppressLint("Recycle")
	private void init(Context context, AttributeSet attrs, int defStyle) {
		// TypeArray�к���������Ҫʹ�õĲ���
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TextViewWithDrawable, defStyle, 0);
		if (a != null) {
			// ��ò�������
			int count = a.getIndexCount();
			int index = 0;
			// �����������Ƚ�index��TypedArray�ж�������
			// �õ������index��Ӧ��attrs.xml�����õĲ���������R�б���õ�����
			// �����õ���������Ŀ�͸�
			for (int i = 0; i < count; i++) {
				index = a.getIndex(i);
				switch (index) {
				case R.styleable.TextViewWithDrawable_bottom_height:
					bottomHeight = a.getDimensionPixelSize(index, -1);
					break;
				case R.styleable.TextViewWithDrawable_bottom_width:
					bottomWidth = a.getDimensionPixelSize(index, -1);
					break;
				case R.styleable.TextViewWithDrawable_left_height:
					leftHeight = a.getDimensionPixelSize(index, -1);
					break;
				case R.styleable.TextViewWithDrawable_left_width:
					leftWidth = a.getDimensionPixelSize(index, -1);
					break;
				case R.styleable.TextViewWithDrawable_right_height:
					rightHeight = a.getDimensionPixelSize(index, -1);
					break;
				case R.styleable.TextViewWithDrawable_right_width:
					rightWidth = a.getDimensionPixelSize(index, -1);
					break;
				case R.styleable.TextViewWithDrawable_top_height:
					topHeight = a.getDimensionPixelSize(index, -1);
					break;
				case R.styleable.TextViewWithDrawable_top_width:
					topWidth = a.getDimensionPixelSize(index, -1);
					break;
				}
			}
			// ��ȡ���������ͼƬ�����գ���-��-��-�� ��˳�����������
			Drawable[] drawables = getCompoundDrawables();
			int dir = 0;
			// 0-left; 1-top; 2-right; 3-bottom;
			for (Drawable drawable : drawables) {
				// �趨ͼƬ��С
				setImageSize(drawable, dir++);
			}
			// ��ͼƬ�Żص�TextView��
			setCompoundDrawables(drawables[0], drawables[1], drawables[2], drawables[3]);

		}

	}

	@Override
	public void setCompoundDrawables(Drawable left, Drawable top, Drawable right, Drawable bottom) {
		setImageSize(left, LEFT);
		setImageSize(top, TOP);
		setImageSize(right, RIGHT);
		setImageSize(bottom, BOTTOM);
		super.setCompoundDrawables(left, top, right, bottom);
	}

	/**
	 * �趨ͼƬ�Ĵ�С
	 * */
	private void setImageSize(Drawable d, int dir) {
		if (d == null) {
			return;
		}

		int height = -1;
		int width = -1;
		// ���ݷ������͸߸�ֵ
		switch (dir) {
		case LEFT:
			// left
			height = leftHeight;
			width = leftWidth;
			break;
		case TOP:
			// top
			height = topHeight;
			width = topWidth;
			break;
		case RIGHT:
			// right
			height = rightHeight;
			width = rightWidth;
			break;
		case BOTTOM:
			// bottom
			height = bottomHeight;
			width = bottomWidth;
			break;
		}
		// �����ĳ������Ŀ���߸�û���趨ֵ����ȥ�趨ͼƬ��С
		if (width != -1 && height != -1) {
			d.setBounds(0, 0, width, height);
		}
	}
}
