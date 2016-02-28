package com.yonyou.am.mob.commonactivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.NumberPicker;

/**
 * 自定义日期选择对话框-不包含日，只有年和月
 * 
 * @author hujieh
 * 
 */
public class AmDatePickerDialog extends DatePickerDialog {

	public AmDatePickerDialog(Context context, OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth) {
		this(context, 0, callBack, year, monthOfYear, dayOfMonth);
	}

	public AmDatePickerDialog(Context context, int theme, OnDateSetListener listener, int year, int monthOfYear,
			int dayOfMonth) {
		super(context, theme, listener, year, monthOfYear, dayOfMonth);
		DatePicker datepicker = this.getDatePicker();
		View dayView = findDayView(datepicker);
		if (dayView != null) {
			dayView.setVisibility(View.GONE);
		}
	}

	private View findDayView(ViewGroup viewGroup) {
		int count = viewGroup.getChildCount();
		View returnView = null;
		for (int i = 0; i < count; i++) {
			View tempView = viewGroup.getChildAt(i);
			if (tempView instanceof NumberPicker) {
				returnView = viewGroup.getChildAt(2);
				break;
			} else if (tempView instanceof ViewGroup) {
				returnView = findDayView((ViewGroup) tempView);
				if (returnView == null) {
					continue;
				} else {
					break;
				}
			}
		}
		return returnView;
	}

	public void onDateChanged(DatePicker view, int year, int month, int day) {
		super.onDateChanged(view, year, month, day);
		this.setTitle(year + "年" + month + "月");
	}

}
