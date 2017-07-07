package com.base.application.baseapplication.net.request;

import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.base.application.baseapplication.net.model.NotLoginError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * JSONObject请求,{@link JsonRequest}
 *
 * @Title:
 * @Description:
 * @Version:
 */
public class BasicJsonRequest extends JsonRequest<JSONObject>
{
	/**
	 * Log TAG.
	 */
	private static final String TAG = "BasicRequest";

	/**
	 * Charset for request.
	 */
	private static final String PROTOCOL_CHARSET = "utf-8";

	/**
	 * Content type for request.
	 */
	private static final String PROTOCOL_CONTENT_TYPE = String.format(
			"application/x-www-form-urlencoded; charset=%s",PROTOCOL_CHARSET);

	/**
	 * user_agent
	 **/
	public static final String USER_AGENT =
			"Mozilla/5.0(Linux; U;SNEBUY-APP; Android " +
					Build.VERSION.RELEASE + "; " +
					Locale.getDefault().getLanguage() + "; " + Build.MODEL +
					") AppleWebKit/533.0 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1";

	/**
	 * 请求头
	 **/
	private final Map<String,String> mHeaders = new HashMap<String,String>();

	private final String mRequestBody;

	/**
	 * 构造方法
	 *
	 * @param method
	 * @param url
	 * @param requestBody
	 * @param listener
	 * @param errorListener
	 */
	public BasicJsonRequest(int method,String url,String requestBody,
			Listener<JSONObject> listener,ErrorListener errorListener)
	{
		super(method,url,requestBody,listener,errorListener);
		mRequestBody = requestBody;
		setRetryPolicy(new DefaultRetryPolicy(30000,0,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
	}

	@Override
	public String getBodyContentType()
	{
		return PROTOCOL_CONTENT_TYPE;
	}

	/**
	 * 添加Header(请求头)
	 *
	 * @param headers 请求头
	 * @Description:
	 */
	public void addHeaders(Map<String,String> headers)
	{
		if(headers != null && !headers.isEmpty())
		{
			mHeaders.putAll(headers);
		}
	}

	@Override
	public Map<String,String> getHeaders() throws AuthFailureError
	{
		mHeaders.put("User-Agent",USER_AGENT);
		return mHeaders;
	}

	@Override
	public String getUrl()
	{
		StringBuffer url = new StringBuffer(super.getUrl());
		if(getMethod() == Method.GET && !TextUtils.isEmpty(mRequestBody))
		{
			url.append("?").append(mRequestBody);
		}
		return url.toString();
	}

	@Override
	public byte[] getBody()
	{
		try
		{
			return mRequestBody == null?null:mRequestBody.getBytes(PROTOCOL_CHARSET);
		}
		catch(UnsupportedEncodingException uee)
		{
			VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
					mRequestBody,PROTOCOL_CHARSET);
			return null;
		}
	}

	@Override
	protected Response<JSONObject> parseNetworkResponse(NetworkResponse response)
	{
		try
		{
			Map<String,String> headers = response.headers;
			// 1, 判断是否未登录
			if(headers.containsKey(NotLoginError.HEADER_NOT_LOGIN_FLAG))
			{

				String value1 = headers.get(NotLoginError.HEADER_NOT_LOGIN_FLAG);
				Log.e(TAG,NotLoginError.HEADER_NOT_LOGIN_FLAG + " : " + value1);

				// 1.1 未登录错误原因
				int notLoginError = NotLoginError.ERROR_NOT_LOGIN;

				// 1.2 再判断是否是账号异常引起的未登录
				if(headers.containsKey(NotLoginError.HEADER_ACCOUNT_ERROR1)
						&& headers.containsKey(NotLoginError.HEADER_ACCOUNT_ERROR2))
				{
					String value2 = headers.get(NotLoginError.HEADER_ACCOUNT_ERROR1);
					String value3 = headers.get(NotLoginError.HEADER_ACCOUNT_ERROR2);
					Log.e(TAG,NotLoginError.HEADER_ACCOUNT_ERROR1 + " : " + value2);
					Log.e(TAG,NotLoginError.HEADER_ACCOUNT_ERROR2 + " : " + value3);

					// 1.3 如果是账号异常,更改未登录的错误原因
					notLoginError = NotLoginError.ERROR_ACCOUNT_ERROR;
				}
				// 返回未登录异常
				return Response.error(new NotLoginError(notLoginError,headers));
			}
			// 2, 获取请求结果
			String result = new String(response.data,HttpHeaderParser.parseCharset(headers));
			Log.d(TAG,result);
			// 构造JSONObject
			JSONObject jsonObject = new JSONObject(result);
			// 返回Response
			return Response.success(jsonObject,HttpHeaderParser.parseCacheHeaders(response));
		}
		catch(UnsupportedEncodingException e)
		{
			return Response.error(new ParseError(e));
		}
		catch(JSONException e)
		{
			return Response.error(new ParseError(e));
		}
	}
}
