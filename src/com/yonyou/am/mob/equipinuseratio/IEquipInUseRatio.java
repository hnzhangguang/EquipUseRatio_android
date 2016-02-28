package com.yonyou.am.mob.equipinuseratio;

import java.util.Map;

/**
 * 功能说明：设备利用率 接口【通过MA代用NC】
 * 
 * @author zhangg
 * @2015-11-21
 */
public interface IEquipInUseRatio {

	/**
	 * 根据用户查询部门设备利用率
	 * 
	 * @param groupid
	 * @param userid
	 * @param infoMap
	 * @return
	 */
	public Map<String, Object> queryDapartEquipRationByUser(String groupid, String userid, Map<?, ?> infoMap);

	/**
	 * 根据用户查询具体设备利用率
	 * 
	 * @param groupid
	 * @param userid
	 * @param infoMap
	 * @return
	 */
	public Map<String, Object> queryEquipRationByUser(String groupid, String userid, Map<?, ?> infoMap);

	/**
	 * 根据用户查询组织设备利用率
	 * 
	 * @param groupid
	 * @param userid
	 * @param infoMap
	 * @return
	 */
	public Map<String, Object> queryOrgEquipRationByUser(String groupid, String userid, Map<?, ?> infoMap);
}
