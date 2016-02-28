package com.yonyou.am.mob.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * ���Ի�ý����TextView����Ҫ��Ϊ�������Ч��
 * 
 * @author hujieh
 * 
 */
public class MarqueeTextView extends TextView {

	public MarqueeTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public MarqueeTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MarqueeTextView(Context context) {
		super(context);
	}

	@Override
	public boolean isFocused() {
		return true;
	}

}
