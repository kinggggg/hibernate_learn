package com.zeek.hibernate.test;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.zeek.hibernate.domain.Area;

/**
 * 测试自关联
 * @author zeek
 *
 */
public class TestSelfRela {

	private static SessionFactory sf = null;
	
	@BeforeClass
	public static void buildSf(){
		Configuration conf = new Configuration();
		conf.addClass(Area.class);
		sf = conf.buildSessionFactory();
	}
	
	@AfterClass
	public static void release(){
		sf.close();
	}
	
	/**
	 * 测试保存
	 */
	@Test
	public void init(){
		Session s = sf.openSession();
		Transaction transaction = s.beginTransaction();
		
		Area a1 = new Area();
		a1.setAreaName("jilinsheng");
		
		Area a2 = new Area();
		a2.setAreaName("changchunshi");
		
		Area a3 = new Area();
		a3.setAreaName("sipingshi");
		
		Area a4 = new Area();
		a4.setAreaName("dehuishi");
		
		a2.setParentArea(a1);
		a3.setParentArea(a1);
		a4.setParentArea(a2);
		
		s.save(a1);
		s.save(a2);
		s.save(a3);
		s.save(a4);
		
		
		transaction.commit();
		s.close();
	}
	
	/**
	 * 测试查询
	 */
	@Test
	public void find(){
		Session s = sf.openSession();
		Transaction transaction = s.beginTransaction();
		
		//这里如果使用load的话，得不到Set中的值????
		Area a = (Area) s.get(Area.class, 1);
		
		
		transaction.commit();
		s.close();
	}
	
}