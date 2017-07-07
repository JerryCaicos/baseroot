package com.base.application.baseapplication.utils;

import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BasicUrlUtil
{
	/**
	 * url中的域名断点
	 */
	private static final char PERIOD = '.';
	private static final String SLASH = "/";
	private static final String COLON = ":";

	public static CharSequence htmlfrom(CharSequence text)
	{
		Pattern htmlflag1 = Pattern.compile("<(.*?)>");
		SpannableStringBuilder builder = new SpannableStringBuilder(text);
		Matcher matcher = htmlflag1.matcher(text);
		while(matcher.find())
		{
			builder.delete(matcher.start(),matcher.end());
			text = builder;
			matcher = htmlflag1.matcher(text);
		}
		Pattern htmlflag2 = Pattern.compile("&(.*?);");
		matcher = htmlflag2.matcher(text);
		while(matcher.find())
		{
			builder.delete(matcher.start(),matcher.end());
			text = builder;
			matcher = htmlflag2.matcher(text);
		}
		return builder.toString();
	}

	public static HashMap<String,String> getParams(String urlStr)
	{
		HashMap<String,String> paramMap = null;
		String[] urlArr = urlStr.split("\\?");
		if(urlArr.length > 1)
		{
			String paramsStr = urlArr[1];
			paramMap = parserParams(paramsStr);
		}
		return paramMap;
	}

	public static Bundle getParamsInBundle(String urlStr)
	{
		Bundle param = null;
		String[] urlArr = urlStr.split("\\?");
		if(urlArr.length > 1)
		{
			String paramsStr = urlArr[1];
			param = parserParamsInBundle(paramsStr);
		}
		return param;
	}

	public static HashMap<String,String> getParamsInMap(String urlStr)
	{
		HashMap<String,String> param = null;
		String[] urlArr = urlStr.split("\\?");
		if(urlArr.length > 1)
		{
			String paramsStr = urlArr[1];
			param = parserParams(paramsStr);
		}
		return param;
	}

	public static HashMap<String,String> parserParams(String paramsStr)
	{
		if(TextUtils.isEmpty(paramsStr))
		{
			return null;
		}
		HashMap<String,String> params = new HashMap<String,String>();
		String[] paramsArr = paramsStr.split("&");
		int paramsSize = paramsArr.length;
		String paramStr = null;
		String[] paramArr = null;
		for(int i = 0;i < paramsSize;i++)
		{
			paramStr = paramsArr[i];
			paramArr = paramStr.split("=");
			if(paramArr.length == 2)
			{
				params.put(paramArr[0],URLDecoder.decode(paramArr[1]));
			}
		}
		return params;
	}

	public static Bundle parserParamsInBundle(String paramsStr)
	{
		if(TextUtils.isEmpty(paramsStr))
		{
			return null;
		}
		Bundle params = new Bundle();
		String[] paramsArr = paramsStr.split("&");
		int paramsSize = paramsArr.length;
		String paramStr = null;
		String[] paramArr = null;
		String paramValue = null;
		for(int i = 0;i < paramsSize;i++)
		{
			paramStr = paramsArr[i];
			paramArr = paramStr.split("=");
			if(paramArr.length == 2)
			{
				try
				{
					paramValue = URLDecoder.decode(paramArr[1]);
				}
				catch(Exception e)
				{
					LogUtils.e("UrlUtil parserParamsInBundle",e);
				}
				if(!TextUtils.isEmpty(paramValue))
				{
					params.putString(paramArr[0],paramValue);
				}
			}
		}
		return params;
	}

	/**
	 * @Description:验证url是否包含http:// 或者https://头
	 */
	public static boolean verifyUrlPrefix(String url)
	{
		return TextUtils.isEmpty(url)?false:url.contains("http://")
				|| url.contains("https://");
	}

	/**
	 * @param loadUrl
	 * @return
	 * @Description 根据要加载的url获取host
	 */
	public static String obtainHost(String loadUrl)
	{
		String host = null;
		String[] url = loadUrl.split(SLASH);
		if(loadUrl.contains(COLON) && url.length > 2)
		{
			if(url[2].contains(COLON))
			{
				String[] urlTmp = url[2].split(COLON);
				url[2] = urlTmp[0];
			}
			host = url[2];
		}
		else
		{
			host = url[0];
		}
		return host;
	}

	public static String obtainDomain(String requestUrl)
	{
		String domain = "";
		String host = obtainHost(requestUrl);
		if(NetUtils.isIpAddress(host))
		{
			domain = host;
		}
		else
		{
			domain = obtainDomain2(host);
		}
		return domain;
	}

	/**
	 * @param host 不可以"/"结尾
	 * @return
	 * @Description
	 */
	public static String obtainDomain2(String host)
	{
		int start = 0;
		int nextIndex = host.indexOf(PERIOD);
		int lastIndex = host.lastIndexOf(PERIOD);
		while(nextIndex < lastIndex)
		{
			start = nextIndex + 1;
			nextIndex = host.indexOf(PERIOD,start);
		}
		if(start > 0)
		{
			return "." + host.substring(start);
		}
		return host;
	}

	/**
	 * @param host 主机
	 * @return 主机是否是IP地址
	 */
	public static boolean isIPAddress(final String host)
	{
		// 非空判断
		if(TextUtils.isEmpty(host))
		{
			return false;
		}
		// 正则匹配
		String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
				+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
				+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
				+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
		return host.matches(regex);
	}

	public static String getSchemePrefix(String spec)
	{
		int colon = spec.indexOf(':');

		if(colon < 1)
		{
			return null;
		}

		for(int i = 0;i < colon;i++)
		{
			char c = spec.charAt(i);
			if(!isValidSchemeChar(i,c))
			{
				return null;
			}
		}

		return spec.substring(0,colon).toLowerCase(Locale.US);
	}

	public static boolean isValidSchemeChar(int index,char c)
	{
		if((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))
		{
			return true;
		}
		if(index > 0 && ((c >= '0' && c <= '9') || c == '+' || c == '-' || c == '.'))
		{
			return true;
		}
		return false;
	}
}
