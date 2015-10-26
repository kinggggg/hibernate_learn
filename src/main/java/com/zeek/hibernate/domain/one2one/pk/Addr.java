package com.zeek.hibernate.domain.one2one.pk;

/**
 * 一对一关联--主键关联
 * 
 * @author zeek
 *
 */
public class Addr {

	private Integer id;
	private String province;
	private String city;
	//建立Addr到User之间1对1关联关系
	private User user;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
