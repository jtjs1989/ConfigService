package com.tcl.configservice.entity;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import com.tcl.configservice.ConfigType;
/**
 * 对应zk中的znode
 * 协议方式为第一行是nodetype 现在支持以下三种 [value;list;map]
 * 第二行开始是数据
 * 例如 nodename=db.url
 * value
 * localhost:3306
 * 
 * 数据库连接url
 * 
 * @author chenbo
 *
 */
public class ConfigNode implements Serializable {

	private static final String SPERAORT = "\n";
	private String namespace;
	/**
	 * znode的名称， 也就是对应 @see ConfigCen.name
	 */
	
	private String nodeName;
	/**
	 * 不包含第一行协议部分
	 */
	private String data;
	/**
	 * 配置描述
	 */
	private String desc;
	/**
	 * 协议类型，默认为value
	 */
	private ConfigType type = ConfigType.value;
	
	private String callback;
	public ConfigNode(){}
	public ConfigNode(String namespace, String nodeName, String data){
		this.namespace = namespace;
		this.nodeName = nodeName;
		this.data = data;
	}
	public String getNamespace() {
		return namespace;
	}
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}
	public String getNodeName() {
		return nodeName;
	}
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public ConfigType getType() {
		return type;
	}
	public void setType(ConfigType type) {
		this.type = type;
	}
	public String getCallback() {
		return callback;
	}
	public void setCallback(String callback) {
		this.callback = callback;
	}
	
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public static ConfigNode resolver(String str) {
		ConfigNode configNode = new ConfigNode();
		String[] ss = str.split("\\n\\n");
		if (ss.length > 1) {
			configNode.setDesc(ss[ss.length-1]);
			ConfigType type = ConfigType.valueOf(ss[0].toLowerCase());
			if (type == null) {
				configNode.setData(ss[0]);
				configNode.setType(ConfigType.value);
			} else {
				configNode.setData(ss[1]);
				configNode.setType(type);
			}
		}
		configNode.setData(str);
		configNode.setType(ConfigType.value);
		return configNode;
	}
	@Override
	public String toString() {
		return "[namespace=" + namespace +",nodename="+nodeName+",type="+type.name()+",data="+data+"]";
	}
	
	public String serize() {
		StringBuilder sb = new StringBuilder();
		sb.append(type.name()).append(SPERAORT).append(SPERAORT).append(data);
		if (!StringUtils.isEmpty(desc)) {
			sb.append(SPERAORT).append(SPERAORT).append(desc);
		}
		return sb.toString();
	}
}
