package com.base.application.baseapplication.net.model;

import com.android.volley.ParseError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

/**
 * 网络请求异常.
 *
 * @Title:
 * @Description:
 * @Version:
 */
@SuppressWarnings("serial")
public class BasicNetError extends Exception
{

	/**
	 * error : parser error
	 **/
	public static final int ERROR_PARSER = 1;

	/**
	 * error : time out error
	 **/
	public static final int ERROR_TIME_OUT = 2;

	/**
	 * error : not login error
	 **/
	public static final int ERROR_NOT_LOGIN = 3;

	/**
	 * The HTTP status code.
	 */
	public final int statusCode;

	/**
	 * error type.
	 */
	public final int errorType;

	public BasicNetError(VolleyError error)
	{
		super(error);
		if(error.networkResponse == null)
		{
			statusCode = -1;
		}
		else
		{
			statusCode = error.networkResponse.statusCode;
		}
		if(error instanceof ParseError)
		{
			errorType = ERROR_PARSER;
		}
		else if(error instanceof TimeoutError)
		{
			errorType = ERROR_TIME_OUT;
		}
		else if(error instanceof NotLoginError)
		{
			errorType = ERROR_NOT_LOGIN;
		}
		else
		{
			errorType = -1;
		}
	}
}
