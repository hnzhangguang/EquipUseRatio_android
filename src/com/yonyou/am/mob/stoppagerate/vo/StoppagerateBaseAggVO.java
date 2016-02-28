package com.yonyou.am.mob.stoppagerate.vo;

import java.util.List;

/**
 * 故障停机率节点使用的数据类-聚合VO，包含一主多子
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
