package com.tcl.configservice.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.I0Itec.zkclient.ZkClient;

import com.tcl.configservice.ConfigType;
import com.tcl.configservice.Constants;
import com.tcl.configservice.entity.ConfigNode;

public class ZkConfigManager implements ConfigManager {

	private ZkClient zkClient;
	
	@Override
	public List<ConfigNode> list(String namespace) {
		String path = Constants.ROOT_PATH + Constants.SPERATOR + namespace;
		List<String> childs = zkClient.getChildren(path);
		if (childs == null || childs.isEmpty()) {
			return Collections.emptyList();
		}
		List<ConfigNode> result = new ArrayList<>();
		childs.forEach(e -> {
			String value = zkClient.readData(path + Constants.SPERATOR + e, true);
			ConfigNode entity = new ConfigNode(namespace, e, value);
			if (value.startsWith("value\n") || value.startsWith("map\n") || value.startsWith("list\n")) {
				String[] values = value.split("\\n");
				ConfigType type = ConfigType.valueOf(values[0]);
				value = value.substring(values[0].length() + 1);
				entity.setData(value);
				entity.setType(type);
			}
			result.add(entity);
		});
		return result;
	}

	@Override
	public List<String> listNamespace() {
		return zkClient.getChildren(Constants.ROOT_PATH);
	}

	
	@Override
	public int addNamespace(String namespace) {
		zkClient.createPersistent(Constants.ROOT_PATH + Constants.SPERATOR + 
				namespace, true);
		return 1;
	}

	@Override
	public int addConfig(String namespace, String key, String value) {
		zkClient.createPersistent(Constants.ROOT_PATH + Constants.SPERATOR + 
				namespace + Constants.SPERATOR + key, value);
		return 1;
	}

	@Override
	public int deleteConfig(String namespace, String key) {
		return zkClient.delete(Constants.ROOT_PATH + Constants.SPERATOR + 
				namespace + Constants.SPERATOR + key) ? 1:0;
	}

	@Override
	public int updateConfig(String namespace, String key, String value) {
		zkClient.writeData(Constants.ROOT_PATH + Constants.SPERATOR + 
				namespace + Constants.SPERATOR + key, value);
		return 1;
	}

	public void setZkClient(ZkClient zkClient) {
		this.zkClient = zkClient;
	}

	@Override
	public int addConfigEntity(ConfigNode entity) {
		zkClient.createPersistent(Constants.ROOT_PATH + Constants.SPERATOR + 
				entity.getNamespace() + Constants.SPERATOR + entity.getNodeName(), 
				entity.getType().name() + "\n" + entity.getData());
		return 1;
	}

	@Override
	public int addConfig(String namespace, String key, String value, ConfigType type) {
		zkClient.createPersistent(Constants.ROOT_PATH + Constants.SPERATOR + 
				namespace + Constants.SPERATOR + key, type.name() + "\n" + value);
		return 1;
	}
}
