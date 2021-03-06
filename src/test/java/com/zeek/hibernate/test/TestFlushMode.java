package com.zeek.hibernate.test;

import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.zeek.hibernate.domain.Customer;
import com.zeek.hibernate.domain.Order;

public class TestFlushMode {

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
	 * 测试缓存清理模式：never
	 * 当清理模式为never时，根据表格，只有在显示调用session.flush()方法时，此清理缓存，因此此时不像数据库中插入数据
	 */
	@Test
	public void testNever(){ 
		Session session = sf.openSession();
		session.setFlushMode(FlushMode.NEVER);
		Transaction tx = session.beginTransaction();
		Customer c = new Customer();
		c.setAge(21);
		session.save(c);
		tx.commit();
		session.close();
	}
	
	/**
	 * 测试缓存清理模式：never
	 * 当清理模式为never时，根据表格，只有在显示调用session.flush()方法时，此清理缓存，因此此时不像数据库中插入数据，必须显示调用flush方法此时才
	 * 清理缓存
	 */
	@Test
	public void testNever2(){ 
		Session session = sf.openSession();
		session.setFlushMode(FlushMode.NEVER);
		Transaction tx = session.beginTransaction();
		Customer c = new Customer();
		c.setAge(21);
		session.save(c);
		session.flush();
		tx.commit();
		session.close();
	}
	
	/**
	 * 清理模式受到主键生成策略的限制：当主键生成策略为identity（即，依赖数据库本省递增）时，清理缓存是不受缓存清理模式影响的：只要执行save方法马上插入数据
	 */
	@Test
	public void testNever3(){ 
		Session session = sf.openSession();
		session.setFlushMode(FlushMode.NEVER);
		Transaction tx = session.beginTransaction();
		Order o = new Order();
		session.save(o);
		session.flush();
		tx.commit();
		session.close();
	}
	
	@Test
	public void testPersit(){ 
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		Customer c = (Customer) session.load(Customer.class, 3);
		c.setAge(33);
		//session.evict(c);
		session.update(c);
		tx.commit();
		session.close();
	}
	
}