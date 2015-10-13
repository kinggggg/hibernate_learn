package com.zeek.hibernate.domain;

import java.sql.Date;

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

	public String getName() {
		if((fname!=null && !fname.equals("")) && (lname!=null && !lname.equals(""))){
			return fname + "," + lname;
		}
		return null;
	}

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
