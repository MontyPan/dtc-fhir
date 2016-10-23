	private static class Set${clazz.simpleName} {
		static void set(${clazz.name} instance, List<String> path, Object value) {
			if (path.size() == 1) {
				set(instance, path.get(0), value);
				return;
			}

			String name = parseName(path.get(0));
			int index = parseIndex(path.get(0));

			Object activity = get(instance, name, index);
			PromiseSetter.set(activity, path.subList(1, path.size()), value);
		}

		static void set(${clazz.name} instance, String path, Object value) {
			String name = parseName(path);
			int index = parseIndex(path);

			switch(name) {
			<#list getterList as getter>
			case "${getter.name}":
				<#if getter.list>
				if (value instanceof ${getter.listTypeName}) {
					List<${getter.listTypeName}> ${getter.name}List = instance.get${getter.name?cap_first}();
					if (${getter.name}List.size() < index + 1) {
						${getter.name}List.add((${getter.listTypeName})value);
					} else {
						${getter.name}List.set(Math.min(${getter.name}List.size() - 1, index), (${getter.listTypeName})value);
					}
				} else {
					throw new UnsupportedOperationException();
				}
				<#else>
				if (value instanceof ${getter.returnTypeName}) {
					instance.set${getter.name?cap_first}((${getter.returnTypeName})value);
				} else {
					throw new IllegalArgumentException("value is not a ${getter.returnTypeName}");
				}
				</#if>
				break;
			</#list>
			default:
				throw new IllegalArgumentException(name + " is not a field of ${clazz.simpleName}");
			}
		}

		static Object get(${clazz.name} _instance_, String _name_, int _index_) {
			switch(_name_) {
			<#list getterList as getter>
			case "${getter.name}":
				<#if getter.list>
				List<${getter.listTypeName}> ${getter.name}List = _instance_.get${getter.name?cap_first}();
				if (${getter.name}List.size() < _index_ + 1) {
					${getter.name}List.add(new ${getter.listTypeName}());
				}
				return ${getter.name}List.get(Math.min(${getter.name}List.size() - 1, _index_));
				<#else>
				{${getter.returnTypeName} _field_ = _instance_.get${getter.name?cap_first}();
				if (_field_ == null) {
					<#if getter.unInit>
					throw new IllegalArgumentException(_name_ + " can't auto initial");
					<#else>
					_field_ = new ${getter.returnTypeName}();
					_instance_.set${getter.name?cap_first}(_field_);
					</#if>
				}
				return _field_;}
				</#if>
			</#list>
			default:
				throw new IllegalArgumentException(_name_ + " is not a field of ${clazz.simpleName}");
			}
		}
	}