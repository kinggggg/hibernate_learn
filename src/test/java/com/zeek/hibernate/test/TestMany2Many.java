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
	
	/**
	 * 改变关联关系
	 * 对于多对多的关联关系，links表，hibernate只能删除，增加这个表的记录，而不能改变某条记录的信息。
	 * 改变关联关系时，hibernate首先将旧的关联关系删除，然后再建立新的关联关系
	 */
	@Test
	public void modifyRela(){
		Session s = sf.openSession();
		Transaction transaction = s.beginTransaction();
		
		Teacher t1 = (Teacher) s.load(Teacher.class, 1);
		Teacher t2 = (Teacher) s.load(Teacher.class, 2);
		
		Student s1 = (Student) s.load(Student.class, 4);
		
		s1.getTeas().remove(t2);
		s1.getTeas().add(t1);
		
		//由于是由Student端的teas维护关系，因此下面的两行可以不写
		/*t1.getStus().remove(s1);
		t2.getStus().add(s1);*/
		
		transaction.commit();
		s.close();
	}
	
	/**
	 * 删除关系
	 */
	@Test
	public void deleteRela1(){
		Session s = sf.openSession();
		Transaction transaction = s.beginTransaction();
		
		Teacher t1 = (Teacher) s.load(Teacher.class, 1);
		
		Student s1 = (Student) s.load(Student.class, 1);
		
		//由于是由Student端的teas维护关系，因此虽然执行下面注释的两个不报告错误，但是无法删除关联关系
		/*t1.getStus().remove(s1);
		s.save(t1);*/
		
		//通过下面的两行可以删除相应的关联关系
		s1.getTeas().remove(t1);
		
		//由于更新缓存策略FlushMode默认为AUTO，因此在commit时自动刷新缓存
		transaction.commit();
		s.close();
	}
	
	/**
	 * 删除关系
	 */
	@Test
	public void deleteStu(){
		Session s = sf.openSession();
		Transaction transaction = s.beginTransaction();
		
		Student s2 = (Student) s.load(Student.class, 2);
		/**
		 * 当执行session的delete方法时，hibernate首先将s2对象从session一级缓存缓存中删除，然后再将数据库中对应的记录删除；
		 * 可以这么想：
		 * 		当s2从session一级缓存中删除后，s2中的teas集合肯定也相应地从一级缓存中删除了，因此此时这个集合当然也发生变化了
		 * 		由于此时是由Student中的teas集合维护关联关系，因此此时hibernate也将links表中相应的记录删除
		 */
		s.delete(s2);
		
		transaction.commit();
		s.close();
	}
	
	/**
	 * 删除关系
	 */
	@Test
	public void deleteTea(){
		Session s = sf.openSession();
		Transaction transaction = s.beginTransaction();
		
		Teacher t1 = (Teacher) s.load(Teacher.class, 1);
		/**
		 * 由于此时是由Student中的teas集合维护关联关系，因此此时删除Teacher时，会出现异常。
		 * 因为此时是由Student中的teas集合维护关联关系，links表中的tid为外键，而删除Teacher时，hibernate不负责删除links中相应的记录
		 */
		s.delete(t1);
		
		transaction.commit();
		s.close();
	}
}