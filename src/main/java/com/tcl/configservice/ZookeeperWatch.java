package com.tcl.configservice;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.SimpleTypeConverter;

import com.tcl.configservice.entity.ConfigNode;

public class ZookeeperWatch {

	private static final Logger logger = LoggerFactory.getLogger(ZookeeperWatch.class);
	private SimpleTypeConverter simpleTypeConverter = new SimpleTypeConverter();
	
	private String appPath;
	
	private ZkClient zkClient;
	
	private List<String> lastChild;
	
	private final DataChangeListener dataChangeListener = new DataChangeListener();

	public void init() {
		logger.debug("-------zkwatch.init-----");
		List<String> keys = zkClient.getChildren(appPath);
		initData(keys);
		addDataChangeWatch(keys);
		addWatch();
		loadData(keys);
	}
	
	/**
	 * 加载数据到ConfigServiceMap
	 * @param keys
	 */
	public void initData(List<String> keys) {
		lastChild = keys;
		keys.forEach(e -> {
			String value = zkClient.readData(appPath + Constants.SPERATOR + e);
			parseData(e, value);
		});
	}
	/**
	 * 监听节点数据变更的事件
	 * @param paths
	 */
	public void addDataChangeWatch(List<String> paths) {
		paths.forEach(e -> {
			zkClient.subscribeDataChanges(appPath + Constants.SPERATOR + e, dataChangeListener);
		});
	}
	/**
	 * 监听nameSpace节点下的子节点变更事件，节点新增或者节点删除
	 */
	public void addWatch() {
		// 首先添加父节点的事件
		zkClient.subscribeChildChanges(appPath, new IZkChildListener() {
			@Override
			public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
				logger.info("childchange parentPath:{}", parentPath);
				List<String> child = new ArrayList<>(currentChilds);
				if (currentChilds.size() < lastChild.size()) { // 有节点被删除
					logger.info("delete path");
					List<String> lastCopy = new ArrayList<>(lastChild);
					lastCopy.removeAll(currentChilds);
					lastCopy.forEach(e -> {
						logger.info("deleted path:{}", e);
						ConfigServiceMap.delete(e);
					});
					
				} else if(currentChilds.size() == lastChild.size()) { // 节点前后数量一样，全量刷新
//					loadData(currentChilds);
				} else { //节点新增
					logger.info("add path");
					child.removeAll(lastChild);
					child.forEach(e -> {
						zkClient.subscribeDataChanges(appPath, dataChangeListener);
						String value = zkClient.readData(appPath+ Constants.SPERATOR + e);
						parseData(e, value);
					});
					loadData(child);
				}
				lastChild = currentChilds;
			}
		});
	}
	
	public class DataChangeListener implements IZkDataListener {
		@Override
		public void handleDataChange(String dataPath, Object data) throws Exception {
			logger.info("datachange dataPath:{}, data:{}", dataPath, data);
			dataPath = getLastPath(dataPath);
			parseData(dataPath, (String)data);
			loadData(Arrays.asList(dataPath));
		}
		@Override
		public void handleDataDeleted(String dataPath) throws Exception {
			logger.info("");
			zkClient.unsubscribeDataChanges(dataPath, dataChangeListener);
		}
	}
	
	public void setNamespace(String namespace) {
		this.appPath = Constants.ROOT_PATH + Constants.SPERATOR + namespace;
	}

	public void setZkClient(ZkClient zkClient) {
		logger.debug("zkwatch.setzkclient------");
		this.zkClient = zkClient;
	}

	/**
	 * 加载数据到对象熟悉中
	 * @param keys
	 */
	public void loadData(List<String> keys) {
		keys.forEach(key -> {
			Object value = ConfigServiceMap.get(key);
			if (value != null) {
				List<Field> fields = BeanInfoCache.getFields(key);
				if (fields != null) {
					for (Field field : fields) {
						Object obj = BeanInfoCache.getBean(field);
						if(field.getType().isArray()) {
							value = ((String)value).split(ConfigServiceMap.defaultSperator);
						}
						Object _value = simpleTypeConverter.convertIfNecessary(value,field.getType());
						try {
							field.set(obj, _value);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		});
	}

	public void loadData(ConfigNode data) {
		if (data.getType() == ConfigType.map ) {
			
			List<Field> fields = BeanInfoCache.getFields(data.getNodeName());
		} else {
			loadData(Arrays.asList(data.getNodeName()));
		}
	}
	public String getLastPath(String dataPath) {
		return dataPath.substring(dataPath.lastIndexOf("/")+1);
	}
	/**
	 * 解析从znode里面读取的数据
	 * @param zValue
	 */
	private void parseData(String key, String zValue) {
		if (StringUtils.isEmpty(zValue)) {
			return;
		}
		String[] values = zValue.split("\\n");
		if (values.length == 1) {
			ConfigServiceMap.put(key, values[0]);
			return;
		} else {
			String dType = values[0];
			if (dType.equalsIgnoreCase(ConfigType.value.name())) {
				ConfigServiceMap.put(key, values[1]);
			} else if (dType.equalsIgnoreCase(ConfigType.list.name())) {
				
			} else if (dType.equalsIgnoreCase(ConfigType.map.name())) {
				Map<String, String> map = new HashMap<>();
				for(int i = 1; i < values.length; i++) {
					String mapK = values[i].substring(0, values[i].indexOf("="));
					String mapV = values[i].substring(values[i].indexOf("=")+1);
					map.put(mapK, mapV);
				}
				ConfigServiceMap.put(key, map);
			} else {
				logger.error("configservice error, znode:{}, value:{}", appPath + "/" +key, zValue);
			}
		}
	}
}
