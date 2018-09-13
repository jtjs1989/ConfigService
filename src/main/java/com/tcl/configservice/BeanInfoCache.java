package com.tcl.configservice;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeanInfoCache {

	private static final Map<String, List<Field>> fieldMap = new HashMap<>();
	
	private static final Map<Field, Object> beanCache = new HashMap<>();
	
	public static void add(Field field, String key, Object bean) {
		beanCache.put(field, bean);
		if (fieldMap.containsKey(key)) {
			fieldMap.get(key).add(field);
		} else {
			fieldMap.put(key, Arrays.asList(field));
		}
	}
	
	public static List<Field> getFields(String key) {
		return fieldMap.get(key);
	}
	
	public static Object getBean(Field field) {
		return beanCache.get(field);
	}
}
