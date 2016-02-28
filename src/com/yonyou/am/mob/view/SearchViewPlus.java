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
 * �Զ���SearchView��ʹ��ʱ����ʵ��{@link SearchViewPlusListener}�ӿ�
 * 
 * @author hujieh
 * 
 */
public class SearchViewPlus extends LinearLayout {

	/**
	 * �����edittext
	 */
	private EditText mEdittext;
	/**
	 * �����ť
	 */
	private ImageView mDeleteBtn;
	/**
	 * ������ť
	 */
	private ImageView mGobtn;
	/**
	 * ������Ĭ��ͼƬ
	 */
	private ImageView mHintImg;
	/**
	 * �û����������
	 */
	private CharSequence mUserQuery;
	/**
	 * �Զ���searchviewʹ�õļ�������Ҫ�û�ʵ��
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
	 * ����SearchView�Ƿ�ѡ��
	 * 
	 * @param focuse
	 */
	public void setFocuse(boolean focuse) {
		// ȥ��ѡ��Ч��
		// if (focuse) {
		// mLayout.setBackground(getContext().getResources().getDrawable(R.drawable.rounded_edittext_select));
		// } else {
		// mLayout.setBackground(getContext().getResources().getDrawable(R.drawable.rounded_edittext));
		// }
	}

	/**
	 * ʹ�ø����������ʵ�ֵĽӿ�
	 * 
	 * @author hujieh
	 * 
	 */
	public interface SearchViewPlusListener {
		/**
		 * �����ѯͼ��ʱ���� ��queryΪ����������
		 */
		public void onSearch(CharSequence query);

		/**
		 * ������������ʱ�İ�������(���Լ�������̰���)
		 */
		public boolean onKeyDown(View v, int keyCode, KeyEvent event, String query);

		/**
		 * ����رհ�ťʱ����
		 */
		public void onDeleteBtnClicked();
	}

	/**
	 * ��ȡ��ѯ����������
	 * 
	 * @return ��ѯ����
	 */
	public String getQuery() {
		return mEdittext.getText() == null ? "" : mEdittext.getText().toString().trim();
	}

	/**
	 * ��ʼ��
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
	 * ���������ʱ�԰����ļ���(���Լ��������)
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
	 * ������ڵ�ֵ�޸�ʱ����
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
