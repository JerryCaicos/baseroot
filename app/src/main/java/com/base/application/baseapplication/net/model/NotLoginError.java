package com.base.application.baseapplication.net.model;

import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.Map;

/**
 * 未登录异常
 *
 * @Version:
 */
@SuppressWarnings("serial")
public class NotLoginError extends VolleyError
{
	/**
	 * header: 未登录标志
	 **/
	public static final String HEADER_NOT_LOGIN_FLAG = "passport.login.flag";

	/**
	 * 账号异常1
	 **/
	public static final String HEADER_ACCOUNT_ERROR1 = "errorCode";
	/**
	 * 账号异常2
	 **/
	public static final String HEADER_ACCOUNT_ERROR2 = "snapshotId";

	/**
	 * 登录失效: Response的Header中存在{@link #HEADER_NOT_LOGIN_FLAG}<BR>
	 * 账号异常导致的登录失效见 {@link #ERROR_ACCOUNT_ERROR}
	 */
	public static final int ERROR_NOT_LOGIN = 1;

	/**
	 * 账号异常导致的登录失效: Response的Header中存在{@link #HEADER_NOT_LOGIN_FLAG},<BR>
	 * 并且还同时存在{@link #HEADER_ACCOUNT_ERROR1} 和 {@link #HEADER_ACCOUNT_ERROR2}
	 */
	public static final int ERROR_ACCOUNT_ERROR = 2;

	/**
	 * 异常类型, 见{@link #ERROR_NOT_LOGIN}, {@link #ERROR_ACCOUNT_ERROR}
	 */
	public final int errorType;

	/**
	 * 响应头, response headers
	 **/
	public final Map<String,String> headers;

	/**
	 * 构造方法
	 *
	 * @param errorType 异常类型, 见 {@link #ERROR_NOT_LOGIN}, {@link #ERROR_ACCOUNT_ERROR}
	 * @param headers   响应头, response headers
	 */
	public NotLoginError(int errorType,Map<String,String> headers)
	{
		this.errorType = errorType;
		this.headers = new HashMap<String,String>();
		if(headers != null && !headers.isEmpty())
		{
			this.headers.putAll(headers);
		}
	}
}
