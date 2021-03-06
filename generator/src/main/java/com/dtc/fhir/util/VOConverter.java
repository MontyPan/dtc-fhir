package com.dtc.fhir.util;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.dtc.common.reflection.MethodUtil;
import com.dtc.common.reflection.TypeUtil;

import org.reflections.Reflections;

import ca.uhn.fhir.model.api.BaseElement;
import freemarker.template.Configuration;
import freemarker.template.Template;

public class VOConverter {
	public static final String BASE_PACKAGE = "com.dtc.fhir.gwt.vo";
	public static final String FHIR_PACKAGE = "ca.uhn.fhir.model";

	private static final List<String> SKIP_METHOD = Arrays.asList(
		"getAllPopulatedChildElementsOfType"
		, "getStructureFhirVersionEnum"

		, "getResources"	//ca.uhn.fhir.model.api.Bundle
		, "getAbstract"	//ca.uhn.fhir.model.dstu2.resource.ValueSet.CodeSystemConcept
	);
	private static final List<String> SKIP_METHOD_TAIL = Arrays.asList(
		"Rep", "Element"
	);

	private static File target;

	private static final Configuration config = new Configuration(Configuration.VERSION_2_3_24);

	public static void main(String[] args) throws Exception {
		init(args);

		Reflections enumRflt = new Reflections("ca.uhn.fhir.model.dstu2.valueset");
		for (Class<?> clazz : enumRflt.getSubTypesOf(Enum.class)) {
			convertEnum(clazz);
		}

		Reflections elementRflt = new Reflections(FHIR_PACKAGE);
		convertClass(BaseElement.class);

		//產生所有 BaseElement 的 child class
		for (Class<?> clazz : elementRflt.getSubTypesOf(BaseElement.class)) {
			convertClass(clazz);
		}
	}

	private static void init(String[] args) throws Exception {
		config.setDefaultEncoding("UTF-8");
		//Refactory 怎麼指到相對位置？
		config.setDirectoryForTemplateLoading(new File(args[0]));
		target = new File(args[1]);

		if (!target.exists()) { target.mkdirs(); }
	}

	private static void convertEnum(Class<?> clazz) throws Exception {
		System.out.println("Convert " + clazz.getName());

		File java = new File(
			toPackageFolder(transferPackage(clazz), target),
			clazz.getSimpleName() + ".java"
		);
		java.getParentFile().mkdirs();

		//TODO 處理 enum 內的 field（如果有需要 Orz）
		HashMap<String, Object> data = new HashMap<>();
		data.put("packageName", transferPackage(clazz));
		data.put("className", clazz.getSimpleName());

		ArrayList<String> valueList = new ArrayList<>();
		data.put("valueList", valueList);

		for (Object v : clazz.getEnumConstants()) {
			valueList.add(v.toString());
		}

		Template temp = config.getTemplate("enum.ftl");
		temp.process(
			data,
			new FileWriter(java)
		);
	}

	private static void convertClass(Class<?> clazz) throws Exception {
		//inner class 跳過不處理
		//XXX 用這種判斷方法好像不是很好... XD
		if (clazz.getName().contains("$")) { return; }

		System.out.println("Convert " + clazz.getName());

		File java = new File(
			toPackageFolder(transferPackage(clazz), target),
			clazz.getSimpleName() + ".java"
		);
		java.getParentFile().mkdirs();

		Template temp = config.getTemplate("vo.ftl");
		temp.process(
			transferClass(clazz),
			new FileWriter(java)
		);
	}

	private static HashMap<String, Object> transferClass(Class<?> clazz) {
		HashMap<String, Object> data = new HashMap<>();
		data.put("packageName", transferPackage(clazz));
		data.put("className", genClassDeclaration(clazz));

		ArrayList<FtlField> fieldList = new ArrayList<>();
		data.put("fieldList", fieldList);

		//FTL 會用 fieldList 產生 field 宣告、getter、setter
		//因為 hapi 的 field 命名都喜歡加上「my」...... ＝＝"
		//所以用 source 的 getter 來產生 fieldList 內容，比較好寫也比較保險
		for(Method method : clazz.getDeclaredMethods()) {
			if (!MethodUtil.isGetter(method)) { continue; }
			//有一些莫名其妙的 getter 也忽略
			if (skipMethod(method)) { continue; }

			System.out.println("\t" + method.getName());
			FtlField field = new FtlField();
			field.setName(MethodUtil.getterToField(method));
			field.setType(method.getGenericReturnType());
			fieldList.add(field);
		}

		//inner class 的處理
		ArrayList<HashMap<String, Object>> innerList = new ArrayList<>();
		data.put("innerList", innerList);

		for (Class<?> inner : clazz.getDeclaredClasses()) {
			innerList.add(transferClass(inner));
		}
		////

		return data;
	}

	private static String genClassDeclaration(Class<?> clazz) {
		StringBuffer result = new StringBuffer(clazz.getSimpleName());

		if (clazz.getTypeParameters().length > 0) {
			StringBuffer generic = new StringBuffer("<");

			for (Type type : clazz.getTypeParameters()) {
				generic.append(TypeUtil.toDeclaration(type));
				generic.append(",");
			}

			result.append(generic.substring(0, generic.length() - 1));
			result.append(">");
		}

		if (clazz.getGenericSuperclass() != Object.class) {
			result.append(" extends ");
			result.append(TypeUtil.toDeclaration(clazz.getGenericSuperclass(), true));
		}

		return result.toString().replace(VOConverter.FHIR_PACKAGE, VOConverter.BASE_PACKAGE);
	}

	/**
	 * 將 FHIR 的 base package 轉換成自己指定的 base package
	 */
	private static String transferPackage(Class<?> clazz) {
		//目前是把 inner class 變成獨立的 class
		//但是這會導致 package name 與其 owner class name 撞名
		//IDE 有出現 warning、也會破壞 package name 的 style
		//TODO 改成標準 inner class 的處理方式（FTL 要怎麼寫呢 T__T）
		String name = clazz.getName().replace('$', '.');

		if (!name.startsWith(FHIR_PACKAGE)) { return BASE_PACKAGE; }

		return BASE_PACKAGE +
			name.substring(FHIR_PACKAGE.length(), name.lastIndexOf('.'));
	}

	private static boolean skipMethod(Method method) {
		if (SKIP_METHOD.contains(method.getName())) { return true; }

		for (String tail : SKIP_METHOD_TAIL) {
			if (method.getName().endsWith(tail)) { return true; }
		}

		return false;
	}

	private static File toPackageFolder(String packageName, File root) {
		return Paths.get(root.getAbsolutePath(), packageName.split("\\.")).toFile();
	}
}
