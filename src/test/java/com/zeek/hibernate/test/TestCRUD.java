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
	/**
	 * 根据id查询客户
	 * @throws Exception
	 */
	@Test
	public void findCustomerById() throws Exception{
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		Customer c = (Customer) session.load(Customer.class, 3);
		System.out.println(c.getName());
		byte[] photo = c.getPhoto();
		FileOutputStream fos = new FileOutputStream("/Users/weibo_li/Downloads/write.png");
		fos.write(photo);
		fos.close();
		tx.commit();
		session.close();
	}
	
	/**
	 */
	@Test
	public void updateCustomer(){
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		Customer c = (Customer) session.load(Customer.class, 3);
		c.setName("jerry");
		tx.commit();
		session.close();
	}
	
	/**
	 */
	@Test
	public void updateCustomer2(){
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		Customer c = new Customer();
		c.setId(3);
		c.setName("jerry");
		session.update(c);
		tx.commit();
		session.close();
	}
	
	/**
	 * 查询所有客户
	 */
	@Test
	public void findAllCustomer(){
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		//hql: hibernate query language. 当中的Customer是类名，不是表名
		String hql = "from Customer" ;
		Query query = session.createQuery(hql);
		List<Customer> list = query.list();
		for(Customer c : list){
			System.out.println(c.getName());
		}
	}
	
	/**
	 * 根据id删除客户
	 */
	@Test
	public void testDelete(){
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		Customer c = (Customer) session.load(Customer.class, 2);
		session.delete(c);
		tx.commit();
		session.close();
	}
	
	/**
	 * 批量删除客户
	 */
	@Test
	public void testDeleteBatch(){
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		String hql = "delete from Customer c where c.name like 't%'" ;
		Query query = session.createQuery(hql);
		query.executeUpdate();
		tx.commit();
		session.close();
	}
}