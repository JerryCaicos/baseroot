package com.base.application.baseapplication.jncax.qrcode;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.base.application.baseapplication.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.Hashtable;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;

/**
 * Created by adminchen on 16/2/2 09:41.
 */
public class QRCodeActivity extends Activity
{
	private EditText mQRCodeEdit;

	private TextView mQRCodeBtn;

	private ImageView mQRCodeImage;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acjn_activity_qr_code);
		initActivity();
	}

	private void initActivity()
	{
		mQRCodeEdit = (EditText)findViewById(R.id.qr_code_edit_text);
		mQRCodeBtn = (TextView)findViewById(R.id.qr_code_btn);
		mQRCodeImage = (ImageView)findViewById(R.id.qr_code_image_view);

		mQRCodeBtn.setOnClickListener(clickListener);
	}

	private View.OnClickListener clickListener = new View.OnClickListener()
	{
		@Override
		public void onClick(View view)
		{
			switch(view.getId())
			{
				case R.id.qr_code_btn:
					disposeBtnCreateQRCodeClickListener();
					break;
			}
		}
	};

	private void disposeBtnCreateQRCodeClickListener()
	{
		String code = mQRCodeEdit.getText().toString();
		if(TextUtils.isEmpty(code))
		{
			Toast.makeText(QRCodeActivity.this,"请输入文本",Toast.LENGTH_LONG).show();
			return;
		}
		DisplayMetrics dm = getResources().getDisplayMetrics();
		int width = dm.widthPixels;
		int size = 396 * width / (720);
		Bitmap bitmap = null;
		try
		{
			Hashtable<EncodeHintType,String> hints = new Hashtable<EncodeHintType,String>();
			hints.put(EncodeHintType.CHARACTER_SET,"utf-8");
			// 图像数据转换，使用了矩阵转换
			BitMatrix bitMatrix = new QRCodeWriter().encode(code,BarcodeFormat.QR_CODE,
					size,size,hints);

			int[] pixels = new int[size * size];
			// 下面这里按照二维码的算法，逐个生成二维码的图片，
			// 两个for循环是图片横列扫描的结果
			for(int y = 0;y < size;y++)
			{
				for(int x = 0;x < size;x++)
				{
					if(bitMatrix.get(x,y))
					{
						pixels[y * size + x] = 0xff000000;
					}
					else
					{
						pixels[y * size + x] = 0x00FFFFFF;
					}
				}
			}
			// 生成二维码图片的格式，使用ARGB_8888
			bitmap = Bitmap.createBitmap(size,size,Bitmap.Config.ARGB_8888);
			bitmap.setPixels(pixels,0,size,0,0,size,size);

			if(bitmap != null)
			{
				mQRCodeImage.setImageBitmap(bitmap);
			}
		}
		catch(WriterException e)
		{
		}
	}


	Observer<String> observer = new Observer<String>()
    {
        @Override
        public void onCompleted()
        {

        }

        @Override
        public void onError(Throwable e)
        {

        }

        @Override
        public void onNext(String s)
        {

        }
    };

    Subscriber<Drawable> subscriber = new Subscriber<Drawable>()
    {
        @Override
        public void onCompleted()
        {

        }

        @Override
        public void onError(Throwable e)
        {

        }

        @Override
        public void onNext(Drawable drawable)
        {

        }
    };

    Observable observable = Observable.create(new Observable.OnSubscribe<Drawable>()
    {
        @Override
        public void call(Subscriber<? super Drawable> subscriber)
        {
//            subscriber.onNext(getDrawable());
        }

    });

}
