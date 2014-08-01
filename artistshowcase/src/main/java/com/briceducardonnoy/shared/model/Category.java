package com.briceducardonnoy.shared.model;

import java.io.Serializable;

public class Category implements Serializable {

	private static final long serialVersionUID = 3280083794359105684L;
	
	private Integer id;
	private String name;
	private String misc;
	
	public Category() {
		
	}
	
	public Category(Integer id, String name, String misc) {
		super();
		this.id = id;
		this.name = name;
		this.misc = misc;
	}

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

	public String getMisc() {
		return misc;
	}

	public void setMisc(String misc) {
		this.misc = misc;
	}
	
	@Override
	public String toString() {
		return "Category " + id + " named " + name + " (" + misc + ")";
	}
	
}
