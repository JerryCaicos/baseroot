package com.base.application.baseapplication.jncax.animation;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;

import com.base.application.baseapplication.R;


/**
 * Created by adminchen on 16/6/23 14:56.
 */
public class AnimationActivity extends Activity
{
	private Button rotateButton = null;
	private Button scaleButton = null;
	private Button alphaButton = null;
	private Button translateButton = null;
	private ImageView image = null;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acjn_activity_animation);

		rotateButton = (Button)findViewById(R.id.rotateButton);
		scaleButton = (Button)findViewById(R.id.scaleButton);
		alphaButton = (Button)findViewById(R.id.alphaButton);
		translateButton = (Button)findViewById(R.id.translateButton);
		image = (ImageView)findViewById(R.id.image);

		rotateButton.setOnClickListener(new RotateButtonListener());
		scaleButton.setOnClickListener(new ScaleButtonListener());
		alphaButton.setOnClickListener(new AlphaButtonListener());
		translateButton.setOnClickListener(
				new TranslateButtonListener());
	}

	/**
	 * Interpolator 用法
	 * Interpolator定义了动画变化的速率，在Animation 中定义了一下几种Interpolator：
	 * 1、AccelerateDecelerateInterpolator，在动画开始和结束的时候速率改变比较慢，中间的时候速率快
	 * 2、AccelerateInterpolator，在动画开始的地方速率改变比较慢，然后开始加速
	 * 3、CycleInterpolator，动画循环播放特定的次数，速率沿着正玄曲线变化
	 * 4、DecelerateInterpolator，在动画开始的地方速率改变比较慢，然后开始减速
	 * 5、LinearInterpolator，动画以均匀的速率改变
	 */
	/**
	 * 淡入淡出动画
	 */
	class AlphaButtonListener implements View.OnClickListener
	{
		public void onClick(View v)
		{
			//创建一个AnimationSet对象，参数为Boolean型，
			//true表示使用Animation的interpolator，false则是使用自己的
			AnimationSet animationSet = new AnimationSet(true);
			//创建一个AlphaAnimation对象，参数从完全的透明度，到完全的不透明
			AlphaAnimation alphaAnimation = new AlphaAnimation(0,1);
			//设置动画执行的时间
			alphaAnimation.setDuration(1000);
			//将alphaAnimation对象添加到AnimationSet当中
			animationSet.addAnimation(alphaAnimation);
			//使用ImageView的startAnimation方法执行动画
			image.startAnimation(animationSet);
		}
	}

	/**
	 * 旋转动画
	 */
	class RotateButtonListener implements View.OnClickListener
	{
		public void onClick(View v)
		{
			AnimationSet animationSet = new AnimationSet(true);
			//参数1：从哪个旋转角度开始
			//参数2：转到什么角度
			//后4个参数用于设置围绕着旋转的圆的圆心在哪里
			//参数3：确定x轴坐标的类型，有ABSOLUT绝对坐标、RELATIVE_TO_SELF相对于自身坐标、RELATIVE_TO_PARENT相对于父控件的坐标
			//参数4：x轴的值，0.5f表明是以自身这个控件的一半长度为x轴
			//参数5：确定y轴坐标的类型
			//参数6：y轴的值，0.5f表明是以自身这个控件的一半长度为x轴
			/**参数4，参数6确定动画开始的中心坐标，（0.0f，0.0f）表示左上角，（1.0f，1.0f）表示右下角**/
			RotateAnimation rotateAnimation = new RotateAnimation(0,360,
					Animation.RELATIVE_TO_SELF,0.5f,
					Animation.RELATIVE_TO_SELF,0.5f);
			rotateAnimation.setDuration(1000);
			//			rotateAnimation.setRepeatCount(2);
			rotateAnimation.setFillAfter(true);
			rotateAnimation.setInterpolator(new LinearInterpolator());
			rotateAnimation.setAnimationListener(new Animation.AnimationListener()
			{
				@Override
				public void onAnimationStart(Animation animation)
				{

				}

				@Override
				public void onAnimationEnd(Animation animation)
				{
					//					AnimationSet animationSet = new AnimationSet(true);
					//					//参数1：x轴的初始值
					//					//参数2：x轴收缩后的值
					//					//参数3：y轴的初始值
					//					//参数4：y轴收缩后的值
					//					//参数5：确定x轴坐标的类型
					//					//参数6：x轴的值，0.5f表明是以自身这个控件的一半长度为x轴
					//					//参数7：确定y轴坐标的类型
					//					//参数8：y轴的值，0.5f表明是以自身这个控件的一半长度为x轴
					//					/**参数6，参数8确定动画开始时的位置，（0.0f，0.0f）表示左上角，（1.0f，1.0f）表示右下角 **/
					//					ScaleAnimation scaleAnimation = new ScaleAnimation(
					//							0,1.0f,0,1.0f,
					//							Animation.RELATIVE_TO_SELF,1.0f,
					//							Animation.RELATIVE_TO_SELF,1.0f);
					//					scaleAnimation.setDuration(1000);
					//					animationSet.addAnimation(scaleAnimation);
					//					image.startAnimation(animationSet);
				}

				@Override
				public void onAnimationRepeat(Animation animation)
				{

				}
			});
			animationSet.addAnimation(rotateAnimation);
			image.startAnimation(animationSet);
		}
	}

	/**
	 * 缩放动画
	 */
	class ScaleButtonListener implements View.OnClickListener
	{
		public void onClick(View v)
		{
			AnimationSet animationSet = new AnimationSet(true);
			//参数1：x轴的初始值
			//参数2：x轴收缩后的值
			//参数3：y轴的初始值
			//参数4：y轴收缩后的值
			//参数5：确定x轴坐标的类型
			//参数6：x轴的值，0.5f表明是以自身这个控件的一半长度为x轴
			//参数7：确定y轴坐标的类型
			//参数8：y轴的值，0.5f表明是以自身这个控件的一半长度为x轴
			/**参数6，参数8确定动画开始时的位置，（0.0f，0.0f）表示左上角，（1.0f，1.0f）表示右下角 **/
			ScaleAnimation scaleAnimation = new ScaleAnimation(
					0,1.0f,0,1.0f,
					Animation.RELATIVE_TO_SELF,1.0f,
					Animation.RELATIVE_TO_SELF,1.0f);
			scaleAnimation.setDuration(1000);
			animationSet.addAnimation(scaleAnimation);
			image.startAnimation(animationSet);
		}
	}

	/**
	 * 移动动画
	 */
	class TranslateButtonListener implements View.OnClickListener
	{
		public void onClick(View v)
		{
			AnimationSet animationSet = new AnimationSet(true);
			//参数1～2：x轴的开始位置
			//参数3～4：x轴的结束位置
			//参数5～6：y轴的开始位置
			//参数7～8：x轴的结束位置
			TranslateAnimation translateAnimation =
					new TranslateAnimation(
							Animation.RELATIVE_TO_PARENT,0f,
							Animation.RELATIVE_TO_PARENT,1.0f,
							Animation.RELATIVE_TO_PARENT,0f,
							Animation.RELATIVE_TO_PARENT,1.0f);
			translateAnimation.setDuration(1000);
			animationSet.addAnimation(translateAnimation);
			image.startAnimation(animationSet);
		}
	}
}
