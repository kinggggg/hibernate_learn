package com.zeek.hibernate.domain.inherit.union;

/**
 * 映射继承关系树--每个类对应一张表
 * @author weibo_li
 *
 */
public class SalaryEmployee extends Employee {

	private float salary;

	public float getSalary() {
		return salary;
	}

	public void setSalary(float salary) {
		this.salary = salary;
	}
	
}
