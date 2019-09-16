package com.tdf.entity.model;

import java.io.Serializable;

public class SimpleModel implements Serializable {

	private static final long serialVersionUID = 68675266425192022L;

	public SimpleModel() {
		// TODO Auto-generated constructor stub
	}

	public SimpleModel(String key, Object value) {
		this.key = key;
		this.value = value;
	}

	private String key;
	private Object value;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
}
