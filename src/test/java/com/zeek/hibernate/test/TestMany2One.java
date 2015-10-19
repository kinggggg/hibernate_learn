package com.zeek.hibernate.test;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.zeek.hibernate.domain.Customer;
import com.zeek.hibernate.domain.Order;

public class TestMany2One {

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
	 * 创建数据库表
	 * @param args
	 */
	public static void main(String[] args) {
		Configuration conf = new Configuration();
		conf.addClass(Customer.class);
		sf = conf.buildSessionFactory();
	}
	
	/**
	 * 测试多对一关联关系，插入
	 */
	@Test
	public void testInsert(){
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		Customer customer = new Customer();
		Order order = new Order();
		order.setCustomer(customer);
		//当先保存Customer，然后保存order时，hibernate的输出sql语句为：
		/*Hibernate: select max(id) from customers
		Hibernate: insert into customers (name, age, birthday, married, photo, description, id) values (?, ?, ?, ?, ?, ?, ?)
		Hibernate: insert into orders (orderno, price, customer) values (?, ?, ?)*/
		//session.save(customer);
		//session.save(order);
		
		//当先保存Customer，然后保存order时，hibernate的输出sql语句如下。可以看到多出了一个对orders表的更新语句：这是因为如果先保存order的话，order的customer字段
		//引用customers表的id作为其外键，当先保存order的话，此时与此order相关联的customer的还没有确定，因此先将此字段空出来，等保存完customer后，再对刚才保存的order
		//进行一次更新
		/*Hibernate: insert into orders (orderno, price, customer) values (?, ?, ?)
		Hibernate: select max(id) from customers
		Hibernate: insert into customers (name, age, birthday, married, photo, description, id) values (?, ?, ?, ?, ?, ?, ?)
		Hibernate: update orders set orderno=?, price=?, customer=? where id=?*/
		/*session.save(order);
		session.save(customer);*/
		
		//若只是保存了order，而没有保存customer的话，会出现如下的异常
		//org.hibernate.TransientObjectException: object references an unsaved transient instance - save the transient instance before flushing: com.zeek.hibernate.domain.Customer
		//这是因为：当hibernate持久化一个临时对象时，在默认情况下，他不会自动持久化所关联的其他临时对象，会抛出TransientObjectException
		//此时可以通过在Order.hbm.xml中的many-to-one元素中加入属性cascade="save-update"
		session.save(order);
		//session.save(customer);
		
		tx.commit();
		session.close();
	}
	
	/**
	 * 测试多对一关联关系，查询
	 * 配置好关联关系后，查询订单时，hibernate自动把与此订单相关联的用户信息查询出来
	 */
	@Test
	public void testLoadOrder(){
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		Order order = (Order) session.load(Order.class, 2);
		System.out.println(order.getId());
		System.out.println(order.getCustomer().getId());
		tx.commit();
		session.close();
	}
	
	/**
	 * 测试一对多关联关系，查询
	 * 配置好关联关系后，查询客户时，hibernate自动把与此客户相关联的订单信息查询出来
	 */
	@Test
	public void testLoadCustomer(){
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		Customer customer = (Customer) session.load(Customer.class, 1);
		System.out.println(customer.getId());
		System.out.println(customer.getOrders().size());
		tx.commit();
		session.close();
	}
	
	/**
	 * 测试多对一关联关系，插入
	 */
	@Test
	public void testInsert2(){
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		Customer customer = new Customer();
		Order order = new Order();
		customer.getOrders().add(order);
		order.setCustomer(customer);
		session.save(order);
		session.save(customer);
		
		tx.commit();
		session.close();
	}
	/**
	 * 测试多对一关联关系，插入
	 * 在set元素上也可以采取级联操作
	 */
	@Test
	public void testInsert3(){
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		Customer customer = new Customer();
		Order order = new Order();
		customer.getOrders().add(order);
		order.setCustomer(customer);
		session.save(customer);
		//session.save(order);
		
		tx.commit();
		session.close();
	}
	
	
}