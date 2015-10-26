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
	
}