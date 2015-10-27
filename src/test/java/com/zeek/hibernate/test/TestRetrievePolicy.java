package com.zeek.hibernate.test;

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

/**
 * 测试检索策略
 * @author zeek
 *
 */
public class TestRetrievePolicy {

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
	 * 初始化数据
	 */
	@Test
	public void insert(){
		Session s = sf.openSession();
		Transaction transaction = s.beginTransaction();
		
		Customer c = new Customer();
		c.setAge(20);
		
		s.save(c);
		Order o = null;
		for(int i = 0; i < 10; i++){
			o = new Order();
			o.setId(+ i);
			o.setPrice(Float.valueOf(20+i));
			c.getOrders().add(o);
			o.setCustomer(c);
			s.save(o);
		}
		
		transaction.commit();
		s.close();
	}
	
	/**
	 * 测试类级别检索策略
	 * 说明如何产生懒加载异常
	 */
	@Test
	public void classLoad(){
		Session s = sf.openSession();
		Transaction transaction = s.beginTransaction();
		
		/**
		 * 其实此时得到的c对象的真实类型不是Customer类型的，而是Customer类型的一个代理对象。
		 * 如果此时在hibernate的配置文件中设置这个类的类级别的检索策略为lazy=true的话，
		 * 此时hibernate并没有对c对象（前面已经说过了其实是Customer类的一个代理对象）
		 * 完成初始化（将有效的属性值比如说age的值设置到c对象中）工作
		 */
		Customer c = (Customer) s.load(Customer.class, 1);
		
		transaction.commit();
		s.close();
		/**
		 * 这样的话，会产生 懒加载异常.当session关闭后，若此时通过c对象访问有效属性（所谓有效属性是指，必须通过查询数据库的方式才可以得到值的属性）时，
		 * 此时这个代理对象才真正向数据库发出查询请求来查询相应的值，但是此时session已经关闭，因此会出现懒加载异常：
	     * org.hibernate.LazyInitializationException
		 */
		c.getAge();
	}
	
	/**
	 * 测试类级别检索策略
	 * 如何解决产生懒加载异常的问题
	 */
	@Test
	public void classLoad2(){
		Session s = sf.openSession();
		Transaction transaction = s.beginTransaction();
		
		/**
		 * 其实此时得到的c对象的真实类型不是Customer类型的，而是Customer类型的一个代理对象。
		 * 如果此时在hibernate的配置文件中设置这个类的类级别的检索策略为lazy=true的话，
		 * 此时hibernate并没有对c对象（前面已经说过了其实是Customer类的一个代理对象）
		 * 完成初始化（将有效的属性值比如说age的值设置到c对象中）工作
		 */
		Customer c = (Customer) s.load(Customer.class, 1);
		
		if(!Hibernate.isInitialized(c)){//测试代理对象c是否已经初始化，没有初始化的话，才进行初始化
			/**
			 * 其实就相当于调用了c.getAge()方法，让代理对象初始化。当代理对象初始化后，虽然关闭session，但是此时代理对象已经初始化，就不再需要访问数据库
			 * 因此就不要session，就不会发生懒加载异常的情况
			 */
			Hibernate.initialize(c);
		}
		
		transaction.commit();
		s.close();
		/**
		 * 这样的话，会产生 懒加载异常.当session关闭后，若此时通过c对象访问有效属性（所谓有效属性是指，必须通过查询数据库的方式才可以得到值的属性）时，
		 * 此时这个代理对象才真正向数据库发出查询请求来查询相应的值，但是此时session已经关闭，因此会出现懒加载异常：
	     * org.hibernate.LazyInitializationException
		 */
		c.getAge();
	}
	
	/**
	 * 测试类级别检索策略，用get方法，get不受影响（<class lazy="true">）
	 */
	@Test
	public void getEntity(){
		Session s = sf.openSession();
		Transaction transaction = s.beginTransaction();
		
		//类级别的检索策略设置不影响get方法：使用get方法时，马上查询数据库，而不管此时类级别检索策略是什么
		Customer c = (Customer) s.get(Customer.class, 1);
		if(!Hibernate.isInitialized(c)){
			Hibernate.initialize(c);
		}
		c.getAge();
		transaction.commit();
		s.close();
	}
	
	/**
	 * 测试关联级别检索策略，set lazy=false
	 * 
	 */
	@Test
	public void setImmi(){
		Session s = sf.openSession();
		Transaction transaction = s.beginTransaction();
		
		//此时不仅仅查询客户信息，也查询出与此客户对应的订单信息
		Customer c = (Customer) s.get(Customer.class, 1);
		transaction.commit();
		s.close();
	}
	
	/**
	 * 测试关联级别检索策略，set lazy=true
	 * 
	 */
	@Test
	public void setLazy(){
		Session s = sf.openSession();
		Transaction transaction = s.beginTransaction();
		
		//此时只是查询客户信息
		Customer c = (Customer) s.get(Customer.class, 1);
		Set<Order> orders = c.getOrders();//不查询order信息
		if(!Hibernate.isInitialized(orders)){//先判断orders是否已经初始化
			Hibernate.initialize(orders);//这个与调用c.getOrders().size()方法效果是一样的，c.getOrders().size()是访问属性的有效值，因此必须查询数据库才可以得到
		}
		c.getOrders().size();
		transaction.commit();
		s.close();
	}
	
	/**
	 * 设置set为迫切左外连接查询，这样就使用一条sql把所有的信息查询出，提高了数据库访问效率
	 */
	@Test
	public void setLazyJoin(){
		Session s = sf.openSession();
		Transaction transaction = s.beginTransaction();
		
		//无论是立即检索还是延迟检索，每一次查询的sql语句都只是查询一个表的信息，
		//虽然立即检索和延迟检索查询的时机不一样，但毕竟每一次查询的sql语句都只是查询一个表的信息
		//通过左外连接查询，就可以向数据库发送一条sql语句，而把所有的信息查询出来，这样就大大提高了查询的效率
		Customer c = (Customer) s.get(Customer.class, 1);
		Set<Order> orders = c.getOrders();
		if(!Hibernate.isInitialized(orders)){
			Hibernate.initialize(orders);
		}
		c.getOrders().size();
		transaction.commit();
		s.close();
	}
	
}