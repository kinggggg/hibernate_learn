package com.zeek.hibernate.test;

import org.junit.Test;

import com.zeek.hibernate.service.CustomerService;


/**
 * 测试自关联
 * @author zeek
 *
 */
public class TestCustomerService {
	
	@Test
	public void saveData(){
		CustomerService cService = new CustomerService();
		cService.saveData();
	}
	

}