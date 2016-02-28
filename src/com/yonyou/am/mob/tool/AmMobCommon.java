package com.yonyou.am.mob.tool;

/**
 * @页面名称： 资产移动常量类
 * @功能说明：资产移动常量类
 * 
 * @author zhangg
 * @version 1.0
 */
public class AmMobCommon {
	/** 设备利用率数据库表 */
	public static final String EUR_DATABASE_TABLE = "am_eur_table";
	/** 记录日志用 */
	public static final String ammoblog = "ammoblog";
	/** 台账中间日期显示-传值用 */
	public static final String date = "date";
	/** 部门界面返回列表界面的返回码 */
	public static final int DEPART2LIST_RETURNCODE = 1;
	/** 详细界面返回部门界面的返回码 */
	public static final int DETAIL2DEPART_RETURNCODE = 2;
	/** 设备利用率controller */
	public static final String NCAMEquipInUseRationController = "nc.mob.ui.am.controller.NCAMEquipInUseRationController";
	/** 设备故障停机率controller */
	public static final String NCAMStoppagerateController = "nc.mob.ui.am.controller.account.NCAMStoppagerateController";
	/** 设备利用率交易类型 */
	public static final String EquipInUseRation_transitype = "4B06-01";
	/** 消息传递时的数据关键字 */
	public static final String DATA_FIELD = "data";
	/** 消息传递时的日期关键字 */
	public static final String DATE_FIELD = "date";
	/** 消息传递时的标题关键字 */
	public static final String NAME_FIELD = "name";
	/** 消息传递时的类型关键字 */
	public static final String TYPE_FIELD = "type";
	/** 消息传递时的组织主键关键字 */
	public static final String PK_USEDUNIT_FIELD = "pk_usedunit";
	/** 消息传递时的部门主键关键字 */
	public static final String PK_USEDEPT_FIELD = "pk_usedept";
	/** 设备停机率HTML加载完成发送广播的action */
	public static final String STOPPAGE_HTML_LOADDED_ACTION = "com.yonyou.am.mob.stoppage.HTML_LOADDED";

}
