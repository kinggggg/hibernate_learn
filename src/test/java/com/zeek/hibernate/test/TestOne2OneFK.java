package com.zeek.hibernate.test;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.zeek.hibernate.domain.one2one.fk.Addr;
import com.zeek.hibernate.domain.one2one.fk.User;

/**
 * 测试一对一关联关系，之外键关联
 * @author zeek
 *
 */
public class TestOne2OneFK {

	private static SessionFactory sf = null;
	
	@BeforeClass
	public static void buildSf(){
		Configuration conf = new Configuration();
		conf.addClass(User.class);
		conf.addClass(Addr.class);
		sf = conf.buildSessionFactory();
	}
	
	@AfterClass
	public static void release(){
		sf.close();
	}
	
	@Test
	public void insert(){
		Session s = sf.openSession();
		Transaction transaction = s.beginTransaction();
		User u = new User();
		Addr a = new Addr();
		
		u.setAddr(a);
		a.setUser(u);
		s.save(a);
		s.save(u);
		
		transaction.commit();
		s.close();
	}
	
	@Test
	/**
	 * 经测试发现，不能插入一个新的用户，错误信息说：Duplicate entry '1' for key 'UK_26smr7war5jdkafu5ik7iwr9e'
	 * 证明在user表的addrid字段加的唯一性约束起到了作用
	 */
	public void realOne2One(){
		Session s = sf.openSession();
		Transaction transaction = s.beginTransaction();
		Addr a = (Addr) s.load(Addr.class, 1);
		User u = new User();
		
		u.setAddr(a);
		a.setUser(u);
		s.save(u);
		
		transaction.commit();
		s.close();
	}
}