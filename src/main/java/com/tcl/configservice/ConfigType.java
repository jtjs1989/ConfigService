package com.tcl.configservice;
/**
 * 配置节点中的数据类型
 * 类型一定是第一行开始的位置，如果没有类型字段则默认为value
 * 
 * ex. 
 * map
 * k1=v1
 * k2=v2
 * 
 * 或者 
 * list
 * v1
 * @author chenbo
 *
 */
public enum ConfigType {

	value,
	/**
	 * 可以配置map对象
	 */
	map,
	list,
}
