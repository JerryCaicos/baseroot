package com.base.application.baseapplication.service;

/**
 * Created by JerryCaicos on 16/1/16.
 */
public interface BasicConstants
{
	/**
	 * 高保真宽度
	 */
	int HIFI_WIDTH = 720;
	/**
	 * 高保真高度
	 */
	int HIFI_HEIGHT = 1280;

	String PREFS_NAME = "BasicPreferences";// 系统设置

	// 切到后台的时间
	String EXIT_TO_BACK_TIME = "exit_to_back_time";
	/**
	 * 1s 1000ms
	 */
	int SECOUND = 1000;

	String DOWNLOAD_DATA = "downloadData";
	String SWID = "swid";
	String PACKAGENAME = "packageName";
	String PACKAGEACTION = "packageAction";
	String INSTALL_APP = "installApp";

	/**
	 * 接收更新数据的一系列广播
	 **/
	String ACTION_UPDATE_ALL_DATA = "action_update_all_data"; // 告知下载情况更新
	String ACTION_DOWNLOAD_FINISH = "action_download_finish"; // 告知已有任务下载完成
	String ACTION_DATA_INIT_FINISH = "action_data_init_finish"; // 告知Service数据加载完成
	String ACTION_STARTDOWNLOAD = "action_start_download"; // 告知下载情况更新
	String ACTION_CHECK_STATE = "action_check_state"; // 告知去检查软件状态
	String ACTION_LIST_CHECKBOX = "action_list_checkbox"; // 带checkbox列表页，接收checkbox点击事件广播
	String ACTION_DOWNLOAD_STATUS_CHANGE = "action_download_status_change";// 告知下载状态发生改变
	String ACTION_DOWNLOAD_STATUS_CHANGE_ONWEB = "action_download_status_change_onweb";// 告知下载状态发生改变
	String ACTION_HAS_LOCAL_UPDATE = "action_has_local_update";// 发送消息告诉本地有可更新的应用
	String ACTION_ADDED_APP = "action_added_app";// 发送消息告诉安装了app
	String ACTION_REMOVED_APP = "action_removed_app";
	String ACTION_CLEAR_DM_ID = "action_clear_dm_id";

	/**
	 * 检测软件更新完成的广播
	 **/
	String ACTION_CHECK_ALL_UPDATE_FINISH = "action_check_all_update_finish";
	String DATA_UPDATE_COUNT = "update_count";

	String ACTION_INSTALLED_APP_CHANGE = "action_installed_app_change"; // 已安装列表有更新
	String ACTION_UPDATE_FILE_MAN_CHECK = "action_update_file_man_check"; // 更新本地管理选择状态
	String LISTVIEW_CUR_POSITION = "listview_cur_position"; // 当前列表位置

	/**
	 * 设置页相关
	 **/
	String DOWNLOAD_IN_WIFI = "download_in_wifi";
	boolean LOAD_ICON = true;
	int SAMETIME_DOWNLOAD_NUM = 2;
	boolean AUTO_INSTALL = true;
	String TELL_CAN_NOT_DOWNLOAD_WITHOUT_WIFI = "tell_can_not_download_without_wifi";

	/**
	 * 本地扫描
	 **/
	String LOCAL_APKDIR_CACHE = "local_apkdir_cache";

	/**
	 * 评论列表
	 **/
	String REFRESH_COMMENT_LIST = "RefreshCommentList";

	/**
	 * 头像更新
	 **/
	String Last_USERPICTURE_Update_Time = "lastUpdateTime";

	// 定义公用的魔法数字

	int PUBLIC_ONE = 1;


	String PREFS_PHONE_IMEI = "imei";// 手机串号

	String PREFS_USER_NAME = "name";// 历史用户姓名

}
