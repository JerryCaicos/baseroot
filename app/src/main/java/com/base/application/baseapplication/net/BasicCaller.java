package com.base.application.baseapplication.net;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;
import com.base.application.baseapplication.net.model.BasicNetError;
import com.base.application.baseapplication.net.task.BasicNetTask;
import com.base.application.baseapplication.utils.BasicUrlUtil;
import com.base.application.baseapplication.utils.LogUtils;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 网络请求实际操作类
 *
 * @Title:
 * @Description:
 * @Version:
 */
public class BasicCaller
{
	/**
	 * Global request queue for Volley
	 */
	private RequestQueue mRequestQueue;

	/**
	 * {@link BasicHurlStack}
	 **/
	private BasicHurlStack mBasicHurlStack;

	/**
	 * {@link Context}
	 **/
	private Context mContext;

	/**
	 * 保存公共的cookie, 如:风控,城市ID等,cookie须设置domain;<BR/>
	 * 如果想应用于所有接口,并自动设置接口对应的domain,domain设置为 {@link #AUTO_MATCH_DOMAIN}
	 */
	private final List<HttpCookie> mPublicCookieList = new ArrayList<HttpCookie>();
	/**
	 * 当前cookie需要设置到所有接口中,并且自动匹配接口的domain
	 */
	private static final String AUTO_MATCH_DOMAIN = "auto.match.domain";


	/**
	 * A singleton instance of the application class for easy access in other
	 * places
	 */
	private static BasicCaller sInstance;

	/**
	 * task异常监听
	 **/
	private OnTaskErrorListener mOnTaskErrorListener;

	/**
	 * @return ApplicationController singleton instance
	 */
	public static synchronized BasicCaller getInstance()
	{
		if(sInstance == null)
		{
			sInstance = new BasicCaller();
		}
		return sInstance;
	}

	/**
	 * 须在Application初始化的时候调用
	 */
	public void init(Context context)
	{
		mContext = context;
		android.webkit.CookieSyncManager.createInstance(mContext);
	}

	/**
	 * 设置Debug模式
	 */
	public void setDebug(boolean isDebug)
	{
		VolleyLog.DEBUG = isDebug;
	}

	/**
	 * @return The Volley Request queue, the queue will be created if it is null
	 */
	private RequestQueue getRequestQueue()
	{
		if(mContext == null)
		{
			throw new NullPointerException(
					"do you forget to call init when your application create ?");
		}
		// lazy initialize the request queue, the queue instance will be
		// created when it is accessed for the first time
		if(mRequestQueue == null)
		{
			mBasicHurlStack = new BasicHurlStack();
			mRequestQueue = Volley.newRequestQueue(mContext,mBasicHurlStack);
		}
		return mRequestQueue;
	}

	/**
	 * Adds the specified request to the global queue, if tag is specified then
	 * it is used else Default TAG is used.
	 *
	 * @param shouldCache Cache or Not
	 */
	public <T> void addToRequestQueue(Request<T> req,Object tag,boolean shouldCache)
	{
		// 添加公共cookie
		addPublicCookie(req);

		// set the default tag if tag is empty
		req.setTag(tag);
		req.setShouldCache(shouldCache);
		getRequestQueue().add(req);
	}

	/**
	 * 给请求添加设备指纹
	 *
	 * @param req
	 */
	private <T> void addPublicCookie(Request<T> req)
	{

		if(mPublicCookieList.isEmpty())
		{
			return;
		}

		String url = req.getUrl();
		URI uri;
		try
		{
			uri = new URI(url);
		}
		catch(URISyntaxException e)
		{
			LogUtils.e("setLoginCookie",e);
			return;
		}

		// 如果是自动匹配的cookie,要过滤出来
		HttpCookie autoMatchCookie;
		final List<HttpCookie> list = new ArrayList<HttpCookie>(mPublicCookieList);
		for(HttpCookie cookie : list)
		{
			if(AUTO_MATCH_DOMAIN.equals(cookie.getDomain()))
			{
				autoMatchCookie = new HttpCookie(cookie.getName(),cookie.getValue());
				cookie.setVersion(0);
				cookie.setDomain(getDomain(uri));
				cookie.setPath("/");
				addCookie(uri,autoMatchCookie);
				continue;
			}
			addCookie(uri,cookie);
		}
	}

	/**
	 * Cancels all pending requests by the specified TAG, it is important to
	 * specify a TAG so that the pending/ongoing requests can be cancelled.
	 *
	 * @param tag
	 */
	public void cancelPendingRequests(Object tag)
	{
		if(mRequestQueue != null)
		{
			mRequestQueue.cancelAll(tag);
		}
	}

	/**
	 * 获取Cookie容器
	 *
	 * @return {@link CookieStore}
	 */
	private CookieStore getCookieStore()
	{
		if(mBasicHurlStack == null)
		{
			// 如果HurlStack为空,表示还没有出现网络请求,这里初始化一下,以便于获取CookieStore
			getRequestQueue();
		}
		return mBasicHurlStack.getCookieStore();
	}

	/**
	 * 如果有多个cookie对应同一个名称,那么返回第一个cookie的值
	 *
	 * @param cookieName cookie的名称
	 * @return cookie的值
	 */
	public String getCookieValue(String cookieName)
	{
		HttpCookie cookie = getCookie(cookieName);
		if(cookie == null)
		{
			return "";
		}
		return cookie.getValue();
	}

	/**
	 * 如果有多个cookie对应同一个名称,那么返回第一个cookie
	 *
	 * @param cookieName cookie的名称
	 * @return cookie
	 */
	public HttpCookie getCookie(String cookieName)
	{
		CookieStore cookieStore = getCookieStore();
		if(cookieStore == null)
		{
			return null;
		}

		List<HttpCookie> cookieList = cookieStore.getCookies();
		for(HttpCookie cookie : cookieList)
		{
			if(cookie.getName().equals(cookieName))
			{
				return cookie;
			}
		}
		return null;
	}

	/**
	 * Get all cookies in cookie store which are not expired.
	 *
	 * @return an empty list if there's no http cookie in store, or an immutable
	 * list of cookies
	 */
	public List<HttpCookie> getCookies()
	{
		CookieStore cookieStore = getCookieStore();
		if(cookieStore == null)
		{
			return new ArrayList<HttpCookie>();
		}

		return cookieStore.getCookies();
	}

	/**
	 * Clear cookie store.
	 */
	public void removeAllCookies()
	{
		// 1, 移除java.net.CookieManager中所有cookie
		CookieStore cookieStore = getCookieStore();
		if(cookieStore != null)
		{
			cookieStore.removeAll();
		}
		// 2, 移除android.webkit.CookieManager中所有cookie
		android.webkit.CookieManager cookieManager = android.webkit.CookieManager.getInstance();
		cookieManager.removeAllCookie();
		// 3, 移除所有接口公用的cookie
		mPublicCookieList.clear();
	}

	/**
	 * 添加cookie
	 *
	 * @param spec        a URI whose illegal characters have all been encoded.
	 * @param cookieName  cookie的名称
	 * @param cookieValue cookie的值
	 */
	public void addCookie(String spec,String cookieName,String cookieValue)
	{
		URI uri;
		try
		{
			uri = new URI(spec);
		}
		catch(URISyntaxException e)
		{
			LogUtils.e("setLoginCookie",e);
			return;
		}
		HttpCookie cookie = new HttpCookie(cookieName,cookieValue);
		// 默认version是1,这里人为设置为0
		cookie.setVersion(0);
		// 设置cookie的domain
		cookie.setDomain(getDomain(uri));
		// 设置cookie的path
		cookie.setPath("/");
		// 添加到cookieStore中
		addCookie(uri,cookie);
	}

	/**
	 * @param uri {@link URI}
	 * @return domain
	 */
	private static String getDomain(URI uri)
	{
		// 空判断
		if(uri == null)
		{
			return "";
		}

		// 获取主机
		String host = uri.getHost();
		String domain;
		if(BasicUrlUtil.isIPAddress(host))
		{
			// 如果是Ip地址,直接使用host
			domain = host;
		}
		else
		{
			// 否则获取domain
			domain = BasicUrlUtil.obtainDomain2(host);
		}
		return domain;
	}

	/**
	 * 添加cookie
	 *
	 * @param uri    cookie对应的URI
	 * @param cookie cookie
	 * @see CookieStore#add(URI,HttpCookie)
	 */
	public void addCookie(URI uri,HttpCookie cookie)
	{
		// 这边用于人为的cookie添加.
		CookieManager cookieManager = getCookieManager();
		if(cookieManager == null)
		{
			return;
		}
		try
		{
			cookieManager.put(uri,
					Collections.singletonMap("Set-Cookie",
							Collections.singletonList(getCookieString(cookie))));
		}
		catch(IOException e)
		{
			LogUtils.e(this,e);
		}

		//		// 这边用于认为的cookie添加.
		//		CookieStore cookieStore = getCookieStore();
		//		if(cookieStore == null)
		//		{
		//			return;
		//		}
		//		// 1, 添加到java.net.CookieManager
		//		cookieStore.add(uri,cookie);
		//		// 2, 添加到android.webkit.CookieManager
		//		android.webkit.CookieManager cookieManager = android.webkit.CookieManager.getInstance();
		//		cookieManager.setCookie(uri.toString(),getCookieString(cookie));
	}

	/**
	 * 获取CookieManager
	 *
	 * @return {@link CookieManager}
	 * @Author 12075179
	 * @Date 2015-10-12
	 */
	private CookieManager getCookieManager()
	{
		if(mBasicHurlStack == null)
		{
			// 如果HurlStack为空,表示还没有出现网络请求,这里初始化一下,以便于获取CookieStore
			getRequestQueue();
		}
		return mBasicHurlStack.getCookieManager();
	}

	/**
	 * @param cookie {@link HttpCookie}
	 * @return cookie to String
	 */
	private static String getCookieString(HttpCookie cookie)
	{
		if(cookie == null)
		{
			return "";
		}
		// HttpCookie version 1 直接返回
		if(cookie.getVersion() == 1)
		{
			return cookie.toString();
		}
		// HttpCookie version 0 对应的toString无法满足android.webkit.CookieManager的加入
		// 需要在原来的基础上加上domain 和 path,才能被CookieManager识别.
		StringBuffer cookieString = new StringBuffer(cookie.toString());
		if(TextUtils.isEmpty(cookie.getPath()))
		{
			cookie.setPath("/");
		}
		cookieString.append("; path=").append(cookie.getPath());
		if(!TextUtils.isEmpty(cookie.getDomain()))
		{
			cookieString.append("; domain=").append(cookie.getDomain());
		}
		if(cookie.getSecure())
		{
			cookieString.append("; secure");
		}
		return cookieString.toString();

		//		if(cookie == null)
		//		{
		//			return "";
		//		}
		//		// HttpCookie version 0 对应的toString无法满足android.webkit.CookieManager的加入
		//		// 需要在原来的基础上加上domain 和 path,才能被CookieManager识别.
		//		StringBuffer cookieString = new StringBuffer(cookie.toString())
		//				.append("; ").append("domain=").append(cookie.getDomain())
		//				.append("; ").append("path=").append(cookie.getPath());
		//		return cookieString.toString();
	}

	/**
	 * 添加公共cookie
	 *
	 * @param cookieName  Cookie名称
	 * @param cookieValue Cookie值
	 * @param domain      域名
	 */
	public void addPublicCookie(String cookieName,String cookieValue,String domain)
	{
		// key+domain空判断
		if(TextUtils.isEmpty(cookieName) || TextUtils.isEmpty(domain))
		{
			return;
		}
		// value空处理
		if(cookieValue == null)
		{
			cookieValue = "";
		}

		HttpCookie cookie = new HttpCookie(cookieName,cookieValue);
		// 默认version是1,这里人为设置为0
		cookie.setVersion(0);
		// 设置cookie的domain
		cookie.setDomain(domain);
		// 设置cookie的path
		cookie.setPath("/");

		// 防止重复添加,移除后加入
		mPublicCookieList.remove(cookie);
		mPublicCookieList.add(cookie);
	}

	/**
	 * 添加公共cookie, 谨慎使用, 该cookie会添加到所有的接口中
	 *
	 * @param cookieName  Cookie名称
	 * @param cookieValue Cookie值
	 */
	public void addPublicCookie(String cookieName,String cookieValue)
	{
		// key空判断
		if(TextUtils.isEmpty(cookieName))
		{
			return;
		}
		// value空处理
		if(cookieValue == null)
		{
			cookieValue = "";
		}

		HttpCookie cookie = new HttpCookie(cookieName,cookieValue);
		// 默认version是1,这里人为设置为0
		cookie.setVersion(0);
		// 设置cookie的domain
		cookie.setDomain(AUTO_MATCH_DOMAIN);
		// 设置cookie的path
		cookie.setPath("/");

		// 防止重复添加,移除后加入
		mPublicCookieList.remove(cookie);
		mPublicCookieList.add(cookie);
	}

	/**
	 * 回调Task异常
	 *
	 * @param task  执行的Task
	 * @param error 详细错误
	 * @param <T>
	 */
	public final <T> void onTaskError(BasicNetTask<T> task,BasicNetError error)
	{
		if(mOnTaskErrorListener != null)
		{
			mOnTaskErrorListener.onTaskError(task,error);
		}
	}

	/**
	 * 设置Task执行异常监听
	 *
	 * @param listener {@link OnTaskErrorListener}
	 */
	public final void setOnTaskErrorListener(OnTaskErrorListener listener)
	{
		mOnTaskErrorListener = listener;
	}

	/**
	 * Task网络异常监听
	 */
	public interface OnTaskErrorListener
	{

		/**
		 * Task执行错误的时候, 处于非UI线程
		 *
		 * @param task  执行的Task
		 * @param error 详细错误
		 * @param <T>
		 */
		<T> void onTaskError(BasicNetTask<T> task,BasicNetError error);
	}
}
