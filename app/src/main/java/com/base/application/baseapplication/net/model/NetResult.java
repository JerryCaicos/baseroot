package com.base.application.baseapplication.net.model;

/**
 * 网络请求结果.
 *
 * @Title:
 * @Description:
 * @Version:
 */
public interface NetResult
{
	/**
	 * @return 是否成功
	 * @Description:
	 */
	public boolean isSuccess();

	/**
	 * @return 网络数据的类型,用于不同数据返回的区分.
	 */
	public int getDataType();

	/**
	 * @return 网络处理的数据.
	 * @Description:
	 */
	public Object getData();

	/**
	 * @return 错误码.
	 * @Description:
	 */
	public int getErrorCode();

	/**
	 * @return 错误信息.
	 * @Description:
	 */
	public String getErrorMessage();
}
