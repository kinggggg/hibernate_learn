package com.zeek.hibernate.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.zeek.hibernate.domain.Customer;

public class TestAccess {

	private static SessionFactory sf = null;
	
	@BeforeClass
	public static void buildSf(){
		Configuration conf = new Configuration();
		conf.addClass(Customer.class);
		sf = conf.buildSessionFactory();
	}
	
	@AfterClass
	public static void release(){
		sf.close();
	}
	
	/**
	 * 保存客户
	 */
	@Test
	public void insert(){
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		Customer c = new Customer();
		c.setFname("t");
		c.setLname("om");
		session.save(c);
		tx.commit();
		session.close();
	}
	
	@Test
	public void find(){
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		Customer c = (Customer) session.get(Customer.class, 1);
		System.out.println(c.getFname());
		System.out.println(c.getLname());
		tx.commit();
		session.close();
	}
}