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
