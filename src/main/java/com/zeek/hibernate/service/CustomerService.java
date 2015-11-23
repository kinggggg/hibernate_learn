package com.zeek.hibernate.service;

import com.zeek.hibernate.dao.CustomerDao2;
import com.zeek.hibernate.dao.OrderDao2;
import com.zeek.hibernate.domain.Customer;
import com.zeek.hibernate.domain.Order;
import com.zeek.hibernate.utils.HibernateUtil;

public class CustomerService {
	
	private CustomerDao2 cDao = new CustomerDao2();
	private OrderDao2 oDao = new OrderDao2();
	
	public void saveData(){
		HibernateUtil.currentSession().beginTransaction();
		Customer c = new Customer();
		c.setAge(44);
		Order o = new Order();
		o.setPrice(23f);
		c.getOrders().add(o);
		o.setCustomer(c);
		cDao.insertCustomer(c);
		oDao.insertOrder(o);
		HibernateUtil.currentSession().getTransaction().commit();
		HibernateUtil.currentSession().close();
	}

}
