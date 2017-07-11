package com.base.application.baseapplication.custom.toast;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.base.application.baseapplication.R;

/**
 * Created by JerryCaicos on 16/1/16.
 */
public class BasicToast
{
	public static final String TAG = "BasicToast";

	/**
	 * Animations for all types of BasicToast.
	 */
	public enum Animations
	{
		FADE,
		FLYIN,
		SCALE,
		POPUP
	}

	/**
	 * Durations for all types of BasicToast.
	 */
	public static class Duration
	{
		public static final int VERY_SHORT = (1500);
		public static final int SHORT = (2000);
		public static final int MEDIUM = (2750);
		public static final int LONG = (3500);
		public static final int EXTRA_LONG = (4500);
	}

	private Animations mAnimations = Animations.FADE;
	private int mGravity = Gravity.BOTTOM | Gravity.CENTER;
	private int mDuration = Duration.SHORT;
	private int mXOffset = 0;
	private int mYOffset = 0;
	private TextView mMessageTextView;
	private View mToastView;
	private WindowManager mWindowManager;
	private WindowManager.LayoutParams mWindowManagerParams;

	/**
	 * Instantiates a new {@value #TAG}.
	 *
	 * @param context {@link android.content.Context}
	 */
	public BasicToast(Context context)
	{

		mWindowManager = (WindowManager)context.getApplicationContext().getSystemService(
				Context.WINDOW_SERVICE);

		final LayoutInflater layoutInflater = (LayoutInflater)context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mToastView = layoutInflater.inflate(R.layout.layout_basic_toast,null);
		mMessageTextView = (TextView)mToastView.findViewById(R.id.tv_basic_toast);

		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		mYOffset = dm.heightPixels >> 2;
	}

	/**
	 * Shows the {@value #TAG}. If another {@value #TAG} is showing than
	 * this one will be added to a queue and shown when the previous {@value #TAG}
	 * is dismissed.
	 */
	public void show()
	{

		mWindowManagerParams = new WindowManager.LayoutParams();

		mWindowManagerParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		mWindowManagerParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		mWindowManagerParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
				| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		mWindowManagerParams.format = PixelFormat.TRANSLUCENT;
		mWindowManagerParams.windowAnimations = getAnimation();
		mWindowManagerParams.type = WindowManager.LayoutParams.TYPE_TOAST;
		mWindowManagerParams.gravity = mGravity;
		mWindowManagerParams.x = mXOffset;
		mWindowManagerParams.y = mYOffset;

		BasicToastManager.getInstance().add(this);
	}

	/**
	 * Sets the message text of the {@value #TAG}.
	 *
	 * @param text {@link CharSequence}
	 */
	public void setText(CharSequence text)
	{
		mMessageTextView.setText(text);
	}

	/**
	 * Sets the duration that the {@value #TAG} will show.
	 *
	 * @param duration {@link Duration}
	 */
	public void setDuration(int duration)
	{

		if(duration > Duration.EXTRA_LONG)
		{
			this.mDuration = Duration.EXTRA_LONG;
		}
		else
		{
			this.mDuration = duration;
		}
	}

	/**
	 * Returns the duration of the {@value #TAG}.
	 *
	 * @return int
	 */
	public int getDuration()
	{
		return this.mDuration;
	}

	/**
	 * Sets the show/hide animations of the {@value #TAG}.
	 *
	 * @param animations {@link BasicToast.Animations}
	 */
	public void setAnimations(Animations animations)
	{
		this.mAnimations = animations;
	}

	/**
	 * Dismisses the {@value #TAG}.
	 */
	public void dismiss()
	{
		BasicToastManager.getInstance().removeBasicToast(this);
	}

	/**
	 * Returns the {@value #TAG} view.
	 *
	 * @return {@link android.view.View}
	 */
	public View getView()
	{
		return mToastView;
	}

	/**
	 * Returns true if the {@value #TAG} is showing.
	 *
	 * @return boolean
	 */
	public boolean isShowing()
	{
		return mToastView != null && mToastView.isShown();
	}

	/**
	 * Returns the window manager that the {@value #TAG} is attached to.
	 *
	 * @return {@link android.view.WindowManager}
	 */
	public WindowManager getWindowManager()
	{
		return mWindowManager;
	}

	/**
	 * Returns the window manager layout params of the {@value #TAG}.
	 *
	 * @return {@link android.view.WindowManager.LayoutParams}
	 */
	public WindowManager.LayoutParams getWindowManagerParams()
	{
		return mWindowManagerParams;
	}

	/**
	 * Private method used to return a specific animation for a animations enum
	 */
	private int getAnimation()
	{
		if(mAnimations == Animations.FLYIN)
		{
			return android.R.style.Animation_Translucent;
		}
		else if(mAnimations == Animations.SCALE)
		{
			return android.R.style.Animation_Dialog;
		}
		else if(mAnimations == Animations.POPUP)
		{
			return android.R.style.Animation_InputMethod;
		}
		else
		{
			return android.R.style.Animation_Toast;
		}
	}

	/**
	 * Returns a standard {@value #TAG}.
	 *
	 * @param context  {@link android.content.Context}
	 * @param text     {@link CharSequence}
	 * @param duration {@link BasicToast.Duration}
	 * @return {@link BasicToast}
	 */
	public static BasicToast create(Context context,CharSequence text,int duration)
	{

		BasicToast basicToast = new BasicToast(context);
		basicToast.setText(text);
		basicToast.setDuration(duration);
		return basicToast;
	}

	/**
	 * Dismisses and removes all showing/pending {@value #TAG}.
	 */
	public static void cancelAllSuperToasts()
	{
		BasicToastManager.getInstance().cancelAllBasicToasts();
	}
}
