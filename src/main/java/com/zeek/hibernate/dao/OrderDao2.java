package com.zeek.hibernate.dao;

import com.zeek.hibernate.domain.Order;
import com.zeek.hibernate.utils.HibernateUtil;

public class OrderDao2 {
	
	public void insertOrder(Order order){
		HibernateUtil.currentSession().save(order);
	}

}
