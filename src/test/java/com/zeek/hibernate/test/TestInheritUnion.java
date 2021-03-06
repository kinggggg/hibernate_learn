package com.zeek.hibernate.test;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.zeek.hibernate.domain.inherit.union.Employee;
import com.zeek.hibernate.domain.inherit.union.HourEmployee;
import com.zeek.hibernate.domain.inherit.union.SalaryEmployee;

/**
 * 映射继承关系树--每个类对应自己的表，每个表包含自身全部的信息，记录在所有的表中仍然具备唯一性
 * @author zeek
 *
 */
public class TestInheritUnion {

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
		List list = s.createQuery(hql).list();
		System.out.println(list.size());
		transaction.commit();
		s.close();
	}
	
	/**
	 * 删除2号Employee
	 */
	@Test
	public void deleteEmployee(){
		Session s = sf.openSession();
		Transaction transaction = s.beginTransaction();
		Employee employee = (Employee) s.load(Employee.class, 2);
		s.delete(employee);
		transaction.commit();
		s.close();
	}
	
}