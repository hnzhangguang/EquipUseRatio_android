package com.yonyou.am.mob.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.yonyou.am.mob.equipinuseratio.R;

/**
 * 自定义SearchView，使用时必须实现{@link SearchViewPlusListener}接口
 * 
 * @author hujieh
 * 
 */
public class SearchViewPlus extends LinearLayout {

	/**
	 * 输入框edittext
	 */
	private EditText mEdittext;
	/**
	 * 清除按钮
	 */
	private ImageView mDeleteBtn;
	/**
	 * 搜索按钮
	 */
	private ImageView mGobtn;
	/**
	 * 搜索框默认图片
	 */
	private ImageView mHintImg;
	/**
	 * 用户输入的内容
	 */
	private CharSequence mUserQuery;
	/**
	 * 自定义searchview使用的监听，需要用户实现
	 */
	private SearchViewPlusListener mListener;

	public SearchViewPlus(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public SearchViewPlus(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public SearchViewPlus(Context context) {
		super(context);
		init();
	}

	public void setSearchViewPlusListener(SearchViewPlusListener l) {
		mListener = l;
	}

	/**
	 * 设置SearchView是否选中
	 * 
	 * @param focuse
	 */
	public void setFocuse(boolean focuse) {
		// 去掉选中效果
		// if (focuse) {
		// mLayout.setBackground(getContext().getResources().getDrawable(R.drawable.rounded_edittext_select));
		// } else {
		// mLayout.setBackground(getContext().getResources().getDrawable(R.drawable.rounded_edittext));
		// }
	}

	/**
	 * 使用该搜索框必须实现的接口
	 * 
	 * @author hujieh
	 * 
	 */
	public interface SearchViewPlusListener {
		/**
		 * 点击查询图标时触发 ，query为搜索框内容
		 */
		public void onSearch(CharSequence query);

		/**
		 * 焦点在搜索框时的按键监听(可以监听软键盘按键)
		 */
		public boolean onKeyDown(View v, int keyCode, KeyEvent event, String query);

		/**
		 * 点击关闭按钮时触发
		 */
		public void onDeleteBtnClicked();
	}

	/**
	 * 获取查询输入框的内容
	 * 
	 * @return 查询内容
	 */
	public String getQuery() {
		return mEdittext.getText() == null ? "" : mEdittext.getText().toString().trim();
	}

	/**
	 * 初始化
	 */
	private void init() {
		LayoutInflater.from(getContext()).inflate(R.layout.searcviewbar_new, this);
		mEdittext = (EditText) this.findViewById(R.id.search_et_input);
		mEdittext.addTextChangedListener(mTextWatcher);
		mEdittext.setOnKeyListener(mOnKeyListener);
		mEdittext.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				SearchViewPlus.this.setFocuse(true);
			}
		});
		mDeleteBtn = (ImageView) this.findViewById(R.id.search_delete_btn);
		mDeleteBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				mEdittext.setText("");
				SearchViewPlus.this.setFocuse(false);
				if (mListener != null) {
					mListener.onDeleteBtnClicked();
				}
			}
		});
		mGobtn = (ImageView) this.findViewById(R.id.search_go_btn);
		mGobtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				SearchViewPlus.this.setFocuse(false);
				if (mListener != null && !TextUtils.isEmpty(mUserQuery)) {
					mListener.onSearch(mUserQuery);
				}
			}
		});
		mHintImg = (ImageView) this.findViewById(R.id.search_hint_image);
		setFocuse(false);
	}

	/**
	 * 输入框输入时对按键的监听(可以监听软键盘)
	 */
	private OnKeyListener mOnKeyListener = new OnKeyListener() {
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			SearchViewPlus.this.setFocuse(false);
			if (mListener != null) {
				return mListener.onKeyDown(v, keyCode, event, getQuery());
			}
			return false;
		}
	};

	/**
	 * 输入框内的值修改时触发
	 * 
	 * @param newText
	 */
	private void onTextChanged(CharSequence newText) {
		CharSequence text = mEdittext.getText();
		mUserQuery = text;
		boolean hasText = !TextUtils.isEmpty(text);
		mHintImg.setVisibility(hasText ? View.GONE : View.VISIBLE);
		mGobtn.setVisibility(hasText ? View.VISIBLE : View.GONE);
		mDeleteBtn.setVisibility(hasText ? View.VISIBLE : View.GONE);
	}

	private TextWatcher mTextWatcher = new TextWatcher() {

		public void beforeTextChanged(CharSequence s, int start, int before, int after) {
		}

		public void onTextChanged(CharSequence s, int start, int before, int after) {
			SearchViewPlus.this.onTextChanged(s);
		}

		public void afterTextChanged(Editable s) {
		}
	};

}
