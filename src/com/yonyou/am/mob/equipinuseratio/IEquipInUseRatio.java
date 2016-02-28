package com.yonyou.am.mob.equipinuseratio;

import java.util.Map;

/**
 * ����˵�����豸������ �ӿڡ�ͨ��MA����NC��
 * 
 * @author zhangg
 * @2015-11-21
 */
public interface IEquipInUseRatio {

	/**
	 * �����û���ѯ�����豸������
	 * 
	 * @param groupid
	 * @param userid
	 * @param infoMap
	 * @return
	 */
	public Map<String, Object> queryDapartEquipRationByUser(String groupid, String userid, Map<?, ?> infoMap);

	/**
	 * �����û���ѯ�����豸������
	 * 
	 * @param groupid
	 * @param userid
	 * @param infoMap
	 * @return
	 */
	public Map<String, Object> queryEquipRationByUser(String groupid, String userid, Map<?, ?> infoMap);

	/**
	 * �����û���ѯ��֯�豸������
	 * 
	 * @param groupid
	 * @param userid
	 * @param infoMap
	 * @return
	 */
	public Map<String, Object> queryOrgEquipRationByUser(String groupid, String userid, Map<?, ?> infoMap);
}
