package com.zeek.hibernate.domain.one2one.pk;

/**
 * 一对一关联--主键关联
 * 
 * @author zeek
 *
 */
public class User {

	private Integer id;
	private String name;
	//建立User到Addr之间1对1关联关系
	private Addr addr;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Addr getAddr() {
		return addr;
	}

	public void setAddr(Addr addr) {
		this.addr = addr;
	}

}
