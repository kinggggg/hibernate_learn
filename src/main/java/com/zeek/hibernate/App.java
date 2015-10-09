package com.zeek.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.zeek.hibernate.domain.Customer;

public class App {
	public static void main(String[] args) {
		//测试
		//默认使用位于src下的名称为hibernate.properties文件
		Configuration conf = new Configuration();
		//加载Customer.hbm.xml文件
		conf.addClass(Customer.class);
		//当hibernate配置文件的名称不是Customer.hbm.xml(一般的格式为  "实体的名称.hbm.xml")时，可以通过如下的方式加载配置文件
		//conf.addResource("com/zeek/hibernate/domain/c.hbm.xml");
		//构建会话工厂，相当于数据源，相当于连接池
		SessionFactory sf = conf.buildSessionFactory();
		//开启会话（会话相当于连接Connection）
		Session session = sf.openSession();
		//开启事务
		Transaction transaction = session.beginTransaction();
		Customer customer = new Customer();
		customer.setName("tom");
		session.save(customer);
		transaction.commit();
		session.close();
		sf.close();
	}

}
