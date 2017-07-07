package com.base.application.baseapplication.net;

import com.base.application.baseapplication.utils.LogUtils;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * HttpsTrustManager
 *
 * @Title:
 * @Description:
 * @Version:
 */
public class BasicHttpsTrustManager implements X509TrustManager
{
	private static final String TAG = "BasicHttpsTrustManager";
	private static TrustManager[] trustManagers;
	private static final X509Certificate[] _AcceptedIssuers = new X509Certificate[]{};
	private static SSLSocketFactory defaultSSLSocketFactory = null;

	@Override
	public void checkClientTrusted(X509Certificate[] x509Certificates,String s)
			throws CertificateException
	{
		LogUtils.d(TAG,"checkClientTrusted: " + s);
	}

	@Override
	public void checkServerTrusted(X509Certificate[] x509Certificates,String s)
			throws CertificateException
	{
		LogUtils.d(TAG,"checkServerTrusted: " + s);
	}

	@Override
	public X509Certificate[] getAcceptedIssuers()
	{
		return _AcceptedIssuers;
	}

	/**
	 * @return 一个不验证证书的SSLSocketFactory
	 * @Description:
	 */
	public static SSLSocketFactory getDefaultSSLSocketFactory()
	{
		if(defaultSSLSocketFactory == null)
		{
			// 信任所有证书
			SSLContext context = null;
			if(trustManagers == null)
			{
				trustManagers = new TrustManager[]{new BasicHttpsTrustManager()};
			}
			try
			{
				context = SSLContext.getInstance("TLS");
				context.init(null,trustManagers,new SecureRandom());
			}
			catch(NoSuchAlgorithmException e)
			{
				LogUtils.e(TAG,e);
			}
			catch(KeyManagementException e)
			{
				LogUtils.e(TAG,e);
			}
			defaultSSLSocketFactory = context.getSocketFactory();
		}
		return defaultSSLSocketFactory;
	}
}  
