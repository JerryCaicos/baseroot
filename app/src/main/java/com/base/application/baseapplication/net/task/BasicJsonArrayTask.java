package com.base.application.baseapplication.net.task;

import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.base.application.baseapplication.net.BasicCaller;
import com.base.application.baseapplication.net.message.NameValuePair;
import com.base.application.baseapplication.net.request.BasicJsonArrayRequest;
import com.base.application.baseapplication.utils.BasicUrlUtil;
import com.base.application.baseapplication.utils.LogUtils;

import org.json.JSONArray;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * JSONArray网络请求,使用方法如下:
 * <LI>1 实现所有抽象方法;</LI>
 * <LI>2 设置回调setOnResultListener;</LI>
 * <LI>3 执行网络请求BasicCaller.getInstance().excuteTask(task)</LI>
 *
 * @Title:
 * @Description:
 * @Version:
 */
public abstract class BasicJsonArrayTask extends BasicNetTask<JSONArray>
{

	/**
	 * LOG TAG
	 **/
	private static final String TAG = "BasicJsonArrayTask";

	/**
	 * GET
	 **/
	protected static final int METHOD_GET = Method.GET;
	/**
	 * POST
	 **/
	protected static final int METHOD_POST = Method.POST;

	@Override
	protected boolean onRedirect(String location)
	{
		LogUtils.d(TAG,"Location: " + location);
		final BasicJsonArrayRequest request = new BasicJsonArrayRequest(
				Method.GET,location,"",getResponseListener(),getErrorListener());
		// 重定向继续请求
		BasicCaller.getInstance().addToRequestQueue(request,this,false);

		return true;
	}

	@Override
	public Request<JSONArray> getRequest()
	{
		int method = getMethod();
		String url = getUrl();
		if(TextUtils.isEmpty(url))
		{
			LogUtils.e("getRequest","url is empty.");
			return null;
		}
		if(enableHttps())
		{
			url = httpToHttps(url);
		}
		String requestBody = buildRequestBody(getRequestBody());
		BasicJsonArrayRequest request = new BasicJsonArrayRequest(method,url,
				requestBody,getResponseListener(),getErrorListener());
		request.addHeaders(getHeaders());
		request.setBodyContentType(getBodyContentType());

		// 请求日志
		StringBuffer log = new StringBuffer();
		log.append(method == METHOD_GET?"get":"post").append(" : ");
		log.append(url).append("\n");
		log.append("request body : ").append(requestBody);
		LogUtils.i(TAG,log.toString());

		return request;
	}

	protected boolean enableHttps()
	{
		return false;
	}

	private String httpToHttps(String url)
	{

		String protocol = BasicUrlUtil.getSchemePrefix(url);
		if(TextUtils.isEmpty(protocol))
		{
			return url;
		}

		if(protocol.equals("http"))
		{
			return "https" + url.substring(protocol.length());
		}
		return url;
	}

	@Override
	protected void onRetry(int retryCount,Request<JSONArray> request)
	{
		super.onRetry(retryCount,request);
		LogUtils.w(TAG,"Retry --- " + retryCount + " , " + request.toString());
	}

	/**
	 * 请求的Method
	 *
	 * @return {@link #METHOD_GET} 或者 {@link #METHOD_POST}
	 * @Description:
	 * @Author 12075179
	 * @Date 2015-8-12
	 */
	public abstract int getMethod();

	/**
	 * 请求的链接
	 *
	 * @return 请求的连接
	 * @Description:
	 * @Author 12075179
	 * @Date 2015-8-12
	 */
	public abstract String getUrl();

	/**
	 * @return 请求参数
	 */
	public String getPostBody()
	{
		return buildRequestBody(getRequestBody());
	}

	/**
	 * @return 请求参数, 可null.
	 * @Description:
	 * @Author 12075179
	 * @Date 2015-8-12
	 */
	public abstract List<NameValuePair> getRequestBody();

	/**
	 * @param paramList 参数列表
	 * @return 请求体
	 * @Description:
	 */
	private static String buildRequestBody(List<NameValuePair> paramList)
	{
		if(paramList == null || paramList.isEmpty())
		{
			return "";
		}
		StringBuffer requestBody = new StringBuffer();

		int size = paramList.size();
		NameValuePair param = null;
		for(int i = 0;i < size;i++)
		{
			param = paramList.get(i);
			String value = param.getValue();
			if(TextUtils.isEmpty(value))
			{
				value = "";
			}
			else
			{
				try
				{
					value = URLEncoder.encode(value,"utf-8");
				}
				catch(UnsupportedEncodingException e)
				{
					LogUtils.w("buildRequestBody",e);
					value = param.getValue();
				}
			}
			requestBody.append(param.getName()).append('=').append(value);
			if(i < size - 1)
			{
				requestBody.append('&');
			}
		}
		return requestBody.toString();
	}
}
