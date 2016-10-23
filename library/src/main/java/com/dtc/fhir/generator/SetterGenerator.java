package com.dtc.fhir.generator;

import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import javax.swing.JOptionPane;

import com.dtc.fhir.gwt.Element;
import com.dtc.fhir.gwt.Extension;
import com.dtc.fhir.gwt.Resource;

import org.reflections.Reflections;

import freemarker.template.Configuration;

public class SetterGenerator {
	public static void main(String[] args) throws Exception {
		Reflections reflections = new Reflections("com.dtc.fhir.gwt");

		HashMap<String, Object> data = new HashMap<>();

		ArrayList<Class<?>> classList = new ArrayList<>();
		data.put("classList", classList);
		classList.addAll(reflections.getSubTypesOf(Resource.class));
		classList.addAll(reflections.getSubTypesOf(Element.class));
		classList.remove(Extension.class);
		Collections.sort(classList, new Comparator<Class<?>>() {
			@Override
			public int compare(Class<?> o1, Class<?> o2) {
				return o1.getSimpleName().compareTo(o2.getSimpleName());
			}
		});

		ArrayList<String> implementList = new ArrayList<>();
		data.put("implementList", implementList);

		for (Class<?> clazz : classList) {
			implementList.add(genImplement(clazz));
		}

		String ftlName = "fhir/PromiseSetter.ftl";
		FileWriter writer = new FileWriter(
			new File(JOptionPane.showInputDialog("檔案路徑"), "PromiseSetter.java")
		);
		new Configuration().getTemplate(ftlName, "UTF-8")
			.process(data, writer);
	}

	private static String genImplement(Class<?> clazz) throws Exception {
		HashMap<String, Object> data = new HashMap<>();
		data.put("clazz", clazz);

		ArrayList<GetMethod> getMethod = new ArrayList<>();
		data.put("getterList", getMethod);
		for (Method method : clazz.getMethods()) {
			if (method.getName().startsWith("get")) {
				if (method.getName().startsWith("getClass")) { continue; }
				if (method.getName().startsWith("getExtension")) { continue; }
				if (method.getName().startsWith("getContained")) { continue; }

				GetMethod getter = new GetMethod();
				getMethod.add(getter);
				getter.setName(
					method.getName().substring(3,4).toLowerCase() + method.getName().substring(4)
				);
				getter.setReturnType(method.getReturnType());

				if (method.getReturnType() == List.class) {
					getter.setListType(
						((ParameterizedType)method.getGenericReturnType()).getActualTypeArguments()[0]
					);
				}
			}
		}

		String ftlName = "fhir/Implement.ftl";
		StringWriter writer = new StringWriter();
		new Configuration().getTemplate(ftlName, "UTF-8")
			.process(data, writer);
		return writer.getBuffer().toString();
	}
}