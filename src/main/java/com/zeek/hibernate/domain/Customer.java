package com.zeek.hibernate.domain;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

public class Customer {

	private Integer id;

//	private String name;

	private Integer age;

	private Date birthday;

	private boolean married;

	private byte[] photo;

	private String description;
	
	private String fname;
	
	private String lname;
	
	//配置一对多关联映射关系,并初始化orders，防止出现空指针异常
	private Set<Order> orders = new HashSet<Order>();

	public Set<Order> getOrders() {
		return orders;
	}

	public void setOrders(Set<Order> orders) {
		this.orders = orders;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getLname() {
		return lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public boolean isMarried() {
		return married;
	}

	public void setMarried(boolean married) {
		this.married = married;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

	/**
	 * 将访问修饰符public修改为private后，再运行TestAccess中的测试用例时，程序运行完全正常，这是因为:
	 * hibernate访问实体中标准的javabean方式用的反射，不受访问控制符的限制
	 * @return
	 */
	public String getName() {
		if((fname!=null && !fname.equals("")) && (lname!=null && !lname.equals(""))){
			return fname + "," + lname;
		}
		return null;
	}

	//同上
	public void setName(String name) {
		if(name!=null){
			String[] split = name.split(",");
			if(split != null && split.length > 1){
				this.fname = split[0];
				this.lname = split[1];
			}
		}
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
