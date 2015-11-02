package com.zeek.hibernate.test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.zeek.hibernate.domain.Customer;
import com.zeek.hibernate.domain.Order;

/**
 * 测试检索方式
 * @author zeek
 *
 */
public class TestRetrieveWay {

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
	@SuppressWarnings({ "unused", "rawtypes" })
	@Test
	public void init(){
		Session s = sf.openSession();
		Transaction transaction = s.beginTransaction();
		
		//hql
		String hql = "from Customer c where c.name like 't%' and age = 20";
		Query query = s.createQuery(hql);
		List list = query.list();
		System.out.println(list.size());
		
		//qbc
		//创建查询语句
		Criteria cra = s.createCriteria(Customer.class);
		//创建查询条件
		Criterion crn1 = Restrictions.like("name", "t%");
		Criterion crn2 = Restrictions.eq("age", 20);
		Criterion crn3 = Restrictions.and(crn1,crn2);
		List list2 = cra.add(crn3).list();
		
		Set set = new HashSet(list2);
		System.out.println(set.size());
		
		//多态查询：查询所有该类以及该类的子类所对应的表中的所有数据
		List list3 = s.createQuery("from java.lang.Object").list();
		
		//多态查询：查询所有实现该接口的类所对应的表中的数据
		List list4 = s.createQuery("from java.io.Serializable").list();
		
		//hql排序
		List list5 = s.createQuery("from Order o order by o.id desc").list();
		
		//qbc排序
		List list6 = s.createCriteria(Order.class).addOrder(org.hibernate.criterion.Order.desc("id")).list();
		
		//hql分页查询
		List list7 = s.createQuery("from Order").setFirstResult(10).setMaxResults(10).list();
		
		//qbc分页查询
		List list8 = s.createCriteria(Order.class).setFirstResult(10).setMaxResults(10).list();
		
		//单值检索：查询结果有且仅有一条记录，这个等价与list().get(0).只不过是hibernate为我们又封装了下，更为方便
		Customer c = (Customer) s.createQuery("from Customer c where c.id = 1").uniqueResult();
		System.out.println(c.getId() + " " + c.getName() );
		
		//qbc单值检索
		Customer cc = (Customer) s.createCriteria(Customer.class).add(Restrictions.eq("id", 1)).uniqueResult();
		System.out.println(cc.getId() + " " + c.getName());
		
		//hql绑定参数：按照参数名称进行绑定
		hql = "from Customer c where c.name = :a and c.age = :b" ;
		List list9 = s.createQuery(hql).setString("a", "t,om").setInteger("b", 20).list();
		System.out.println(list9.size());
		
		//hql绑定参数：按照参数索引位置进行绑定.索引值从0开始（JDBC的索引值从1开始）
		hql = "from Customer c where c.name = ? and c.age = ?" ;
		list9 = s.createQuery(hql).setString(0, "t,om").setInteger(1, 20).list();
		System.out.println(list9.size());
		
		//命名查询
		Query namedQuery = s.getNamedQuery("findCustomerByName");
		namedQuery.setString(0, "t,om");
		List list10 = namedQuery.list();
		System.out.println(list10.size());
		
		/**
		 * 左外连接和迫切左外连接的区别
		 */
		//左外连接，得到的list集合中的元素类型为对象数组，并且其中的一个元素是Customer类型，另外一个元素是Order类型
		hql = "from Customer c left outer join c.orders" ;
		list10 = s.createQuery(hql).list();
		//迫切左外连接，得到的list集合中的元素类型是Customer类型
		hql = "from Customer c left outer join fetch c.orders" ;
		list10 = s.createQuery(hql).list(); 
		
		//投影查询：简单的说就是只是查询指定的属性（字段）。需要注意的时，此时list集合中的元素不再是Customer对象，而是变成一个一个对象数组。对于这个查询语句来说对象数组含有两个元素一个是String类型，一个是Integer类型
		hql = "select c.name, c.age from Customer c" ;
		list10 = s.createQuery(hql).list();
		
		//聚集函数查询,max,min等等
		hql = "select count(*) from Order" ;
		s.createQuery(hql).uniqueResult();
		
		transaction.commit();
		s.close();
	}
	
}