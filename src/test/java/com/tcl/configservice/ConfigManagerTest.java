package com.tcl.configservice;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.tcl.configservice.entity.ConfigNode;
import com.tcl.configservice.manager.ConfigManager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext.xml")
public class ConfigManagerTest extends AbstractJUnit4SpringContextTests{

	@Autowired
	private ConfigManager configManager;
	
	@Test
	public void testList(){
		List<ConfigNode> list = configManager.list("test");
		System.out.println(list);
	}
	@Test
	public void testAddConfig() {
		configManager.addConfig("test", "testmap", "k1=v1\nk2=v2", ConfigType.map);
	}
	@Test
	public void testDelete() {
		configManager.deleteConfig("test", "testmap");
	}
}
