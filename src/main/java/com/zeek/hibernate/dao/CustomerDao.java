package com.zeek.hibernate.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.zeek.hibernate.domain.Customer;
import com.zeek.hibernate.utils.HibernateUtil;

public class CustomerDao {

	public void insertCustomer(Customer c){
		Session s = null;
		Transaction tx = null;
		try {
			s = HibernateUtil.openSession();
			//在高版本的hibernate中无法通过session得到数据库连接的类型。在视频中老师是通过直接打印出由session得到的connection而得到此时connection的
			//类型从而来验证此时配置的c3p0是否生效
			tx = s.beginTransaction();
			s.save(c);
			tx.commit();
			HibernateUtil.closeSession(s);
		} catch (Exception e) {
			HibernateUtil.rollBackTx(tx);
		}
	}
	
	public void updateCustomer(Customer c){
		
	}
	
	public void deleteCustomer(Integer id){
		
	}
	
	public Customer loadCustomer(Integer id){
		return null;
	}
	
	public Customer getCustomer(Integer id){
		return null;
	}
	
	
}
