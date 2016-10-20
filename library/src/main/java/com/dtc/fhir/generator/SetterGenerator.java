package com.dtc.fhir.generator;

import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JOptionPane;

import com.dtc.fhir.gwt.CodeableConcept;
import com.dtc.fhir.gwt.Identifier;
import com.dtc.fhir.gwt.Provenance;
import com.dtc.fhir.gwt.ProvenanceAgent;
import com.dtc.fhir.gwt.StringDt;

import freemarker.template.Configuration;

public class SetterGenerator {
	public static void main(String[] args) throws Exception {
		HashMap<String, Object> data = new HashMap<>();

		ArrayList<Class<?>> classList = new ArrayList<>();
		data.put("classList", classList);
		//TODO 自動產生 classList
		classList.add(Provenance.class);
		classList.add(CodeableConcept.class);
		classList.add(Identifier.class);
		classList.add(ProvenanceAgent.class);
		classList.add(StringDt.class);

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