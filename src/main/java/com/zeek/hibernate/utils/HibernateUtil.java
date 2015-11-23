package com.zeek.hibernate.utils;


import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
	
	private static SessionFactory sf = null;
	
	static {
		try {
			sf = new Configuration().configure().buildSessionFactory();
		} catch (HibernateException e) {
			e.printStackTrace();
		}
	}
	
	//得到与当前线程绑定的会话
	//1.判断当前线程是否有session，若有的话直接返回
	//2.没有的话，开启新的session，并立即和当前线程绑定
	public static Session currentSession(){
		return sf.getCurrentSession();
	}
	
	//开启session
	public static Session openSession(){
		return sf.openSession();
	}
	
	/**
	 * 开启无状态session：
	 * 在通常的情况下，hibernate中存在一级缓存即session级别的缓存和二级缓存即sessionFactory级别的缓存。session的一些操作比如说save，update
	 * 等操作其实是和session一级缓存在做交互，并且在某些情况下session一级缓存中会缓存很多个对象，这样的话会占用大量的内存；在之前的笔记中还提到过
	 * 在session一级缓存中还维护各个对象的一个镜像对象；有时候session一级缓存还要和二级缓存进行交互等等这些内容；有时候这些功能是不需要的，为了这些不必要的缓存工作此时就可以使用无状态的session
	 * @return
	 */
	public static StatelessSession openStatelessSession(){
		return sf.openStatelessSession();
	}
	
	//回滚事务
	public static void rollBackTx(Transaction tx){
		if(tx != null && !tx.wasRolledBack()){
			tx.rollback();
		}
	}
	
	//关闭会话
	public static void closeSession(Session s){
		if(s != null && s.isOpen()){
			s.close();
		}
	}

}
