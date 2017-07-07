package com.base.application.baseapplication.net.model;

/**
 * 网络请求结果.
 *
 * @Title:
 * @Description:
 * @Version:
 */
public class BasicNetResult implements NetResult
{
	/**
	 * 请求是否成功
	 **/
	private boolean isSuccess;
	/**
	 * 请求返回的数据类型,用于区分不同数据
	 **/
	private int dataType;
	/**
	 * 请求返回的数据
	 **/
	private Object data;
	/**
	 * 错误码
	 **/
	private int errorCode;
	/**
	 * 错误信息
	 **/
	private String errorMessage;

	/**
	 * 网络【请求成功】结果
	 *
	 * @param isSuccess 是否成功
	 */
	public BasicNetResult(boolean isSuccess)
	{
		this(isSuccess,null);
	}

	/**
	 * 网络【请求成功】结果
	 *
	 * @param isSuccess 是否成功
	 * @param data      请求返回的数据
	 */
	public BasicNetResult(boolean isSuccess,Object data)
	{
		this(isSuccess,-1,data);
	}

	/**
	 * 网络请求结果
	 *
	 * @param isSuccess 是否成功
	 * @param dataType  数据类型,用于区分不同的数据
	 * @param data      请求返回的数据
	 */
	public BasicNetResult(boolean isSuccess,int dataType,Object data)
	{
		this.isSuccess = isSuccess;
		this.dataType = dataType;
		this.data = data;
		// 错误码,错误信息 → 默认
		this.errorCode = -1;
		this.errorMessage = "";
	}


	/**
	 * 网络请求失败结果
	 *
	 * @param errorMessage 错误信息
	 */
	public BasicNetResult(String errorMessage)
	{
		this(-1,errorMessage);
	}

	/**
	 * 网络请求失败结果
	 *
	 * @param errorCode    错误码
	 * @param errorMessage 错误信息
	 */
	public BasicNetResult(int errorCode,String errorMessage)
	{
		this(-1,errorCode,errorMessage);
	}

	/**
	 * 网络请求失败结果
	 *
	 * @param dataType     数据类型
	 * @param errorCode    错误码
	 * @param errorMessage 错误信息
	 */
	public BasicNetResult(int dataType,int errorCode,String errorMessage)
	{
		this.isSuccess = false;
		this.dataType = dataType;
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	/**
	 * @return 是否成功
	 * @Description:
	 */
	@Override
	public boolean isSuccess()
	{
		return isSuccess;
	}

	/**
	 * @return 网络处理的数据类型,默认-1.
	 * @Description:
	 */
	public int getDataType()
	{
		return dataType;
	}

	/**
	 * @return 网络处理的数据,可能为null.
	 * @Description:
	 */
	@Override
	public Object getData()
	{
		return data;
	}

	/**
	 * @return 错误码,默认-1.
	 * @Description:
	 */
	@Override
	public int getErrorCode()
	{
		return errorCode;
	}

	/**
	 * @return 错误信息,默认“”.
	 * @Description:
	 */
	@Override
	public String getErrorMessage()
	{
		return errorMessage;
	}
}
