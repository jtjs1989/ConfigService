package com.tcl.configservice;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.math.NumberUtils;

/**
 * 配置管理核心类，所以配置内容存放在这儿，外部应用也可以直接使用此类
 * @author chenbo
 *
 */
public class ConfigServiceMap {

	private static final Map<String, Object> configMap = new ConcurrentHashMap<>();
	public static final String defaultSperator = "[,;]";
	private static final String[] emptyArray = new String[0];
	private ConfigServiceMap() {}
	
	public static String getString(String key, String defualtValue) {
		if (configMap.containsKey(key)) {
			return (String)configMap.get(key);
		} 
		return defualtValue;
	}
	public static Object get(String key) {
		return configMap.get(key);
	}
	
	public static String getString(String key) {
		return getString(key, null);
	}
	public static int getInt(String key, int defaultValue) {
		return NumberUtils.toInt((String)configMap.get(key), defaultValue);
	}
	
	public static int getInt(String key) {
		return getInt(key, -1);
	}
	
	public static float getFloat(String key, float defaultValue) {
		return NumberUtils.toFloat((String)configMap.get(key), defaultValue);
	}
	
	public static float getFloat(String key) {
		return getFloat(key, 0f);
	}
	public static String[] getStringArr(String key) {
		return getStringArr(key, defaultSperator);
	}
	public static String[] getStringArr(String key, String sperator) {
		if(configMap.containsKey(key)) {
			String value = (String)configMap.get(key);
			return value.split(sperator);
		}
		return emptyArray;
	}
	
	public static int[] getIntArr(String key, String sperator) {
		String[] strArr = getStringArr(key, sperator);
		int[] result = new int[strArr.length];
		for (int i=0; i<result.length; i++) {
			result[i] = NumberUtils.toInt(strArr[i]);
		}
		return result;
	}
	
	public static int[] getIntArr(String key) {
		return getIntArr(key, defaultSperator);
	}
	
	public static void putAll(Map<String, String> map) {
		configMap.putAll(map);
	}
	
	public static void put(String key, Object value) {
		configMap.put(key, value);
	}
	public static void delete(String key) {
		configMap.remove(key);
	}
	public static Map<String, Object> getAll() {
		return configMap;
	}
}
