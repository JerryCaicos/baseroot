package com.base.application.baseapplication.jncax.refresh.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.base.application.baseapplication.R;

/**
 * 这个类封装了下拉刷新的布局
 */
public class RotateLoadingLayout extends LoadingLayout
{
	/**
	 * 旋转动画的时间
	 */
	static final int ROTATION_ANIMATION_DURATION = 3000;
	/**
	 * 动画插值
	 */
	static final Interpolator ANIMATION_INTERPOLATOR = new LinearInterpolator();
	/**
	 * Header的容器
	 */
	private LinearLayout mHeaderContainer;
	/** 箭头图片 */
	// private ImageView mArrowImageView;
	/**
	 * 状态提示TextView
	 */
	private TextView mHintTextView;
	/** 最后更新时间的TextView */
	// private TextView mHeaderTimeView;
	// /** 最后更新时间的标题 */
	// private TextView mHeaderTimeViewTitle;
	/**
	 * 旋转的动画
	 */
	private Animation mRotateAnimation;

	/**
	 * 放大缩小动画
	 **/
	private Animation mAnimation;

	private ImageView mLionHead;
	private ImageView mLionOpen;
	private RelativeLayout mLionRL;

	/**
	 * 构造方法
	 *
	 * @param context context
	 */
	public RotateLoadingLayout(Context context)
	{
		super(context);
		init(context);
	}

	/**
	 * 构造方法
	 *
	 * @param context context
	 * @param attrs   attrs
	 */
	public RotateLoadingLayout(Context context,AttributeSet attrs)
	{
		super(context,attrs);
		init(context);
	}

	/**
	 * 初始化
	 *
	 * @param context context
	 */
	private void init(Context context)
	{
		mHeaderContainer = (LinearLayout)findViewById(R.id.pull_to_refresh_header_content);
		// mArrowImageView = (ImageView)
		// findViewById(R.id.pull_to_refresh_header_arrow);
		mHintTextView = (TextView)findViewById(R.id.pull_to_refresh_header_hint_textview);
		// mHeaderTimeView = (TextView)
		// findViewById(R.id.pull_to_refresh_header_time);
		// mHeaderTimeViewTitle = (TextView)
		// findViewById(R.id.pull_to_refresh_last_update_time_text);
		mLionHead = (ImageView)findViewById(R.id.lion_head);
		mLionOpen = (ImageView)findViewById(R.id.lion_open);
		mLionRL = (RelativeLayout)findViewById(R.id.lionrl);
		// 设置刷新背景
		setRefreshImageBackground();

		// mArrowImageView.setScaleType(ScaleType.CENTER);
		// mArrowImageView.setImageResource(R.drawable.default_ptr_rotate);

		float pivotValue = 0.5f; // SUPPRESS CHECKSTYLE
		float toDegree = -720.0f; // SUPPRESS CHECKSTYLE
		mRotateAnimation = new RotateAnimation(0.0f,toDegree,
				Animation.RELATIVE_TO_SELF,pivotValue,
				Animation.RELATIVE_TO_SELF,pivotValue);
		mRotateAnimation.setFillAfter(true);
		mRotateAnimation.setInterpolator(ANIMATION_INTERPOLATOR);
		mRotateAnimation.setDuration(ROTATION_ANIMATION_DURATION);
		mRotateAnimation.setRepeatCount(Animation.INFINITE);
		mRotateAnimation.setRepeatMode(Animation.RESTART);

		mAnimation = AnimationUtils.loadAnimation(context,R.anim.lion);
	}

	@Override
	protected View createLoadingView(Context context,AttributeSet attrs)
	{
		View container = LayoutInflater.from(context).inflate(
				R.layout.pull_to_refresh_header2,null);
		return container;
	}

	@Override
	public void setLastUpdatedLabel(CharSequence label)
	{
		// 如果最后更新的时间的文本是空的话，隐藏前面的标题
		// mHeaderTimeViewTitle
		// .setVisibility(TextUtils.isEmpty(label) ? View.INVISIBLE
		// : View.VISIBLE);
		// mHeaderTimeView.setText(label);
	}

	@Override
	public int getContentSize()
	{
		int defHeight = (int)(getResources().getDisplayMetrics().density * 60);
		if(null != mHeaderContainer)
		{
			return mHeaderContainer.getHeight() != 0?mHeaderContainer
					.getHeight():defHeight;
		}

		return defHeight;
	}

	@Override
	protected void onStateChanged(State curState,State oldState)
	{
		super.onStateChanged(curState,oldState);
	}

	@Override
	protected void onReset()
	{
		resetRotation();
		// mHintTextView.setText(R.string.pull_to_refresh_header_hint_normal);
		mLionOpen.setImageResource(R.drawable.lion_open);
	}

	@Override
	protected void onReleaseToRefresh()
	{
		mHintTextView.setText(R.string.pull_to_refresh_header_hint_ready);

	}

	@Override
	protected void onPullToRefresh()
	{
		mHintTextView.setText(R.string.pull_to_refresh_header_hint_normal);
		// ScaleAnimation scaleAnimation = new ScaleAnimation(0.0f, 1.0f, 0.0f,
		// 1.0f, Animation.RELATIVE_TO_SELF, 0.5f,
		// Animation.RELATIVE_TO_SELF, 0f);
		//
		// AnimationSet animationSet = new AnimationSet(true);
		// scaleAnimation.setDuration(2000);
		// animationSet.addAnimation(scaleAnimation);
		// animationSet.setFillAfter(true);
		// mLionRL.startAnimation(animationSet);
	}

	@Override
	protected void onRefreshing()
	{
		resetRotation();
		// mArrowImageView.startAnimation(mRotateAnimation);
		// mLionRL.startAnimation(mAnimation);
		mLionHead.startAnimation(mRotateAnimation);
		mHintTextView.setText(R.string.pull_to_refresh_header_hint_loading);
		mLionOpen.setImageResource(R.drawable.lion_close);
		// mLionOpen.setImageResource(R.drawable.lion_open);

	}

	@Override
	public void onPull(float scale)
	{
		float angle = scale * 180f; // SUPPRESS CHECKSTYLE
		// mArrowImageView.setRotation(angle);
		mLionHead.setRotation(angle);
	}

	/**
	 * 重置动画
	 */
	private void resetRotation()
	{
		// mArrowImageView.clearAnimation();
		// mArrowImageView.setRotation(0);
		mLionHead.clearAnimation();
		mLionHead.setRotation(0);
		// mLionRL.clearAnimation();
	}

	private void setRefreshImageBackground()
	{
		//        SuningSP sp = SuningSP.getInstance();
		//        String refreshUrl = sp.getPreferencesVal(
		//                HomeConstants.SP_HOME_CONGIF1, "");
		//        if (!TextUtils.isEmpty(refreshUrl))
		//        {
		//            ImageLoader imageLoad = new ImageLoader(getContext());
		//            imageLoad.setBitmapCompressFormat(CompressFormat.PNG);
		//            if (SuningTabActivity.width > 0)
		//            {
		//                FrameLayout.LayoutParams ll = new FrameLayout.LayoutParams(
		//                        (int) (SuningTabActivity.width),
		//                        (int) (SuningTabActivity.width));
		//                mHeaderContainer.setLayoutParams(ll);
		//            }
		//            mLionRL.setVisibility(View.GONE);
		//            imageLoad.loadImageBackground(refreshUrl, mHeaderContainer, -1);
		//        }
		//        else
		//        {
		//            // String homeimgUrl = SuningEBuyConfig.getInstance()
		//            // .getPreferencesVal(HomeConstants.SP_HOME_CONGIF0, "");
		//            // if (TextUtils.isEmpty(homeimgUrl))
		//            // {
		//            // mRefreshImage.setBackgroundResource(R.drawable.pulltofresh);
		//            mLionRL.setVisibility(View.VISIBLE);
		//            mHeaderContainer.setBackgroundDrawable(null);
		//            // }
		//            // else
		//            // {
		//            // mRefreshImage.setBackgroundResource(0);
		//            // }
		//        }
	}
}