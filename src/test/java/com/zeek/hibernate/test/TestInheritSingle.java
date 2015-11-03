package com.zeek.hibernate.test;

import java.util.List;
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
import com.zeek.hibernate.domain.inherit.single.Employee;
import com.zeek.hibernate.domain.inherit.single.HourEmployee;
import com.zeek.hibernate.domain.inherit.single.SalaryEmployee;

/**
 * 映射继承关系树测试，即，在hibernate中如何配置存在继承关系的java类
 * @author zeek
 *
 */
public class TestInheritSingle {

	private static SessionFactory sf = null;
	
	@BeforeClass
	public static void buildSf(){
		Configuration conf = new Configuration();
		conf.addClass(Employee.class);
		sf = conf.buildSessionFactory();
	}
	
	@AfterClass
	public static void release(){
		sf.close();
	}
	
	/**
	 * 测试插入
	 */
	@Test
	public void insert(){
		Session s = sf.openSession();
		Transaction transaction = s.beginTransaction();
		Employee ee = new Employee();
		HourEmployee he = new HourEmployee();
		SalaryEmployee se = new SalaryEmployee();
		s.save(ee);
		s.save(he);
		s.save(se);
		
		transaction.commit();
		s.close();
	}
	
	/**
	 * 查询所有的钟点工
	 */
	@Test
	public void findAllHourEmployee(){
		Session s = sf.openSession();
		Transaction transaction = s.beginTransaction();
		String hql = "from HourEmployee" ;
		//生成的sql语句自动加上“houremploy0_.etype='he'”，也即此时只是查询所有的钟点工数据，数量为1
		List list = s.createQuery(hql).list();
		System.out.println(list.size());
		transaction.commit();
		s.close();
	}
	
	/**
	 * 查询所有的员工
	 */
	@Test
	public void findAllEmployee(){
		Session s = sf.openSession();
		Transaction transaction = s.beginTransaction();
		String hql = "from Employee" ;
		//也即此时查询所有的Employee及其子类在数据库中的数据，对于这个例子来书，是查询所有的员工信息，数量为3
		List list = s.createQuery(hql).list();
		System.out.println(list.size());
		transaction.commit();
		s.close();
	}
	
}