package com.tcl.configservice.manager;

import java.util.List;

import com.tcl.configservice.ConfigType;
import com.tcl.configservice.entity.ConfigNode;

public interface ConfigManager {

	int addConfigEntity(ConfigNode entity);
	int addNamespace(String namespace);
	int addConfig(String namespace, String key, String value);
	int addConfig(String namespace, String key, String value, ConfigType type);
	int deleteConfig(String namespace, String key);
	/**
	 * 修改不改变node 协议字段
	 * @param namespace
	 * @param key
	 * @param value
	 * @return
	 */
	int updateConfig(String namespace, String key, String value);
	
	List<ConfigNode> list(String namespace);
	
	List<String> listNamespace();

}
