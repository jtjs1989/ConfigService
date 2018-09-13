package com.tcl.configservice.spring;

import java.util.Properties;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import com.tcl.configservice.ConfigServiceMap;

public class ConfigPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {

	
	@Override
	protected String resolvePlaceholder(String placeholder, Properties props, int systemPropertiesMode) {
		if (ConfigServiceMap.get(placeholder) != null && 
				ConfigServiceMap.get(placeholder) instanceof String) {
			return ConfigServiceMap.getString(placeholder);
		} else {
			return super.resolvePlaceholder(placeholder, props, systemPropertiesMode);
		}
	}
}
