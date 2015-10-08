package com.zeek.hibernate.domain.many2many;

import java.util.HashSet;
import java.util.Set;

/**
 * 多对多关联关系--Student端
 * @author zeek
 *
 */
public class Student {

	private Integer id;
	private String stuname;
	
	private Set<Teacher> teas = new HashSet<Teacher>();

	public Set<Teacher> getTeas() {
		return teas;
	}

	public void setTeas(Set<Teacher> teas) {
		this.teas = teas;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getStuname() {
		return stuname;
	}

	public void setStuname(String stuname) {
		this.stuname = stuname;
	}
	
	public void addTeachers(Teacher...teas){
		for(Teacher t : teas){
			this.getTeas().add(t);
		}
	}

}
