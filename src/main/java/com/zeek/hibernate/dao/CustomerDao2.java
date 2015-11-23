package com.zeek.hibernate.dao;

import com.zeek.hibernate.domain.Customer;
import com.zeek.hibernate.utils.HibernateUtil;

public class CustomerDao2 {

	public void insertCustomer(Customer c){
		HibernateUtil.currentSession().save(c);
	}
}
