package com.yonyou.am.mob.stoppagerate.vo;

import java.util.List;

/**
 * ����ͣ���ʽڵ�ʹ�õ�������-�ۺ�VO������һ������
 * 
 * @author hujieh
 * 
 */
public class StoppagerateBaseAggVO {

	private StoppagerateBaseVO parent;
	private List<StoppagerateBaseVO> childs;

	public StoppagerateBaseVO getParent() {
		return parent;
	}

	public void setParent(StoppagerateBaseVO parent) {
		this.parent = parent;
	}

	public List<StoppagerateBaseVO> getChilds() {
		return childs;
	}

	public void setChilds(List<StoppagerateBaseVO> childs) {
		this.childs = childs;
	}

}
