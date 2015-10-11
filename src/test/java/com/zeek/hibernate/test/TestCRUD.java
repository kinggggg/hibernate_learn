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

public class TestCRUD {

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
	public void testInsert(){
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		Customer c = new Customer();
		c.setName("tomas");
		c.setAge(23);
		c.setBirthday(Date.valueOf("1988-12-12"));
		c.setMarried(true);
		c.setDescription("dddddddddddddddddddddddddddddddddddd");
		byte[] bytes;
		try {
			FileInputStream in = new FileInputStream(new File("/Users/weibo_li/Documents/code/hibernate_learn/src/main/java/write.png"));
			bytes = new byte[1024];
			in.read(bytes);
			c.setPhoto(bytes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		session.save(c);
		tx.commit();
		session.close();
	}
	
}