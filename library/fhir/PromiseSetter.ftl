package com.dtc.fhir.gwt.util;

import java.util.Arrays;
import java.util.List;
import com.dtc.fhir.gwt.*;

public class PromiseSetter {
	public static void set(Resource instance, String path, Object value) {
		List<String> pathList = Arrays.asList(path.split("[..]"));
		set(instance, pathList, value);
	}

	public static void set(Element instance, String path, Object value) {
		List<String> pathList = Arrays.asList(path.split("[..]"));
		set(instance, pathList, value);
	}
	
	private static int parseIndex(String name) {
		int start = name.indexOf("[");

		if (start == -1) { return 0; }

		int end = name.indexOf("]", start);

		if (end == -1) { return 0; }

		try {
			return Integer.parseInt(name.substring(start+1, end));
		} catch (Exception e) {
			return 0;
		}
	}
	
	private static String parseName(String name) {
		int start = name.indexOf("[");
		return start == -1 ? name : name.substring(0, start);
	}
	
	// ======== 以上是不會變動分隔線 ======== //
	
	private static void set(Object instance, List<String> path, Object value) {
		switch (instance.getClass().getSimpleName()) {
		<#list classList as clazz>
		case "${clazz.simpleName}":
			Set${clazz.simpleName}.set((${clazz.simpleName})instance, path, value);
			break;
		</#list>
		default:
			throw new IllegalArgumentException("Unspoort class: " + instance.getClass().getSimpleName());
		}
	}
	
	<#list implementList as implement>
${implement}
	</#list>
}