package com.base.application.baseapplication;

import android.app.Application;
import android.content.Context;

import com.base.application.baseapplication.net.BasicCaller;
import com.base.application.baseapplication.service.BasicSP;
import com.base.application.baseapplication.service.BasicService;
import com.base.application.baseapplication.service.DeviceInfoService;
import com.base.application.baseapplication.service.NetConnectService;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by adminchen on 16/1/16.
 */
public class BasicApplication extends Application
{
	private static BasicApplication instance;

	/**
	 * 应用服务容器
	 **/
	private Map<String,BasicService> serviceMap;

	public static final BasicApplication getInstance()
	{
		return instance;
	}

	@Override
	public void onCreate()
	{
		super.onCreate();
		// 单例赋值
		instance = this;
		android.webkit.CookieSyncManager.createInstance(this);
		android.webkit.CookieManager.getInstance().setAcceptCookie(true);
		android.webkit.CookieManager cookieManager = android.webkit.CookieManager.getInstance();
		cookieManager.removeAllCookie();

		// SP初始化
		BasicSP.init(instance);
		// 图片缓存的默认加载ID设置 + 图片地址设置
		// TODO: 16/2/19
		//		ImageLoader.setDefaultLoadImageId(R.drawable.default_background_small);
		// 初始化网络请求 + DEBUG模式
		BasicCaller.getInstance().init(instance);
		BasicCaller.getInstance().setDebug(BuildConfig.DEBUG);
		// 初始化服务
		initService();
	}

	@Override
	protected void attachBaseContext(Context base)
	{
		super.attachBaseContext(base);
	}

	/**
	 * 初始化Service
	 *
	 * @Description:
	 * @Author 12075179
	 * @Date 2015-8-12
	 */
	private void initService()
	{

		// 注册应用服务
		serviceMap = new HashMap<String,BasicService>();
		serviceMap.put(BasicService.DEVICE_INFO,new DeviceInfoService());
		serviceMap.put(BasicService.NET_CONNECT,new NetConnectService());

		// 启动所有Service
		for(Map.Entry<String,BasicService> entry : serviceMap.entrySet())
		{
			entry.getValue().onApplicationCreate(this);
		}
	}

	/**
	 * 获取一个服务
	 *
	 * @param service 服务的名称
	 * @return 一个服务
	 * @Description:
	 * @Author 12075179
	 * @Date 2015-8-12
	 */
	public BasicService getService(String service)
	{
		return serviceMap.get(service);
	}

	/**
	 * @return 返回“设备信息服务”{@link DeviceInfoService}
	 * @Author 12075179
	 * @Date 2015-10-12
	 */
	public DeviceInfoService getDeviceInfoService()
	{
		return (DeviceInfoService)getService(BasicService.DEVICE_INFO);
	}


	/**
	 * 退出应用
	 *
	 * @Description:
	 * @Author 12075179
	 * @Date 2015-8-12
	 */
	public void exit()
	{
		// 1,关闭所有Service
		for(Map.Entry<String,BasicService> entry : serviceMap.entrySet())
		{
			entry.getValue().onApplicationDestory(this);
		}

		// 2,关闭数据库
		//		databaseHelper.close();

		//		closeUpdate();
		// 4,结束当前线程
		System.exit(0);
	}

	/**
	 * 结束Application进程
	 *
	 * @Author 12075179
	 * @Date 2015-8-12
	 */
	private final void destory()
	{
		// 1, 关闭数据库
		// TODO: 16/2/19
		//		databaseHelper.close();
		// 2,销毁所有Service
		for(Map.Entry<String,BasicService> entry : serviceMap.entrySet())
		{
			entry.getValue().onApplicationDestory(this);
		}
		// 3,结束进程
		System.exit(0);
	}
}
