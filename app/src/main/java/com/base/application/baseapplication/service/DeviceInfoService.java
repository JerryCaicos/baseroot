package com.base.application.baseapplication.service;

import android.content.Context;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import com.base.application.baseapplication.utils.LogUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by adminchen on 16/1/16.
 */
public class DeviceInfoService implements BasicService
{
	private static final int CONVERTSPARM = 0xFF;// Converts the specified

	private static DeviceInfoService deviceInfoInstance;
	/**
	 * 屏幕<宽度><BR>
	 * The absolute width of the display in pixels.
	 */
	public int screenWidth;

	/**
	 * 屏幕<高度><BR>
	 * The absolute height of the display in pixels.
	 */
	public int screenHeight;

	/**
	 * The screen density expressed as dots-per-inch. May be either
	 * {@link DisplayMetrics#DENSITY_LOW}, {@link DisplayMetrics#DENSITY_MEDIUM}
	 * , or {@link DisplayMetrics#DENSITY_HIGH}.
	 */
	public int densityDpi;

	/**
	 * 屏幕<密度><BR>
	 * The logical density of the display. This is a scaling factor for the
	 * Density Independent Pixel unit, where one DIP is one pixel on an
	 * approximately 160 dpi screen (for example a 240x320, 1.5"x2" screen),
	 * providing the baseline of the system's display. Thus on a 160dpi screen
	 * this density value will be 1; on a 120 dpi screen it would be .75; etc.
	 * <p>
	 * <p>
	 * This value does not exactly follow the real screen size (as given by
	 * {@link DisplayMetrics#xdpi} and {@link DisplayMetrics#ydpi}, but rather
	 * is used to scale the size of the overall UI in steps based on gross
	 * changes in the display dpi. For example, a 240x320 screen will have a
	 * density of 1 even if its width is 1.8", 1.3", etc. However, if the screen
	 * resolution is increased to 320x480 but the screen size remained 1.5"x2"
	 * then the density would be increased (probably to 1.5).
	 */
	public float density;

	/**
	 * 设备ID<BR>
	 * A 64-bit number (as a hex string) that is randomly generated when the
	 * user first sets up the device and should remain constant for the lifetime
	 * of the user's device. The value may change if a factory reset is
	 * performed on the device.
	 * <p class="note">
	 * <strong>Note:</strong> When a device has <a href= "{@docRoot}
	 * about/versions/android-4.2.html#MultipleUsers">multiple users</a>
	 * (available on certain devices running Android 4.2 or higher), each user
	 * appears as a completely separate device, so the {@code ANDROID_ID} value
	 * is unique to each user.
	 * </p>
	 */
	public String deviceId;

	/**
	 * The version number of this package, as specified by the &lt;manifest&gt;
	 * tag's AndroidManifest_versionCode attribute.
	 */
	public int versionCode;

	/**
	 * The version name of this package, as specified by the &lt;manifest&gt;
	 * tag's AndroidManifest_versionName attribute.
	 */
	public String versionName;

	private static final String KEY_IS_VOICE_ENABLED = "is_voice_enabled";
	private static final String KEY_IS_VIBRATE_ENABLED = "is_vibrate_enabled";
	/**
	 * 是否允许声音
	 **/
	private boolean isVoiceEnabled;
	/**
	 * 是否允许震动
	 **/
	private boolean isVibrateEnabled;

	/**
	 * DeviceInfoService,插件专用,其他地方禁止使用这个方法获取DeviceInfoService.
	 *
	 * @return DeviceInfoService单例
	 */
	public static DeviceInfoService getInstance()
	{
		if(deviceInfoInstance == null)
		{
			synchronized(DeviceInfoService.class)
			{
				if(deviceInfoInstance == null)
				{
					deviceInfoInstance = new DeviceInfoService();
				}
			}
		}
		return deviceInfoInstance;
	}

	@Override
	public void onApplicationCreate(Context context)
	{
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		screenWidth = dm.widthPixels;
		screenHeight = dm.heightPixels;
		densityDpi = dm.densityDpi;
		density = dm.density;
		deviceId = getTelphoneIMEI(context);
		try
		{
			versionCode = context.getPackageManager().getPackageInfo(
					context.getPackageName(),0).versionCode;
			versionName = context.getPackageManager().getPackageInfo(
					context.getPackageName(),0).versionName;
		}
		catch(PackageManager.NameNotFoundException e)
		{
			LogUtils.e(this,e);
			versionCode = -1;
			versionName = "0.00";
		}

		// 声音是否允许使用, 震动是否允许使用
		isVoiceEnabled = BasicSP.getInstance().getPreferencesVal(KEY_IS_VOICE_ENABLED,true);
		isVibrateEnabled = BasicSP.getInstance().getPreferencesVal(KEY_IS_VIBRATE_ENABLED,true);
	}

	@Override
	public void onApplicationDestory(Context context)
	{

	}

	/**
	 * 从assets文件夹下获取文件的内容
	 *
	 * @param context  context
	 * @param fileName 文件名称
	 * @return 文件的内容
	 */
	private static String getAssetsFileContent(Context context,String fileName)
	{
		String content = null;
		InputStream inputStream = null;
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(); // 输出流
		byte buf[] = new byte[1024]; // 字节缓存
		int len;
		try
		{
			inputStream = context.getAssets().open(fileName); // 打开文本
			while((len = inputStream.read(buf)) != -1)
			{ // 循环读出文本内容到输出流中
				outputStream.write(buf,0,len);
			}
			content = outputStream.toString(); // 将输出流中的内容导出为string形式
		}
		catch(IOException e)
		{
			LogUtils.e("getAssetsFileContent",e);
		}
		finally
		{
			try
			{
				// 关闭输出流和输入流
				outputStream.close();
				if(inputStream != null)
				{
					inputStream.close();
				}
			}
			catch(IOException e)
			{
				LogUtils.e("getAssetsFileContent",e);
			}
		}
		return content;
	}

	/**
	 * 从手机内存（ROM）中读取数据
	 *
	 * @param filename
	 * @return
	 * @throws Exception
	 */
	private static String readROMFile(String filename,Context context)
	{
		try
		{
			File file = new File(filename);
			if(!file.exists())
			{
				return null;
			}
			FileInputStream inputStream = new FileInputStream(file);
			byte[] buffer = new byte[1024];
			int len = 0;
			// 写入到手机内存中
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			while((len = inputStream.read(buffer)) != -1)
			{
				outputStream.write(buffer,0,len);
			}
			byte[] data = outputStream.toByteArray();// 得到二进制数据
			inputStream.close();
			outputStream.close();
			return new String(data);
		}
		catch(FileNotFoundException e)
		{
			LogUtils.e("readROMFile",e);
		}
		catch(IOException e)
		{
			LogUtils.e("readROMFile",e);
		}
		catch(Exception e)
		{
			LogUtils.e("readROMFile",e);
		}
		return null;
	}

	public static String getTelphoneIMEI(Context context)
	{
		String imei = ((TelephonyManager)context
				.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
		if(imei == null || imei.equals(""))
		{
			imei = BasicSP.getInstance().getPreferencesVal("imei","");
			if(imei.equals(""))
			{
				String now = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS")).format(new Date());
				imei = getMD5Str(now);
				BasicSP.getInstance().putPreferencesVal("imei",imei);
			}
		}
		return imei;
	}

	public static String getMD5Str(String str)
	{
		MessageDigest messageDigest = null;
		try
		{
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			messageDigest.update(str.getBytes("UTF-8"));
		}
		catch(NoSuchAlgorithmException e)
		{
			// System.exit(-1);
			LogUtils.e("getMD5Str",e);
		}
		catch(UnsupportedEncodingException e)
		{
			LogUtils.e("getMD5Str",e);
		}
		if(messageDigest == null)
		{
			return "";// 2015/10/12 by 12075179.
		}
		byte[] byteArray = messageDigest.digest();
		StringBuffer md5StrBuff = new StringBuffer();
		for(int i = 0;i < byteArray.length;i++)
		{
			if(Integer.toHexString(CONVERTSPARM & byteArray[i]).length() == 1)
			{
				md5StrBuff.append("0").append(
						Integer.toHexString(CONVERTSPARM & byteArray[i]));
			}
			else
			{
				md5StrBuff.append(Integer.toHexString(CONVERTSPARM
						& byteArray[i]));
			}
		}
		return md5StrBuff.toString();
	}

	/**
	 * 设置是否允许声音
	 *
	 * @param enable
	 */
	public void setVoiceEnabled(boolean enable)
	{
		this.isVoiceEnabled = enable;
		BasicSP.getInstance().putPreferencesVal(KEY_IS_VOICE_ENABLED,enable);
	}

	/**
	 * @return 是否允许声音 {@link #isVoiceEnabled}
	 */
	public boolean isVoiceEnabled()
	{
		return isVoiceEnabled;
	}

	/**
	 * 设置是否允许震动
	 *
	 * @param enable
	 */
	public void setVibrateEnabled(boolean enable)
	{
		this.isVibrateEnabled = enable;
		BasicSP.getInstance().putPreferencesVal(KEY_IS_VIBRATE_ENABLED,enable);
	}

	/**
	 * @return 是否允许震动 {@link #isVibrateEnabled}
	 */
	public boolean isVibrateEnabled()
	{
		return isVibrateEnabled;
	}
}
