package com.base.application.baseapplication.net.task;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.base.application.baseapplication.net.BasicCaller;
import com.base.application.baseapplication.net.model.BasicNetError;
import com.base.application.baseapplication.net.model.BasicNetResult;
import com.base.application.baseapplication.net.model.NetResult;
import com.base.application.baseapplication.utils.LogUtils;

import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.util.Map;

/**
 * 网络任务, 基本原理如下:
 * <PRE>
 * 1, BasicNetTask.getRequest() ————> Request
 * <p/>
 * 2, Volley + Request ————> Response || Error
 * if(Response) {
 * BasicNetTask.onNetResponse(Response) ————> Object
 * } else {
 * BasicNetTask.onNetErrorResponse(Error) ————> Object
 * }
 * 3, BasicNetTask.OnResultListener.onResult(Object)
 * </PRE>
 * 说明:
 * <LI>1 实现抽象方法getRequest()得到Request;</LI>
 * <LI>2 Volley发起上面的Request,得到Response 或者 Error;</LI>
 * <LI>2.1 实现抽象方法onNetResponse(Response)得到Object;</LI>
 * <LI>2.2 实现抽象方法onNetErrorResponse(Error)得到Object;</LI>
 * <LI>3 回调Object给BasicNetTask.OnResultListener.</LI>
 *
 * @Title:
 * @Description:
 * @Version:
 */
public abstract class BasicNetTask<T>
{
	/**
	 * 任务ID
	 **/
	private int id;
	/**
	 * 任务携带的tag.
	 **/
	private Object mTag = null;

	/**
	 * Whether or not this request has been canceled.
	 */
	private boolean mCanceled = false;
	/**
	 * Whether or not this request is running.
	 **/
	private boolean isRunning;


	/**
	 * task loading 类型: 没有加载框
	 **/
	public final static int TL_TYPE_NONE = 0;
	/**
	 * task loading 类型: 有加载框,task启动-显示,task结束|取消-隐藏
	 **/
	public final static int TL_TYPE_NORMAL = 1;
	/**
	 * task loading 类型: 有加载框,task启动-显示,task结束|取消-隐藏, 如果task有后续task, 结束时-不隐藏
	 **/
	public final static int TL_TYPE_FOLLOW_UP = 2;
	/**
	 * 加载框类型
	 * <PRE>
	 * <LI>{@link #TL_TYPE_NONE}<LI/>
	 * <LI>{@link #TL_TYPE_NORMAL}<LI/>
	 * <LI>{@link #TL_TYPE_FOLLOW_UP}<LI/>
	 * <PRE/>
	 */
	private int loadingType = TL_TYPE_NORMAL;
	/**
	 * 加载框是否可以取消
	 **/
	private boolean loadingCancelable = false;
	/**
	 * The current retry count.
	 */
	private int mCurrentRetryCount;
	/**
	 * The maximum number of attempts.
	 */
	private int mMaxNumRetries;

	/**
	 * The default number of retries
	 */
	public static final int DEFAULT_MAX_RETRIES = 2;
	/**
	 * Charset for request.
	 */
	protected static final String PROTOCOL_CHARSET = "utf-8";

	/**
	 * Content type for request.
	 */
	protected static final String PROTOCOL_CONTENT_TYPE = String.format(
			"application/x-www-form-urlencoded; charset=%s",PROTOCOL_CHARSET);
	/**
	 * 结果事件
	 **/
	private OnResultListener onResultListener;

	/**
	 * 生命周期事件
	 **/
	private LifecycleCallbacks lifecycleCallbacks;

	/**
	 * Volley: UI → Thread → UI, 但是解析需要时间,所以需要再次  → Thread → UI
	 **/
	private Handler mHandler = new TaskHandler(Looper.getMainLooper(),this);

	public void setId(int id)
	{
		this.id = id;
	}

	public int getId()
	{
		return id;
	}

	/**
	 * @return 任务携带的tag.
	 * @see #setTag(Object)
	 */
	public Object getTag()
	{
		return mTag;
	}

	/**
	 * 给任务设置一个Tag.
	 *
	 * @param tag 任务携带的tag
	 * @see #getTag()
	 */
	public void setTag(final Object tag)
	{
		mTag = tag;
	}

	/**
	 * Mark this request as canceled.  No callback will be delivered.<BR>
	 * 取消这个任务,网络请求肯定是发出去了,但是不会有返回.
	 */
	public void cancel()
	{
		mCanceled = true;
		//  调用Task结束方法
		onCanceled();
	}

	/**
	 * Returns true if this request has been canceled.
	 */
	public boolean isCanceled()
	{
		return mCanceled;
	}

	/**
	 * Returns true if this request is running.
	 */
	public boolean isRunning()
	{
		return isRunning;
	}

	/**
	 * 设置 {@link #loadingType}
	 *
	 * @param type
	 */
	public void setLoadingType(int type)
	{
		loadingType = type;
	}

	/**
	 * @return {@link #loadingType}
	 */
	public int getLoadingType()
	{
		return loadingType;
	}

	/**
	 * 设置 {@link #loadingCancelable}
	 *
	 * @param cancelable
	 */
	public void setLoadingCancelable(boolean cancelable)
	{
		loadingCancelable = cancelable;
	}

	/**
	 * @return {@link #loadingCancelable}
	 */
	public boolean isLoadingCancelable()
	{
		return loadingCancelable;
	}

	/**
	 * 响应监听
	 **/
	private Listener<T> responseListener = new Listener<T>()
	{
		@Override
		public void onResponse(final T response)
		{
			if(mCanceled)
			{
				// 如果取消了,直接返回
				return;
			}
			// 由于解析需要时间,启动子线程
			new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					Message message = mHandler.obtainMessage();
					message.obj = onNetResponse(response);
					mHandler.sendMessage(message);
				}
			}).start();
		}
	};

	/**
	 * @return Content-Type
	 */
	public String getBodyContentType()
	{
		return PROTOCOL_CONTENT_TYPE;
	}

	/**
	 * @return {@link Listener}
	 * @Description:
	 */
	public final Listener<T> getResponseListener()
	{
		return responseListener;
	}

	/**
	 * 响应监听
	 **/
	private ErrorListener errorListener = new ErrorListener()
	{

		@Override
		public void onErrorResponse(final VolleyError error)
		{
			if(mCanceled)
			{
				// 如果取消了,直接返回
				return;
			}
			// 重试NoConnectionError
			if(error instanceof NoConnectionError)
			{
				mCurrentRetryCount++;
				if(mCurrentRetryCount <= mMaxNumRetries)
				{
					// 获取Request进行重试
					Request<T> request = getRequest();
					if(request != null)
					{
						// 如果进行重试,不回调异常结果
						BasicCaller.getInstance().addToRequestQueue(request,this,shouldCache());
						// 回调方法
						onRetry(mCurrentRetryCount,request);
						return;
					}
				}
			}
			else
			{
				// 获取状态码
				final int status;
				NetworkResponse response = error.networkResponse;
				status = response != null?response.statusCode:0;

				// 处理30X
				if(status == HttpURLConnection.HTTP_MOVED_PERM
						|| status == HttpURLConnection.HTTP_MOVED_TEMP
						|| status == HttpURLConnection.HTTP_SEE_OTHER)
				{

					final String location = error.networkResponse.headers.get("Location");
					if(!TextUtils.isEmpty(location) && onRedirect(location))
					{
						// 如果进行重定向,不回调异常结果
						return;
					}
				}
			}

			// 由于解析需要时间,启动子线程
			new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					// 1, 异常回调
					BasicNetError netError = new BasicNetError(error);
					BasicCaller.getInstance().onTaskError(BasicNetTask.this,netError);
					// 2, 结果回调
					Message message = mHandler.obtainMessage();
					message.obj = onNetErrorResponse(netError);
					mHandler.sendMessage(message);
				}
			}).start();
		}
	};

	/**
	 * 在Task请求重试的时候,会调用这个方法
	 *
	 * @Author 12075179
	 * @Date 2015-9-21
	 */
	protected void onRetry(int retryCount,Request<T> request)
	{
		// do nothing
	}

	/**
	 * @return {@link ErrorListener}
	 * @Description:
	 */
	public final ErrorListener getErrorListener()
	{
		return errorListener;
	}

	/**
	 * 设置结果事件监听
	 *
	 * @param listener {@link OnResultListener}
	 */
	public final void setOnResultListener(OnResultListener listener)
	{
		this.onResultListener = listener;
	}

	/**
	 * 设置生命周期监听
	 *
	 * @param callbacks {@link LifecycleCallbacks}
	 */
	public void setLifecycleCallbacks(LifecycleCallbacks callbacks)
	{
		lifecycleCallbacks = callbacks;
	}

	/**
	 * @return 是否允许网络缓存
	 */
	public boolean shouldCache()
	{
		return false;
	}

	/**
	 * @return 请求头
	 */
	public Map<String,String> getHeaders()
	{
		return null;
	}

	/**
	 * 在Task开始执行的时候,会调用这个方法
	 */
	protected void onStart()
	{
		// 更新运行状态
		isRunning = true;
		// 回调
		if(lifecycleCallbacks != null)
		{
			lifecycleCallbacks.onStart(this);
		}
	}

	/**
	 * 在任务取消的时候,会调用这个方法
	 */
	protected void onCanceled()
	{
		// 更新运行状态
		isRunning = false;
		// 回调
		if(lifecycleCallbacks != null)
		{
			lifecycleCallbacks.onCanceled(this);
		}
	}

	/**
	 * 在Task结束的时候,会调用这个方法
	 */
	protected void onFinished()
	{
		// 更新运行状态
		isRunning = false;
		// 回调
		if(lifecycleCallbacks != null)
		{
			lifecycleCallbacks.onFinished(this);
		}
	}

	/**
	 * 执行任务.
	 */
	public final void execute()
	{
		Request<T> request = getRequest();
		if(request == null)
		{
			LogUtils.e("execute","request is null.");
			return;
		}
		mMaxNumRetries = getRetryNum();
		mCurrentRetryCount = 0;
		onStart();
		BasicCaller.getInstance().addToRequestQueue(request,this,shouldCache());
	}

	/**
	 * @return 重试次数
	 */
	protected int getRetryNum()
	{
		return DEFAULT_MAX_RETRIES;
	}

	/**
	 * @return 网络请求
	 */
	public abstract Request<T> getRequest();

	/**
	 * 响应处理
	 *
	 * @param response 响应
	 * @return 处理结果,回调给OnResultListener.onResult
	 * @Description:
	 */
	public abstract NetResult onNetResponse(T response);

	/**
	 * 重定向处理
	 *
	 * @param location 重定向地址
	 * @return 是否重定向,true: 表示进行重定向,则不回调结果; false: 表示不重定向,回调异常结果.
	 * @Description:
	 */
	protected boolean onRedirect(String location)
	{
		return false;
	}

	/**
	 * 响应异常处理
	 *
	 * @param error 响应异常
	 * @return 处理结果,回调给OnResultListener.onResult
	 * @Description:
	 */
	public abstract NetResult onNetErrorResponse(BasicNetError error);

	/**
	 * 任务结果监听
	 *
	 * @Title:
	 * @Description:
	 * @Version:
	 */
	public interface OnResultListener
	{

		/**
		 * 任务结果处理,UI线程.
		 *
		 * @param task   被执行的任务
		 * @param result onNetResponse 或者 onNetErrorResponse 的处理结果
		 * @Description:
		 */
		<T> void onResult(BasicNetTask<T> task,NetResult result);
	}

	/**
	 * static handler weak reference the SuningNetTask to prevent memory overflow.
	 */
	private class TaskHandler extends Handler
	{

		/**
		 * weak reference task.
		 */
		WeakReference<BasicNetTask> mmTaskReference;

		/**
		 * constructor.
		 *
		 * @param looper The looper, must not be null.
		 * @param task   {@link BasicNetTask}
		 */
		TaskHandler(Looper looper,BasicNetTask task)
		{
			super(looper);
			this.mmTaskReference = new WeakReference<BasicNetTask>(task);
		}

		@Override
		public void handleMessage(Message msg)
		{
			BasicNetTask task = mmTaskReference.get();
			// prevent exception
			if(task != null)
			{
				task.handleMessage(msg);
			}
		}
	}

	/**
	 * handle message.
	 *
	 * @param msg {@link Message}
	 */
	public final void handleMessage(Message msg)
	{
		// 这里post给UI
		BasicNetResult result = (BasicNetResult)msg.obj;
		// 响应处理+回调
		if(onResultListener != null)
		{
			onResultListener.onResult(BasicNetTask.this,result);
		}
		// 调用Task结束方法
		onFinished();
	}

	/**
	 * 生命周期回调
	 */
	public interface LifecycleCallbacks
	{

		/**
		 * 任务开始的时候
		 *
		 * @param task
		 */
		<T> void onStart(BasicNetTask<T> task);

		/**
		 * 任务取消的时候
		 *
		 * @param task
		 */
		<T> void onCanceled(BasicNetTask<T> task);

		/**
		 * 任务完成的时候
		 *
		 * @param task
		 */
		<T> void onFinished(BasicNetTask<T> task);
	}
}
