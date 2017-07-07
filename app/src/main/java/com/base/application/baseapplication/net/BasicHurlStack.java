package com.base.application.baseapplication.net;

import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.OkUrlFactory;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * An {@link HttpStack} based on {@link OkHttpClient}.
 *
 * @Title:
 * @Description:
 * @Version:
 */
public class BasicHurlStack extends HurlStack
{
	/**
	 * OkUrlFactory
	 **/
	private OkUrlFactory mOkUrlFactory;
	/**
	 * {@link CookieManager}
	 **/
	private CookieManager mCookieManager;

	/**
	 * 构造方法
	 */
	public BasicHurlStack()
	{
		super(null,BasicHttpsTrustManager.getDefaultSSLSocketFactory());
		// 构造一个OkHttpClient实例
		mOkUrlFactory = new OkUrlFactory(new OkHttpClient());
		// 设置Cookie管理
		mCookieManager = new WebkitCookieManagerProxy(null,CookiePolicy.ACCEPT_ALL);
		CookieHandler.setDefault(mCookieManager);
	}

	@Override
	protected HttpURLConnection createConnection(URL url) throws IOException
	{
		// 使用OKHttp的HttpURLConnection
		HttpURLConnection connection = mOkUrlFactory.open(url);
		// 禁止自动302
		connection.setInstanceFollowRedirects(false);
		return connection;
	}

	/**
	 * @return {@link CookieStore}
	 */
	CookieStore getCookieStore()
	{
		return mCookieManager.getCookieStore();
	}

	/**
	 * @return {@link CookieManager}
	 */
	CookieManager getCookieManager()
	{
		return mCookieManager;
	}
}
