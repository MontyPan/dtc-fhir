package com.dtc.fhir.generator;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;

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
	public String getReturnTypeName() {
		return returnType.isArray() ? returnType.getSimpleName() : returnType.getName();
	}
	public void setReturnType(Class<?> returnType) {
		this.returnType = returnType;
	}
	public String getListTypeName() {
		Class<?> clazz = (Class<?>)listType;
		return clazz.isArray() ? clazz.getSimpleName() : clazz.getName();
	}
	public void setListType(Type listGenericType) {
		this.listType = listGenericType;
	}
	public boolean isList() {
		return listType != null;
	}
	public boolean isUnInit() {
		return returnType.isEnum() || returnType.isArray() ||
			returnType == BigInteger.class || returnType == BigDecimal.class ||
			returnType == Integer.class;
	}
}