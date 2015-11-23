package com.zeek.hibernate.test;

import org.junit.Test;

import com.zeek.hibernate.dao.CustomerDao;
import com.zeek.hibernate.domain.Customer;


/**
 * 测试自关联
 * @author zeek
 *
 */
public class TestCustomerDao {
	
	/**
	 * 测试插入
	 */
	@Test
	public void insert(){
		CustomerDao dao = new CustomerDao();
		Customer c = new Customer();
		c.setAge(23);
		dao.insertCustomer(c);
	}

}