package com.zeek.hibernate.test;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.zeek.hibernate.domain.Customer;

/**
 * 测试自关联
 * @author zeek
 *
 */
public class TestCfgXml {

	private static SessionFactory sf = null;
	
	@BeforeClass
	public static void buildSf(){
		sf = new Configuration().configure().buildSessionFactory();
	}
	
	@AfterClass
	public static void release(){
		sf.close();
	}
	
	/**
	 * 之前所说的在hql语句中使用的是类的名称，比如说下面的这个hql语句
	 * from Customer
	 * 中Customer指的是类的名称，而不是表的名称，其实这样说是不准确的！准确的说上面hql中Customer应该是hibernate配置文件中
	 * entity-name的名称。当hibernate读取其配置文件时（对本程序来说指的是位于classpath路径下的hibernate.cfg.xml文件）不允许
	 * 相同的entity-name存在；而如果class元素没有配置entity-name属性的话，默认的值就是class类的名称；由于这个程序为测试练习程序
	 * 因此在不同的包中存在名称一样的类，因此此时hibernate加载hibernate.cfg.xml时会报告错误；但是在实际的开发中对于类的名称相同
	 * 的情况是很少了因此一般来说都很少关注entity-name这个属性
	 * 使用hibernate.cfg.xml作为hibernate的配置文件后，测试保存
	 */
	@Test
	public void init(){
		Session s = sf.openSession();
		Transaction transaction = s.beginTransaction();
		
		Customer c = new Customer();
		s.save(c);
		
		transaction.commit();
		s.close();
	}
	
}