package com.base.application.baseapplication.net;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class WebkitCookieManagerProxy extends CookieManager
{

	private android.webkit.CookieManager mWebkitCookieManager;

	public WebkitCookieManagerProxy(CookieStore cookieStore,CookiePolicy acceptAll)
	{
		super(cookieStore,acceptAll);
		mWebkitCookieManager = android.webkit.CookieManager.getInstance();
		mWebkitCookieManager.setAcceptCookie(true);
	}

	@Override
	public Map<String,List<String>> get(URI uri,Map<String,List<String>> requestHeaders)
			throws IOException
	{
		// make sure our args are valid
		if((uri == null) || (requestHeaders == null))
		{
			throw new IllegalArgumentException("Argument is null");
		}

		// save our url once
		String url = uri.toString();

		// prepare our response
		Map<String,List<String>> res = new java.util.HashMap<String,List<String>>();

		// get the cookie
		String cookie = this.mWebkitCookieManager.getCookie(url);

		// return it
		if(cookie != null)
		{
			res.put("Cookie",Arrays.asList(cookie));
		}
		return res;
		// return super.get(uri, requestHeaders);
	}

	/**
	 * Sets all cookies of a specific URI in the {@code responseHeaders} into
	 * the cookie cache.
	 *
	 * @param uri             the origin URI of the cookies.
	 * @param responseHeaders a list of request headers.
	 * @throws IOException if an error occurs during the I/O operation.
	 */
	public void put(URI uri,Map<String,List<String>> responseHeaders)
			throws IOException
	{
		// make sure our args are valid
		if((uri == null) || (responseHeaders == null))
		{
			return;
		}

		// save our url once
		String url = uri.toString();

		// go over the headers
		List<String> cookie = responseHeaders.get("Set-Cookie");
		if(cookie != null)
		{
			for(String headerValue : cookie)
			{
				this.mWebkitCookieManager.setCookie(url,headerValue);
			}
		}
		List<String> cookie2 = responseHeaders.get("Set-Cookie2");
		if(cookie2 != null)
		{
			for(String headerValue : cookie2)
			{
				this.mWebkitCookieManager.setCookie(url,headerValue);
			}
		}
		super.put(uri,responseHeaders);
	}
}
