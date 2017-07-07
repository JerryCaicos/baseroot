package com.base.application.baseapplication.custom.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.base.application.baseapplication.R;
import com.base.application.baseapplication.utils.LogUtils;

/**
 * 通用对话框, 样式如下:
 * <PRE>
 * ______________________
 * |        title         |
 * |                      |
 * |       message        |
 * |                      |
 * |  cancel        OK    |
 * |______________________|
 * </PRE>
 *
 * @Title:
 * @Description:
 * @Author:12075179
 * @Since:2015-9-10
 * @Version:
 */
public class CustomDialog extends BasicDialogFragment
{
	/**
	 * fragment的名字.
	 */
	private static final String NAME = "CustomDialog";

	/**
	 * 对话框标题
	 **/
	protected static final String KEY_TITLE = "c_title";
	/**
	 * 对话框内容
	 **/
	protected static final String KEY_MESSAGE = "c_message";
	/**
	 * 对话框【左边】按钮文字
	 **/
	protected static final String KEY_LEFT_BTN_TEXT = "c_left_btn_text";
	/**
	 * 对话框【右边】按钮文字
	 **/
	protected static final String KEY_RIGHT_BTN_TEXT = "c_right_btn_text";
	/**
	 * 是否能够取消
	 **/
	protected static final String KEY_CANCELABLE = "cancelable";

	/**
	 * 左边的按钮
	 **/
	private Button mLeftButton;
	/**
	 * 右边的按钮
	 **/
	private Button mRightButton;

	private View.OnClickListener mLeftBtnClickListener;
	private View.OnClickListener mRightBtnClickListener;

	private DialogInterface.OnCancelListener mOnCancelListener;
	private DialogInterface.OnDismissListener mOnDismissListener;
	private DialogInterface.OnShowListener mOnShowListener;

	/**
	 * 创建CustomDialog, 使用{@link CustomDialog.Builder}
	 */
	protected CustomDialog()
	{
	}

	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState)
	{
		View containerView = inflater.inflate(R.layout.dialog_custom,container,false);

		// 如果有信息,显示加载信息
		Bundle bundle = getArguments();
		if(bundle == null)
		{
			// 没有信息直接返回
			return containerView;
		}
		// 1,标题
		CharSequence title = bundle.getCharSequence(KEY_TITLE);
		TextView titleView = (TextView)containerView.findViewById(R.id.tv_cdialog_title);
		if(TextUtils.isEmpty(title))
		{
			titleView.setVisibility(View.GONE);
		}
		else
		{
			titleView.setVisibility(View.VISIBLE);
			titleView.setText(title);
		}

		// 2,内容
		CharSequence message = bundle.getCharSequence(KEY_MESSAGE);
		TextView contentView = (TextView)containerView.findViewById(R.id.tv_cdialog_content);
		if(TextUtils.isEmpty(message))
		{
			contentView.setVisibility(View.GONE);
		}
		else
		{
			contentView.setVisibility(View.VISIBLE);
			contentView.setText(message);
		}

		// 3,左边的按钮
		CharSequence leftBtnText = bundle.getCharSequence(KEY_LEFT_BTN_TEXT);
		mLeftButton = (Button)containerView.findViewById(R.id.btn_cdialog_left);
		if(!TextUtils.isEmpty(leftBtnText))
		{
			mLeftButton.setVisibility(View.VISIBLE);
			// 3.1 设置按钮文字
			mLeftButton.setText(leftBtnText);
			// 3.1 设置点击事件
			mLeftButton.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					if(mLeftBtnClickListener != null)
					{
						mLeftBtnClickListener.onClick(v);
					}
					dismissAllowingStateLoss();
				}
			});
		}
		else
		{
			if(mLeftBtnClickListener == null)
			{
				// 3.2 如果没有点击事件 && 没有设置按钮文字, 隐藏按钮
				mLeftButton.setVisibility(View.GONE);
			}
			else
			{
				mLeftButton.setVisibility(View.VISIBLE);
				// 3.3 如果没有设置按钮文字,使用默认按钮文字
				// 3.3 设置点击事件
				mLeftButton.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						if(mLeftBtnClickListener != null)
						{
							mLeftBtnClickListener.onClick(v);
						}
						dismissAllowingStateLoss();
					}
				});
			}
		}

		// 4,右边的按钮
		CharSequence rightBtnText = bundle.getCharSequence(KEY_RIGHT_BTN_TEXT);
		mRightButton = (Button)containerView.findViewById(R.id.btn_cdialog_right);
		if(!TextUtils.isEmpty(rightBtnText))
		{
			mRightButton.setVisibility(View.VISIBLE);
			// 4.1 设置按钮文字
			mRightButton.setText(rightBtnText);
			// 4.1 设置点击事件
			mRightButton.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					if(mRightBtnClickListener != null)
					{
						mRightBtnClickListener.onClick(v);
					}
					dismissAllowingStateLoss();
				}
			});
		}
		else
		{
			if(mRightBtnClickListener == null)
			{
				// 4.2 如果没有点击事件 && 没有设置按钮文字, 隐藏按钮
				mRightButton.setVisibility(View.GONE);
			}
			else
			{
				mRightButton.setVisibility(View.VISIBLE);
				// 4.3 如果没有设置按钮文字,使用默认按钮文字
				// 4.3 设置点击事件
				mRightButton.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						if(mRightBtnClickListener != null)
						{
							mRightBtnClickListener.onClick(v);
						}
						dismissAllowingStateLoss();
					}
				});
			}
		}

		// 5,是否可以取消, 默认可以取消
		setCancelable(bundle.getBoolean(KEY_CANCELABLE,true));

		return containerView;
	}

	public void setLeftBtnClickListener(final View.OnClickListener listener)
	{
		mLeftBtnClickListener = listener;
	}

	public void setRightBtnClickListener(final View.OnClickListener listener)
	{
		mRightBtnClickListener = listener;
	}

	public void setOnCancelListener(DialogInterface.OnCancelListener mOnCancelListener)
	{
		this.mOnCancelListener = mOnCancelListener;
	}

	public void setOnDismissListener(DialogInterface.OnDismissListener mOnDismissListener)
	{
		this.mOnDismissListener = mOnDismissListener;
	}

	public void setOnShowListener(DialogInterface.OnShowListener mOnShowListener)
	{
		this.mOnShowListener = mOnShowListener;
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		// 设置对话框的样式
		setStyle(STYLE_NO_FRAME,R.style.customdialog);
	}

	@Override
	public void onCancel(DialogInterface dialog)
	{
		super.onCancel(dialog);
		if(mOnCancelListener != null)
		{
			mOnCancelListener.onCancel(dialog);
		}
	}

	@Override
	public void onDismiss(DialogInterface dialog)
	{
		super.onDismiss(dialog);
		if(mOnDismissListener != null)
		{
			mOnDismissListener.onDismiss(dialog);
		}
	}

	@Override
	public void onStart()
	{
		// 在onStart()里面调用,保证dialog已经完成初始化.

		// 1, 设置对话框的大小
		Window window = getDialog().getWindow();
		WindowManager wm = window.getWindowManager();
		Point windowSize = new Point();
		wm.getDefaultDisplay().getSize(windowSize);
		// 设置宽度为屏幕宽度88%
		window.getAttributes().width = (int)(windowSize.x * 0.88f);
		window.getAttributes().height = ViewGroup.LayoutParams.WRAP_CONTENT;
		window.setGravity(Gravity.CENTER);

		super.onStart();

		// 2, 对话显示之前,设置一下按钮分割线的显示状态
		View buttonDivider = getView().findViewById(R.id.view_cdialog_btn_divider);
		if(mLeftButton != null && mRightButton != null && buttonDivider != null)
		{
			if(mLeftButton.getVisibility() == View.VISIBLE
					&& mRightButton.getVisibility() == View.VISIBLE)
			{
				buttonDivider.setVisibility(View.VISIBLE);
			}
			else
			{
				buttonDivider.setVisibility(View.GONE);
			}
		}

		// 3, 设置对话的事件
		getDialog().setOnShowListener(mOnShowListener);
	}

	@Override
	public String getName()
	{
		return NAME;
	}

	/**
	 * Example:
	 * <PRE>
	 * View.OnClickListener leftClickListener = new View.OnClickListener() {
	 * public void onClick(View v) {
	 * // handle left button click.
	 * }
	 * };
	 * <p/>
	 * View.OnClickListener rightClickListener = new View.OnClickListener() {
	 * public void onClick(View v) {
	 * // handle right button click.
	 * }
	 * };
	 * <p/>
	 * CustomDialog.Builder builder = new CustomDialog.Builder()
	 * .setTitle("title")
	 * .setMessage("message")
	 * .setLeftButton("cancel", leftClickListener)
	 * .setsetRightButton("OK", rightClickListener);
	 * builder.show(fragmentManager);
	 * </PRE>
	 *
	 * @Title:
	 * @Description:
	 * @Author:12075179
	 * @Since:2015-9-10
	 * @Version:
	 */
	public static class Builder
	{

		/**
		 * 存放数据的容器
		 **/
		private Bundle mmBundle;

		private View.OnClickListener mmLeftBtnClickListener;
		private View.OnClickListener mmRightBtnClickListener;

		private DialogInterface.OnCancelListener mmOnCancelListener;
		private DialogInterface.OnDismissListener mmOnDismissListener;
		private DialogInterface.OnShowListener mmOnShowListener;

		/**
		 * Constructor using a context for this builder and the {@link CustomDialog} it creates.
		 */
		public Builder()
		{
			mmBundle = new Bundle();
		}

		/**
		 * Set the title displayed in the {@link Dialog}.<BR>
		 * <p/>
		 * 如果想隐藏标题, 这个方法不调用即可
		 *
		 * @return This Builder object to allow for chaining of calls to set methods
		 */
		public Builder setTitle(CharSequence title)
		{
			mmBundle.putCharSequence(KEY_TITLE,title);
			return this;
		}


		/**
		 * Set the message to display.<BR>
		 * <p/>
		 * 如果想隐藏内容信息, 这个方法不调用即可
		 *
		 * @return This Builder object to allow for chaining of calls to set methods
		 */
		public Builder setMessage(CharSequence message)
		{
			mmBundle.putCharSequence(KEY_MESSAGE,message);
			return this;
		}


		/**
		 * Set a listener to be invoked when the Left button of the dialog is pressed.<BR>
		 * <p/>
		 * 如果想隐藏左边的按钮, 这个方法不调用即可; <BR>
		 * <LI>只设置text,不设置listener, 点击按钮, 对话框消失</LI>
		 * <LI>只设置listener,不设置text, 按钮显示默认文字“取消”</LI>
		 *
		 * @param leftBtnText The text to display in the Left button
		 * @param listener    The {@link View.OnClickListener} to use.
		 * @return This Builder object to allow for chaining of calls to set methods
		 */
		public Builder setLeftButton(CharSequence leftBtnText,final View.OnClickListener listener)
		{
			mmBundle.putCharSequence(KEY_LEFT_BTN_TEXT,leftBtnText);
			mmLeftBtnClickListener = listener;
			return this;
		}


		/**
		 * Set a listener to be invoked when the Right button of the dialog is pressed.<BR>
		 * <p/>
		 * 如果想隐藏左边的按钮, 这个方法不调用即可; <BR>
		 * <LI>只设置text,不设置listener, 点击按钮, 对话框消失</LI>
		 * <LI>只设置listener,不设置text, 按钮显示默认文字“确认”</LI>
		 *
		 * @param rightBtnText The text to display in the Right button
		 * @param listener     The {@link View.OnClickListener} to use.
		 * @return This Builder object to allow for chaining of calls to set methods
		 */
		public Builder setRightButton(CharSequence rightBtnText,final View.OnClickListener listener)
		{
			mmBundle.putCharSequence(KEY_RIGHT_BTN_TEXT,rightBtnText);
			mmRightBtnClickListener = listener;
			return this;
		}

		/**
		 * Sets whether the dialog is cancelable or not.  Default is true.
		 *
		 * @return This Builder object to allow for chaining of calls to set methods
		 */
		public Builder setCancelable(boolean cancelable)
		{
			mmBundle.putBoolean(KEY_CANCELABLE,cancelable);
			return this;
		}

		/**
		 * Sets the callback that will be called if the dialog is canceled.
		 * <p/>
		 * <p>Even in a cancelable dialog, the dialog may be dismissed for reasons other than
		 * being canceled or one of the supplied choices being selected.
		 * If you are interested in listening for all cases where the dialog is dismissed
		 * and not just when it is canceled, see
		 * {@link #setOnDismissListener(DialogInterface.OnDismissListener) setOnDismissListener}.</p>
		 *
		 * @return This Builder object to allow for chaining of calls to set methods
		 * @see #setCancelable(boolean)
		 * @see #setOnDismissListener(DialogInterface.OnDismissListener)
		 */
		public Builder setOnCancelListener(DialogInterface.OnCancelListener onCancelListener)
		{
			mmOnCancelListener = onCancelListener;
			return this;
		}

		/**
		 * Sets the callback that will be called when the dialog is dismissed for any reason.
		 *
		 * @return This Builder object to allow for chaining of calls to set methods
		 */
		public Builder setOnDismissListener(DialogInterface.OnDismissListener onDismissListener)
		{
			mmOnDismissListener = onDismissListener;
			return this;
		}

		/**
		 * Sets the callback that will be called when the dialog is showed for any reason.
		 *
		 * @return This Builder object to allow for chaining of calls to set methods
		 */
		public Builder setOnShowListener(DialogInterface.OnShowListener onShowListener)
		{
			mmOnShowListener = onShowListener;
			return this;
		}


		/**
		 * Creates a {@link CustomDialog} with the arguments supplied to this builder.
		 */
		public CustomDialog create()
		{
			final CustomDialog dialog = new CustomDialog();
			// 1,设置显示内容
			dialog.setArguments(mmBundle);
			// 2,设置操作事件
			dialog.setLeftBtnClickListener(mmLeftBtnClickListener);
			dialog.setRightBtnClickListener(mmRightBtnClickListener);

			dialog.setOnCancelListener(mmOnCancelListener);
			dialog.setOnDismissListener(mmOnDismissListener);
			dialog.setOnShowListener(mmOnShowListener);

			return dialog;
		}

		/**
		 * Creates a {@link CustomDialog} with the arguments supplied to this builder and
		 * {@link DialogFragment#show(FragmentManager,String)}'s the dialog.
		 */
		public void show(FragmentManager fm)
		{
			if(fm == null)
			{
				LogUtils.e(NAME,"show error : fragment manager is null.");
				return;
			}
			CustomDialog dialog = create();
			LogUtils.d(NAME,"show custom dialog.");
			dialog.show(fm,dialog.getName());
		}
	}
}
