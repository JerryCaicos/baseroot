package com.base.application.baseapplication.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by JerryCaicos on 16/1/16.
 */
public class Md5Utils
{

	/**
	 * 一个散列方法,改变一个字符串(如URL)到一个散列适合使用作为一个磁盘文件名。
	 */
	public static String md5String(String str)
	{
		String cacheKey;
		try
		{
			final MessageDigest mDigest = MessageDigest.getInstance("MD5");
			mDigest.update(str.getBytes());
			cacheKey = bytesToHexString(mDigest.digest());
		}
		catch(NoSuchAlgorithmException e)
		{
			cacheKey = String.valueOf(str.hashCode());
		}
		return cacheKey;
	}

	public static String getMd5ByFile(File file)
	{
		String value = null;
		FileInputStream in = null;
		try
		{
			in = new FileInputStream(file);
			MappedByteBuffer byteBuffer = in.getChannel()
											.map(FileChannel.MapMode.READ_ONLY,0,file.length());
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(byteBuffer);
			BigInteger bi = new BigInteger(1,md5.digest());
			value = bi.toString(16);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(null != in)
			{
				try
				{
					in.close();
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
		}
		return value;
	}

	private static String bytesToHexString(byte[] bytes)
	{
		StringBuilder sb = new StringBuilder();
		int len = bytes.length;
		for(int i = 0;i < len;i++)
		{
			String hex = Integer.toHexString(0xFF & bytes[i]);
			if(hex.length() == 1)
			{
				sb.append('0');
			}
			sb.append(hex);
		}
		return sb.toString();
	}
}
