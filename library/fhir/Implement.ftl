	private static class Set${clazz.simpleName} {
		static void set(${clazz.simpleName} instance, List<String> path, Object value) {
			if (path.size() == 1) {
				set(instance, path.get(0), value);
				return;
			}

			String name = parseName(path.get(0));
			int index = parseIndex(path.get(0));

			Object activity = get(instance, name, index);
			PromiseSetter.set(activity, path.subList(1, path.size()), value);
		}

		static void set(${clazz.simpleName} instance, String path, Object value) {
			String name = parseName(path);
			int index = parseIndex(path);

			switch(name) {
			<#list getterList as getter>
			case "${getter.name}":
				<#if getter.list>
				if (value instanceof ${getter.listType.simpleName}) {
					List<${getter.listType.simpleName}> ${getter.name}List = instance.get${getter.name?cap_first}();
					if (${getter.name}List.size() < index + 1) {
						${getter.name}List.add((${getter.listType.simpleName})value);
					} else {
						${getter.name}List.set(Math.min(${getter.name}List.size() - 1, index), (${getter.listType.simpleName})value);
					}
				} else {
					throw new UnsupportedOperationException();
				}
				<#else>
				if (value instanceof ${getter.returnType.simpleName}) {
					instance.set${getter.name?cap_first}((${getter.returnType.simpleName})value);
				} else {
					throw new IllegalArgumentException("value is not a ${getter.returnType.simpleName}");
				}
				</#if>
				break;
			</#list>
			default:
				throw new IllegalArgumentException(name + " is not a field of ${clazz.simpleName}");
			}
		}

		static Object get(${clazz.simpleName} instance, String name, int index) {
			switch(name) {
			<#list getterList as getter>
			case "${getter.name}":
				<#if getter.list>
				List<${getter.listType.simpleName}> ${getter.name}List = instance.get${getter.name?cap_first}();
				if (${getter.name}List.size() < index + 1) {
					${getter.name}List.add(new ${getter.listType.simpleName}());
				}
				return ${getter.name}List.get(Math.min(${getter.name}List.size() - 1, index));
				<#else>
				${getter.returnType.simpleName} ${getter.name} = instance.get${getter.name?cap_first}();
				if (${getter.name} == null) {
					${getter.name} = new ${getter.returnType.simpleName}();
					instance.set${getter.name?cap_first}(${getter.name});
				}
				return ${getter.name};
				</#if>
			</#list>
			default:
				throw new IllegalArgumentException(name + " is not a field of ${clazz.simpleName}");
			}
		}
	}