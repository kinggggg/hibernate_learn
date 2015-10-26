package com.zeek.hibernate.test;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.zeek.hibernate.domain.Address;
import com.zeek.hibernate.domain.Customer;
import com.zeek.hibernate.domain.Order;

/**
 * 测试组成关系
 * @author zeek
 *
 */
public class TestComponent {

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
	 * 保存客户
	 */
	@Test
	public void testInsert(){
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		Customer c = new Customer();
		Address homeAddress = new Address();
		homeAddress.setProvince("jinlin");
		homeAddress.setCity("changchun");
		
		Address comAddress = new Address();
		comAddress.setProvince("liaoning");
		comAddress.setCity("dalian");
		
		c.setHomeAddress(homeAddress);
		c.setComAddress(comAddress);
		
		session.save(c);
		tx.commit();
		session.close();
	}
	
	/**
	 * 测试查询
	 */
	@Test
	public void testLoad(){
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		Customer c = (Customer) session.load(Customer.class, 5);
		Address com = c.getComAddress();
		/**
		 * 若与Customer中comAddress对应的数据库字段的值全部为null的话，此时得到的comAddress属性值就为null！
		 * 虽然在Customer中定义comAddress属性已经new出了一个对象，但是此时还是为null
		 */
		if(com == null){
			com = new Address();
			//仅仅是重新new出一个Address对象是不行的，因为此时新new出的这个对象没有和c对象进行关联，因此还需要执行下面的一行代码
			c.setComAddress(com);
		}
		com.setProvince("pp");
		com.setCity("cc");
		System.out.println(c);
		tx.commit();
		session.close();
	}
}