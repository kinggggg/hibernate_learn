package com.zeek.hibernate.test;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.zeek.hibernate.domain.many2many.Student;
import com.zeek.hibernate.domain.many2many.Teacher;
import com.zeek.hibernate.domain.one2one.fk.Addr;
import com.zeek.hibernate.domain.one2one.fk.User;

/**
 * 测试多对多关联关系
 * @author zeek
 *
 */
public class TestMany2Many {

	private static SessionFactory sf = null;
	
	@BeforeClass
	public static void buildSf(){
		Configuration conf = new Configuration();
		conf.addClass(Student.class);
		conf.addClass(Teacher.class);
		sf = conf.buildSessionFactory();
	}
	
	@AfterClass
	public static void release(){
		sf.close();
	}
	
	@Test
	public void insert(){
		Session s = sf.openSession();
		Transaction transaction = s.beginTransaction();
		Teacher t1 = new Teacher();
		Teacher t2 = new Teacher();
		
		Student s1 = new Student();
		Student s2 = new Student();
		Student s3 = new Student();
		Student s4 = new Student();
		
		//注释了这里也是可以保存成功的:这是因为是由Student来维护关系的
		/*t1.addStudents(s1, s2, s3);
		t2.addStudents(s2, s3, s4);*/
		
		s1.addTeachers(t1);
		s2.addTeachers(t1, t2);
		s3.addTeachers(t1, t2);
		s4.addTeachers(t2);
		
		
		s.save(t1);
		s.save(t2);
		s.save(s1);
		s.save(s2);
		s.save(s3);
		s.save(s4);
		
		transaction.commit();
		s.close();
	}
	
}