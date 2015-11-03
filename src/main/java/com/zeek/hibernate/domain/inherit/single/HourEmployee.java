package com.zeek.hibernate.domain.inherit.single;

/**
 * 映射继承关系树--单表策略
 * @author weibo_li
 *
 */
public class HourEmployee extends Employee {
	private float rate;

	public float getRate() {
		return rate;
	}

	public void setRate(float rate) {
		this.rate = rate;
	}
	
}
