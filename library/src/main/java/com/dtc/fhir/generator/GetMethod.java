package com.dtc.fhir.generator;

import java.lang.reflect.Type;

public class GetMethod {
	private String name;
	private Class<?> returnType;
	private Type listType;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Class<?> getReturnType() {
		return returnType;
	}
	public void setReturnType(Class<?> returnType) {
		this.returnType = returnType;
	}
	public Type getListType() {
		return listType;
	}
	public void setListType(Type listGenericType) {
		this.listType = listGenericType;
	}
	public boolean isList() {
		return listType != null;
	}
}