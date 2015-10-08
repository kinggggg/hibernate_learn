package com.zeek.hibernate.domain.many2many;

import java.util.HashSet;
import java.util.Set;

/**
 * 多对对关联关系--Teacher端
 * @author zeek
 *
 */
public class Teacher {
	
	private Integer id;
	private String teano;
	
	private Set<Student> stus = new HashSet<Student>();
	
	public Set<Student> getStus() {
		return stus;
	}
	public void setStus(Set<Student> stus) {
		this.stus = stus;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTeano() {
		return teano;
	}
	public void setTeano(String teano) {
		this.teano = teano;
	}
	
	public void addStudents(Student...stus){
		for(Student s : stus){
			this.getStus().add(s);
		}
	}
	

}
