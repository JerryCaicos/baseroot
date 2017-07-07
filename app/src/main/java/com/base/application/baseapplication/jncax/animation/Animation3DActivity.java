package com.base.application.baseapplication.jncax.animation;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.base.application.baseapplication.R;

/**
 * Created by adminchen on 16/6/24 15:06.
 */
public class Animation3DActivity extends Activity implements
		AdapterView.OnItemClickListener, View.OnClickListener
{
	//照片列表
	private ListView mPhotosList;
	private ViewGroup mContainer;
	private ImageView mImageView;

	// 照片的名字，用于显示在list中
	private static final String[] PHOTOS_NAMES = new String[]{
			"Lyon",
			"Livermore",
			"Tahoe Pier",
			"Lake Tahoe",
			"Grand Canyon",
			"Bodie"
	};

	// 资源id
	private static final int[] PHOTOS_RESOURCES = new int[]{
			R.drawable.acjn_photo1,
			R.drawable.acjn_photo2,
			R.drawable.acjn_photo3,
			R.drawable.acjn_photo4,
			R.drawable.acjn_photo5,
			R.drawable.acjn_photo6
	};

	/**
	 * Interpolator 用法
	 * Interpolator定义了动画变化的速率，在Animation 中定义了一下几种Interpolator：
	 * 1、AccelerateDecelerateInterpolator，在动画开始和结束的时候速率改变比较慢，中间的时候速率快
	 * 2、AccelerateInterpolator，在动画开始的地方速率改变比较慢，然后开始加速
	 * 3、CycleInterpolator，动画循环播放特定的次数，速率沿着正玄曲线变化
	 * 4、DecelerateInterpolator，在动画开始的地方速率改变比较慢，然后开始减速
	 * 5、LinearInterpolator，动画以均匀的速率改变
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.acjn_activity_3d_animation);

		mPhotosList = (ListView)findViewById(android.R.id.list);
		mImageView = (ImageView)findViewById(R.id.picture);
		mContainer = (ViewGroup)findViewById(R.id.container);

		// 准备ListView
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1,PHOTOS_NAMES);

		mPhotosList.setAdapter(adapter);
		mPhotosList.setOnItemClickListener(this);

		// 准备ImageView
		mImageView.setClickable(true);
		mImageView.setFocusable(true);
		mImageView.setOnClickListener(this);

		//设置需要保存缓存
		mContainer.setPersistentDrawingCache(ViewGroup.PERSISTENT_ANIMATION_CACHE);
	}

	/**
	 * Setup a new 3D rotation on the container view.
	 *
	 * @param position the item that was clicked to show a picture, or -1 to show the list
	 * @param start    the start angle at which the rotation must begin
	 * @param end      the end angle of the rotation
	 */
	private void applyRotation(int position,float start,float end)
	{
		// 计算中心点
		final float centerX = mContainer.getWidth() / 2.0f;
		final float centerY = mContainer.getHeight() / 2.0f;

		// Create a new 3D rotation with the supplied parameter
		// The animation listener is used to trigger the next animation
		final Rotate3dAnimation rotation =
				new Rotate3dAnimation(start,end,centerX,centerY,0.0f,true);
		rotation.setDuration(500);
		rotation.setFillAfter(true);
		rotation.setInterpolator(new AccelerateInterpolator());
		//设置监听
		rotation.setAnimationListener(new MyAnimationListener(position));

		mContainer.startAnimation(rotation);
	}

	public void onItemClick(AdapterView parent,View v,int position,long id)
	{
		// 设置ImageView
		mImageView.setImageResource(PHOTOS_RESOURCES[position]);
		applyRotation(position,0,90);
	}

	//点击图像时，返回listview
	public void onClick(View v)
	{
		applyRotation(-1,180,90);
	}

	/**
	 * This class listens for the end of the first half of the animation.
	 * It then posts a new action that effectively swaps the views when the container
	 * is rotated 90 degrees and thus invisible.
	 */
	private final class MyAnimationListener implements Animation.AnimationListener
	{
		private final int mPosition;

		private MyAnimationListener(int position)
		{
			mPosition = position;
		}

		public void onAnimationStart(Animation animation)
		{
		}

		//动画结束
		public void onAnimationEnd(Animation animation)
		{
			mContainer.post(new SwapViews(mPosition));
		}

		public void onAnimationRepeat(Animation animation)
		{
		}
	}

	/**
	 * This class is responsible for swapping the views and start the second
	 * half of the animation.
	 */
	private final class SwapViews implements Runnable
	{
		private final int mPosition;

		public SwapViews(int position)
		{
			mPosition = position;
		}

		public void run()
		{
			final float centerX = mContainer.getWidth() / 2.0f;
			final float centerY = mContainer.getHeight() / 2.0f;
			Rotate3dAnimation rotation;

			if(mPosition > -1)
			{
				//显示ImageView
				mPhotosList.setVisibility(View.GONE);
				mImageView.setVisibility(View.VISIBLE);
				mImageView.requestFocus();

				rotation = new Rotate3dAnimation(90,180,centerX,centerY,0.0f,false);
			}
			else
			{
				//返回listview
				mImageView.setVisibility(View.GONE);
				mPhotosList.setVisibility(View.VISIBLE);
				mPhotosList.requestFocus();

				rotation = new Rotate3dAnimation(90,0,centerX,centerY,0.0f,false);
			}

			rotation.setDuration(500);
			rotation.setFillAfter(true);
			rotation.setInterpolator(new DecelerateInterpolator());
			//开始动画
			mContainer.startAnimation(rotation);
		}
	}

}
