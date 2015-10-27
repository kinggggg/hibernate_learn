package com.zeek.hibernate.test;

import java.util.Set;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.zeek.hibernate.domain.Customer;
import com.zeek.hibernate.domain.Order;

/**
 * 测试检索策略
 * @author zeek
 *
 */
public class TestRetrievePolicy {

	private static SessionFactory sf = null;
	
	@BeforeClass
	public static void buildSf(){
		Configuration conf = new Configuration();
		conf.addClass(Customer.class);
		conf.addClass(Order.class);
		sf = conf.buildSessionFactory();
	}
	
	@AfterClass
	public static void release(){
		sf.close();
	}
	
	/**
	 * 初始化数据
	 */
	@Test
	public void insert(){
		Session s = sf.openSession();
		Transaction transaction = s.beginTransaction();
		
		Customer c = new Customer();
		c.setAge(20);
		
		s.save(c);
		Order o = null;
		for(int i = 0; i < 10; i++){
			o = new Order();
			o.setId(+ i);
			o.setPrice(Float.valueOf(20+i));
			c.getOrders().add(o);
			o.setCustomer(c);
			s.save(o);
		}
		
		transaction.commit();
		s.close();
	}
	
}