package com.base.application.baseapplication.jncax.animation;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.base.application.baseapplication.R;

/**
 * Created by JerryCaicos on 16/6/24 17:11.
 */
public class Animation3DActivity2 extends Activity
{
	//界面总布局
	private RelativeLayout layout;
	//文字页面
	private TextView mTextView;
	//图片界面
	private ImageView mImageView;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acjn_activity_3d_animation2);

		layout = (RelativeLayout)findViewById(R.id.layout);
		mTextView = (TextView)findViewById(R.id.textView);
		mImageView = (ImageView)findViewById(R.id.imageView);

		//		//点击文字界面3D变换到图片界面
		//		mTextView.setOnClickListener(new View.OnClickListener()
		//		{
		//
		//			@Override
		//			public void onClick(View arg0)
		//			{
		//				//获取布局的中心点位置，作为旋转的中心点(Camera默认旋转的中心点是在视图的左上角的，这里需要重新定位一下旋转中心点)
		//
		//				float centerX = layout.getWidth() / 2f;
		//				float centerY = layout.getHeight() / 2f;
		//				// 构建3D旋转动画对象，旋转角度为0到90度，这使得textview将会从可见变为不可见
		//				//把我文字界面看做一个平面，旋转90度后，平面的左边棱正对着我们，只能看到一条棱，相当于看不见了(从平面变成了一根线)
		//				Rotate3dAnimation animation = new Rotate3dAnimation(0,90,centerX,centerY,100.0f,
		//						true);
		//				animation.setDuration(500);//动画持续时间设为500毫秒
		//				animation.setFillAfter(true);//动画完成后保持完成的状态
		//				animation.setAnimationListener(new TextToImageRotate());
		//				layout.startAnimation(animation);
		//			}
		//
		//		});

		//点击图片界面3D变换到文字界面
		mImageView.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{
				//获取布局的中心点位置，作为旋转的中心点（(Camera默认旋转的中心点是在视图的左上角的，这里需要重新定位一下旋转中心点)）

				float centerX = layout.getWidth() / 2f;
				float centerY = layout.getHeight() / 2f;
				// 构建3D旋转动画对象，旋转角度为180到90度，这使得imageview将会从可见变为不可见  (和点击文字的旋转方向相反)
				Rotate3dAnimation animation = new Rotate3dAnimation(0,180,centerX,centerY,0.0f,
						false);
				animation.setDuration(500);//动画持续时间设为500毫秒
				animation.setFillAfter(true);//动画完成后保持完成的状态
				//				animation.setAnimationListener(new ImageToTextRotate());
				layout.startAnimation(animation);

			}

		});
	}

	/**
	 * 文字界面向图片界面转换的动画监听，用于完成文字界面旋转90度后的后续动作
	 * (文字界面旋转90度后变成不可见，然后图片界面旋转90度变成可见)
	 *
	 * @author fangfang
	 */
	class TextToImageRotate implements Animation.AnimationListener
	{

		@Override
		public void onAnimationEnd(Animation arg0)
		{
			//获取布局的中心点位置，作为选注的中心点(Camera默认旋转的中心点是在视图的左上角的，这里需要重新定位一下旋转中心点)
			float centerX = layout.getWidth() / 2f;
			float centerY = layout.getHeight() / 2f;
			//将textview隐藏
			mTextView.setVisibility(View.GONE);
			//imageview显示
			mImageView.setVisibility(View.VISIBLE);
			mImageView.requestFocus();
			// 构建3D旋转动画对象，旋转角度为90到100度，这使得ImageView将会从不可见变为可见
			//文件界面旋转90度后不可见，此时，图片界面从90度的位置继续旋转90度到180度，正好可见（从一根线变成了一个平面）
			Rotate3dAnimation animation = new Rotate3dAnimation(90,0,centerX,centerY,100.0f,true);
			animation.setDuration(500);//设置动画时间
			animation.setFillAfter(true);//动画完成后保持完成的状态
			layout.setAnimation(animation);
		}

		@Override
		public void onAnimationRepeat(Animation arg0)
		{
			// TODO Auto-generated method stub

		}

		@Override
		public void onAnimationStart(Animation arg0)
		{
			// TODO Auto-generated method stub

		}

	}

	/**
	 * 图片界面向文字界面转换的动画监听，用于完成图片界面旋转90度后的后续动作
	 * (图片界面旋转90度后变成不可见，然后文字界面旋转90度变成可见)
	 *
	 * @author fangfang
	 */
	class ImageToTextRotate implements Animation.AnimationListener
	{

		@Override
		public void onAnimationEnd(Animation arg0)
		{
			//获取布局的中心点位置，作为选注的中心点(Camera默认旋转的中心点是在视图的左上角的，这里需要重新定位一下旋转中心点)
			float centerX = layout.getWidth() / 2f;
			float centerY = layout.getHeight() / 2f;
			//将imageview隐藏
			mImageView.setVisibility(View.GONE);
			//textview显示
			mTextView.setVisibility(View.VISIBLE);
			mTextView.requestFocus();
			// 构建3D旋转动画对象，旋转角度为90到0度，这使得ImageView将会从不可见变为可见  (和点击文字的旋转方向相反)
			Rotate3dAnimation animation = new Rotate3dAnimation(90,0,centerX,centerY,100.0f,true);
			animation.setDuration(500);//设置动画时间
			animation.setFillAfter(true);//动画完成后保持完成的状态
			layout.setAnimation(animation);
		}

		@Override
		public void onAnimationRepeat(Animation arg0)
		{
			// TODO Auto-generated method stub

		}

		@Override
		public void onAnimationStart(Animation arg0)
		{
			// TODO Auto-generated method stub

		}

	}
}
