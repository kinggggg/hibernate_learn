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
	
	/**
	 * 测试cascade属性
	 */
	@Test
	public void testCascade(){
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		Customer customer = new Customer();
		Order order1 = new Order();
		Order order2 = new Order();
		Order order3 = new Order();
		
		order1.setCustomer(customer);
		customer.getOrders().add(order2);
		customer.getOrders().add(order3);
		//前提：在两端均配置了级联cascade
		
		//数据库一共保存4条记录：当save order1时，由于customer与order1相关联，故也save customer；当save customer时，由于order2月order3与之相关联，故也save 
		//order2和order3
		/*Hibernate: insert into customers (name, age, birthday, married, photo, description, id) values (?, ?, ?, ?, ?, ?, ?)
		Hibernate: insert into orders (orderno, price, cid) values (?, ?, ?)
		Hibernate: insert into orders (orderno, price, cid) values (?, ?, ?)
		Hibernate: insert into orders (orderno, price, cid) values (?, ?, ?)*/
		//session.save(order1);
		
		//数据库一共保存3条记录
		/*Hibernate: insert into customers (name, age, birthday, married, photo, description, id) values (?, ?, ?, ?, ?, ?, ?)
		Hibernate: insert into orders (orderno, price, cid) values (?, ?, ?)
		Hibernate: insert into orders (orderno, price, cid) values (?, ?, ?)*/
		//session.save(customer);
		
		//保存一条记录
		//Hibernate: insert into orders (orderno, price, cid) values (?, ?, ?)
		session.save(order2);
		tx.commit();
		session.close();
	}
	
	/**
	 * 测试改变关联关系
	 */
	@Test
	public void testChangeRela(){
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		Customer customer = (Customer) session.load(Customer.class, 1);
		Order order = (Order) session.load(Order.class, 10);
		customer.getOrders().add(order);
		order.setCustomer(customer);
		//在一的一端Customer端没有配置inverter=true后，表明hibernate不再监控customer对象中orders集合的变化，只监控order对象中customer属性的变化：也即此时
		//关系的维护由order端负责，此时hibernate只执行一次更新，性能提升
		tx.commit();
		session.close();
	}
	
	@Test
	public void testChangeRela2(){
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		Customer customer = (Customer) session.load(Customer.class, 1);
		Order order = (Order) session.load(Order.class, 10);
		//由于在一的一端Customer端配置了inverter="true"因此改变关系的话，只能通过order.setCustomer(customer);代码来改变关系
		order.setCustomer(customer);
		//customer.getOrders().add(order);//这行代码不能改变关联关系
		tx.commit();
		session.close();
	}
	
	
	@Test
	public void testRemoveRela(){
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		Customer customer = (Customer) session.load(Customer.class, 1);
		Order order = (Order) session.load(Order.class, 9);
		//由于在一端Customer端已经配置inverter=true，若要删除两者之间的关系的话，可以通过order.setCustomer(null);来删除关系
		//当然也可执行两个语句，只不过，执行customer.getOrders().remove(order);时没有什么影响
		//order.setCustomer(null);
		customer.getOrders().remove(order);
		tx.commit();
		session.close();
	}
	
	
	
	
}