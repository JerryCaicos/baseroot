package com.base.application.baseapplication.service;

import android.app.Application;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.base.application.baseapplication.utils.Base64;
import com.base.application.baseapplication.utils.LogUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

/**
 * Created by adminchen on 16/1/16.
 */
public class BasicSP
{
	public static String SP_NAME = "BasicPreferences";
	/**
	 * 单例
	 **/
	private static BasicSP mInstance;

	/**
	 * {@link SharedPreferences}
	 **/
	private SharedPreferences mPreferences;

	private static Application application;

	/**
	 * @return BasicSP.
	 * @Author 12075179
	 * @Date 2015-10-12
	 */
	public static BasicSP getInstance()
	{
		if(mInstance == null)
		{
			synchronized(BasicSP.class)
			{
				if(mInstance == null)
				{
					mInstance = new BasicSP();
				}
			}
		}
		return mInstance;
	}

	public static void init(Application app)
	{
		application = app;
	}

	/**
	 * BasicSP构造方法.
	 */
	private BasicSP()
	{
		mPreferences = application.getSharedPreferences(SP_NAME,0);
	}

	/**
	 * 保存boolean 类型的值到SharedPrefrences中
	 *
	 * @param key   保存时的key
	 * @param value 保存时的value
	 */
	public void putPreferencesVal(String key,boolean value)
	{
		SharedPreferences.Editor editor = mPreferences.edit();
		editor.putBoolean(key,value);
		editor.commit();
	}

	/**
	 * 获取保存在SharedPrefrences中的boolean类型的值
	 *
	 * @param key        保存时的key
	 * @param defaultVal 默认值
	 * @return
	 */
	public boolean getPreferencesVal(String key,boolean defaultVal)
	{
		return mPreferences.getBoolean(key,defaultVal);
	}

	/**
	 * 保存int 类型的值到SharedPrefrences中
	 *
	 * @param key   保存时的key
	 * @param value 保存时的value
	 */
	public void putPreferencesVal(String key,int value)
	{
		SharedPreferences.Editor editor = mPreferences.edit();
		editor.putInt(key,value);
		editor.commit();
	}

	/**
	 * 获取保存在SharedPrefrences中的int类型的值
	 *
	 * @param key        保存时的key
	 * @param defaultVal 默认值
	 * @return
	 */
	public int getPreferencesVal(String key,int defaultVal)
	{
		return mPreferences.getInt(key,defaultVal);
	}

	/**
	 * 保存long 类型的值到SharedPrefrences中
	 *
	 * @param key   保存时的key
	 * @param value 保存时的value
	 */
	public void putPreferencesVal(String key,long value)
	{
		SharedPreferences.Editor editor = mPreferences.edit();
		editor.putLong(key,value);
		editor.commit();
	}

	/**
	 * 获取保存在SharedPrefrences中的int类型的值
	 *
	 * @param key        保存时的key
	 * @param defaultVal 默认值
	 * @return
	 */
	public long getPreferencesVal(String key,long defaultVal)
	{
		return mPreferences.getLong(key,defaultVal);
	}

	/**
	 * float 类型的值到SharedPrefrences中
	 *
	 * @param key   保存时的key
	 * @param value 保存时的value
	 */
	public void putPreferencesVal(String key,float value)
	{
		SharedPreferences.Editor editor = mPreferences.edit();
		editor.putFloat(key,value);
		editor.commit();
	}

	/**
	 * 获取保存在SharedPrefrences中的float类型的值
	 *
	 * @param key        保存时的key
	 * @param defaultVal 默认值
	 * @return
	 */
	public float getPreferencesVal(String key,float defaultVal)
	{
		return mPreferences.getFloat(key,defaultVal);
	}


	/**
	 * 保存String 类型的值到SharedPrefrences中
	 *
	 * @param key   保存时的key
	 * @param value 保存时的value
	 */
	public void putPreferencesVal(String key,String value)
	{
		SharedPreferences.Editor editor = mPreferences.edit();
		editor.putString(key,value);
		editor.commit();
	}

	/**
	 * 获取保存在SharedPrefrences中的String类型的值
	 *
	 * @param key        保存时的key
	 * @param defaultVal 默认值
	 * @return
	 */
	public String getPreferencesVal(String key,String defaultVal)
	{
		return mPreferences.getString(key,defaultVal);
	}

	/**
	 * 保存object 类型的值到SharedPrefrences中
	 *
	 * @param key    保存时的key
	 * @param object 保存时的Object对象
	 */
	public void putPreferencesObj(String key,Object object)
	{
		if(null == object)
		{
			return;
		}
		// // 创建字节输出流
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = null;
		try
		{
			// 创建对象输出流，并封装字节流
			oos = new ObjectOutputStream(baos);
			// 将对象写入字节流
			oos.writeObject(object);
			// 将字节流编码成base64的字符窜
			String oAuth_Base64 = new String(Base64.encode(baos.toByteArray()));
			putPreferencesVal(key,oAuth_Base64);
		}
		catch(IOException e)
		{
			LogUtils.e("putPreferencesObj",e);
		}
		finally
		{
			if(null != baos)
			{
				try
				{
					baos.close();
				}
				catch(IOException e)
				{
					LogUtils.e("putPreferencesObj",e);
				}
			}
			if(null != oos)
			{
				try
				{
					oos.close();
				}
				catch(IOException e)
				{
					LogUtils.e("putPreferencesObj",e);
				}
			}
		}
	}

	/**
	 * 获取保存在SharedPrefrences中的Object类型的值
	 *
	 * @param key 保存时的key
	 * @return Object 返回的Object对象
	 */
	public Object getPreferencesObj(String key)
	{
		Object obj = null;
		String productBase64 = getPreferencesVal(key,"");
		if(TextUtils.isEmpty(productBase64))
		{
			return null;
		}
		// 读取字节
		byte[] base64 = Base64.decode(productBase64);
		// 封装到字节流
		ByteArrayInputStream bais = new ByteArrayInputStream(base64);
		try
		{
			// 再次封装
			ObjectInputStream bis = new ObjectInputStream(bais);
			try
			{
				// 读取对象
				obj = bis.readObject();
			}
			catch(ClassNotFoundException e)
			{
				LogUtils.e("getPreferencesObj",e);
			}
			bis.close();
			bais.close();
		}
		catch(StreamCorruptedException e)
		{
			LogUtils.e("getPreferencesObj",e);
		}
		catch(IOException e)
		{
			LogUtils.e("getPreferencesObj",e);
		}
		return obj;
	}
}
