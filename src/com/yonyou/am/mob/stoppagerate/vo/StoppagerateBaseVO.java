package com.yonyou.am.mob.stoppagerate.vo;

/**
 * ����ͣ���ʽڵ�ʹ�õ�������
 * 
 * @author hujieh
 * 
 */
public class StoppagerateBaseVO {
	private String pk;
	private String name;
	private String code;

	public String getPk() {
		return pk;
	}

	public void setPk(String pk) {
		this.pk = pk;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getPercent() {
		return percent;
	}

	public void setPercent(String percent) {
		this.percent = percent;
	}

	private String percent;

}
